import java.io.*;
import java.util.*;

public class Epsilon {
    public static Map<String, List<String>> avt;
    public static Set<String> eps;

    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("epsilon.in"));
        int n = in.read() - '0';
        in.read();
        String st = in.readLine();
        avt = new HashMap<>();
        eps = new HashSet<>();
        for (int i = 0; i < n; i++) {
            String cur = String.valueOf((char) in.read());
            if (!avt.containsKey(cur)) {
                avt.put(cur, new ArrayList<>());
            }
            in.skip(4);
            String relate = in.readLine();
            avt.get(cur).add(relate);
            if (relate.isEmpty()) {
                eps.add(cur);
            }
        }
        boolean changed = true;
        while (changed) {
            changed = false;
            for (String key : avt.keySet()) {
                if (eps.contains(key)) {
                    continue;
                }
                for (String rel : avt.get(key)) {
                    if (checkEps(rel)) {
                        eps.add(key);
                        changed = true;
                    }
                }
            }
        }
        BufferedWriter out = new BufferedWriter(new FileWriter("epsilon.out"));
        for (char i = 'A'; i <= 'Z'; i++) {
            if (eps.contains(String.valueOf(i))) {
                out.write(i + " ");
            }
        }
        in.close();
        out.close();
    }

    private static boolean checkEps(String rel) {
        if (rel.isEmpty()) {
            return true;
        }

        for (int i = 0; i < rel.length(); i++) {
            char symbol = rel.charAt(i);
            if (symbol >= 'a' && symbol <= 'z') {
                return false;
            }
            if (symbol >= 'A' && symbol <= 'Z') {
                if (!eps.contains(String.valueOf(symbol))) {
                    return false;
                }
            }
        }

        return true;
    }
}
