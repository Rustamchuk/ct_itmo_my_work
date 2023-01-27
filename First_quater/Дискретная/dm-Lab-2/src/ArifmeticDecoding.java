import java.math.BigDecimal;
import java.math.BigInteger;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Scanner;

public class ArifmeticDecoding {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int[] ususals = new int[n];
        int sum = 0;
        BigDecimal two = new BigDecimal(2);
        for (int i = 0; i < n; i++) {
            ususals[i] = sc.nextInt();
            sum += ususals[i];
        }

        sc.nextLine();
        String code = sc.nextLine();
        int q = code.length();
        BigDecimal pow = two.pow(q);

        BigInteger p = new BigInteger(code, 2);
        BigDecimal pD = new BigDecimal(p);

        BigDecimal flag = new BigDecimal(String.valueOf(pD.divide(pow, new MathContext(1000, RoundingMode.UP))));

        int cnt = 0;
        BigDecimal l = BigDecimal.valueOf(0), r = BigDecimal.valueOf(1);
        BigDecimal cur;
        BigDecimal cur2;
        BigDecimal last = BigDecimal.valueOf(0);
        while (cnt < sum) {
            cnt++;
            cur = r.subtract(l).divide(BigDecimal.valueOf(sum), 1000, RoundingMode.UP);
            for (int i = 0; i < n; i++) {
                cur2 = cur.multiply(BigDecimal.valueOf(ususals[i])).add(last);
                if (cur2.compareTo(BigDecimal.ZERO) == 0) { continue; }
                if (cur2.compareTo(flag) >= 0) {
                    l = last;
                    r = cur2;
                    System.out.print((char) (97 + i));
                    break;
                }
                last = cur2;
            }
        }
    }
}
