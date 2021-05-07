#include <stdlib.h>
#include <stdio.h>
#include <string.h>

#define FILE_SIZE 1000
#define LINE_SIZE 12
#define on_error(...) { fprintf(stderr, __VA_ARGS__); fflush(stderr); exit(1); }

void echo(char *arg){
    char input[LINE_SIZE];
    printf("sb: %p\n", input);
    strcpy(input, arg);
    printf("Echo response: %s\n", input);
}

int main(int argc, char **argv){
    if (argc < 2) on_error("Usage: %s [argument]\n", argv[0]);
    char text[FILE_SIZE];
    printf("lb: %p\n", text);
    FILE *file;
    file = fopen(argv[1], "r");
    fread(text, sizeof(char), FILE_SIZE, file);
    fclose(file);
    text[strlen(text)-1] = 0;
    char *line = strtok(strdup(text), "\n");
    while(line) {
       echo(line);
       line  = strtok(NULL, "\n");
    }
    return 0;
}