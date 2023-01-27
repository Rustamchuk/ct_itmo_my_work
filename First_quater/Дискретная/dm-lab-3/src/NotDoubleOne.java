import java.util.Scanner;

public class NotDoubleOne {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        StringBuilder out = new StringBuilder();
        String cur;
        int m = (int) Math.pow(2, n);
        int len = Integer.toBinaryString(m - 1).length();
        int a = 0;
        loop:
        for (int i = 0; i < m - 1; i++) {
            cur = Integer.toBinaryString(i);
            int cnt = 0;
            for (int j = 0; j < cur.length(); j++) {
                if (cur.charAt(j) == '1') {
                    cnt++;
                    if (cnt >= 2) {
                        continue loop;
                    }
                } else {
                    cnt = 0;
                }
            }
            out.append("0".repeat(Math.max(0, len - cur.length())));
            out.append(cur).append('\n');
            a++;
        }
        System.out.println(a);
        System.out.println(out.toString());
    }
}
