import java.util.Scanner;

public class Multiply {
    public static long n;
    public static long target;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        n = sc.nextLong();
        target = sc.nextLong();
        System.out.println(binarySearch());
    }

    public static long binarySearch() {
        long l = 0;
        long r = n * n;

        while (r - l > 1) {
            long mid = l + (r - l) / 2;
            if (function(mid) >= target) {
                r = mid;
            } else {
                l = mid;
            }
        }
        return r;
    }

    public static long function(long number) {
        long res = 0;
        long cur;
        for (int i = 1; i <= n; i++) {
            cur = number / i;
            if (cur < n) {
                res += cur;
                continue;
            }
            res += n;
        }
        return res;
    }
}
