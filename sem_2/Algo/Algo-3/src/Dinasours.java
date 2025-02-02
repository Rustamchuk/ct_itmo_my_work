import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;

public class Dinasours {
    public static int[][] up;
    public static int[] tin;
    public static int[] tout;
    public static int[] depth;
    public static int[] s;
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
        if (depth[u] > depth[v]) {
            int a = u;
            u = v;
            v = a;
        }
        for (int i = log; i >= 0; i--) {
            if ((1 << i) > depth[v] - depth[u]) {
                continue;
            }
            v = up[v][i];
        }
        if (v == u) {
            return v;
        }
        for (int i = log; i >= 0; i--) {
            if (up[v][i] != up[u][i]) {
                v = up[v][i];
                u = up[u][i];
            }
        }
        return up[u][0];
    }

    public static void main(String[] args) {
        FastReader sc = new FastReader();

        int n = sc.nextInt() + 1;
        log = (int) (Math.log(n) / Math.log(2));
        up = new int[n][log + 2];
        tin = new int[n];
        tout = new int[n];
        depth = new int[n];
        s = new int[n];
        int[] tree = new int[n];
        int ind = 2;
        tree[1] = 1;
        up[1][0] = 1;
        depth[1] = 1;
        s[1] = 1;
        for (int i = 2; i <= n; i++) {
            char cur = sc.next().charAt(0);
            if (cur == '+') {
                up[ind][0] = sc.nextInt();
                tree[ind] = up[ind][0];
                s[ind] = ind;
                depth[ind] = depth[up[ind][0]] + 1;
                for (int j = 0; (1 << j) < n; j++) {
                    up[ind][j + 1] = up[up[ind][j]][j];
                }
                ind++;
            } else if (cur == '-') {
                int in = sc.nextInt();
                int p = tree[in];
                if (in != s[in]) {
                    continue;
                }
                p = get(p);
                s[in] = p;
            } else {
                System.out.println(get(LCA(sc.nextInt(), sc.nextInt())));
            }
        }
    }

    public static int get(int a) {
        if (s[a] != a) {
            s[a] = get(s[a]);
        }
        return s[a];
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
