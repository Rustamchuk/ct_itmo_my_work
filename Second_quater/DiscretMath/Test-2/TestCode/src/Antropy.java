import java.util.Scanner;

public class Antropy {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        double[] f = new double[n];
        double[] pi = new double[n];
        double sum = 0;
        for (int i = 0; i < n; i++) {
            f[i] = sc.nextDouble();
            sum += f[i];
        }
//        double math = 0;
        double antropy = 0;
        for (int i = 0; i < n; i++) {
            pi[i] = f[i] / sum;
//            math += pi[i] * x[i];
            antropy += -1 * pi[i] * Math.log(pi[i]) / Math.log(2);
        }
        System.out.println(antropy);
        // 4.968209029255625
    }
}
