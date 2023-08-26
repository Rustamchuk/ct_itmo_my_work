import java.io.*;
import java.util.*;

public class Useless {
    public static Map<Integer, List<Integer>> avt;
    public static void main(String[] args) throws IOException {
        BufferedReader in = new BufferedReader(new FileReader("useless.in"));
        int n = in.read() - '0';
        in.read();
        String st = in.readLine();

        boolean[] isGenerating = new boolean[26];
        int[] counter = new int[26];
        avt = new HashMap<>();
        Queue<Integer> queue = new LinkedList<>();


        for (int i = 0; i < n; i++) {
            int cur = in.read() - 'A';
            in.skip(4);
            String relate = in.readLine();
            if (!avt.containsKey(cur)) {
                avt.put(cur, new ArrayList<>());
            }

            for (char c : relate.toCharArray()) {
                if (c >= 'A' && c <= 'Z' && !avt.get(cur).contains(c - 'A')) {
                    avt.get(cur).add(c - 'A');
                    counter[c - 'A'] = 1;
                }
            }
        }
        queue.add(st.charAt(0) - 'A');
        while (!queue.isEmpty()) {
            int cur = queue.poll();
            if (!avt.containsKey(cur)) {
                isGenerating[cur] = true;
                continue;
            }
            queue.addAll(avt.get(cur));
        }
        for (int a : avt.keySet()) {
            if (counter[a] == 0 && a != st.charAt(0) - 'A') {
                isGenerating[a] = true;
            }
        }
        BufferedWriter out = new BufferedWriter(new FileWriter("useless.out"));
        for (int i = 0; i < 26; i++) {
            if (isGenerating[i]) {
                out.write((char) (i + 'A') + " ");
            }
        }
        out.newLine();
        in.close();
        out.close();
    }
}