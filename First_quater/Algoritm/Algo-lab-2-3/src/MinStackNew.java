import java.util.Arrays;
import java.util.Scanner;

public class MinStackNew {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] minStack = new int[n];
        int pos = -1;
        StringBuilder a = new StringBuilder();
        for (int i = 0; i < n; i++) {
            int cur = sc.nextInt();
            if (cur == 1) {
                pos++;
                if (pos <= 0) {
                    minStack[pos] = sc.nextInt();
                } else {
                    minStack[pos] = Math.min(sc.nextInt(), minStack[pos - 1]);
                }
            } else if (cur == 2) {
                pos--;
            } else if (cur == 3) {
                a.append(minStack[pos]).append(System.lineSeparator());
            }
        }
        System.out.println(a.toString());
    }
}