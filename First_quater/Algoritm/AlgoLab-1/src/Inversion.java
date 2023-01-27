import java.util.Arrays;
import java.util.Scanner;

public class Inversion {
    public static long cnt = 0;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        for (int i = 0; i < n; i++) {
            a[i] = sc.nextInt();
        }
        mergeSort(a, n);
        //System.out.println(Arrays.toString(a));
        System.out.println(cnt);
    }

    public static void mergeSort(int[] arr, int len) {
        if (len < 2) {
            return;
        }
        int len1 = len / 2;
        int len2 = len - len1;
        int[] arr1 = new int[len1];
        int[] arr2 = new int[len2];
        for (int i = 0; i < len1; i++) {
            arr1[i] = arr[i];
        }
        for (int i = len1; i < len; i++) {
            arr2[i - len1] = arr[i];
        }
        mergeSort(arr1, len1);
        mergeSort(arr2, len2);
        merge(arr, arr1, arr2, len1, len2);
    }

    public static void merge(int[] arr, int[] arr1, int[] arr2, int l1, int l2) {
        int i = 0;
        int j = 0;
        int pos = 0;
        while (i < l1 && j < l2) {
            if (arr1[i] <= arr2[j]) {
                arr[pos++] = arr1[i++];
            } else {
                cnt += l1 - i;
                arr[pos++] = arr2[j++];
            }
        }
        while (i < l1) {
            arr[pos++] = arr1[i++];
        }
        while (j < l2) {
            arr[pos++] = arr2[j++];
        }
//        System.out.print(Arrays.toString(arr1));
//        System.out.print(Arrays.toString(arr2));
//        System.out.print(cnt);
//        System.out.println();
    }
}
