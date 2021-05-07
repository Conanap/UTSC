#include <unistd.h>
#include <stdio.h>
#include <dirent.h>
#include <string.h>
#include <sys/stat.h>
#include <stdlib.h>

extern void search_dir(char *search_d, char *search_f);

void search_dir(char *search_d, char *search_f) {
        struct stat statbuf;
        stat(search_d, &statbuf);

        if(!S_ISDIR(statbuf.st_mode)) {
                return;
        }

        DIR *dir = opendir(search_d);
        if(dir == NULL) {
                perror("opendir");
                exit(1);
        }

        struct dirent *next;
        int pid;
        char *nextp;

        while((next = readdir(dir)) != NULL) {
                if(strcmp(next->d_name, ".") == 0 || strcmp(next->d_name, "..") == 0) {
                        continue;
                }
                if(strcmp(next->d_name, search_f) == 0) {
                        pid = fork();
                        if(pid == 0) {
                                chdir(search_d);
                                execl("/bin/pwd", "pwd", (char *) 0);
                                perror("execl");
                                exit(1);
                        } else {
                                wait(&pid);
                                if(pid != 0) {
                                        fprintf(stderr, "Something went wrong with exec\n");
                                        exit(1);
                                }
                        }
                } else {
                        nextp = (char *)malloc((strlen(search_d) + strlen(next->d_name) + 2) * sizeof(char));
                        if(!nextp) {
                                perror("malloc");
                                exit(1);
                        }
                        strcpy(nextp, search_d);
                        strcat(nextp, "/");
                        strcat(nextp, next->d_name);
                        search_dir(nextp, search_f);
                }
        }
        closedir(dir);
}

int main (int argc, char ** argv) {
        if(argc != 3) {
                fprintf(stderr, "Usage: myfind searchDir searchName\n");
                exit(1);
        }

        char *search_d = argv[1];
        char *search_f = argv[2];
        struct stat statbuf;

        stat(search_d, &statbuf);

        search_dir(search_d, search_f);
        return 0;
}