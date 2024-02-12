import java.util.ArrayList;
import java.util.Scanner;
import java.util.SortedSet;
import java.util.TreeSet;

public class ConnectPoint {
    public static ArrayList<ArrayList<int[]>> edges;
    public static SortedSet<Integer> map;
    public static int time = 0;
    private static int[] tin, fup;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt() + 1;
        int k = sc.nextInt();
        edges = new ArrayList<>(n + 1);
        map = new TreeSet<>();
        tin = new int[n + 1];
        fup = new int[n + 1];
        for (int j = 0; j < n; j++)
        {
            edges.add(new ArrayList<>());
            edges.get(0).add(new int[1]);
        }
        for (int j = 1; j <= k; j++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            edges.get(a).add(new int[]{b, j});
            edges.get(b).add(new int[]{a, j});
        }
        for (int j = 1; j < n; j++) {
            if (edges.get(0).get(j)[0] == 0) {
                dfs(j, -1);
            }
        }
        System.out.println(map.size());
        for (Integer integer : map) {
            System.out.print(integer + " ");
        }
    }

    public static void dfs(int v, int p) {
        edges.get(0).get(v)[0] = 1;
        tin[v] = fup[v] = time++;
        int kids = 0;
        for (int i = 0; i < edges.get(v).size(); i++) {
            int to = edges.get(v).get(i)[0];
            if (to == p) {
                continue;
            }
            if (edges.get(0).get(to)[0] == 1) {
                fup[v] = Math.min(fup[v], tin[to]);
            } else {
                dfs(to, v);
                fup[v] = Math.min(fup[v], fup[to]);
                if (fup[to] >= tin[v] && p != -1) {
                    map.add(v);
                }
                kids++;
            }
        }
        if (p == -1 && kids > 1) {
            map.add(v);
        }
    }
}
