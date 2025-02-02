import java.util.ArrayList;
import java.util.Scanner;

public class TopSort {
    public static ArrayList<ArrayList<Integer>> edges;
    public static int[] out;
    public static int i;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int k = sc.nextInt();
        edges = new ArrayList<>(n + 1);
        out = new int[n];
        i = n;
        for (int j = 0; j <= n; j++) {
            edges.add(new ArrayList<>());
            edges.get(0).add(0);
        }
        for (int j = 0; j < k; j++) {
            edges.get(sc.nextInt()).add(sc.nextInt());
        }
        for (int j = 1; j <= n; j++) {
            if (edges.get(0).get(j) == 0) {
                if (dfs(j) != -1) {
                    System.out.println(-1);
                    return;
                }
            }
        }
        for (int l = 0; l < n; l++) {
            System.out.print(out[l] + " ");
        }
    }

    public static int dfs(int v) {
        edges.get(0).set(v, 2);
        for (int u:edges.get(v)) {
            if (edges.get(0).get(u) == 2) {
                return u;
            } else if (edges.get(0).get(u) == 0) {
                int res = dfs(u);
                if (res != -1) {
                    if (res == 0) {
                        return res;
                    }
                    if (res == v) {
                        return 0;
                    }
                    return res;
                }
            }
        }
        edges.get(0).set(v, 1);
        out[--i] = v;
        return -1;
    }
}
