import java.util.Arrays;
import java.util.Scanner;

public class Undepending {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        double[][] eps = new double[n][m];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                eps[i][j] = sc.nextDouble();
            }
        }

        double ultra;
        double left;
        double right;
        for (int i = 1; i < m; i++) {
            ultra = 0;
            left = 0;
            right = 0;
            for (int j = 0; j < n; j++) {
                ultra += eps[j][0] * eps[j][i] / n;
                left += eps[j][0] / n;
                right += eps[j][i] / n;
            }
            if (Math.abs(ultra - left * right) <= 0.000000000001) {
                System.out.println(i + 1);
            }
        }
        int cnt = 0;
        for (int i = 0; i < m; i++) {
            for (int j = i + 1; j < m; j++) {
                ultra = 0;
                left = 0;
                right = 0;
                for (int k = 0; k < n; k++) {
                    ultra += eps[k][i] * eps[k][j] / n;
                    left += eps[k][i] / n;
                    right += eps[k][j] / n;
                }
                if (Math.abs(ultra - left * right) <= 0.000000000001) {
                    cnt++;
                }
            }
        }
        System.out.println(">>>" + cnt);
        /*
        2
        3
        4
        5
        6
        7
        12
        >>>18
         */
    }
}
