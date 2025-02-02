import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;

public class LCA {
    private static List<List<Integer>> treeLCA;
    private static int[] dist;
    public static void main(String[] args) {
        FastReader sc = new FastReader();
        int n = sc.nextInt();
        treeLCA = new ArrayList<>();
        int[] last = new int[n + 1];
        for (int i = 0; i <= n; i++) {
            treeLCA.add(new ArrayList<>());
        }

        for (int i = 2; i <= n; i++) {
            int a = sc.nextInt();
            treeLCA.get(a).add(i);
            last[i] = a;
        }

        dist = new int[n + 1];
        dfs(1, 1);

        int m = sc.nextInt();
        StringBuilder output = new StringBuilder();
        for (int i = 0; i < m; i++) {
            int u = sc.nextInt();
            int v = sc.nextInt();
            while (u != v) {
                if (dist[u] <= dist[v]) {
                    v = last[v];
                } else {
                    u = last[u];
                }
            }
            output.append(u).append("\n");
        }
        System.out.print(output);
    }

    private static void dfs(int node, int currentDepth) {
        dist[node] = currentDepth;
        for (int child : treeLCA.get(node)) {
            dfs(child, currentDepth + 1);
        }
    }

    static class FastReader {
        BufferedReader br;
        StringTokenizer st;

        public FastReader() {
            br = new BufferedReader(new InputStreamReader(System.in));
        }

        String next() {
            while (st == null || !st.hasMoreElements()) {
                try {
                    st = new StringTokenizer(br.readLine());
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            return st.nextToken();
        }

        int nextInt() {
            return Integer.parseInt(next());
        }
    }
}