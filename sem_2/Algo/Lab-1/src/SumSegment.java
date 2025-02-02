import java.util.Scanner;

public class SumSegment {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int pow = (int) Math.pow(2, 16);
        long cur;

        long[] sums = new long[sc.nextInt()];

        int x = sc.nextInt(), y = sc.nextInt();

        sums[0] = sc.nextInt();
        cur = sums[0];

        for (int i = 1; i < sums.length; i++) {
            cur = ((x * cur + y) % pow);
            sums[i] = cur + sums[i - 1];
        }

        int m = sc.nextInt();
        if (m != 0) {
            int[] c = new int[m * 2];

            int z = sc.nextInt(), t = sc.nextInt();

            int cr = sc.nextInt();
            c[0] = cr % sums.length;

            pow = (int) Math.pow(2, 30);
            for (int i = 1; i < c.length; i++) {
                cr = (z * cr + t) % pow;
                if (cr < 0) cr = pow + cr;
                c[i] = cr % sums.length;
            }

            long sum = 0;

            for (int i = 0; i < m; i++) {
                sum += sums[Math.max(c[2 * i], c[2 * i + 1])];
                int min = Math.min(c[2 * i], c[2 * i + 1]);
                if (min > 0) sum -= sums[min - 1];
            }
            System.out.println(sum);
        } else {
            System.out.println(0);
        }
    }
}
