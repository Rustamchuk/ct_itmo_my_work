package search;

public class BinarySearchShift {
    // Pred: args != null (args.length > 0)
    public static void main(String[] args) {
        // Pred: a.length > 0 && a - "по убыванию"
        int[] arr = new int[args.length];
        int sum = 0;
        for (int i = 0; i < args.length; i++) {
            arr[i] = Integer.parseInt(args[i]);
            sum += arr[i];
        }
        int l = -1;
        int r = arr.length - 1;

        int R;
        if (sum % 2 != 0) {
            R = iterativeBinarySearch(arr);
        } else {
            R = recursiveBinarySearch(arr, l, r);
        }
        System.out.println(R);
        // Post: a[R] = max(a)
    }

    // Контракт
    // Pred: a.length > 0
    // Post: a[R] = max(a)
    public static int iterativeBinarySearch(int[] a) {
        int l = -1;
        int r = a.length - 1;
        // INV: a[r] > a[l] (l != -1) && l < r
        while (l < r - 1) {
            // a[r] > a[l] (l != -1) && l < r - 1
            int m = l + (r - l) / 2;
            // a[r] > a[l] (l != -1) && l < r - 1 && l < m < r
            // (<- l < l + (r - l) / 2 < r <- 0.5l - 0.5r < 0 < 0.5r - 0.5l <- l - r < 0 && r - l > 0 <- l < r <= l < r - 1)
            if (a[m] > a[r]) {
                // l < r - 1 && l < m < r && a[m] > a[r] (l != -1)
                r = m;
                // a[m] > a[l] && l < r
                // a[r'] > a[l] (l != -1) && l < r && a[r'] > a[r]
            } else {
                // l < r - 1 && l < m < r && a[m] <= a[l]
                l = m;
                // a[r] > a[m] && l < r
                // a[r] > a[l'] && l < r
            }
//            a[r] > a[l] (l != -1) && l < r
        }
        // a[r] > a[l] (l != -1) && l < r && l == r - 1
        return r;
    }

    // Контракт
    // Pred: a.length > 0 && 0 <= l < r < a.length
    // Post: a[R] = max(a)
    public static int recursiveBinarySearch(int[] a, int l, int r) {
        //INV: a[r] > a[l] (l != -1) && l < r
        if (l == r - 1) {
            // a[r] > a[l] (l != -1) && l == r - 1 (l < r)
            return r;
        }
        // a[r] > a[l] (l != -1) && l < r - 1
        int m = l + (r - l) / 2;
        // a[r] > a[l] (l != -1) && l < r - 1 && l < m < r
        // (<- l < l + (r - l) / 2 < r <- 0.5l - 0.5r < 0 < 0.5r - 0.5l <- l - r < 0 && r - l > 0 <- l < r <= l < r - 1)
        if (a[m] > a[r]) {
            // l < r - 1 && l < m < r && a[m] > a[r]
            r = m;
            // a[m] > a[l] && l < r
            // a[r'] > a[l] (l != -1) && l < r && a[r'] > a[r]
        } else {
            // l < r - 1 && l < m < r && a[m] <= a[l]
            l = m;
            // a[r] > a[m] && l < r
            // a[r] > a[l'] && l < r
        }
//        a[r] > a[l] (l != -1) && l < r
        return recursiveBinarySearch(a, l, r);
    }
}
