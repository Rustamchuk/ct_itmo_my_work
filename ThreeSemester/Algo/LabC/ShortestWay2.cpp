#include <iostream>
#include <queue>
#include <set>
#include <vector>

using namespace std;

const int MAX = 1e9;

struct Edge {
    int v, w;
};

int main() {
    std::ios::sync_with_stdio(false);
    std::cin.tie(NULL);

    int n, m, s;
    cin >> n >> m;
    n++;
    s = 1;
    vector<vector<Edge>> edges(n);
    vector<int> dist(n, MAX);
    int u, v, w;
    for (int i = 0; i < m; i++) {
        cin >> u >> v >> w;
        edges[u].push_back({v, w});
        edges[v].push_back({u, w});
    }
    dist[s] = 0;
    priority_queue<pair<int, int>, std::vector<std::pair<int, int>>, std::greater<>> queue;
    queue.push({0, s});
    while (!queue.empty()) {
        int d_v = queue.top().first;
        int v = queue.top().second;
        queue.pop();
        if (d_v != dist[v]) {
            continue;
        }
        for (Edge e : edges[v]) {
            if (dist[v] + e.w < dist[e.v]) {
                dist[e.v] = dist[v] + e.w;
                queue.push({dist[e.v], e.v});
            }
        }
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
    for (int i = 1; i < n; i++) {
        cout << dist[i] << " ";
    }
    return 0;
}