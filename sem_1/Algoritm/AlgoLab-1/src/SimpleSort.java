import java.util.Scanner;

public class SimpleSort {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }
        quickSort(a, 0, n - 1);

        for (int i : a) {
            System.out.print(i);
            System.out.print(" ");
        }
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
