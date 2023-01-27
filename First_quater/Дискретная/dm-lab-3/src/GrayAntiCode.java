import java.util.Arrays;
import java.util.Scanner;

public class GrayAntiCode {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] arr = new int[(int) Math.pow(3, n)][n];
        int cur = 3;
        StringBuilder a = new StringBuilder();
        int l = 0;
        for (int i = n - 1; i >= 0; i--) {
            for (int j = 0; j < arr.length; j++) {
                arr[j][i] = l++;
                if ((j + 1) % cur == 0) {
                    if (l == 3)
                        l = 1;
                    else if (l == 2)
                        l = 0;
                    else
                        l = 2;
                } else if (l > 2) {
                    l = 0;
                }
            }
            cur *= 3;
        }
        for (int i = 0; i < arr.length; i++) {
            for (int j = 0; j < arr[i].length; j++) {
                a.append(arr[i][j]);
            }
            a.append('\n');
        }
        System.out.println(a.toString());
    }
}
