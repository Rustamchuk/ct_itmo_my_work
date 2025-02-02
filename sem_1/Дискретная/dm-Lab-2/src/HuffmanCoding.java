import java.util.Arrays;
import java.util.Scanner;

public class HuffmanCoding {
    public static int stopped;
    public static int finish;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long[] cnt = new long[n];
        long[] units = new long[n];
        for (int i = 0; i < n; i++) {
            cnt[i] = sc.nextInt();
        }
        long summ = countingHuff(cnt, 0, n);
        while (stopped != finish) {
            summ += countingHuff(cnt, stopped, finish);
        }
        System.out.println(summ);
    }

    public static long countingHuff(long[] arrStart, int left, int right) {
        int n = right - left;
        long[] units = new long[n];
        long[] arr = new long[n];
        for (int i = 0; i < n; i++) {
            units[i] = Long.MAX_VALUE;
            arr[i] = arrStart[i + left];
        }


        quickSort(arr, 0, n - 1);
        long allCnt = 0;
        int i = 0;
        int j = 0;
        long first, second, firstSecond;
        int last = n - 1;
        for (int k = 0; k < n; k++) {
            if (i >= n) {
                last = k;
                break;
            } else {
                if (i + 1 >= n)
                    first = arr[i];
                else
                    first = arr[i] + arr[i + 1];
            }
            if (j + 1 >= units.length) { break; }
            if (units[j + 1] == Long.MAX_VALUE)
                second = Long.MAX_VALUE;
            else
                second = units[j] + units[j + 1];
            if (units[j] == Long.MAX_VALUE)
                firstSecond = Long.MAX_VALUE;
            else
                firstSecond = arr[i] + units[j];
            if (first <= second && first <= firstSecond) {
                if (i + 1 >= n)
                    allCnt -= first;
                i += 2;
                units[k] = first;
                allCnt += first;
            } else if (second <= first && second <= firstSecond) {
                j += 2;
                units[k] = second;
                allCnt += second;
            } else if (firstSecond <= first) {
                i++;
                j++;
                units[k] = firstSecond;
                allCnt += firstSecond;
            }
        }
        stopped = j;
        finish = last;
        System.arraycopy(units, 0, arrStart, 0, n);
        return allCnt;
    }

    public static void quickSort(long[] arr, int left, int right) {
        if (left >= right) {
            return;
        }
        int index = left + (int)(Math.random() * ((right - left) + 1));
        long q = arr[index];
        int i = left;
        int j = right;
        long cur;
        while (i <= j) {
            while (q > arr[i]) {
                i++;
            }

            while (q < arr[j]) {
                j--;
            }

            if (i <= j) {
                cur = arr[i];
                arr[i] = arr[j];
                arr[j] = cur;
                i++;
                j--;
            }
        }
        quickSort(arr, left, j);
        quickSort(arr, i, right);
    }
}
