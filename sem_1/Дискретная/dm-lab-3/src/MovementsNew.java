import java.util.Scanner;

public class MovementsNew {
    public static int n;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        for (int i = 1; i < n + 1; i++) {
            gen(String.valueOf(i));
        }
    }

    public static void gen(String p) {
        if (p.length() == n) {
            print(p);
            return;
        }
        loop:
        for (int i = 1; i < n + 1; i++) {
            for (int j = 0; j < p.length(); j++) {
                if (p.charAt(j) - '0' == i)
                    continue loop;
            }
            gen(p + i);
        }
    }

    public static void print(String b) {
        for (int i = 0; i < b.length(); i++) {
            System.out.print(b.charAt(i) + " ");
        }
        System.out.println();
    }
}
