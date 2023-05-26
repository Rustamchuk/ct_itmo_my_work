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
        double multX;
        double multY;
        for (int i = 1; i < m; i++) {
            ultra = 0;
            multX = 0;
            multY = 0;
            for (int j = 0; j < n; j++) {
                ultra += eps[j][0] * eps[j][i] / n;
                multX += eps[j][0] / n;
                multY += eps[j][i] / n;
            }
//            if (String.format("%.3f",ultra).equals(String.format("%.3f", multX * multY)))
            if (Math.abs(ultra - multX * multY) <= 0.00000000001)
//            if (ultra == multX * multY)
                System.out.println(i + 1);
        }
        int cnt = 0;
        for (int x = 0; x < m; x++) {
            for (int y = x + 1; y < m; y++) {
                ultra = 0;
                multX = 0;
                multY = 0;
                for (int j = 0; j < n; j++) {
                    ultra += eps[j][x] * eps[j][y] / n;
                    multX += eps[j][x] / n;
                    multY += eps[j][y] / n;
                }
//                if (String.format("%.11f",ultra).equals(String.format("%.11f", multX * multY)))
                if (Math.abs(ultra - multX * multY) <= 0.000000000001)
//                if (ultra == multX * multY)
                    cnt++;
            }
        }
        System.out.println(cnt);
    }
}
