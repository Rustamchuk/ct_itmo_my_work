//package ChempionShip;

import java.util.Scanner;

public class I {
    public static void main(String[] args) {
        int xr = Integer.MIN_VALUE;
        int yr = Integer.MIN_VALUE;
        int xl = Integer.MAX_VALUE;
        int yl = Integer.MAX_VALUE;

        Scanner sc = new Scanner(System.in);
        int count = sc.nextInt();

        for (int i = 0; i < count; i++) {
            int xi = sc.nextInt();
            int yi = sc.nextInt();
            int hi = sc.nextInt();

            xl = Math.min(xl, xi - hi);
            xr = Math.max(xr, xi + hi);
            yl = Math.min(yl, yi - hi);
            yr = Math.max(yr, yi + hi);
        }

        int h = (int) Math.ceil((double) (Math.max(xr - xl, yr - yl)) / 2);
        int x  = (xl + xr) / 2;
        int y  = (yl + yr) / 2;
        System.out.println(x + " " + y + " " + h);
    }
}
