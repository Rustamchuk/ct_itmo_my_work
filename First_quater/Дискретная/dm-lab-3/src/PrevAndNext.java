import java.util.Scanner;

public class PrevAndNext {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        StringBuilder c = new StringBuilder(sc.next());
        previous(new StringBuilder(c.toString()));
        next(new StringBuilder(c.toString()));
    }

    public static void previous(StringBuilder c) {
        if (c.charAt(c.length() - 1) == '1') {
            c.delete(c.length() - 1, c.length());
            c.append(0);
            System.out.println(c.toString());
        } else {
            int ind = c.length() - 1;
            while (ind >= 0 && c.charAt(ind) != '1') {
                ind--;
            }
            if (ind < 0) {
                System.out.println("-");
            } else {
                int g = c.length();
                c.delete(ind, c.length());
                c.append(0);
                c.append("1".repeat(g - ind - 1));
                System.out.println(c.toString());
            }
        }
    }

    public static void next(StringBuilder c) {
        if (c.charAt(c.length() - 1) == '0') {
            c.delete(c.length() - 1, c.length());
            c.append(1);
            System.out.println(c.toString());
        } else {
            int ind = c.length() - 1;
            while (ind >= 0 && c.charAt(ind) != '0') {
                ind--;
            }
            if (ind < 0) {
                System.out.println("-");
            } else {
                int g = c.length();
                c.delete(ind, c.length());
                c.append(1);
                c.append("0".repeat(g - ind - 1));
                System.out.println(c.toString());
            }
        }
    }
}
