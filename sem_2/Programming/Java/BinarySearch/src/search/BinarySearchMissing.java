package search;

import java.util.Arrays;

public class BinarySearchMissing {
    public static void main(String[] args) {
        // Pred: ∃x ∈ a: a[x] < key && a - "не по возрастанию"
//        args = new String[]{"3", "5", "4", "2", "1"};
        int key = Integer.parseInt(args[0]);
        int[] arr = new int[args.length - 1];
        for (int i = 1; i < args.length; i++) {
            arr[i - 1] = Integer.parseInt(args[i]);
        }
        int l = -1;
        int r = arr.length;

        int R;
        R = iterativeBinarySearch(arr, key);
        System.out.println(R);

        R = recursiveBinarySearch(arr, key, l, r);
        System.out.println(R);
        // Post: a[R] <= key && R = min(R, R + 1, R + 2, ...)
    }

    // Контракт
    // Pred: ∃x ∈ a: a[x] < key && a - "не по возрастанию"
    // Post: a[R] <= key && R = min(R, R + 1, R + 2, ...)
    public static int iterativeBinarySearch(int[] a, int key) {
        int l = -1;
        int r = a.length;
        // a[r] <= key && a[l] > key (l != -1)
        while (l < r - 1) {
            // a[r] <= key && l < r - 1
            int m = l + (r - l) / 2;
            // a[r] <= key && l < r - 1 && l < m < r
            // (<- l < l + (r - l) / 2 < r <- 0.5l - o.5r < 0 < 0.5r - o.5l <- l - r < 0 && r - l > 0 <- l < r <= l < r - 1)
            if (a[m] > key) {
                // a[r] <= key && l < r - 1 && a[m] > key && l < m < r
                l = m;
                // a[r] <= key && a[m] > key && l < r
                // a[r] <= key && a[l] > key && l < r
            } else if (a[m] < key) {
                // a[r] <= key && l < r - 1 && a[m] <= key && l < m < r
                r = m;
                // a[r] <= key && a[m] <= key && l < r
                // a[r] <= key && a[r] <= key && l  < r
                // a[r] <= key && l < r
            } else {
                return m;
            }
            // a[r] <= key && a[l] > key (l != -1) && l < r
        }
        // a[r] <= key && a[l] > key (l != -1) && l == r - 1 (l < r)
        return -1 * r - 1;
    }

    // Контракт
    // Pred: ∃x ∈ a: a[x] < key && a - "не по возрастанию" && l < r
    // Post: a[R] <= key && R = min(R, R + 1, R + 2, ...)
    public static int recursiveBinarySearch(int[] a, int key, int l, int r) {
        // a[r] <= key && a[l] > key (l >= 0) && l < r
        if (l == r - 1) {
            // a[r] <= key && a[l] > key (l >= 0) && l == r - 1 (l < r)
            return -1 * r - 1;
        }
        // a[r] <= key && a[l] > key (l >= 0) && l < r - 1
        int m = l + (r - l) / 2;
        // a[r] <= key && a[l] > key (l >= 0) && l < r - 1 && l < m < r
        // (<- l < l + (r - l) / 2 < r <- 0.5l - o.5r < 0 < 0.5r - o.5l <- l - r < 0 && r - l > 0 <- l < r <= l < r - 1)
        if (a[m] > key) {
            // a[r] <= key && l < r - 1 && l < m < r && a[m] > key
            l = m;
            // a[r] <= key && a[m] > key && l < r
            // a[r] <= key && a[l] > key && l < r
        } else if (a[m] < key) {
            // a[r] <= key && l < r - 1 && l < m < r && a[m] <= key
            r = m;
            // a[r] <= key && a[m] <= key && l < r
            // a[r] <= key && a[r] <= key && l < r
            // a[r] <= key && l < r
        } else {
            return m;
        }
        // a[r] <= key && a[l] > key (l >= 0) && l < r
        return recursiveBinarySearch(a, key, l, r);
    }
}
