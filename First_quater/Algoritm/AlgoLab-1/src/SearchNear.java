import java.util.Scanner;

public class SearchNear {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] arr = new int[sc.nextInt()];
        int n = sc.nextInt();

        for (int i = 0; i < arr.length; i++) {
            arr[i] = sc.nextInt();
        }

        for (int i = 0; i < n; i++) {
            System.out.println(arr[binSearch(arr, sc.nextInt())]);
        }
    }

    public static int binSearch(int[]  arr, int x)
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
        if (r == arr.length) {
            r = arr.length - 1;
        }
        if (l == -1) {
            l = 0;
        }
        int lx = Math.abs(arr[l] - x);
        int rx = Math.abs(arr[r] - x);
        if (lx < rx) {
            return l;
        } else if (lx > rx) {
            return r;
        } else {
            if (arr[l] > arr[r]) {
                return r;
            } else {
                return l;
            }
        }
    }
}
