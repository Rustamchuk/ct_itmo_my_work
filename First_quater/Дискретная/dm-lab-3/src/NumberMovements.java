import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class NumberMovements {
    public static int n;
    public static long k;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        k = sc.nextLong();
        List<Integer> a = new ArrayList<>();
        for (int i = 1; i < n + 1; i++) {
            a.add(i);
        }
        for (int i = 1; i < n + 1; i++) {
            long f = 1;
            for (int j = 1; j <= n - i; j++) {
                f *= j;
            }
            long ans = (k / f);
//            System.out.println("(" + ans + ")");
            if ((int) ans < 0)
                ans -= ans + ans;
            System.out.print(a.get((int) ans) + " ");
            a.remove((int) ans);
            k = (k % f);
        }
    }
}