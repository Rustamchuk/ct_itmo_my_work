import java.util.Scanner;

public class Arrays {
    public static int n;
    public static int sum;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        System.out.println();
        for (int i = 1; i <= n; i++) {
            gen(String.valueOf(i), i, 1);
        }
    }

    public static void gen(String p, int last, int len) {
        System.out.println(p);
        if (len == n) {
            return;
        }
        loop:
        for (int i = last + 1; i <= n; i++) {
            gen(p + " " + i, i, len + 1);
        }
    }
}
