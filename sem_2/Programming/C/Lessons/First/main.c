#include <stdio.h>
#include <string.h>
#include <stdlib.h>

char *printHello();

int main(void) {
    printf("Hello world!\n");
    printf("I am %i and I am %s", 20 - 1, "Rustam\n");
    for (int i = 0; i < 5; ++i) {
        printf("I know C %i seconds\n", i + 1);
    }
    int a = 0;
    int b = 1;
    printf("%i\n", a);
    scanf("%i %i", &a, &b); // input
    printf("%i %i\n", a, b);
//    char *out = printHello();
//    printf("%s", out);
    FILE *in = fopen("i"
                     "n.txt", "r");
    fscanf(in, "%i %i", &a, &b);
    printf("%i %i\n", a, b);

    int h, w, x;
    int b[h*w];
    b[h*w+x] // двумерный

    int q[y][x]; // NONONONONONONONONNONONO переполнение стэка
    int (*q)[x] = malloc(y** *sizeof(int)); // размер массива это всегда переменная
    
    return 0;
}

//char *ptintHello() {
//    return "Hello";
//}

