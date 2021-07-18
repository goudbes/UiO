#define _POSIX_SOURCE
#define _BSD_SOURCE
#include <stdio.h>      /* for printf() and fprintf() */
#include <sys/socket.h> /* for socket(), connect(), send(), and recv() */
#include <arpa/inet.h>  /* for sockaddr_in and inet_addr() */
#include <stdlib.h>     /* for atoi() and exit() */
#include <string.h>     /* for memset() */
#include <unistd.h>     /* for close() */
#include <stdbool.h>
#include <signal.h>
#include <limits.h>
#include <netdb.h>
#include <fcntl.h>
#include <wait.h>
#include "job.h"
#include "common.h"

// Pipe related variables
#define PIPE_COUNT 2
int pipes[PIPE_COUNT][2] = {{-1, -1}, {-1, -1}};
#define CHILD_1 0
#define CHILD_2 1
#define READ_PIPE 0
#define WRITE_PIPE 1

bool debug = false;

// Current number of children
int child_count = 0;

// Identifier of children
int my_id = -1;

// PID for children
pid_t child_one = -1;
pid_t child_two = -1;

// Signal handler variables
sig_atomic_t volatile g_running = true;
sig_atomic_t volatile g_child_stop = false;

// The socket file descriptor
int sockfd = -1;


/**
 * This function is used to terminate children correctly
 * and waits for them to die.
 */
void kill_children() {

    if(child_one != -1) {
        if(kill(child_one, SIGTERM) == -1){
            perror("WARNING: Could not terminate child one");
        }
        if (waitpid(child_one, NULL, 0) != child_one) {
            perror("WARNING: Could not kill child one");
        }
        child_one = -1;
    }
    if(child_two != -1) {
        if(kill(child_two, SIGTERM) == -1){
            perror("WARNING: Could not terminate child two");
        }
        if (waitpid(child_two, NULL, 0) != child_two) {
            perror("WARNING: Could not kill child two");
        }
        child_two = -1;
    }
}

/**
 * Quitting the client and cleaning up ( children, sockets and pipes )
 * @param exit_status the exit status
 */
void quit(int exit_status) {
    kill_children();
    if(sockfd != -1) {
        debug_print("Closing socket\n");
        if(close(sockfd) == -1) {
            perror("WARNING: Error when closing socket");
        }
        sockfd = -1;
    }
    if(pipes[my_id][WRITE_PIPE] != -1) {
        debug_print("Closing pipes...\n");
        close(pipes[my_id][WRITE_PIPE]);
    }
    debug_print("Exiting...\n");
    exit(exit_status);
}

/**
 * Notify server of client termination and quit
 */
void notify_terminate_success() {
    char t = 'T';
    if(write(sockfd,&t, 1) < 1){
        perror("WARNING: Could not tell server that I am terminating");
    }
    quit(EXIT_SUCCESS);
}

/**
 * Quit properly when we get SIGINT (CTRL-C) or SIGTERM.
 * @param signum the signal
 */
void sig_handler(int signum)
{
    if (signum == SIGINT || signum == SIGTERM) {
        debug_print("Quitting on SIGINT or SIGTERM...\n");
        g_running = false;
        notify_terminate_success();
        quit(EXIT_SUCCESS);
    } //program interrupt signal
}

/**
 * Error handling function.
 *
 * Prints error message and notify server that client terminates
 * due to error.
 *
 * @param errorMessage an error message
 */
void error(char *errorMessage)
{
    perror(errorMessage);

    // If we have a valid socket, tell the server that we are going down
    if(sockfd != -1) {
        char msg[4] = {'E', 0, 0, 0};
        debug_print("Sending a msg to server that the client terminates due to error...\n");

        // Ignore SIGPIPE if the connection has died
        // (otherwise we die here if server has died already)
        // See https://stackoverflow.com/a/1795213
        struct sigaction new_actn, old_actn;
        new_actn.sa_handler = SIG_IGN;
        sigemptyset (&new_actn.sa_mask);
        new_actn.sa_flags = 0;
        sigaction (SIGPIPE, &new_actn, &old_actn);

        if(write(sockfd, &msg, sizeof(msg)) < (ssize_t) sizeof(msg)){
            perror("WARNING: Could not tell server that I am terminating");
        }

        // Restore normal signal handling
        sigaction (SIGPIPE, &old_actn, NULL);
    }

    quit(EXIT_FAILURE);
}

/**
 * Marking child to stop when parent process sends SIGTERM.
 * @param signum the signal
 */
void child_sig_handler(int signum) {
    if(signum == SIGTERM) {
        debug_print("Terminating child when parent process sends SIGTERM...\n");
        g_child_stop = true;
    }
}

/**
 * Function run only by the children that provides communication
 * between the parent process and children.
 *
 * The children are reading message from the parent in the pipe
 * and prints them to stdout or stderr.
 *
 * The pipe is set to O_NONBLOCK which means that if
 * there is nothing to read it returns -1 and errno is
 * set to EAGAIN. Thus I can check if we have been told to quit.
 */
void child_start() {
    if (signal(SIGTERM, &child_sig_handler) == SIG_ERR) {
        perror("ERROR: Could not setup signal handler in child");
        close(pipes[my_id][READ_PIPE]);
        exit(EXIT_FAILURE);
    }
    const unsigned int BUFFER_SIZE_MAX = 1 << 24; // 16777216
    unsigned int BUFFER_SIZE = 1 << 8; // 256
    debug_print("Creating a buffer with size %u\n", BUFFER_SIZE);
    char* buf = (char*) malloc(BUFFER_SIZE);
    if(!buf) {
        perror("ERROR: Could not allocate child buffer\n");
        close(pipes[my_id][READ_PIPE]);
        exit(EXIT_FAILURE);
    }
    memset(buf, 0, BUFFER_SIZE);
    unsigned int buf_pos = 0;
    char c;
    while(1) {
        while (read(pipes[my_id][READ_PIPE], &c, 1) > 0){
            buf[buf_pos] = c;
            buf_pos++;

            if(buf_pos > BUFFER_SIZE-1) {
                BUFFER_SIZE <<= 1;
                debug_print("Increasing buffer size to %u\n", BUFFER_SIZE);
                if(BUFFER_SIZE>=BUFFER_SIZE_MAX) {
                    fprintf(stderr, "ERROR: Max buffer size reached\n");
                    break;
                }
                // Increase buffer size
                void* new_buf = (char*) realloc(buf, BUFFER_SIZE);
                if(!new_buf) {
                    fprintf(stderr, "ERROR: Could not realloc buffer size %u\n", BUFFER_SIZE);
                    break;
                }
                buf = new_buf;
            }

            if(c == '\0') {
                if (my_id == CHILD_1) {
                    debug_print("Child process prints to stdout... \n");
                    printf("%s\n", buf);
                } else if (my_id == CHILD_2) {
                    debug_print("Child process prints to stderr... \n");
                    fprintf(stderr, "%s\n", buf);
                } else {
                    fprintf(stderr, "ERROR: Unknown child id %d\n", my_id);
                }
                buf_pos = 0;
            }
        }
        if(errno != EAGAIN) {
            perror("ERROR: Failed to read");
            close(pipes[my_id][READ_PIPE]);
            exit(EXIT_FAILURE);
        } else {
            if(g_child_stop) {
                break;
            }
            // Sleeping to avoid high CPU usage
            usleep(100);
        }
    }
    if(close(pipes[my_id][READ_PIPE]) == -1){
        debug_print("Closing read pipe... \n");
        perror("WARNING: Could not close child's read pipe");
    }
    free(buf);
}

/**
 * Creating child processes.
 *
 * The children runs child_start() but are not returning from this function.
 *
 * The parent returns the child PID.
 */
pid_t make_child_process() {
    pid_t child;
    debug_print("Creating child process... \n");
    child = fork();
    if (child >= 0) {
        // Immediately close the unused pipes
        if (child ==  0) {
            my_id = child_count;
            if(close(pipes[my_id][WRITE_PIPE]) == 1){
                perror("WARNING: Could not close child's write pipe");
            }
            pipes[my_id][WRITE_PIPE] = -1;
            child_start();
            exit(EXIT_SUCCESS);
        } else {
            if(close(pipes[child_count][READ_PIPE]) == -1){
                perror("WARNING: Could not close parent's read pipe");
            }
            pipes[child_count][READ_PIPE] = -1;
            child_count++;
            return child;
        }
    } else {
        error("Failed to create a child");
    }

    return child;
}

/**
 * Validating read calls by checking the return value
 * and quit if we could not read all bytes.
 *
 * @param fd the file descriptor
 * @param buf the buffer to read from
 * @param count number of bytes to read
 */
void validate_read(int fd, void *buf, size_t count){
    if(read(fd, buf, count) < (ssize_t) count){
        error("ERROR: read failed");
    }
}

unsigned char calculate_checksum(unsigned int text_length, const char* job_text) {
    int checksum = 0;
    for (unsigned int i = 0; i < text_length; i++) {
        checksum += job_text[i];
    }
    return (unsigned char) (checksum % 32);
}

/**
 * Get one or more jobs from the server
 *
 * I chose to send a single message for multiple jobs
 * For 'G' I am also sending a number of jobs that server should send.
 * If the number is 0 it means we ask for all jobs.
 *
 * @param sockfd the socket
 * @param n number of jobs, 0=All
 */
void get_jobs(int sockfd, unsigned int n) {
    unsigned char a[4];
    a[0] = 'G';
    //Fitting n into 3 bytes
    a[3] =  n & 0xff;
    a[2] = (n>>8)  & 0xff;
    a[1] = (n>>16) & 0xff;
    if (n == 0) {
        debug_print("Sending message to a server asking for all jobs...\n");
    } else {
        debug_print("Sending message to a server with %u jobs\n", n);
    }
    if (write(sockfd, &a, 4) != 4) {
        error("Couldn't send message to server");
        return;
    }
    int m = n==0 ? INT_MAX : n;
    for(int i=0; i<m; i++) {
        debug_print("Getting a job from server...\n");
        job j;
        validate_read(sockfd, &j.job_info, 1); // Read job_info
        validate_read(sockfd, &j.text_length, sizeof(int)); // Read text length
        j.job_text = malloc(j.text_length + 1);
        validate_read(sockfd, j.job_text, j.text_length); // Read text
        j.job_text[j.text_length] = '\0';
        unsigned char check_sum = calculate_checksum(j.text_length, j.job_text);
        debug_print("Calculated check sum %u\n", check_sum);
        if (check_sum != (j.job_info & 31)) {
            error("Incorrect check sum, job is ignored");
            free(j.job_text);
            continue;
        }
        switch(j.job_info & 224){
            case 0:
                debug_print("Sending job to child:\n");
                if(write(pipes[CHILD_1][WRITE_PIPE], j.job_text, j.text_length+1) < j.text_length+1){
                    error("ERROR: Could not communicate with child");
                }
                break;
            case 32:
                debug_print("Sending job to child:\n");
                if(write(pipes[CHILD_2][WRITE_PIPE], j.job_text, j.text_length+1) < j.text_length+1){
                    error("ERROR: Could not communicate with child");
                }
                break;
            case 224:
                printf("No more jobs to read\n");
                free(j.job_text);
                kill_children();
                notify_terminate_success();
                break;
            default:
                fprintf(stderr, "WARNING: Invalid job type\n");
                break;
        }
        free(j.job_text);
    }
}

/**
 * Get IP from name ( using first found IPv4 address for simplicity )
 * @param host host name ( for example: localhost )
 * @param port the port number ( for example: 9734 )
 * @param ipstr pointer to string that will be filled with the ip
 * @return -1 on failure, 0 on success
 */
int get_ip_from_name(const char* host, const char* port, char (*ipstr)[INET_ADDRSTRLEN]) {

    struct addrinfo hints;
    struct addrinfo *result, *p;
    memset(&hints, 0, sizeof(struct addrinfo));
    hints.ai_family = AF_INET;       // Allow only IPv4 (for simplicity)
    hints.ai_socktype = SOCK_STREAM; // TCP stream sockets
    hints.ai_flags = AI_PASSIVE;     // Fill in my IP for me
    if(getaddrinfo(host, port, &hints, &result)) {
        return -1;
    }
    p = result;
    void *addr;

    struct sockaddr_in *ipv4 = (struct sockaddr_in *)p->ai_addr;
    addr = &(ipv4->sin_addr);

    // convert the IP to a string
    inet_ntop(p->ai_family, addr, *ipstr, sizeof *ipstr);
    freeaddrinfo(result); // free the linked list
    return 0;
}

/**
 * Main checks all arguments, creates pipes, children, socket and prints a menu.
 *
 * Usage: Hjemmeeksamen_client <Host name> <Server Port>
 *
 */
int main(int argc, char *argv[]) {

    if (argc != 3 && argc != 4){
        fprintf(stderr, "Usage: %s <Host name> <Server Port> [-debug] \n", argv[0]);
        quit(EXIT_FAILURE);
    }

    if(argc == 4) {
        const char* deb = "-debug";
        if (strcmp(deb, argv[3]) == 0) {
            debug = true;
        } else {
            printf("%s", argv[3]);
            fprintf(stderr, "Usage: %s <Host name> <Server Port> [-debug] \n", argv[0]);
            quit(EXIT_FAILURE);
        }
    }

    in_port_t port;
    if(!str_to_uint16(argv[2], &port)){
        fprintf(stderr, "ERROR: Invalid port number\n");
        quit(EXIT_FAILURE);
    }

    char ipstr[INET_ADDRSTRLEN];
    if(get_ip_from_name(argv[1], argv[2], &ipstr) == -1) {
        fprintf(stderr, "ERROR: Could not get host by name\n");
        quit(EXIT_FAILURE);
    }

    debug_print("Setting up the pipes...\n");

    // Creating one pipe per child
    int i = 0;
    for(i=0; i<PIPE_COUNT; i++) {
        if(pipe(pipes[i]) != 0) {
            error("Error creating pipe");
        }
        // Set the read pipe to be non-blocking
        // so I can abort the read process.
        fcntl(pipes[i][READ_PIPE], F_SETFL,
              fcntl(pipes[i][READ_PIPE], F_GETFL) |
              O_NONBLOCK);
    }


    child_one = make_child_process();
    debug_print("Created child process >> %d <<\n", child_one);
    child_two = make_child_process();
    debug_print("Created child process >> %d <<\n", child_two);

    // Connect to server
    socklen_t len;
    struct sockaddr_in address;
    // Create socket for client
    debug_print("Creating socket...\n");
    if((sockfd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP)) == -1) {
        error("Failed to create socket");
    }
    // Name socket as agreed with the server
    address.sin_family = AF_INET;
    address.sin_addr.s_addr = inet_addr(ipstr);
    address.sin_port = htons(port);
    len = sizeof(address);
    debug_print("Connecting to the server: %s:%d\n",
                inet_ntoa(address.sin_addr),
                port);
    if(connect(sockfd, (struct sockaddr*) &address, len) == -1) {
        // Need to close socket before reaching quit this time
        // since quit would try to talk to the server.
        if(close(sockfd)==-1){
            perror("WARNING: Error creating socket");
        }
        sockfd = -1;
        error("Failed to connect to server");
    }

    if(signal(SIGINT, &sig_handler) == SIG_ERR ||
       signal(SIGTERM, &sig_handler) == SIG_ERR) {
        error("ERROR: Could not setup signal handler");
    }

    while (g_running)
    {
        printf("\n");
        printf("[            MENU                  ]\n");
        printf("1 - Get job from the server\n");
        printf("2 - Get several jobs from the server\n");
        printf("3 - Get all the jobs from the server\n");
        printf("4 - Quit\n");
        printf("Choose menu number:\n");
        char input;
        scanf(" %c",&input);
        switch(input) {
            case '1':
                // Get one job from server
                get_jobs(sockfd, 1);
                break;
            case '2':
                printf("How many jobs (1 - 16777215):\n");
                int job_count=0;
                char r;
                if(scanf(" %d%c",&job_count,&r) != 2 || r != '\n') {
                    int c;
                    // Clear stdin before we go back to menu
                    while ((c = getchar()) != '\n' && c != EOF) {}
                    printf("ERROR: Invalid input\n");
                    break;
                }
                if(job_count>0 && job_count < (1 << 24)) {
                    get_jobs(sockfd, (unsigned int) job_count);
                } else {
                    printf("ERROR: Invalid number of jobs\n");
                }
                break;
            case '3':
                get_jobs(sockfd, 0);
                break;
            case '4':
                kill_children();
                debug_print("Notifying server that child processes were terminated...\n");
                notify_terminate_success();
                break;
            default:
                printf("Invalid input, please type again:\n");
        }
    }
}
