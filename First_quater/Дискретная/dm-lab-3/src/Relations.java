import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Relations {
    public static int n;
    public static int k;

    public static int pos;

    public static int len;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        k = sc.nextInt();
        for (int i = 1; i < n + 1; i++) {
            pos = i;
            len = 1;
            gen(String.valueOf(i));
        }
    }

    public static void gen(String p) {
        if (len == k) {
            System.out.println(p);
            return;
        }
        loop:
        for (int i = pos + 1; i < n + 1; i++) {
            pos = i;
            len++;
            gen(p + " " + i);
            len--;
        }
    }
}
