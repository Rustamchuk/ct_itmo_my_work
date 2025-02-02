import java.util.Arrays;
import java.util.Random;
import java.util.Scanner;

public class EntropyBrain {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = 4;
        double[] x = new double[n];
        double[] p = new double[n];
        double sum = 0;
        all:
        for (int i = 0; i < 500; i++) {
            System.out.println(i);
            sum = 0;
            sum += i;
            x[0] = i;
            for (int j = i; j < 500; j++) {
                sum += j;
                x[1] = j;
                for (int k = j; k < 500; k++) {
                    sum += k;
                    x[2] = k;
                    for (int m = k; m < 500; m++) {
                        sum += m;
                        x[3] = m;
                        double res = 0;
                        for (int l = 0; l < n; l++) {
                            p[l] = x[l] / sum;
                            res += -1 * p[l] * Math.log(p[l]) / Math.log(2);
                        }
                        if (Math.abs(res - 1.5890) <= 0.0001 && sum >= 50) {
                            System.out.println(i);
                            System.out.println(j);
                            System.out.println(k);
                            System.out.println(m);
                            break all;
                        }
                        sum -= m;
                    }
                    sum -= k;
                }
                sum -= j;
            }
        }
    }
}
