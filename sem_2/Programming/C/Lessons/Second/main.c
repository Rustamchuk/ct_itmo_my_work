#include <stdio.h>

struct A
        {
    double x;
    int y;
        };

union B
{
    float x;
    int y;
};

enum
{
    AB, // 0
    CD = 3, // 3
    EF, // 4
};

int f(int x, float y) {
    return x + y;
}

int g(int x, float y) {
    return x * y;
}

int main() {
    printf("Hello, World!\n");
    int (*pf)(int x, float y);
    pf = f;
    int a = pf(2, 3);
    printf("%i\n", a);

    int b = 2;
    float c = 3;
    int d = (b != c ? g : f)(b, c);
    printf("%i\n", d);

    char *s = "Hello";
    int k = sizeof(s[0]);

    return 0;
}
