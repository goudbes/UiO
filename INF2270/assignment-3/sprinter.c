#include <stdio.h>  // printf
#include <stdlib.h> // malloc
#include <stdarg.h> // va_arg


char* itoa(long long i, char b[]){
    char const digit[] = "0123456789";
    char* p = b;
    if(i<0){
        *p++ = '-';
        i *= -1;
    }
    long long shifter = i;
    do{ //Move to where representation ends
        ++p;
        shifter = shifter/10;
    }while(shifter);
    *p = '\0';
    do{ //Move back, inserting digits as u go
        *--p = digit[i%10];
        i = i/10;
    }while(i);
    return b;
}

char* uitoa(unsigned int i, char b[]){
    char const digit[] = "0123456789";
    char* p = b;
    unsigned int shifter = i;
    do{ //Move to where representation ends
        ++p;
        shifter = shifter/10;
    }while(shifter);
    *p = '\0';
    do{ //Move back, inserting digits as u go
        *--p = digit[i%10];
        i = i/10;
    }while(i);
    return b;
}

char* itox(unsigned int i, char* b){
    char const digit[] = "0123456789abcdef";
    char* p = b;
    unsigned int shifter = i;
    do{ //Move to where representation ends
        ++p;
        shifter = shifter/16;
    }while(shifter);
    *p = '\0';
    do{ //Move back, inserting digits as u go
        *--p = digit[i%16];
        i = i/16;
    }while(i);
    return b;
}

int sprinter(unsigned char* res, unsigned char* format, ...) {

  va_list args;
  va_start(args, format);
  
  int j=0; // points at current res pos
  char* val = NULL;
  
  for(int i=0; ; i++) {

    switch(format[i]) {
    case '\0':
      res[j] = '\0';
      return j;
    case '%':
      switch(format[i+1]) {
	// c d s u x #x %
      case 'c':
	// int instead of char, since compiler complains otherwise
	res[j++] = va_arg(args, int);
	break;
      case 'd':
	val = (char*) calloc(32, 1);
	val = itoa(va_arg(args, int), val);
	for(int k=0; val[k] != '\0'; k++) {
	  res[j++] = val[k];
	}
	free(val);
	break;
      case '%':
	res[j++] = '%';
	break;
      case '#':
	if(format[i+2] == 'x') {
	  val = (char*) calloc(32, 1);
	  val[0] = '0';
	  val[1] = 'x';
	  itox(va_arg(args, int), val+2);
	  for(int k=0; val[k] != '\0'; k++) {
	    res[j++] = val[k];
	  }
	  free(val);
	  i++;
	} else {
	  return -1;
	}
	break;
      case 'x':
	val = (char*) calloc(32, 1);
	val = itox(va_arg(args, int), val);
	for(int k=0; val[k] != '\0'; k++) {
	  res[j++] = val[k];
	}
	free(val);
	break;
      case 'u':
	val = (char*) calloc(32, 1);
	val = uitoa(va_arg(args, unsigned int), val);
	for(int k=0; val[k] != '\0'; k++) {
	  res[j++] = val[k];
	}
	free(val);
	break;
      case 's':
	val = va_arg(args, char*);
	for(int k=0; val[k] != '\0'; k++) {
	  res[j++] = val[k];
	}
	break;
      default:
	return -1;
      }
      i++;
      break;
    default:
      res[j++] = format[i];
    }
  }  
}

/*
int main(void) {

  unsigned char* res = (unsigned char*) calloc(128, 1);
  int ret = sprinter(res, (unsigned char*) "henrik %s hei", "taxi");
  if( ret < 0) {
    printf("ERROR: sprinter returned failure\n");
    return 1;
  }

  printf("%s\n", res);
  printf("Returned: %d\n", ret);
  
  return 0;
}

*/
