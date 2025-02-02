import java.util.Arrays;
import java.util.Scanner;

public class AntropyRandomy {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = 4;
        int min = 50;
        int max = 1000;
        double[] f = new double[n];
        double[] pi = new double[n];
        double sum = 0;
        all:
        for (int i = 1; i < 500; i++) {
            f[0] = i;
            sum += i;
            System.out.println(i + " " + sum);
            for (int j = i; j < 500; j++) {
                f[1] = j;
                sum += j;
                for (int k = j; k < 500; k++) {
                    f[2] = k;
                    sum += k;
                    for (int l = k; l < 500; l++) {
                        f[3] = l;
                        sum += l;
//                        for (int p = l; p < 250; p++) {
//                            f[4] = p;
//                            sum += p;
//                            for (int q = p; q < 300; q++) {
//                                f[5] = q;
//                                sum += q;
                                double antropy = 0;
                                for (int m = 0; m < n; m++) {
                                    pi[m] = f[m] / sum;
                                    antropy += -1 * pi[m] * Math.log(pi[m]) / Math.log(2);
                                }
//                            System.out.println(Arrays.toString(f) + " " + sum);
                                if (Math.abs(antropy - 1.4076) <= 0.00001 && sum >= min && sum <= max) {
                                    System.out.println(Arrays.toString(f));
                                    System.out.println(antropy);
                                    break all;
                                }
//                                sum -= q;
//                            }
//                            sum -= p;
//                        }
                        sum -= l;
                    }
                    sum -= k;
                }
                sum -= j;
            }
            sum = 0;
        }
        int cnt = 0;
        int[] arr = new int[4];
        for (int i = 0; i < f[0]; i++) {
            cnt++;
            System.out.print("a");
        }
        arr[0] = cnt;
        cnt = 0;
        for (int i = 0; i < f[1]; i++) {
            cnt++;
            System.out.print("b");
        }
        arr[1] = cnt;
        cnt = 0;
        for (int i = 0; i < f[2]; i++) {
            cnt++;
            System.out.print("c");
        }
        arr[2] = cnt;
        cnt = 0;
        for (int i = 0; i < f[3]; i++) {
            cnt++;
            System.out.print("d");
        }
        System.out.println();
        arr[3] = cnt;
        System.out.println(Arrays.toString(arr));
    }
}
