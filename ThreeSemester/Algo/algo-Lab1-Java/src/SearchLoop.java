import java.util.ArrayList;
import java.util.Scanner;

public class SearchLoop {
    public static int[] out;
    public static ArrayList<ArrayList<Integer>> edges;
    public static int c;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt() + 1;
        int m = sc.nextInt();
        edges = new ArrayList<>(n + 1);
        out = new int[n];
        for (int i = 0; i < n; i++) {
            edges.add(new ArrayList<>());
            edges.get(0).add(0);
        }
        for (int i = 0; i < m; i++) {
            edges.get(sc.nextInt()).add(sc.nextInt());
        }
        for (int i = 1; i < n; i++) {
            if (edges.get(0).get(i) == 0) {
                dfs(i);
                if (c != 0) {
                    System.out.println("YES");
                    for (int j = c - 1; j >= 0; j--) {
                        System.out.print(out[j] + " ");
                    }
                    return;
                }
            }
        }
        System.out.println("NO");
    }

    public static int dfs(int v) {
        edges.get(0).set(v, 2);
        for (int u:edges.get(v)) {
            if (edges.get(0).get(u) == 2) {
                out[c++] = v;
                return u;
            } else if (edges.get(0).get(u) == 0) {
                int res = dfs(u);
                if (res != -1) {
                    if (res == 0) {
                        return res;
                    }
                    out[c++] = v;
                    if (res == v) {
                        return 0;
                    }
                    return res;
                }
            }
        }
        edges.get(0).set(v, 1);
        return -1;
    }
}
