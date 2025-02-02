import java.util.LinkedHashMap;
import java.util.Scanner;

public class C {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int cnt = sc.nextInt();
        LinkedHashMap<Integer, int[]> elements = new LinkedHashMap<>();
        int high = 0;

        for (int i = 0; i < cnt; i++) {
            int cur = sc.nextInt();
            if (cur !=  0) {
                int[] needElem = new int[cur];
                for (int j = 0; j < cur; j++) {
                    needElem[j] = sc.nextInt();
                }
                int[] values = new int[(int) Math.pow(2, cur)];
                for (int j = 0; j < values.length; j++) {
                    values[j] = sc.nextInt();
                }
                high++;
                if (high == 1) {
                    elements.put(i + 1, values);
                } else {
                    
                }
            } else {
                elements.put(i + 1, new int[0]);
            }
        }
    }
}
