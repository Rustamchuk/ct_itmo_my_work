import java.io.*;
import java.util.*;

public class Nfc {
    public static Map<Character, List<String>> avt;
    public static long[][][] dp;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("nfc.in"));

        int n = in.read() - '0';
        in.read();
        char start = (char) in.read();
        in.readLine();

        avt = new HashMap<>();
        for (int i = 0; i < n; i++) {
            char cur = (char) (in.read() - 'A');
            in.skip(4);
            String relate = in.readLine();
            if (!avt.containsKey(cur)) {
                avt.put(cur, new ArrayList<>());
            }
            avt.get(cur).add(relate);
        }

        String word = in.readLine();
        dp = new long[26][word.length()][word.length()];
        counter(word);

        BufferedWriter out = new BufferedWriter(new FileWriter("nfc.out"));
        out.write(String.valueOf(dp[start - 'A'][0][word.length() - 1]));
        out.newLine();
        out.close();
    }

    public static void counter(String word) {
        for (int len = 1; len <= word.length(); len++) {
            for (int i = 0; i + len - 1 < word.length(); i++) {
                int j = i + len - 1;
                for (char m = 0; m < 26; m++) {
                    if (!avt.containsKey(m)) {
                        continue;
                    }
                    for (String rule : avt.get(m)) {
                        if (len != 1) {
                            if (rule.length() == 2) {
                                for (int k = i; k < j; k++) {
                                    dp[m][i][j] += dp[rule.charAt(1) - 'A'][k + 1][j] * dp[rule.charAt(0) - 'A'][i][k];
                                    dp[m][i][j] %= 1000000007;
                                }
                            }
                        } else {
                            if (rule.length() == 1 && rule.charAt(0) == word.charAt(i)) {
                                dp[m][i][j]++;
                            }
                        }
                    }
                }
            }
        }
    }
}
