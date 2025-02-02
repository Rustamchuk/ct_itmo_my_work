import java.util.Arrays;
import java.util.Scanner;

public class DuplicateBalls {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] ballStack = new int[n][2];
        int pos = -1;
        int cnt = 0;
        for (int i = 0; i < n; i++) {
            int cur = sc.nextInt();
            if (pos >= 0) {
                if (ballStack[pos][0] == cur) {
                    ballStack[pos][1] += 1;
                } else {
                    if (ballStack[pos][1] >= 3) {
                        cnt += ballStack[pos][1];
                        pos--;
                    }
                    if (ballStack[pos][0] == cur) {
                        ballStack[pos][1] += 1;
                    } else {
                        ballStack[++pos] = new int[]{cur, 1};
                    }
                }
            } else {
                ballStack[++pos] = new int[]{cur, 1};
            }
        }
        if (ballStack[pos][1] >= 3) {
            cnt  += ballStack[pos][1];
        }
        System.out.println(cnt);
    }
}
