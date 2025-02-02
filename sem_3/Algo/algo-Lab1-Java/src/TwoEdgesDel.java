import java.util.ArrayList;
import java.util.List;

public class TwoEdgesDel {
    private static int time = 0;
    private static ArrayList<ArrayList<Integer>> graph;
    private static boolean[] visited;
    private static int[] tin, fup, res;

    public static void main(String[] args) {
        graph = new ArrayList<ArrayList<Integer>>(10);
        for (int i = 0; i < 10; i++) {
            graph.add(new ArrayList<>());
        }

        graph.get(1).add(2);
        graph.get(2).add(3);
        graph.get(2).add(5);
        graph.get(2).add(4);
        graph.get(4).add(6);
        graph.get(5).add(6);
        graph.get(5).add(7);
        graph.get(7).add(8);
        graph.get(7).add(9);
        graph.get(8).add(9);
        graph.get(9).add(8);
        graph.get(8).add(7);
        graph.get(9).add(7);
        graph.get(7).add(5);
        graph.get(6).add(5);
        graph.get(6).add(4);
        graph.get(5).add(2);
        graph.get(4).add(2);
        graph.get(3).add(2);
        graph.get(2).add(1);

        visited = new boolean[graph.size()];
        tin = new int[graph.size()];
        fup = new int[graph.size()];
        res = new int[graph.size()];

        findBridges();
    }

    public static void findBridges() {
        for (int i = 0; i < graph.size(); ++i) {
            if (!visited[i]) {
                dfs(i, -1);
            }
        }
    }

    private static void dfs(int v, int p) {
        visited[v] = true;
        tin[v] = fup[v] = time++;
        for (int to : graph.get(v)) {
            if (to == p) continue;
            if (visited[to]) {
                if (fup[v] > tin[to]) {
                    fup[v] = tin[to];
                    res[to] += 1;
                    res[v] += res[to];
                } else if (res[to] <= 1) {
                    System.out.println(v + "-" + to);
                }
            } else {
                dfs(to, v);
                if (fup[v] > tin[to]) {
                    fup[v] = tin[to];
                    res[v] += res[to];
                }
                if (res[v] <= 1 || fup[to] > tin[v]) {
                    System.out.println("Bridge: " + v + " - " + to);
                }
            }
        }
    }
}
