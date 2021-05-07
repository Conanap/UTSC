/*
 * fsh.c - the Feeble SHell.
 */

#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <fcntl.h>
#include <sys/types.h>
#include <sys/wait.h>
#include <sys/stat.h>
#include <string.h>
#include "fsh.h"
#include "parse.h"
#include "error.h"

int showprompt = 1;
int laststatus = 0;  /* set by anything which runs a command */
int arg_num = 0;
int optv = 0, opti = 0, optc = 0;


int main(int argc, char **argv)
{
    char buf[10000];
    struct parsed_line *p;
    extern void execute(struct parsed_line *p);
    extern int parse_in_cmd(int argc, char **argv);
    int optind;
    FILE *file;

    optind = parse_in_cmd(argc, argv);

    if(((optind + 1 == argc) || !isatty(0)) && !opti)
        showprompt = 0; // has an optional file

    if(optind + 1 < argc) {
        fprintf(stderr, "%s: [-v] [-i] [{file | -c command}]\n", argv[0]);
        return(1);
    }
    else if(optind + 1 == argc) {
        if((file = fopen(argv[optind], "r")) == NULL) {
            perror(argv[optind]);
            return (1);
        }
        while(fgets(buf, sizeof buf, file)) {
            if(showprompt)
                printf("$ ");
            p = parse(buf);
            execute(p);
            freeparse(p);
        }
        return(0);
    }

    while (1) {
	if (showprompt)
	    printf("$ ");
	if (fgets(buf, sizeof buf, stdin) == NULL)
	    break;
	if ((p = parse(buf))) {
	    execute(p);
	    freeparse(p);
        }
    }

    return(laststatus);
}

int parse_in_cmd(int argc, char **argv) {
    int option;
    while((option = getopt(argc, argv, "vic")) != -1) {
        switch(option) {
            case 'i':
            opti = 1;
            break;
            case 'c':
            optc = 1;
            break;
            case 'v':
            optv = 1;
            break;
        }
    }
    return optind;
}


void execute(struct parsed_line *p)
{
    int status;
    extern void execute_one_subcommand(struct parsed_line *p);
    extern void pipe_execute(struct parsed_line *p);

    fflush(stdout);
    switch (fork()) {
    case -1:
	perror("fork");
	laststatus = 127;
	break;
    case 0:
	/* child */
    if(p->pl->next) // pipe
        pipe_execute(p);
    else // no pipe, just execute command
	   execute_one_subcommand(p);
	break;
    default:
	/* parent */
	wait(&status);
	laststatus = status >> 8;
	}
}

/*
 * execute when there's 2 commands 1 pipe
 */
void pipe_execute(struct parsed_line *p) {
    int pipefd[2];
    extern void execute_one_subcommand(struct parsed_line *p);

    if(pipe(pipefd)) { // exit if pipe fails
        perror("pipe");
        exit(1);
    }

    fflush(stdout);
    switch(fork()) {
        case -1:
        perror("fork");
        laststatus=127;
        break;
        case 0:
        // chiild
        close(pipefd[0]);
        dup2(pipefd[1], 1);
        close(pipefd[1]);
        if(p->pl->next->isdouble) // \&
            dup2(1, 2);
        execute_one_subcommand(p);
        default:
        // parent
        close(pipefd[1]);
        dup2(pipefd[0], 0);
        close(pipefd[0]);
        arg_num++;
        execute_one_subcommand(p);
    }
}


/*
 * execute_one_subcommand():
 * Do file redirections if applicable, then [you can fill this in...]
 * Does not return, so you want to fork() before calling me.
 */
void execute_one_subcommand(struct parsed_line *p)
{
	extern char **environ;
	char *location[3], *loc, **arg_loc, **print_arg;
	location[0] = "/bin/";
	location[1] = "/usr/bin/";
	location[2] = "./";
	struct stat statbuff;
	int i = 0, found = -1;
    // only redirect at end of pipe
    int redirect = ((p->pl->next && arg_num == 1) || (p->pl->next == NULL && arg_num == 0));

    if(arg_num == 1)
        arg_loc = p->pl->next->argv;
    else
        arg_loc = p->pl->argv;

    if (p->inputfile) { // input redirect
		close(0);
		if (open(p->inputfile, O_RDONLY, 0) < 0) {
	    	perror(p->inputfile);
	    	exit(1);
		}
    }

    if(optv) {
        for(print_arg = arg_loc; *print_arg; print_arg++)
            printf("%s ", *print_arg);
        printf("\n");
    }

    if (p->outputfile && redirect) { // output redirect
		close(1);
		if (open(p->outputfile, O_WRONLY|O_CREAT|O_TRUNC, 0666) < 0) {
	    	perror(p->outputfile);
	    	exit(1);
		}
    }
    if(p->output_is_double) // >&
        dup2(1,2);

    if (p->pl) {
    	if(!strchr(arg_loc[0], '/')) { // finding right path
    		while(i < 3 && found) {
    			loc = efilenamecons(location[i], arg_loc[0]);
    			found = stat(loc, &statbuff);
    			i++;
    		}
    	} else // if a / is in argv[0], don't ammend it
    		loc = arg_loc[0];
    	if(i < 3) { // prevent array out of index (ie runs only if command found)
    		if(execve(loc, arg_loc, environ)) {
                perror(loc);
                exit(1);
            }
        }
    	else { // command not found
    		fprintf(stderr, "%s: Command not found\n", arg_loc[0]);
    		exit(1);
    	}
    }
    exit(0);
}
