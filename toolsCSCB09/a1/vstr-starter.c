#include <stdio.h>

int main(int argc, char **argv)
{
    int from, to;
    if (argc != 3) {
        fprintf(stderr, "usage: vstr fromchar tochar\n");
        return(1);
    }
    from = argv[1][0];
    to = argv[2][0];

    int c;

    while ((c = getchar()) != EOF) {
        putchar(c);
    }

    return(0);
}
