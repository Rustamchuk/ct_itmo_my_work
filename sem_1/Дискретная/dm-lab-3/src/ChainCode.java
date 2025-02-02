import java.util.LinkedHashMap;
import java.util.Scanner;

public class ChainCode {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        LinkedHashMap<String, Integer> list = new LinkedHashMap<String, Integer>();
        StringBuilder cur = new StringBuilder();
        cur.append("0".repeat(n));
        list.put(cur.toString(), 0);
        String cr;
        for (int i = 0; i < Math.pow(2, n) - 1; i++) {
            cr = (cur.toString().substring(1, cur.length()));
            cur.setLength(0);
            cur.append(cr);
            if (!list.containsKey(cr + "1"))
                cur.append(1);
            else
                cur.append(0);
            list.put(cur.toString(), 0);
        }
        for (String o :
                list.keySet()) {
            System.out.println(o);
        }
    }
}
