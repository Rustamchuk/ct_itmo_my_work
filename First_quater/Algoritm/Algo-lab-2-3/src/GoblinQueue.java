import java.util.Arrays;
import java.util.Scanner;

public class GoblinQueue {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        String cur;

        int n = sc.nextInt() + 1;
        int[] left = new int[n];
        int[] right = new int[n];
        int startL = 0;
        int startR = 0;
        int lenL = 0;
        int lenR = 0;
        int posL = 0;
        int posR = 0;

        int number;

        for (int i = 1; i < n; i++) {
            cur = sc.next();
            if (cur.equals("+")) {
                lenR++;
                right[posR] = sc.nextInt();
                posR = right[posR];
            } else if (cur.equals("*")) {
                lenR++;
                int nw = right[startR];
                right[startR] = sc.nextInt();
                right[right[startR]] = nw;
                if (lenR == 1)
                    posR = right[startR];
            } else {
                lenL--;
                System.out.println(left[startL]);
                startL = left[startL];
            }
            if (lenL < lenR) {
                lenL++;
                lenR--;
                left[posL] = right[startR];
                posL = left[posL];
                startR = right[startR];
            }
//            int ind = startL;
//            for (int j = 0; j < lenL; j++) {
//                System.out.print(left[ind] + " ");
//                ind = left[ind];
//            }
//            System.out.println(" - end L");
//            ind = startR;
//            for (int j = 0; j < lenR; j++) {
//                System.out.print(right[ind] + " ");
//                ind = right[ind];
//            }
//            System.out.println(" - end R");
        }
    }
}
