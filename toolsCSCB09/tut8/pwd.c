#include <stdio.h>
#include <sys/types.h>
#include <sys/stat.h>
#include <dirent.h>
#include <unistd.h>

int main() {
	struct stat lstatbuff;
	struct dirent *dirp;
	int inode;
	short found = 1;
	DIR *dir;
	if(lstat(".", &lstatbuff)) {
		perror("lstat(\".\")");
		return(2);
	}
	inode = lstatbuff.st_ino;
	while(inode != 2) {
		if(chdir("..")) {
			perror("chdir(\"..\")");
			return(2);
		}
		if((dir = opendir(".")) == NULL) {
			perror("opendir(\".\")");
			return(2);
		}
		while((dirp = readdir(dir)) && found) {
			if(inode == dirp->d_ino) {
				printf("%s\n", dirp->d_name);
				found = 0;
			}
		}
		found = 1;
		if(lstat(".", &lstatbuff)) {
			perror("lstat(\".\")");
			return(2);
		}
		closedir(dir);
		inode = lstatbuff.st_ino;
	}
	printf("and then we're at the root directory.\n");
	return 0;
}