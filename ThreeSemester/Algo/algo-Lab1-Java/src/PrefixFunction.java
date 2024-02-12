import java.util.Scanner;

public class PrefixFunction {
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        char[] s = sc.nextLine().toCharArray();
        int n = s.length;
        int[] pi = new int[n];
        StringBuilder out = new StringBuilder();
        out.append(0).append(" ");
        for (int i = 1; i < n; i++) {
            int j = pi[i - 1];
            while (j > 0 && s[i] != s[j])
                j = pi[j-1];
            if (s[i] == s[j])  ++j;
            pi[i] = j;
            out.append(pi[i]).append(" ");
        }
        System.out.println(out);
    }
}
