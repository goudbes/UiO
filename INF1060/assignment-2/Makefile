CC=gcc
CFLAGS=-Wall -Wextra -g

.PHONY: all clean run check

all: ifidrift

ifidrift: ifidrift.c
		$(CC) $(CFLAGS) $^ -o $@

run: ifidrift
		./ifidrift $(FILENAME)

check: ifidrift
    valgrind --leak-check=full ./ifidrift

clean:
		rm -f ifidrift
