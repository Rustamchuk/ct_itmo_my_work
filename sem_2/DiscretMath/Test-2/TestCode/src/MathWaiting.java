import java.util.Scanner;

public class MathWaiting {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        double[] x = new double[n];
        double[] f = new double[n];
        double[] pi = new double[n];
        double sum = 0;
        for (int i = 0; i < n; i++) {
            x[i] = sc.nextDouble();
            f[i] = sc.nextDouble();
            sum += f[i];
        }
        double math = 0;
        for (int i = 0; i < n; i++) {
            pi[i] = f[i] / sum;
            math += pi[i] * x[i];
        }
        System.out.println(math);
        // 513479.37361874036
    }
}
