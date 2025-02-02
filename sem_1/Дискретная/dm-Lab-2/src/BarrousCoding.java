import java.util.Arrays;
import java.util.Scanner;

public class BarrousCoding {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        String start = sc.nextLine();
        int n = start.length();

        String[] words = new String[n];
        words[0] = start;
        StringBuilder cur = new StringBuilder();
        int curInd;
        for (int i = 1; i < n; i++) {
            for (int k = 0; k < n; k++) {
                if (k + i >= n) {
                    curInd = k + i - n;
                } else {
                    curInd = k + i;
                }
                cur.append(start.charAt(curInd));
            }
            words[i] = cur.toString();
            cur.setLength(0);
        }
        Arrays.sort(words);
        for (int i = 0; i < n; i++) {
            System.out.print(words[i].charAt(n - 1));
        }
    }
}
