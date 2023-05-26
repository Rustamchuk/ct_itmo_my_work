import java.util.Scanner;

public class Stars {
    public static long[][][] fenwik;
    public static int n;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        fenwik = new long[n][n][n];
        while (true) {
            int com = sc.nextInt();
            if (com == 1) {
                add(sc.nextInt(), sc.nextInt(), sc.nextInt(), sc.nextLong(), false);
            } else if (com == 2) {
                System.out.println(getSum(sc.nextInt(), sc.nextInt(), sc.nextInt(),
                        sc.nextInt(), sc.nextInt(), sc.nextInt()));
            } else {
                break;
            }
        }
    }

    public static long add(int x, int y, int z, long value, boolean get) {
        int xx, yy, zz;
        xx = x;
        long sum = 0;
        while ((xx < n && !get) || (get && xx >= 0)) {
            yy = y;
            while ((yy < n && !get) || (get && yy >= 0)) {
                zz = z;
                while ((zz < n && !get) || (get && zz >= 0)) {
                    if (get) {
                        sum += fenwik[xx][yy][zz];
                        zz = ((zz + 1) & zz) - 1;
                    } else {
                        fenwik[xx][yy][zz] += value;
                        zz = (zz + 1) | zz;
                    }
                }
                if (get) yy = ((yy + 1) & yy) - 1;
                else yy = (yy + 1) | yy;
            }
            if (get) xx = ((xx + 1) & xx) - 1;
            else xx = ((xx + 1) | xx);
        }
        return sum;
    }

    public static long getSum(int x1, int y1, int z1, int x2, int y2, int z2) {
        return add(x2, y2, z2, 0, true) - add(x1 - 1, y2, z2, 0, true)
                - add(x2, y1 - 1, z2, 0, true) + add(x1 - 1, y1 - 1, z2, 0, true)

                - add(x2, y2, z1 - 1, 0, true) + add(x1 - 1, y2, z1 - 1, 0, true)
                + add(x2, y1 - 1, z1 - 1, 0, true)
                - add(x1 - 1, y1 - 1, z1 - 1, 0, true);
    }
}


/*
Вася любит наблюдать за звездами. Но следить за всем небом сразу ему тяжело. Поэтому он наблюдает только за частью пространства, ограниченной кубом размером n × n × n. Этот куб поделен на маленькие кубики размером 1 × 1 × 1. Во время его наблюдений могут происходить следующие события:

В каком-то кубике появляются или исчезают несколько звезд.
К нему может заглянуть его друг Петя и поинтересоваться, сколько видно звезд в части пространства, состоящей из нескольких кубиков.
Входные данные
Первая строка входного файла содержит натуральное число 1 ≤ n ≤ 128. Координаты кубиков — целые числа от 0 до n - 1. Далее следуют записи о происходивших событиях по одной в строке. В начале строки записано число m. Если m равно:

1, то за ним следуют 4 числа — x, y, z (0 ≤ x, y, z < n) и k ( - 20000 ≤ k ≤ 20000) — координаты кубика и величина, на которую в нем изменилось количество видимых звезд;
2, то за ним следуют 6 чисел — x1, y1, z1, x2, y2, z2 (0 ≤ x1 ≤ x2 < n, 0 ≤ y1 ≤ y2 < N, 0 ≤ z1 ≤ z2 < n), которые означают, что Петя попросил подсчитать количество звезд в кубиках (x, y, z) из области: x1 ≤ x ≤ x2, y1 ≤ y ≤ y2, z1 ≤ z ≤ z2;
3, то это означает, что Васе надоело наблюдать за звездами и отвечать на вопросы Пети. Эта запись встречается во входном файле только один раз и будет последней.
Количество записей во входном файле не больше 100 002.
Выходные данные
Для каждого Петиного вопроса выведите искомое количество звезд.

Пример
входные данныеСкопировать
2
2 1 1 1 1 1 1
1 0 0 0 1
1 0 1 0 3
2 0 0 0 0 0 0
2 0 0 0 0 1 0
1 0 1 0 -2
2 0 0 0 1 1 1
3
выходные данныеСкопировать
0
1
4
2

 */