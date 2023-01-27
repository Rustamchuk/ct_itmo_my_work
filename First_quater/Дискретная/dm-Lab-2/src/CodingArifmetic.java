import java.util.Arrays;
import java.util.Scanner;

public class CodingArifmetic {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        System.out.println(n);
        int[] usuals = new int[n];
        sc.nextLine();
        String word = sc.nextLine();
        int len = word.length();
        int sum = 0;
        for (int i = 0; i < len; i++) {
            usuals[word.charAt(i) - 97]++;
            sum++;
        }
        for (int i : usuals) {
            System.out.print(i + " ");
        }
        System.out.println();
        double l = 0, r = 1;
        double cur, cur2;
        double last;
        for (int i = 0; i < len; i++) {
            cur = (r - l) / sum;
            if (word.charAt(i) - 97 <= len / 2) {
                last = l;
                for (int j = 0; j < n; j++) {
                    cur2 = cur * usuals[j];
                    if (j == word.charAt(i) - 97) {
                        l = last;
                        r = last + cur2;
                        break;
                    } else {
                        last += cur2;
                    }
                }
            } else {
                last = r;
                for (int j = n - 1; j >= 0; j--) {
                    cur2 = cur * usuals[j];
                    if (j == word.charAt(i) - 97) {
                        l = last - cur2;
                        r = last;
                        break;
                    } else {
                        last -= cur2;
                    }
                }
            }
        }
//        System.out.println(r + " " + l);
        int q = 0;
        double pow = 0;
        for (int i = 0; i < 1000; i++) {
            if (1 / Math.pow(2, i) <= r) {
                q = i;
                pow = Math.pow(2, q);
                break;
            }
        }
        int p = 0;
        double divide = 0;
        for (int i = 0; i < 1000; i++) {
            if (i / pow >= l) {
                if (i / pow > r) {
                    q++;
                    pow = Math.pow(2, q);
                } else {
                    p = i;
                    divide = i / pow;
                    break;
                }
            }
        }
        if  (divide > r) {
            for (int i = q; i < q + 1000; i++) {
                if (p / Math.pow(2, i) < r) {
                    q = i;
                    pow = Math.pow(2, q);
                    divide = p / pow;
                }
            }
        }
        if (divide < l) {
            for (int i = p; i < p + 1000; i++) {
                if (p / pow > l) {
                    p = i;
                    divide = p / pow;
                }
            }
        }
        String ans = Integer.toBinaryString(p);
        StringBuilder answer = new StringBuilder();
        if (ans.length() < q) {
            answer.append("0".repeat(Math.max(0, q - ans.length())));
            answer.append(ans);
        } else {
            answer.append(ans);
        }
        System.out.println(answer.toString());
    }
}