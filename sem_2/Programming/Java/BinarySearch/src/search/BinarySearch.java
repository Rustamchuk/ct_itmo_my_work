package search;

import java.util.Arrays;

public class BinarySearch {
    // Pred: args != null (args.length > 0)
    public static void main(String[] args) {
        // Pred: ∃x ∈ a: a[x] < key && a - "не по возрастанию"
//        args = new String[]{"3", "5", "4", "2", "1"};
        int key = Integer.parseInt(args[0]);
        int[] arr = new int[args.length - 1];
        int sum = 0;
        for (int i = 1; i < args.length; i++) {
            arr[i - 1] = Integer.parseInt(args[i]);
            sum += arr[i - 1];
        }
        int l = -1;
        int r = arr.length;

        int R;
        if (sum % 2 != 0) {
            R = iterativeBinarySearch(arr, key);
        } else {
            R = recursiveBinarySearch(arr, key, l, r);
        }
        System.out.println(R);
        // Post: a[R] <= key && R = min(R, R + 1, R + 2, ...)
    }

    // Контракт
    // Pred: ∃x ∈ a: a[x] < key && a - "не по возрастанию"
    // Post: a[R] <= key && R = min(R, R + 1, R + 2, ...)
    public static int iterativeBinarySearch(int[] a, int key) {
        int l = -1;
        int r = a.length;
        // INV: a[r] <= key && a[l + 1] > key
        while (l < r - 1) {
            // a[r] <= key && l < r - 1
            int m = l + (r - l) / 2;
            // a[r] <= key && l < r - 1 && l < m < r
            // (<- l < l + (r - l) / 2 < r <- 0.5l - 0.5r < 0 < 0.5r - 0.5l <- l - r < 0 && r - l > 0 <- l < r <= l < r - 1)
            if (a[m] > key) {
                // a[r] <= key && l < r - 1 && a[m] > key && l < m < r
                l = m;
                // a[r] <= key && a[m] > key && l < r
                // a[r] <= key && a[l] > key && l < r
            } else {
                // a[r] <= key && l < r - 1 && a[m] <= key && l < m < r
                r = m;
                // a[r] <= key && a[m] <= key && l < r
                // a[r] <= key && a[r] <= key && l < r
                // a[r] <= key && l < r
            }
            // a[r] <= key && a[l] > key (l != -1) && l < r
        }
        // a[r] <= key && a[l] > key (l != -1) && l == r - 1 (l < r)
        return r;
    }

    // Контракт
    // Pred: ∃x ∈ a: a[x] < key && a - "не по возрастанию" && 0 <= l < r < a.length
    // Post: a[R] <= key && R = min(R, R + 1, R + 2, ...)
    // INV: a[r] <= key && a[l + 1] > key
    public static int recursiveBinarySearch(int[] a, int key, int l, int r) {
        // a[r] <= key && a[l] > key (l >= 0) && l < r
        if (l == r - 1) {
            // a[r] <= key && a[l] > key (l >= 0) && l == r - 1 (l < r)
            return r;
        }
        // a[r] <= key && a[l] > key (l >= 0) && l < r - 1
        int m = l + (r - l) / 2;
        // a[r] <= key && a[l] > key (l >= 0) && l < r - 1 && l < m < r
        // (<- l < l + (r - l) / 2 < r <- 0.5l - 0.5r < 0 < 0.5r - 0.5l <- l - r < 0 && r - l > 0 <- l < r <= l < r - 1)
        if (a[m] > key) {
            // a[r] <= key && l < r - 1 && l < m < r && a[m] > key
            l = m;
            // a[r] <= key && a[m] > key && l < r
            // a[r] <= key && a[l] > key && l < r
        } else {
            // a[r] <= key && l < r - 1 && l < m < r && a[m] <= key
            r = m;
            // a[r] <= key && a[m] <= key && l < r
            // a[r] <= key && a[r] <= key && l < r
            // a[r] <= key && l < r
        }
        // a[r] <= key && a[l] > key (l >= 0) && l < r
        return recursiveBinarySearch(a, key, l, r);
    }
}