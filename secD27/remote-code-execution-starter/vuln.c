#include <stdio.h>
#include <stdlib.h>
#include <sys/types.h>
#include <sys/socket.h>
#include <netinet/in.h>
#include <unistd.h>
#include <string.h>
#include <time.h>

#define MAX_SIZE 1000
#define on_error(...) { fprintf(stderr, __VA_ARGS__); fflush(stderr); exit(1); }

int echo(int client_fd){
    char result[MAX_SIZE];
    char input[MAX_SIZE];
    while (1){
        // get input
        int input_len = recv(client_fd, input, MAX_SIZE, 0);
        if (!input_len) break;
        if (input_len < 0) return 1;

        // format result
        int result_len = sprintf (result, "ECHO response (%d bytes received):\n", input_len);
        result_len = result_len + input_len;
        strcat(result, input);

        // send result
        int err = send(client_fd, result, result_len, 0);
        if (err < 0) on_error("Client write failed\n");
    }
}

int main (int argc, char *argv[]) {
  if (argc < 2) on_error("Usage: %s [port]\n", argv[0]);

  int port = atoi(argv[1]);

  int server_fd, client_fd, err;
  struct sockaddr_in server, client;

  server_fd = socket(AF_INET, SOCK_STREAM, 0);
  if (server_fd < 0) on_error("Could not create socket\n");

  server.sin_family = AF_INET;
  server.sin_port = htons(port);
  server.sin_addr.s_addr = htonl(INADDR_ANY);

  int opt_val = 1;
  setsockopt(server_fd, SOL_SOCKET, SO_REUSEADDR, &opt_val, sizeof opt_val);

  err = bind(server_fd, (struct sockaddr *) &server, sizeof(server));
  if (err < 0) on_error("Could not bind socket\n");

  err = listen(server_fd, 128);
  if (err < 0) on_error("Could not listen on socket\n");

  printf("Server is listening on %d\n", port);

  while (1) {
    socklen_t client_len = sizeof(client);
    client_fd = accept(server_fd, (struct sockaddr *) &client, &client_len);

    if (client_fd < 0) on_error("Could not establish new connection\n");

    echo(client_fd);

  }

  return 0;
}