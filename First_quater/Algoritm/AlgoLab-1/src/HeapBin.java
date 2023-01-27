import java.util.Arrays;
import java.util.Scanner;

public class HeapBin {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int[] arr = new int[sc.nextInt()];
        int pos = 0;
        int left = 0;
        int right = 0;
        int j = 0;
        for (int i = 0; i < arr.length; i++) {
            if (sc.nextInt() == 0) {
                arr[pos] = sc.nextInt();
                j = pos++;
                while (arr[j] > arr[(j - 1) / 2]) {
                    left = arr[j];
                    arr[j] = arr[(j - 1) / 2];
                    arr[(j - 1) / 2] = left;
                    j = (j - 1) / 2;
                }
            } else {
                System.out.println(arr[0]);
                arr[0] = arr[pos - 1];
                arr[pos - 1] = 0;
                j = 0;
                while (2 * j + 1 < arr.length) {
                    right = 2 * j + 2;
                    left = 2 * j + 1;
                    if (right < arr.length && arr[right] > arr[left]){
                        left = right;
                    }
                    if (arr[j] >= arr[left]) {
                        break;
                    }
                    right = arr[j];
                    arr[j] = arr[left];
                    arr[left] = right;
                    j = left;
                }
            }
        }
    }
}
