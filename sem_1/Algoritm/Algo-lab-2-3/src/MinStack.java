import java.lang.constant.DynamicConstantDesc;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class MinStack {
    public static int[] b;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int cur;
        int[][] a = new int[n][2];
        b = new int[n];
        int pos = 0;
        int where;
        for (int i = 0; i < n; i++) {
            cur = sc.nextInt();
            if (cur == 1) {
                a[pos][0] = sc.nextInt();
                a[pos][1] = pos;
                b[pos] = pos;
                siftUp(a, pos);
                pos++;
//                for (int[] k :
//                        a) {
//                    System.out.println(Arrays.toString(k));
//                }
//                System.out.println(Arrays.toString(b));
            } else if (cur == 2) {
                pos--;
                a[b[pos]][0] = a[pos][0];
                a[b[pos]][1] = a[pos][1];
                b[b[pos]] = b[pos];
                a[pos][0] = 0;
                a[pos][1] = 0;
                b[pos] = 0;
                siftDown(a, b[pos], pos);
//                for (int[] k :
//                        a) {
//                    System.out.println(Arrays.toString(k));
//                }
//                System.out.println(Arrays.toString(b));
            } else {
                System.out.println(a[0][0]);
            }
        }
    }

    public static void siftUp(int[][] a, int j) {
        int d = (j - 1) / 2;
        if (a[d][0] > a[j][0]) {
            int cur1 = a[j][0];
            int cur2 = a[j][1];
            b[a[j][1]] = d;
            b[a[d][1]] = j;
            a[j][0] = a[d][0];
            a[j][1] = a[d][1];
            a[d][0] = cur1;
            a[d][1] = cur2;
//            System.out.println(Arrays.toString(b));
            siftUp(a, d);
        }
    }

    public static void siftDown(int[][] a, int i, int max) {
        int d = 2 * i + 1;
        int cur1, cur2;
        if (a[d][0] < a[i][0] && d < max) {
            cur1 = a[i][0];
            cur2 = a[i][1];
            a[i][0] = a[d][0];
            a[i][1] = a[d][1];
            a[d][0] = cur1;
            a[d][1] = cur2;
            b[i] = d;
            b[d] = i;
            siftDown(a, d, max);
            return;
        }
        d++;
        if (a[d][0] < a[i][0] && d < max) {
            cur1 = a[i][0];
            cur2 = a[i][1];
            a[i][0] = a[d][0];
            a[i][1] = a[d][1];
            a[d][0] = cur1;
            a[d][1] = cur2;
            b[i] = d;
            b[d] = i;
            siftDown(a, d, max);
        }
    }
}
