import java.util.Scanner;

public class Printer {
    public static int min;
    public static int max;
    public static long target;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        target = sc.nextInt();
        int first = sc.nextInt();
        int second = sc.nextInt();

        min = Math.min(first, second);
        max = Math.max(first, second);

        binarySearch();
    }

    public static void binarySearch() {
        long l = -1;
        long r = Integer.MAX_VALUE;

        while (r - l > 1)
        {
            long mid = (r + l) / 2;
            if (function(mid)) {
                r = mid;
            } else {
                l = mid;
            }
        }
        System.out.println(r + min);
    }

    public static boolean function(long time) {
        return (time / min + time / max) >= target - 1;
    }
}