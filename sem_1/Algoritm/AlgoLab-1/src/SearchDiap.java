import java.util.Arrays;
import java.util.Scanner;

public class SearchDiap {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }
        quickSort(a, 0, n - 1);

        int k = sc.nextInt();
        int l, r;
        StringBuilder b = new StringBuilder();
        for (int i = 0; i < k; i++) {
            l = sc.nextInt();
            r = sc.nextInt();
            b.append(binSearchRight(a, r) - binSearchLeft(a, l)).append(" ");
        }
        System.out.println(b.toString());
    }

    public static int binSearchLeft(int[]  arr, int x)
    {
        int l = -1;
        int r = arr.length;
        while (r - l > 1)
        {
            int mid = l + (r - l) / 2;
            if (arr[mid] >= x) {
                r = mid;
            } else {
                l = mid;
            }
        }
        return r;
    }

    public static int binSearchRight(int[]  arr, int x)
    {
        int l = -1;
        int r = arr.length;
        while (r - l > 1)
        {
            int mid = l + (r - l) / 2;
            if (arr[mid] <= x) {
                l = mid;
            } else {
                r = mid;
            }
        }
        return l + 1;
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
