import java.util.Scanner;

public class OpenClosed {
    public static int n;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        gen(0, 0, new String());
    }

    public static void gen(int counter_open, int counter_close, String ans) {
        if (counter_open + counter_close == 2 * n) {
            System.out.println(ans);
            return;
        }
        if (counter_open < n)
            gen(counter_open + 1, counter_close, ans + '(');
        if (counter_open > counter_close)
            gen(counter_open, counter_close + 1, ans + ')');
    }
}
