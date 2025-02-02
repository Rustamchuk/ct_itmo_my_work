import java.util.Scanner;

public class NextMultiMovement {
    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        int n = sc.nextInt();
//        int[] a = new int[n];
//        for (int i = 0; i < n; i++) {
//            a[i] = sc.nextInt();
//        }
//        int ind = n - 2;
//        while (ind >= 0 && a[ind] >= a[ind + 1])
//            ind--;
//        if (ind < 0) {
//            System.out.println("0 ".repeat(n));
//        } else {
//            int sec = ind + 1;
//            while (sec < n - 1 && a[sec + 1] > a[ind])
//                sec++;
//            int cur = a[ind];
//            a[ind] = a[sec];
//            a[sec] = cur;
//            StringBuilder b = new StringBuilder();
//            for (int k = 0; k < ind + 1; k++) {
//                b.append(a[k]).append(" ");
//            }
//            for (int k = n - 1; k >= ind + 1; k--) {
//                b.append(a[k]).append(" ");
//            }
//            System.out.println(b.toString());
//        }
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        int ind = -1;
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
            if (i != 0) {
                if (a[i - 1] < a[i]) {
                    ind = i - 1;
                }
            }
        }
        StringBuilder res = new StringBuilder();
        if (ind == -1) {
            System.out.println("0 ".repeat(n));
        } else {
            int sec = ind + 1;
            while (sec < n - 1 && a[sec + 1] > a[ind])
                sec++;
            int cur = a[ind];
            a[ind] = a[sec];
            a[sec] = cur;
            for (int i = 0; i <= ind; i++) {
                res.append(a[i]).append(" ");
            }
            for (int i = n - 1; i > ind; i--) {
                res.append(a[i]).append(" ");
            }
            System.out.println(res.toString());
        }
    }
}
