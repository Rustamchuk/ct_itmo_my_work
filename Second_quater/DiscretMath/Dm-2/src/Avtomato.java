import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Avtomato {
    public static Map<Character, List<String>> avt;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("automaton.in"));
        int n = in.read() - '0';
        in.read();
        char st = (char) in.read();
        in.readLine();
        avt = new HashMap<>();
        for (int i = 0; i < n; i++) {
            char cur = (char) in.read();
            if (!avt.containsKey(cur)) {
                avt.put(cur, new ArrayList<>());
            }
            in.skip(4);
            String relate = in.readLine();
            if (relate.length() == 0 || relate.length() == 1 && (relate.charAt(0) < 'a' || relate.charAt(0) > 'z')) {
                relate = "0";
            }
            avt.get(cur).add(relate);
        }
        int m = in.read() - '0';
        in.readLine();
        BufferedWriter out = new BufferedWriter(new FileWriter("automaton.out"));
        for (int i = 0; i < m; i++) {
            if (checkWord(st, in.readLine(), 0)) {
                out.write("yes\n");
                continue;
            }
            out.write("no\n");
        }
        in.close();
        out.close();
    }

    public static boolean checkWord(char cr, String word, int ind) {
        if (word == null || word.isEmpty()) {
            word = "0";
        }
        if (ind >= word.length()) {
            return avt.get(cr).contains("0");
        }
        if (avt.containsKey(cr)) {
            for (String relate : avt.get(cr)) {
                if (relate.charAt(0) == word.charAt(ind)) {
                    if (relate.length() == 2) {
                        if (!checkWord(relate.charAt(1), word, ind + 1)) {
                            continue;
                        }
                        return true;
                    } else if (word.length() == ind + 1) {
                        return true;
                    }
                }
            }
        }
        return false;
    }
}