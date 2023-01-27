import java.util.Arrays;
import java.util.Scanner;

public class Movements {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] alf = new int[n];
        int[] arr = new int[n * (n - 1)];
        if (n == 1) {
            arr = new int[n];
        }
        StringBuilder a = new StringBuilder();
        int m = 0;
        for (int i = 0; i < n; i++) {
            alf[i] = i + 1;
            a.append(i + 1);
        }
        arr[m++] = Integer.parseInt(a.toString());
        a.setLength(0);
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < n - 1; j++) {
                if (j == n - 2 && i == n - 1)
                    break;
                int b = alf[j];
                alf[j] = alf[j + 1];
                alf[j + 1] = b;
                for (int k :
                        alf) {
                    a.append(k);
                }
                arr[m++] = Integer.parseInt(a.toString());
                a.setLength(0);
            }
        }
        if (m < arr.length) {
            int b = alf[0];
            alf[0] = alf[1];
            alf[1] = b;
            for (int k :
                    alf) {
                a.append(k);
            }
            arr[m] = Integer.parseInt(a.toString());
            a.setLength(0);
        } else {
            m--;
        }
        quickSort(arr, 0, m);
        for (int v :
                arr) {
            String cur = String.valueOf(v);
            for (int i = 0; i < cur.length(); i++) {
                a.append(cur.charAt(i)).append(" ");
            }
            a.append('\n');
        }
        System.out.println(a.toString());
    }

    public static void quickSort(int[] arr, int left, int right) {
        if (left >= right) {
            return;
        }
        int index = left + (int)(Math.random() * ((right - left) + 1));
        int q = arr[index];
        int i = left;
        int j = right;
        int cur;
        while (i <= j) {
            while (q > arr[i]) {
                i++;
            }

            while (q < arr[j]) {
                j--;
            }

            if (i <= j) {
                cur = arr[i];
                arr[i] = arr[j];
                arr[j] = cur;
                i++;
                j--;
            }
        }
        quickSort(arr, left, j);
        quickSort(arr, i, right);
    }
}