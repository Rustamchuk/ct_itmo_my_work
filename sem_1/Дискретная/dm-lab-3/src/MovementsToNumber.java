import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Scanner;

public class MovementsToNumber {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] a = new int[n];
        LinkedHashMap<Integer, Integer> list = new LinkedHashMap<>();
        for (int i = 1; i < n + 1; i++) {
            a[i - 1] = sc.nextInt();
            list.put(i, i);
        }
        long pos = 0;
        for (int i = 0; i < n; i++) {
            long cnt = 0;
            for (int j = 1; j < a[i]; j++) {
                if (list.containsKey(j))
                    cnt++;
            }
            long f = 1;
            for (int j = 1; j < n - i; j++) {
                f *= j;
            }
            list.remove(a[i]);
            cnt = cnt * f;
            if (cnt != 0) {
                pos += cnt;
            }
        }
        System.out.println(pos);
    }
}
