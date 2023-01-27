import java.text.DecimalFormat;
import java.util.Scanner;

public class Numbers {
    public static double C = Double.parseDouble(new Scanner(System.in).nextLine());

    public static void main(String[] args) {
        double EPS = 0.0000001;
        double right = 1e13;
        double left = EPS;

        while (right - left > EPS) {
            double m = (right + left) / 2;
            double result = function(m);
            if (result >= 0) {
                right = m;
            } else {
                left = m;
            }
        }
        System.out.println(right);
    }

    public static double function(double x) {
        return Math.pow(x, 2) + Math.sqrt(x) - C;
    }
}