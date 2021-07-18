#include <sys/types.h>
#include <sys/socket.h>
#include <stdio.h>
#include <sys/un.h>
#include <unistd.h>
#include <netinet/in.h>
#include <arpa/inet.h>
#include <stdlib.h>
#include <argp.h>
#include <signal.h>
#include <stdbool.h>
#include <string.h>
#include "job.h"
#include "common.h"

// Filename for file with jobs
char* filename;
// File descriptor for the file with jobs
FILE* fp = NULL;
// End-of-file reached
int eof_reached = 0;
// Sockets
int server_sockfd = -1;
int client_sockfd = -1;
bool debug = false;


/**
 * Quitting the server and cleaning up ( sockets and file )
 * @param exit_status the exit status
 */
void quit(int exit_status) {
    debug_print("Closing server socket... \n");
    if(server_sockfd != -1) {
        if(close(server_sockfd) == -1) {
            perror("WARNING: Error when closing server socket");
        }
        server_sockfd = -1;
    }
    debug_print("Closing client socket... \n");
    if(client_sockfd != -1) {
        if(close(client_sockfd) == -1) {
            perror("WARNING: Error when closing client socket");
        }
        client_sockfd = -1;
    }
    if(fp) {
        debug_print("Closing opened file descriptor... \n");
        fclose(fp);
        fp = NULL;
    }
    exit(exit_status);
}

/**
 * Error handling function.
 *
 * Prints error message.
 *
 * @param errorMessage an error message
 */
void error(char* message) {
    perror(message);
    quit(EXIT_FAILURE);
}

/**
 * Validating write calls by checking the return value
 * and quit if we could not write all bytes.
 *
 * @param fd the file descriptor
 * @param buf the buffer to write to
 * @param count number of bytes to write
 */
void validate_write(int fd, const void *buf, size_t count){
    if(write(fd, buf, count) < (ssize_t) count){
        error("ERROR: write failed");
    }
}

/**
 * Send one job to the client
 * @param client_sockfd the client socket
 * @param do_job a job
 */
void send_job(int client_sockfd, const job *do_job) {
    // Here it could be possible to send all data
    // in a single write. I though it was more clear
    // to send each data element separately.

    switch (do_job->job_info & 224) {
        case 0:
            debug_print("Sending job with type 0 and checksum %d and text length:%d\n",
                       (do_job->job_info & 31), do_job->text_length); break;
        case 32:
            debug_print("Sending job with type E and checksum %d and text length:%d\n",
                       (do_job->job_info & 31), do_job->text_length); break;
        case 224:
            debug_print("Sending job with type Q and checksum %d and text length:%d\n",
                       (do_job->job_info & 31), do_job->text_length); break;
        default: break;
    }
    validate_write(client_sockfd, &do_job->job_info, 1);                  // Send info
    validate_write(client_sockfd, &do_job->text_length, sizeof(int));     // Send text_length
    validate_write(client_sockfd, do_job->job_text, do_job->text_length); // Send job_text
}

/**
 * Quit properly when we get SIGINT (CTRL-C).
 * @param signum the signal
 */
void sig_handler(int signum) {
    if (signum == SIGINT) {
        debug_print("Quitting on SIGINT\n");
        printf("Server aborted\n");
        quit(EXIT_SUCCESS);
    }
}

unsigned char calculate_checksum(unsigned int text_length, const char* job_text) {
    int checksum = 0;
    for (unsigned int i = 0; i < text_length; i++) {
        checksum += job_text[i];
    }
    debug_print("Calculated jobs check sum %d\n", checksum % 32);
    return (unsigned char) (checksum % 32);
}

/**
 * Reads one job from the file and returns it.
 * @return the file or NULL if failed
 */
job* read_job() {
    if(eof_reached) {
        debug_print("Reached eof\n");
        return NULL;
    }
    job* j = malloc(sizeof(job));
    if(j==NULL) {
        perror("WARNING: malloc failed");
        return NULL;
    }
    memset(j, 0, sizeof(job));
    if (fread(&j->job_info,1,1,fp) != 1) {
        if(feof(fp)) {
            fclose(fp);
            fp = NULL;
            eof_reached = 1;
            free(j);
            return NULL;
        }        
        error("WARNING: Couldn't read job type");
        free(j);
        return NULL;
    }
    if (fread(&j->text_length, sizeof(unsigned int),1,fp) != 1) {
        error("WARNING: Couldn't read text length");
        free(j);
        return NULL;
    }
    debug_print("Text length: %u\n", j->text_length);
    // If the text length is missing or corrupt it can be anything
    // prevent it from using extreme values.
    if(j->text_length > 50000000) {
        fprintf(stderr, "WARNING: %u > max text length limit. Aborting.\n", j->text_length);
        free(j);
        return NULL;
    }

    //We are filling in first 3 bits in job_info depending on job type
    switch (j->job_info) {
        case 'O':
            j->job_info = 0; // 000
            break;
        case 'E':
            j->job_info = 32; // 001
            break;
        case 'Q':
            j->job_info = 224; // 111
            break;
        default:
            perror("WARNING: Unknown job type");
            free(j);
            return NULL;

    }
    j->job_text = malloc(j->text_length+1);
    if(j->job_text==NULL) {
        perror("WARNING: malloc failed");
        return NULL;
    }

    size_t bytes_read = fread(j->job_text, 1 ,j->text_length,fp);
    if(bytes_read < j->text_length) {
        fprintf(stderr, "WARNING: Found job with too short text\n");
        free(j->job_text);
        free(j);
        // This is most likely due to eof reached
        // but could possibly be another read error.
        // I treat it the same, that there is nothing more to read.
        return NULL;
    }
    j->job_text[j->text_length] = '\0';
    // Calculated check sum and adding it to info, and since it is
    // always less than 32, it won't change more than 5 bits
    j->job_info += calculate_checksum(j->text_length, j->job_text);
    debug_print("Job read successfully from file...\n");
    return j;
}

/**
 * This function takes a filename as an argument
 * and tries to open it.
 *
 * @param filename_in filename
 * @return file descriptor on success or NULL on error
 */
 FILE* readfile(char* filename_in) {

    if(access (filename_in, F_OK)!= -1) {
        //opening a file
        debug_print("Opening file %s for reading...\n", filename_in);
        FILE* fp = fopen(filename_in,"rb");
        if(fp == NULL) {
            error("Could not open file");
        }
        return fp;

   } else {
        fprintf(stderr, "Input file %s does not exist.\n", filename_in);
        quit(EXIT_FAILURE);
    }
    return NULL;
}

/**
 * Creates and opens a socket and listens.
 * @param port the port number
 */
void setup_connection(in_port_t port) {
    socklen_t server_len;
    struct sockaddr_in server_address;

    debug_print("Creating socket...\n");
    //Creating socket
    server_sockfd = socket(AF_INET, SOCK_STREAM, IPPROTO_TCP);
    if (server_sockfd == -1) {
        error("Error opening socket");
    }

    // If socket is in TIME_WAIT state, reuse it anyway.
    int yes = 1;
    if (setsockopt(server_sockfd, SOL_SOCKET, SO_REUSEADDR, &yes, sizeof(int))) {
        error("ERROR: Could not set socket option");
    }

    //Naming the socket
    server_address.sin_family = AF_INET;
    server_address.sin_addr.s_addr = htonl(INADDR_ANY);
    //Making sure it has host byte order
    server_address.sin_port = htons(port);
    //size of server address structure
    server_len = sizeof(server_address);
    //Hooks up socket with server address and port

    // Printing only port to avoid hardoding a default network interface
    // ( the machine might have multiple IP's)
    debug_print("Using port %d\n", port);

    if (bind(server_sockfd, (struct sockaddr *) &server_address, server_len)<0) {
        error("ERROR: binding");
    }

    //create connection queue and listen
    debug_print("Start listening...\n");
    if (listen(server_sockfd,1)== -1) {
        error("ERROR: listening");
    }
}

/**
 * Main checks all arguments and creates sockets and starts waiting for
 * a client to be connected.
 *
 * Usage: Hjemmeeksamen_server <File name> <Server Port>
 */
int main(int argc, char **argv) {

    if (argc != 3 && argc != 4){
        fprintf(stderr, "Usage: %s <File name> <Server Port> [-debug] \n", argv[0]);
        quit(EXIT_FAILURE);
    }

    if(argc == 4) {
        const char deb[7] = "-debug";
        if (strcmp(deb, argv[3]) == 0) {
            debug = true;
        } else {
            printf("%s", argv[3]);
            fprintf(stderr, "Usage: %s <File name> <Server Port> [-debug] \n", argv[0]);
            quit(EXIT_FAILURE);
        }
    }

    filename = argv[1];

    debug_print("Filename: %s\n", filename);
    debug_print("Checking if port number is valid...\n");
    in_port_t port;
    if(!str_to_uint16(argv[2], &port)){
        fprintf(stderr, "ERROR: Invalid port number\n");
        quit(EXIT_FAILURE);
    }

    debug_print("Setting up signal handler...\n");
    if(signal(SIGINT, &sig_handler) == SIG_ERR){
        error("ERROR: Could not setup signal handler");
    }

    fp = readfile(filename);
    setup_connection(port);

    while(1) {
        printf("Server waiting\n");
        struct sockaddr_in client_address;
        socklen_t client_len = sizeof(client_address);
        client_sockfd = accept(server_sockfd,(struct sockaddr*) &client_address,&client_len);
        if(client_sockfd == -1){
            perror("WARNING: Could not accept");
            continue;
        }
        // Close the socket such that only one client can connect
        debug_print("Closing socket, so only one client can connect...\n");
        if(close(server_sockfd) == -1) {
            error("ERROR: Could not close server socket");
        }
        server_sockfd = -1;
        printf("Client connected from %s\n", inet_ntoa(client_address.sin_addr));
        // A client connected, can now listen for incoming messages
        while(1) {
            char ch;
            // maximum 4 bytes
            if(read(client_sockfd, &ch, 1) == -1) {
                perror("WARNING: Read error (ch)");
                continue;
            }
            debug_print("Got message from client: %c\n", ch);
            if(ch == 'G') {
                unsigned char chn[3];
                if(read(client_sockfd, &chn, 3) == -1) {
                    error("ERROR: Read error (n)");
                }
                int n = chn[2] + (chn[1] << 8) + (chn[0] << 16);
                int m = n==0 ? INT_MAX : n;
                debug_print("Starting to send %d jobs...\n", m);
                for(int i=0; i<m; i++) {
                    job *do_job = read_job();
                    if(do_job!=NULL) {
                        send_job(client_sockfd, do_job);
                        free(do_job->job_text);
                        free(do_job);
                    } else {
                        // No more jobs to send
                        debug_print("No more jobs to send, sending job type Q... \n");
                        job quit_job;
                        quit_job.job_info = 224;
                        quit_job.text_length = 0;
                        quit_job.job_text = NULL;
                        send_job(client_sockfd, &quit_job);
                        // The client should now exit - await confirmation
                        break;
                    }
                }
            } else if(ch == 'T') {
                printf("Client disconnected\n");
                printf("Server shutting down\n");
                quit(EXIT_SUCCESS);
            } else if(ch == 'E') {
                fprintf(stderr, "Client disconnected with ERROR\n");
                printf("Server shutting down\n");
                quit(EXIT_SUCCESS);
            }
        }
    }
}



