//package ChempionShip;

import java.util.HashMap;
import java.util.Scanner;

public class M {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int t = sc.nextInt();

        while (t > 0) {
            int n = sc.nextInt();
            int[] a = new int[n];

            for (int i = 0; i < n; i++) {
                a[i] = sc.nextInt();
            }

            HashMap<Integer, Integer> c = new HashMap<>();
            int cnt = 0;
            for (int j = n - 1; j > 0; j--) {
                for (int i = 0; i < j; i++) {
                    int cur = 2 * a[j] - a[i];
                    if (c.containsKey(cur) && cur >= 0) {
                        cnt += c.get(cur);
                    }
                }
                if (c.containsKey(a[j])) {
                    c.put(a[j], c.get(a[j]) + 1);
                } else {
                    c.put(a[j], 1);
                }
            }
            System.out.println(cnt);
            t--;
        }
    }
}