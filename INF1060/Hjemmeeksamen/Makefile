CC=gcc
CFLAGS=-Wall -Wextra -pedantic -g -std=c99

.PHONY: all clean

all: Hjemmeeksamen_client Hjemmeeksamen_server

Hjemmeeksamen_client: client.c job.h common.h
	$(CC) $(CFLAGS) $^ -o $@

Hjemmeeksamen_server: server.c job.h common.h
	$(CC) $(CFLAGS) $^ -o $@

clean:
	rm -f Hjemmeeksamen_client Hjemmeeksamen_server
