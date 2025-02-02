import java.util.Scanner;

public class Entropy {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        double[] x = new double[n];
        double[] p = new double[n];
        double sum = 0;
        for (int i = 0; i < n; i++) {
            x[i] = sc.nextDouble();
            sum += x[i];
        }
        double res = 0;
        for (int i = 0; i < n; i++) {
            p[i] = x[i] / sum;
            res += -1 * p[i] * Math.log(p[i]) / Math.log(2);
        }
        System.out.println(res);
    }
}
