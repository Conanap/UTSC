#define DUNGEONSIZE 10
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
// Albion Ka Hei Fung
// June 4, 2016
// 1002444321
// v0.0.4

int main(int argc, char **argv) {
    // you can only play if you give me a map!
	if(argc != 2) {
		fprintf(stderr, "usage: %s file", argv[0]);
		return(1);
	}

    // var init; not wasting operating time so checked first ^
    extern void printMap(char *map[10][10]);
	short i = 0, j = 0, v = 0, h = 0;
	int c, len;
	char *map[DUNGEONSIZE][DUNGEONSIZE];
    char *t = NULL, *in = NULL, *pass = "pass", split[2] = " ", *token;
    size_t size = 0;
    FILE *fp = fopen(argv[1], "r");
    if(fp == NULL) {
        perror(argv[1]);
        return (2);
    }

	// get file info
    while(((len = getline(&t, &size, fp)) != -1) && (i < 10)) {
        if(t[len - 1] == '\n') // removing new line char
            t[len - 1] = '\0';
        token = strtok(t, split); //spliting to tokens
        while(token != NULL && j < 10) {
        	// loop through to alloc
           if ((map[i][j] = malloc((strlen(t) + 1) * sizeof(char))) == NULL) {
                perror("malloc");
                exit(2);
            }
            strcpy(map[i][j], token);
            j++;
            token = strtok(NULL, split); // next token
        } // if rest of the room in row i is pass
        while(j < 10) {
            if((map[i][j] = malloc((strlen(pass) + 1) * sizeof(char))) == NULL) {
                perror("malloc");
                exit(2);
            }
            strcpy(map[i][j], pass);
            j++;
        }
        if( j == 10 && token != NULL) {
            fprintf(stderr, "too many rooms in the horizontal at row %d\n", i);
            exit(2);
        }
        i++;
        j = 0;
    }

    // too many lines
    if(len != -1 && i == 10) {
        fprintf(stderr, "too many rooms in the vertical\n");
        exit(2);
    }

    // if rest of the dungeon is pass
    while( i < 10) {
        while ( j < 10) {
            if((map[i][j] = malloc((strlen(pass) + 1) * sizeof(char))) == NULL) {
                perror("malloc");
                exit(1);
            }
            strcpy(map[i][j], pass);
            j++;
        }
        i++;
        j = 0;
    }

    // freeing mem and closing files and reseting indexies
    free(t);
    fclose(fp);
    i = 0;
    j = 0;
    in = malloc(sizeof(char) * 2000);

    printf("you are at: %s\n", map[i][j]);

    while((fgets(in, 2000, stdin)) != NULL) {
    	switch (c = in[0]) { // what is this key?
    		case 's':
    			// traverse south
    			v = 1;
    			h = 0;
    			break;
    		case 'w':
    			//traverse west
    			v = 0;
    			h = -1;
    			break;

    		case 'n':
    			//traverse north
    			v = -1;
    			h = 0;
    			break;
    		case 'e':
    			//traverse e
    			v = 0;
    			h = 1;
    			break;
            case 'm':
                // see the map you cheater
                v = 0;
                h = 0;
                break;
            case'\n':
                // getchar takes the entre as a char too...
                v = 2;
                break;
    		default:
                // invalid input
    			fprintf(stderr, "Type n, w, e, s, or m.\n");
    			v = 2;
				break;
    	}

		// first iteration; if we don't do this loop will never run
        if(v != 2) {
            i += v;
            j += h;
            if(j < 0)
                j = DUNGEONSIZE - 1;
            else if(j == DUNGEONSIZE)
                j = 0;
            if(i < 0)
                i = DUNGEONSIZE - 1;
            else if(i == DUNGEONSIZE)
                i = 0;
        }
        // find the next non pass room
		while(!strcmp(map[i][j], "pass") && v != 2) {
			i += v;
			j += h;
            // stay in bounds
			if(j < 0)
				j = DUNGEONSIZE - 1;
			else if(j == DUNGEONSIZE)
				j = 0;
			if(i < 0)
				i = DUNGEONSIZE - 1;
			else if(i == DUNGEONSIZE)
				i = 0;
		}
        // output what's required
        if(v == h)
            printMap(map);
        else if (v != 2) // only print current spot if valid input
            printf("You are at: %s\n", map[i][j]);
    }
    
    free(in);
    return(0);
}

void printMap(char *map[10][10]) {
    int i = 0, j = 0;
    while (i < 10) {
        while (j < 10) {
            if(j != 9)
                printf("%s ", map[i][j]);
            else // only new line if we're at the end of dungeon
                printf("%s\n", map[i][j]);
            j++;
        }
        i++;
        j = 0;
    }
}