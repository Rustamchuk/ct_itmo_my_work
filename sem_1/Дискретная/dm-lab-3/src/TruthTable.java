import java.util.Scanner;

public class TruthTable {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        StringBuilder out = new StringBuilder();
        String cur;
        int m = (int) Math.pow(2, n);
        int len = Integer.toBinaryString(m - 1).length();
        for (int i = 0; i < m - 1; i++) {
            cur = Integer.toBinaryString(i);
            out.append("0".repeat(Math.max(0, len - cur.length())));
            out.append(cur).append('\n');
        }
        out.append(Integer.toBinaryString(m - 1));
        System.out.println(out.toString());
    }
}
