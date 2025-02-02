import java.util.Scanner;

public class DIFFERENT_3 {
    public static int n;
    public static int sum;
    public static int len;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        for (int i = 1; i <= n; i++) {
            gen(String.valueOf(i), i, i);
        }
        System.out.println("Answer: " + len);
    }

    public static void gen(String p, int sum, int last) {
        if (sum == n) {
            len++;
            System.out.println(p);
            return;
        } else if (sum > n) {
            return;
        }
        loop:
        for (int i = 1; i < n; i++) {
            if (last < i)
                gen(p + "+" + i, sum + i, i);
        }
    }
}
