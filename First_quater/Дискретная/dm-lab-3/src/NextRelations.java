import java.util.Scanner;

public class NextRelations {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        int[] relate = new int[k];
        for (int i = 0; i < k; i++) {
            relate[i] = sc.nextInt();
        }
        if (Math.abs(relate[k - 1] - n) >= 2) {
            relate[k - 1] += 1;
            if (relate[k - 1] > n) {
                System.out.println(-1);
            } else {
                for (int j = 0; j < k; j++) {
                    System.out.print(relate[j] + " ");
                }
            }
        } else {
            int i = k - 2;
            while (i >= 0 && (Math.abs(relate[i + 1] - relate[i]) < 2))
                i--;
            if (i < 0) {
                relate[k - 1] += 1;
                if (relate[k - 1] > n) {
                    System.out.println(-1);
                } else {
                    for (int j = 0; j < k; j++) {
                        System.out.print(relate[j] + " ");
                    }
                }
            } else {
                relate[i] += 1;
                for (int j = i + 1; j < k; j++)
                    relate[j] = relate[j - 1] + 1;
                for (int j = 0; j < k; j++) {
                    System.out.print(relate[j] + " ");
                }
            }
        }
    }
}
