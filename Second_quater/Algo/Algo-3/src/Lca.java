import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.StringTokenizer;

public class Lca {
    public static int[][] up;
    public static int[] tin;
    public static int[] tout;
    public static int timer = 0;
    public static int log;

    public static void dfs(ArrayList<Integer>[] tree, int node, int par) {
        tin[node] = timer++;
        up[node][0] = par;

        for (int i = 1; i < up[node].length; i++) {
            up[node][i] = up[up[node][i - 1]][i - 1];
        }

        for (int child : tree[node]) {
            if (child != par) {
                dfs(tree, child, node);
            }
        }
        tout[node] = timer++;
    }

    public static boolean checkParent(int u, int v) {
        return tin[u] <= tin[v] && tin[v] <= tout[v] && tout[v] <= tout[u];
    }

    public static int LCA(int u, int v) {
        if (checkParent(u, v)) {
            return u;
        }
        if (checkParent(v, u)) {
            return v;
        }

        for (int i = log; i >= 0; i--) {
            if (!checkParent(up[u][i], v)) {
                u = up[u][i];
            }
        }

        return up[u][0];
    }

    public static void main(String[] args) {
        FastReader sc = new FastReader();

        int n = sc.nextInt() + 1;
        log = (int) (Math.log(n) / Math.log(2));
        up = new int[n][log + 1];
        tin = new int[n];
        tout = new int[n];
        ArrayList<Integer>[] tree = new ArrayList[n];

        tree[1] = new ArrayList<>();
        for (int i = 2; i < n; i++) {
            tree[i] = new ArrayList<>();
            tree[sc.nextInt()].add(i);
        }

        dfs(tree, 1, 1);

        int m = sc.nextInt();
        for (int i = 0; i < m; i++) {
            System.out.println(LCA(sc.nextInt(), sc.nextInt()));
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

        long nextLong() {
            return Long.parseLong(next());
        }

        double nextDouble() {
            return Double.parseDouble(next());
        }

        String nextLine() {
            String str = "";
            try {
                str = br.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return str;
        }

        String[] nextStringArray() {
            String[] tokens = null;
            try {
                tokens = br.readLine().split("\\s+");
            } catch (IOException e) {
                e.printStackTrace();
            }
            return tokens;
        }
    }
}
