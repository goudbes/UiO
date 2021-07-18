#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <signal.h>
#include <stdbool.h>
#define INPUT_SIZE 253
sig_atomic_t volatile g_running = true;
struct ruter* map[250];
int map_size = 250;
int size = 0;
char* filename;

struct ruter {
  unsigned char id; //Ruter ID
  char flagg; //flagg
  unsigned char modellengde; // Lengde på produsent/ modell-strengen
  char modell[253];//Produsent/modell
} ru;

void readFile(char* filename_in) {
  FILE *fp;
  filename = filename_in;

  if(access (filename, F_OK)!= -1) {
    int i;
    fp = fopen(filename,"rb");
    int n;
    fread(&n, sizeof(int), 1, fp);
    char ignore;
    fread(&ignore, 1, 1, fp);

    size = n;
    for(i=0;i<n;i++)  {
      struct ruter* ru = malloc(256);
      memset(ru, 0, 256);
      fscanf(fp, "%c%c%c", &ru->id, &ru->flagg, &ru->modellengde);
      //printf("%d:: %d %0x %d\n",i, ru->id, ru->flagg, ru->modellengde);
      fread(&ru->modell, sizeof(char), ru->modellengde, fp);
      //printf("%d: %d %0x %d %s\n",i, ru->id, ru->flagg, ru->modellengde, ru->modell);
      map[ru->id] = ru;
    }

    for(i=0;i<n;i++) {
      printf("%d %0x %d %s",map[i]->id, (unsigned char) map[i]->flagg, map[i]->modellengde, map[i]->modell);
    }
    fclose(fp);
  } else {
    fprintf(stderr, "Input file does not exist.\n");
    exit(1);
  }
}

void print_ruter_info() {
  int router_id;
  printf("\n");
  //printf("Information about router: \n");
  printf("Please, enter router id: \n");
  scanf(" %d",&router_id);  // The space makes sure to skip newlines
  if (router_id < 0 || router_id >= map_size) {
    printf("Invalid id.\n");
    return;
  }
  if(map[router_id]==NULL) {
    printf("Router with id %d wasn't found\n", router_id);
  } else {
    printf("Information about the router: %d %04x %d %s",map[router_id]->id, (unsigned char) map[router_id]->flagg, map[router_id]->modellengde, map[router_id]->modell);
  }
}

void remove_router() {
  int router_id;
  printf("\n");
  printf("Please, enter router id: \n");
  scanf(" %d",&router_id);  // The space makes sure to skip newlines
  if (router_id < 0 || router_id >= map_size) {
    printf("Invalid id.\n");
    return;
  }
  if(map[router_id]==NULL) {
    printf("Router with id %d wasn't found\n", router_id);
  } else {
    free(map[router_id]);
    map[router_id]=NULL;
    printf("Router was successfully removed.\n");
  }
}


int wait_for_key() {
  while(true) {
    int key = getchar();
    if( key == '0' || key == '1' ) {
      return key == '1';
    }
  }
}

void change_flag() {
  int router_id;
  printf("\n");
  printf("Please, enter router id: \n");
  scanf(" %d",&router_id);  // The space makes sure to skip newlines
  if (router_id < 0 || router_id >= map_size) {
    printf("Invalid id.\n");
    return;
  }
  if(map[router_id]==NULL) {
    printf("Router with id %d wasn't found\n", router_id);
  } else {
    struct ruter* ru = map[router_id];
    unsigned char flag = ru->flagg;

    flag = flag >> 4;
    printf("Current change number: %x\n", flag );
    if(flag == 15) {
      printf("No more changes allowed\n");
      return;
    }

    flag++;
    flag = flag << 4;

    printf("Change router flags:\n");
    printf("Press 1 if router is in use, press 0 otherwise\n");
    int res = wait_for_key();
    flag |= res;
    printf("Press 1 if router is wireless, press 0 otherwise\n");
    res = wait_for_key();
    flag |= res << 1;
    printf("Press 1 if router supports 5GHz, press 0 otherwise\n");
    res = wait_for_key();
    flag |= res << 2;
    ru->flagg = flag;
    printf("Router flag changed\n");
  }
}


void add_router() {
  char input[INPUT_SIZE] = {0};
  int router_id;
  printf("Please, enter ID for the new router:\n");
  scanf(" %d",&router_id);
  if ((router_id >= 0 && router_id < map_size) && map[router_id]!=NULL) {
    printf("Router with this ID already exists.\n");
    return;
  } else if ((router_id >= 0 && router_id < map_size) && map[router_id]==NULL){
    struct ruter* ru = malloc(256);
    memset(ru, 0, 256);
    ru->id = router_id;
    char flag = 0;
    printf("Adding FLAG to router:\n");
    printf("Press 1 if router is in use, press 0 otherwise\n");
    int res = wait_for_key();
    flag |= res;
    printf("Press 1 if router is wireless, press 0 otherwise\n");
    res = wait_for_key();
    flag |= res << 1;
    printf("Press 1 if router supports 5GHz, press 0 otherwise\n");
    res = wait_for_key();
    flag |= res << 2;
    ru->flagg = flag;
    printf("Please, enter manufacturer/modell:\n");
    char tmp;
    int i = 0;
    //doesn't allow more than INPUT_SIZE, cuts the rest
    while((tmp = fgetc(stdin)) != EOF)
    {
      if (tmp=='\n') {
        if (i==0) {
          continue;
        }
        break;
      }
      if(i==INPUT_SIZE-1) {
        continue;
      }
      input[i++] = tmp;
      //printf("%s\n", input);
    }
    ru->modellengde = i+1;
    strncpy(ru->modell, input, i);
    ru->modell[i] = '\0';

    map[router_id] = ru;
  }  else if(router_id < 0 || router_id>=map_size) {
    printf("Invalid id.\n");
    return;
  }
}


void change_router_modell() {
  char input[INPUT_SIZE] = {0};
  int router_id;
  printf("\n");
  //printf("Information about router: \n");
  printf("Please, enter router id: \n");
  scanf(" %d",&router_id);  // The space makes sure to skip newlines
  if (router_id < 0 || router_id >= map_size) {
    printf("Invalid id.\n");
    return;
  }

  if(map[router_id]==NULL) {
    printf("Router with id %d wasn't found\n", router_id);
  } else {

    printf("Please, enter new manufactorer/modell:\n");
    char tmp;
    int i = 0;
    //doesn't allow more than INPUT_SIZE, cuts the rest
    while((tmp = fgetc(stdin)) != EOF)
    {
      if (tmp=='\n') {
        if (i==0) {
          continue;
        }
        break;
      }
      if(i==INPUT_SIZE-1) {
        continue;
      }
      input[i++] = tmp;
      //printf("%s\n", input);
    }
    map[router_id]->modellengde = i+1;
    strncpy(map[router_id]->modell, input, i);
    map[router_id]->modell[i] = '\0';
  }
}

void write_to_file() {
  FILE* fp;
  fp = fopen(filename, "wb");
  int counter = 0;
  int i;
  //number of routers to add
  for (i=0; i < map_size;i++) {
    if (map[i] != NULL) {
      counter++;
    }
  }
  fwrite((void*) &counter, sizeof(int), 1, fp);
  fwrite("\n",sizeof(char),1,fp);
  //adding the routers
  for (i = 0; i< map_size;i++) {
    if (map[i] != NULL) {
      fwrite(&map[i]->id,sizeof(char),1,fp);
      fwrite(&map[i]->flagg,sizeof(char),1,fp);
      fwrite(&map[i]->modellengde,sizeof(char),1,fp);
      fwrite(&map[i]->modell,sizeof(char),map[i]->modellengde,fp);
    }
  }
  fclose(fp);
  printf("Wrote to file.\n");
}

void free_map(){
  int i;
  for(i=0; i<map_size; i++) {
    free(map[i]);
  }
}

void sig_handler(int signum)
{
  if (signum == SIGINT) {
    g_running = false;
    write_to_file();
    free_map();
    exit(0);
  } //program interrupt signal
}

int main(int argc,char* argv[]) {

  if(argc==2) {
    readFile(argv[1]);

    char input;
    signal(SIGINT, &sig_handler);

    while (g_running)
    {
      printf("\n");
      printf("*************MENU**************************\n");
      printf("1 - Print information about ruter by ID\n");
      printf("2 - Change the flag for ID\n");
      printf("3 - Change manufacturer/modell for ID\n");
      printf("4 - Add a new router \n");
      printf("5 - Remove a router from database\n");
      printf("6 - Quit\n");
      printf("Choose menu number:\n");
      scanf(" %c",&input);
      switch(input) {
        case '1':print_ruter_info(); break;
        case '2':change_flag(); break;
        case '3':change_router_modell(); break;
        case '4':add_router(); break;
        case '5':remove_router();break;
        case '6':
        write_to_file();
        free_map();
        exit(0);
        default:
        printf("Invalid input, please type again:\n");
      }
    }

  } else if(argc > 2) {
    printf("Too many arguments. \n");
  } else {
    printf("One argument expected.\n");
  }
  return 0;
}
