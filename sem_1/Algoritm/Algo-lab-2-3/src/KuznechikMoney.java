import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class KuznechikMoney {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[][] dp = new int[n][2];
        dp[0] = new int[] {0, -1};
        for (int i = 1; i < n - 1; i++) {
            int max = Integer.MIN_VALUE;
            for (int j = i - 1; j >= i - k && j >= 0; j--) {
                if (dp[j][0] > max) {
                    max = dp[j][0];
                    dp[i][1] = j;
                }
            }
            dp[i][0] = max + sc.nextInt();
        }
        int max = Integer.MIN_VALUE;
        int i = n - 1;
        for (int j = i - 1; j >= i - k && j >= 0; j--) {
            if (dp[j][0] > max) {
                max = dp[j][0];
                dp[i][1] = j;
            }
        }
        dp[i][0] = max;
        System.out.println(dp[i][0]);
        int len = 0;
        List<Integer> res = new ArrayList<>();
        while (i != 0) {
            len++;
            res.add(i + 1);
            i = dp[i][1];
        }
        res.add(1);
        System.out.println(len);
        for (int j = res.size() - 1; j >= 0; j--) {
            System.out.print(res.get(j) + " ");
        }
    }
}
