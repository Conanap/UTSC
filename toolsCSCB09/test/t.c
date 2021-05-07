#include <stdio.h>
int main(){
	extern int g(int x, int y, int z);
	printf("%d", g(1, 2, 3));
	return(0);
}
