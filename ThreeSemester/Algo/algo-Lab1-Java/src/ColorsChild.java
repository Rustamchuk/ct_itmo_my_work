import java.util.ArrayList;
import java.util.Scanner;

public class ColorsChild {
    public static ArrayList<ArrayList<Integer>> edges;
    public static int[] out;
    public static int i;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt() + 1;
        int k = sc.nextInt();
        edges = new ArrayList<>(n + 1);
        out = new int[n];
        i = 1;
        for (int j = 0; j < n; j++) {
            edges.add(new ArrayList<>());
            edges.get(0).add(0);
        }
        for (int j = 0; j < k; j++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            edges.get(a).add(b);
            edges.get(b).add(a);
        }
        for (int j = 1; j < n; j++) {
            if (edges.get(0).get(j) == 0) {
                dfs(j);
                i++;
            }
        }
        for (int l = 1; l < n; l++) {
            System.out.print(out[l] + " ");
        }
    }

    public static void dfs(int v) {
        edges.get(0).set(v, 1);
        for (int u:edges.get(v)) {
            if (edges.get(0).get(u) == 0) {
                dfs(u);
            }
        }
        out[v] = i;
    }
}
