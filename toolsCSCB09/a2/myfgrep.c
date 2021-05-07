#include <stdio.h>
#include <string.h>
#include <unistd.h>
#include <stdlib.h>
// Albion Ka Hei Fung
// June 8, 2016
// 1002444321
// v0.0.4

int l = 0, h = 0, m = 0, mc = -1;

int main(int argc, char **argv) {

	extern int pOut(int fcount, int found, char *file, char *line);
	extern char *readline(FILE *fp);
	extern void printError(char *argv);

	int len, option, search;
	int fcount = 0, found = 0, tfound = 0;
	char *line = NULL; //*search;
	size_t size = 0;
	FILE *fp;

	// getting options
	while ((option = getopt(argc, argv, "lhm:")) != -1) {
		switch(option) {
			case 'l':
				l = 1;
				if(h)
					h = 0;
				break;

			case 'h':
				// l negates h
				if(!l)
					h = 1;
				break;

			case 'm':
				mc = atoi(optarg);
				m = 1;
				break;

			case '?':
				printError(argv[0]);
				return(2);

			default:
				break;
		}
	}

	search = optind;

	// stdin
	if(++optind == argc) {
		while((mc != 0) && ((len = getline(&line, &size, stdin)) != -1)) {
			if(strstr(line, argv[search])) {
				mc = pOut(fcount, tfound, "\0", line);
				found++;
			}
		}
	}
	// have files to read
	else if(optind < argc) {
		fcount = argc - optind;
		while((mc != -2) && (optind < argc)) {
			if(!strcmp(argv[optind], "-"))
				fp = stdin;
			else if((fp = fopen(argv[optind], "r")) == NULL) {
				perror(argv[optind]);
				return(2);
			}
			while((len = getline(&line, &size, fp))!= -1) {
				if(line != NULL && strstr(line, argv[search])) {
					mc = pOut(fcount, tfound, argv[optind], line);
					tfound++;
				}
			}
			found += tfound;
			tfound = 0;
			fclose(fp);
			optind++;
		}
	} else {
		printError(argv[0]);
		mc = -2;
	}
	if(line != NULL)
		free(line);
	if(mc == -2)
		return(2);
	else if (!found)
		return(1);
	else
		return(0);
}

int pOut(int fcount, int found, char* token, char *line) {
	char *file, colon = ':';
	if(h || fcount < 2) {
		file = "\0";
		colon = '\0';
	}
	else
		file = token;

	if(!l) {
		if(!m)
			printf("%s%c%s", file, colon, line);
		else if(--mc >= 0)
				printf("%s%c%s", file, colon, line);
	}
	else if(!found){
		if(m)
			mc--;
		if(token[0] == '\0' && ((!m) ? mc : mc >= 0))
			printf("stdin\n");
		else if((!m) ? mc : mc >= 0)
			printf("%s\n", token);
	}
	return (mc);
}

void printError(char *argv) { // literally only here cuz I can't fit 80 char
	fprintf(stderr,"usage: %s [-lh] [-m count] searchstring [file...]\n", argv);
}
