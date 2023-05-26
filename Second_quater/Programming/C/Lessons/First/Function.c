#include <stdio.h>

typedef unsigned int uint;

uint max(uint, uint);

int main(void) {
    uint a, b, c;
    scanf("%i %i %i", &a, &b, &c);
    if (a > b) {
        if (a < c)
            printf("%i", a);
        else if (b > c)
            printf("%i", b);
        else
            printf("%i", c);
    } else {
        if (b < c)
            printf("%i", b);
        else if (a > c)
            printf("%i", a);
        else
            printf("%i", c);
    }

//    scanf("%i %i", &a, &b);
//    uint c = max(a, b);
//    printf("%i", c);
}

uint max(uint a, uint b) {
    uint c = a / b;
    c = 2 - (2 + c) / (c + 1);
    return a * c + b * (1 - c);
}