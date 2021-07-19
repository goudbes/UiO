#include <stdio.h>
#include <stdarg.h>
#include <string.h>
typedef unsigned char uc;


extern int sprinter(uc* res, uc* format, ...);

int main() {
  uc t[200];
  int test;
  test = sprinter(t,"%u", 255);
  printf("Test '%s' Ret=%d\n", t, test);
}
