import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.StringTokenizer;
import java.util.concurrent.atomic.AtomicInteger;

public class Cheapest {
    public static Node[][] up;
    public static int[] tin;
    public static int[] tout;
    public static int timer = 0;
    public static int log;

    public static void dfs(ArrayList<Node>[] tree, int node, int par, int c) {
        tin[node] = timer++;
        up[node][0] = new Node(par, c);

        for (int i = 1; i < up[node].length; i++) {
            up[node][i].ind = up[up[node][i - 1].ind][i - 1].ind;
            if (up[node][i - 1].cash < up[up[node][i - 1].ind][i - 1].cash) {
                up[node][i].cash = up[node][i - 1].cash;
            } else {
                up[node][i].cash = up[up[node][i - 1].ind][i - 1].cash;
            }
        }

        for (Node child : tree[node]) {
            if (child.ind != par) {
                dfs(tree, child.ind, node, child.cash);
            }
        }
        tout[node] = timer++;
    }

    public static boolean checkParent(int u, int v) {
        return tin[u] > tin[v] || tin[v] > tout[v] || tout[v] > tout[u];
    }

    public static int LCA(AtomicInteger u, AtomicInteger v) {
        AtomicInteger res = new AtomicInteger(Integer.MAX_VALUE);
        Helper(res, v, u);
        Helper(res, u, v);
        return res.get();
    }

    public static void Helper(AtomicInteger res, AtomicInteger v, AtomicInteger u) {
        for (int i = log; i >= 0; i--) {
            if (checkParent(up[v.get()][i].ind, u.get())) {
                res.set(Math.min(res.get(), up[v.get()][i].cash));
                v.set(up[v.get()][i].ind);
            }
        }

        if (checkParent(v.get(), u.get())) {
            res.set(Math.min(res.get(), up[v.get()][0].cash));
        }
    }

    public static void main(String[] args) {
        FastReader sc = new FastReader();

        int n = sc.nextInt() + 1;
        log = (int) (Math.log(n) / Math.log(2));
        up = new Node[n][log + 1];
        tin = new int[n];
        tout = new int[n];
        ArrayList<Node>[] tree = new ArrayList[n];

        tree[1] = new ArrayList<>();
        for (int i = 2; i < n; i++) {
            tree[i] = new ArrayList<>();
            tree[sc.nextInt()].add(new Node(i, sc.nextInt()));
        }
        for (int i = 0; i < n; i++) {
            for (int j = 0; j < log + 1; j++) {
                up[i][j] = new Node(0, 0);
            }
        }

        dfs(tree, 1, 1, Integer.MAX_VALUE);

        int m = sc.nextInt();
        for (int i = 0; i < m; i++) {
            System.out.println(LCA(new AtomicInteger(sc.nextInt()), new AtomicInteger(sc.nextInt())));
        }
    }

    private static class Node {
        int ind;
        int cash;

        public Node(int p, int c) {
            ind = p;
            cash = c;
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
