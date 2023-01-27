import java.util.Scanner;

public class J {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = Integer.parseInt(sc.nextLine());
        int[][] d = new int[n][n];
        for (int i = 0; i < n; i++) {
            String s = sc.nextLine();
            for (int j = 0; j < n; j++) {
                d[i][j] = Integer.parseInt(s.charAt(j) + "");
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = i + 1; j < n; j++) {
                int cur = 0;
                for (int k = i + 1; k <= j - 1; k++) {
                    cur += d[i][k] * d[k][j];
                }
                cur %= 10;
                if (cur != d[i][j]) {
                    d[i][j] = 1;
                } else {
                    d[i][j] = 0;
                }
            }
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n; j++) {
                System.out.print(d[i][j]);
            }
            System.out.println("");
        }
    }
}