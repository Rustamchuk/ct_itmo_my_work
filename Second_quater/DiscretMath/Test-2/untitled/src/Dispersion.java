import java.util.Scanner;

public class Dispersion {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        double[] x = new double[n];
        double[] f = new double[n];
        double[] p = new double[n];
        double sum = 0;
        for (int i = 0; i < n; i++) {
            x[i] = sc.nextDouble();
            f[i] = sc.nextDouble();
            sum += f[i];
        }
        double res = 0;
        for (int i = 0; i < n; i++) {
            p[i] = f[i] / sum;
            res += p[i] * x[i];
        }
        double fin = 0;
        for (int i = 0; i < n; i++) {
            fin += (x[i] - res) * (x[i] - res) * p[i];
        }
        System.out.println(fin);
    }
}
