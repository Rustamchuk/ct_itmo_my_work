import java.util.Scanner;

public class UpperEquence {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] dp = new int[n][3];
        int max = 1;
        int pos = 0;
        int ind = 0;
        for (int i = 0; i < n; i++) {
            dp[pos][0] = sc.nextInt();
            dp[pos][1] = 1;
            for (int j = pos - 1; j >= 0; j--) {
                if (dp[j][0] < dp[pos][0] && dp[j][1] > dp[pos][1] - 1) {
                    dp[pos][1] = dp[j][1] + 1;
                    dp[pos][2] = j;
                    if (dp[pos][1] > max) {
                        max = dp[pos][1];
                        ind = pos;
                        break;
                    }
                }
            }
            if (dp[pos][1] == 1) {
                dp[pos][2] = -1;
            }
            pos++;
        }
        System.out.println(max);
        StringBuilder v = new StringBuilder();
        int[] res = new int[max];
        int i = max - 1;
        while (ind != -1) {
            res[i] = dp[ind][0];
            ind = dp[ind][2];
            i--;
        }
        v.append(res[0]);
        for (int j = 1; j < max; j++) {
            v.append(" ").append(res[j]);
        }
        System.out.println(v.toString());
    }
}
