import java.util.*;

public class DeykhstrWays {
//    public static int[] out;
    public static int c;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt() + 1;
        int s = sc.nextInt();
        int f = sc.nextInt();
        int[][] edges = new int[n][n];
//        out = new int[n];
//        HashMap<Integer, Integer> map = new HashMap();
//        map.put(s, 0);
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                edges[i][j] = sc.nextInt();
                if (edges[i][j] == -1) {
                    edges[i][j] = Integer.MAX_VALUE;
                }
            }
        }

        int[] dist = new int[n];
        int[] visited = new int[n];
        Arrays.fill(dist, Integer.MAX_VALUE);
        dist[s] = 0;
        int min;
        for (int i = 1; i < n; i++) {
            min = 0;
            for (int j = 1; j < n; j++) {
                if (visited[j] == 0 && (dist[j] < dist[min])) {
                    min = j;
                }
            }
            if (min == 0) {
                break;
            }
            for (int j = 1; j < n; j++) {
                if (visited[j] == 0 && edges[min][j] != Integer.MAX_VALUE && dist[min] + edges[min][j] < dist[j]) {
                    dist[j] = dist[min] + edges[min][j];
                }
            }
            visited[min] = 1;
        }
//        while (cur != f) {
//            min = Integer.MAX_VALUE;
//            int minI = -1;
//            for (int i = 1; i < n; i++) {
//                if (map.containsKey(i) || edges[cur][i] == Integer.MAX_VALUE) {
//                    continue;
//                }
//                min = Math.min(edges[cur][i], min);
//                minI = i;
//            }
//            if (minI == -1) {
//                break;
//            }
//            map.put(minI, min);
//            for (int i = 1; i < n; i++) {
//                if (map.containsKey(i)) {
//                    continue;
//                }
//                if (edges[cur][i] == Integer.MAX_VALUE && edges[minI][i] != Integer.MAX_VALUE) {
//                    edges[minI][i] = map.get(minI) + edges[minI][i];
//                    continue;
//                } else if (edges[cur][i] != Integer.MAX_VALUE && edges[minI][i] == Integer.MAX_VALUE) {
//                    edges[minI][i] = edges[cur][i];
//                    continue;
//                } else if (edges[cur][i] == Integer.MAX_VALUE && edges[minI][i] == Integer.MAX_VALUE) {
//                    continue;
//                }
//                edges[minI][i] = Math.min(edges[cur][i], map.get(minI) + edges[minI][i]);
//            }
//            cur = minI;
//        }

//        if (!map.containsKey(f)) {
//            System.out.println(-1);
//            return;
//        }
//        System.out.println(map.get(f));

        if (dist[f] == Integer.MAX_VALUE) {
            System.out.println(-1);
            return;
        }
        System.out.println(dist[f]);
    }

}
