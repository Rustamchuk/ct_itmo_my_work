import java.util.Scanner;

public class Ferm {
    public static int poleSp;
    public static int forestSp;
    public static double high;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        poleSp = sc.nextInt();
        forestSp = sc.nextInt();
        sc.nextLine();
        high = Double.parseDouble(sc.nextLine());
        System.out.print(String.valueOf(Math.round(trippleSearch() * 10000d) / 10000d));
    }

    public static double trippleSearch() {
        double left = 0;
        double right = 1;
        double EPS = 0.0000000000001;
        double a;
        double b;
        while (right - left > EPS) {
            a = (left + left + right) / 3;
            b = (left + right + right) / 3;
            if (functionForest(b) - functionForest(a) + functionPole(b) - functionPole(a) > EPS)
                right = b;
            else
                left = a;
        }
        return right;
    }

    public static double gip(double a, double b) {
        return Math.pow(Math.pow(a, 2) + Math.pow(b, 2), 0.5);
    }

    public static double functionForest(double a) {
        return gip(1 - high, a) * forestSp;
    }

    public static double functionPole(double a) {
        return gip(high, 1 - a) * poleSp;
    }
}
