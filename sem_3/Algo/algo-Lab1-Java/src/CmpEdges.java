import java.util.*;

public class CmpEdges {
    public static ArrayList<ArrayList<Integer>> edges;
    public static ArrayList<ArrayList<Integer>> edgesR;
    public static ArrayList<Integer> order;
    public static int[] cmpId;
    public static int i;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt() + 1;
        int k = sc.nextInt();
        edges = new ArrayList<>(n + 1);
        edgesR = new ArrayList<>(n + 1);
        order = new ArrayList<>(n + 1);
        cmpId = new int[n];
        i = 1;
        for (int j = 0; j < n; j++) {
            edges.add(new ArrayList<>());
            edgesR.add(new ArrayList<>());
            edges.get(0).add(0);
            edgesR.get(0).add(0);
        }
        for (int j = 0; j < k; j++) {
            int a = sc.nextInt();
            int b = sc.nextInt();
            edges.get(a).add(b);
            edgesR.get(b).add(a);
        }
        for (int j = 1; j < n; j++) {
            if (edges.get(0).get(j) == 0) {
                topSort(j);
            }
        }
        for (int j = 0; j < n - 1; j++) {
            int l = order.get(n - j - 2);
            if (edgesR.get(0).get(l) == 0) {
                dfs(l);
                i++;
            }
        }
        int c = 0;
        TreeSet<Integer>[] gra = new TreeSet[n];
        for (int j = 0; j < n; j++) {
            gra[j] = new TreeSet<>();
        }
        for (int j = 1; j < n; j++) {
            for (int l :
                    edges.get(j)) {
                if (cmpId[j] != cmpId[l]) {
                    gra[cmpId[j]].add(cmpId[l]);
                }
            }
        }
        for (int j = 0; j < n; j++) {
            c += gra[j].size();
        }
        System.out.println(c);
    }

    public static void topSort(int v) {
        edges.get(0).set(v, 1);
        for (int u:edges.get(v)) {
            if (edges.get(0).get(u) == 0) {
                topSort(u);
            }
        }
        order.add(v);
    }

    public static void dfs(int v) {
        edgesR.get(0).set(v, 1);
        cmpId[v] = i;
        for (int u:edgesR.get(v)) {
            if (edgesR.get(0).get(u) == 0) {
                dfs(u);
            }
        }
    }
}
