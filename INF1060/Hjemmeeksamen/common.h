#ifndef HJEMMEEKSAMEN_COMMON_H
#define HJEMMEEKSAMEN_COMMON_H

#include <stdlib.h>
#include <stdbool.h>
#include <stdint.h>
#include <errno.h>
#include <stdarg.h>
#include <stdio.h>
#include <unistd.h>

/**
 * Convert a string to uint16_t safely.
 *
 * Taken from Stackoverflow to ensure valid port range.
 *
 * @param str The string to convert
 * @param res The output value
 * @return false if the string could not be converted
 */
bool str_to_uint16(const char *str, uint16_t *res) {
    char *end;
    errno = 0;
    long val = strtol(str, &end, 10);
    if (errno || end == str || *end != '\0' || val < 0 || val >= 0x10000) {
        return false;
    }
    *res = (uint16_t)val;
    return true;
}

extern bool debug;

/**
 * Print message only if debug is enabled
 *
 * Based on example for vprintf at: http://www.cplusplus.com/reference/cstdio/vprintf/
 *
 * @param format format string like printf
 * @param ... variable number of arguments
 */
void debug_print(const char* format, ... ) {
    if(debug) {
        printf(">> %d << ", getpid());
        va_list args;
        va_start(args, format);
        vprintf(format, args);
        va_end(args);
    }
}

#endif //HJEMMEEKSAMEN_COMMON_H
