import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;

public class WordNKA {
    public static void main(String[] args) throws IOException {
        BufferedReader reader = new BufferedReader(new FileReader("problem2.in"));
        char[] line = reader.readLine().toCharArray();
        String[] params = reader.readLine().split(" ");
        int vars = Integer.parseInt(params[0]) + 1;
        int n = Integer.parseInt(params[1]);
        int end = Integer.parseInt(params[2]);

        int[][][] nodes = new int[vars][26][vars];
        boolean[] ends = new boolean[vars];

        String[] endNodes = reader.readLine().split(" ");
        for (String endNode : endNodes) {
            ends[Integer.parseInt(endNode)] = true;
        }

        for (int i = 0; i < n; i++) {
            String[] node = reader.readLine().split(" ");
            int p = Integer.parseInt(node[0]);
            int t = Integer.parseInt(node[1]);
            int l = node[2].charAt(0) - 'a';
            nodes[p][l][t] = 1;
        }

        Map<Integer, Integer> indexes = new HashMap<>();
        indexes.put(1, 1);

        for (char value : line) {
            int v = value - 'a';
            Map<Integer, Integer> newInd = new HashMap<>();
            for (Integer key : indexes.keySet()) {
                for (int k = 0; k < vars; k++) {
                    if (nodes[key][v][k] == 1) {
                        newInd.put(k, 1);
                    }
                }
            }
            indexes.clear();
            indexes = newInd;
        }

        BufferedWriter writer = new BufferedWriter(new FileWriter("problem2.out"));
        boolean was = false;
        for (int key : indexes.keySet()) {
            if (ends[key]) {
                was = true;
                writer.write("Accepts");
                break;
            }
        }
        if (!was) {
            writer.write("Rejects");
        }
        writer.close();
    }
}