import java.util.Scanner;

public class TurtleMoney {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        int[][][] board = new int[n][m][2];
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < m; j++) {
                board[i][j][0] = sc.nextInt();
                int left = Integer.MIN_VALUE;
                int up = Integer.MIN_VALUE;
                if (i > 0)
                    up = board[i - 1][j][0];
                if (j > 0)
                    left = board[i][j - 1][0];
                if (i != 0 || j != 0) {
                    if (up > left)
                        board[i][j][1] = 1;
                    else
                        board[i][j][1] = 0;
                    board[i][j][0] += Math.max(up, left);
                }
            }
        }
        int i = n - 1;
        int j = m - 1;
        System.out.println(board[i][j][0]);
        StringBuilder a = new StringBuilder();
        while (i != 0 || j != 0) {
            if (board[i][j][1] == 1) {
                i--;
                a.append("D");
            } else {
                j--;
                a.append("R");
            }
        }
        System.out.println(a.reverse());
    }
}
