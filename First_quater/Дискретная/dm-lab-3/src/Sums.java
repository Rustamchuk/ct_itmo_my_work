import java.util.LinkedHashMap;
import java.util.Scanner;

public class Sums {
    public static int n;
    public static int sum;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        for (int i = 1; i <= n; i++) {
            gen(String.valueOf(i), i, i);
        }
    }

    public static void gen(String p, int sum, int last) {
        if (sum == n) {
            System.out.println(p);
            return;
        } else if (sum > n) {
            return;
        }
        loop:
        for (int i = 1; i < n; i++) {
            if (last <= i)
                gen(p + "+" + i, sum + i, i);
        }
    }
}
