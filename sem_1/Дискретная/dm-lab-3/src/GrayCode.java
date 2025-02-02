import java.util.Arrays;
import java.util.Scanner;

public class GrayCode {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[][] arr = new int[(int) Math.pow(2, n)][n];
        int cur = 2;
        StringBuilder a = new StringBuilder();
        for (int i = n - 1; i >= 0; i--) {
            for (int j = cur / 2; j < cur; j++) {
                arr[j][i] = 1;
            }
            if (i != 0) {
                for (int j = 0; j < cur; j++) {
                    for (int k = i; k < n; k++) {
                        arr[j + cur][k] = arr[cur - j - 1][k];
                    }
                }
                cur *= 2;
            } else {
                for (int j = 0; j < arr.length; j++) {
                    for (int k = 0; k < arr[j].length; k++) {
                        a.append(arr[j][k]);
                    }
                    a.append('\n');
                }
            }
        }
        System.out.println(a.toString());
    }
}
