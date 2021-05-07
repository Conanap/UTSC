#include <stdio.h>
#include <sys/stat.h>
#include <sys/types.h>
#include <dirent.h>
#include <stdlib.h>
#include <string.h>
// Albion Ka Hei Fung
// June 4, 2016
// 1002444321
// v0.0.4

int main(int argc, char **argv) {
	if(argc < 3) {
		fprintf(stderr, "usage: %s file dir [...]\n", argv[0]);
		return (2);
	}

	extern int look4dir(char *path, DIR *dir, char *curr);
	int i = 2, count = 0, status;
	DIR *dir;

	// loop for file in each dir
	while (i < argc) {
		if((dir = opendir(argv[i])) != NULL) {
			status = look4dir(argv[1], dir, argv[i]);
			// -1 means no found
			if(status != -1)
				count += status;
		} else {
			perror(argv[i]);
			return(2);
		}
		i++;
	}
	// found nothing then we return 1, 0 o/w
	(count > 0) ? (status = 0) : (status = 1);
	return (status);
}

int look4dir(char *name, DIR *dir, char *curr) {
	struct stat lstatbuf, statbuf;
	struct dirent *dirp;
	int found = 0, status, len;
	char *fpath = NULL;
	DIR *temp = NULL;

	while((dirp = readdir(dir))) {
		// length of path; can't fit in 80 chars
		len = strlen(curr) + strlen(dirp->d_name) + 2;
		// alloc mem for the path
		if((fpath = malloc(sizeof(char) * len)) == NULL) {
			perror("malloc"); // cant allocß
			exit(2);
		}
		// reset path b/c I pass this for recur call
		strcpy(fpath, curr);
		// prevent repeating / (breaks code)
		if(fpath[strlen(curr) - 1] != '/')
			strcat(fpath, "/");
		strcat(fpath, dirp->d_name);
		if(lstat(fpath, &lstatbuf)) { //can't get lstat
			perror(dirp->d_name);
			exit(2);
		} // if looking for . or ..
		if(!strcmp(dirp->d_name, ".") || !strcmp(dirp->d_name, "..")) {
			// 2 if bc if name!=.||.., would recursive check them, saves code
			if(!strcmp(dirp->d_name, name)) {
				printf("%s\n", fpath);
				found++;
			}
		} // skips . and ..; else will suffice
		else {
			// name match & reg file
			if(S_ISREG(lstatbuf.st_mode) && !strcmp(name, dirp->d_name)) { 
				// pwd
				printf("%s\n", fpath);
				found++;
			}else if(S_ISDIR(lstatbuf.st_mode) && strcmp(name, dirp->d_name)) { 
				// directory, recursive looking
				if((temp = opendir(fpath)) != NULL) {
					status = look4dir(name, temp, fpath);
					if(status != -1)
						found += status;
				}
			}else if(S_ISDIR(lstatbuf.st_mode) && !strcmp(name, dirp->d_name)) {
				// pwd since dir name match
				printf("%s\n", fpath);
				found++;
			}else if (S_ISLNK(lstatbuf.st_mode)){ //symlink
				if(stat(dirp->d_name, &statbuf)) { // cant get stat
					perror(dirp->d_name);
					exit(2);
				}
				// not else if since exit()
				if(S_ISREG(statbuf.st_mode) && !strcmp(name, dirp->d_name)) {
				// pwd since file name match
					printf("%s\n", fpath);
					found++;
				}else if(S_ISDIR(statbuf.st_mode)&& strcmp(name,dirp->d_name)) {
					// links to directory, reccursive looking
					if((temp = opendir(fpath)) != NULL) {
						status = look4dir(name, temp, fpath);
						if(status != -1)
							found += status;
					}
				}else if(S_ISDIR(statbuf.st_mode)&& !strcmp(name,dirp->d_name)){
					// dir name matches, pwd
					printf("%s\n", fpath);
					found++;
				}
			}
		}
		free(fpath); // free dat string
	}
	closedir(dir); // gotta close dat dir
	!found ? found-- : found; // a return status
	return(found);
}
