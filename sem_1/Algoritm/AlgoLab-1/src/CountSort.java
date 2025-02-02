import java.util.Scanner;

public class CountSort {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] possibleNumbers = new int[101];
        for (int i = 0; i < n; i++) {
            possibleNumbers[sc.nextInt()]++;
        }

        for (int i = 0; i < possibleNumbers.length; i++) {
            for (int j = 0; j < possibleNumbers[i]; j++) {
                System.out.print(i + " ");
            }
        }
    }
}