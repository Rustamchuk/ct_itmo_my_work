import java.util.Arrays;
import java.util.Scanner;

public class TableMin {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();


        int m = sc.nextInt();

        int[][] table = new int[n][(int) (Math.log(n) / Math.log(2)) + 1];
        table[0][0] = sc.nextInt();
        for (int i = 1; i < n; i++)
            table[i][0] = (23 * table[i - 1][0] + 21563) % 16714589;

        for (int j = 1; (1 << j) <= n; j++) {
            for (int i = 0; (i + (1 << j) - 1) < n; i++) {
                table[i][j] = Math.min(table[i][j - 1], table[i + (1 << (j - 1))][j - 1]);
            }
        }
//        for (int[] line :
//                table) {
//            System.out.println(Arrays.toString(line));
//        }
        int u = sc.nextInt();
        int v = sc.nextInt();
        int r = 0;
        int u2, v2;
        for (int i = 1; i <= m; i++) {
            if (u > v) {
                u2 = v;
                v2 = u;
            } else {
                u2 = u;
                v2 = v;
            }
            u2 -= 1;
            v2 -= 1;
            int j = (int) (Math.log(v2 - u2 + 1) / Math.log(2));
            r = Math.min(table[u2][j], table[v2 - (1 << j) + 1][j]);
//            System.out.println(u + " " + v + " " + r);
            if (i == m) break;
            u = ((17 * u + 751 + r + 2 * i) % n) + 1;
            v = ((13 * v + 593 + r + 5 * i) % n) + 1;
        }
        System.out.println(u + " " + v + " " + r);
    }
}
