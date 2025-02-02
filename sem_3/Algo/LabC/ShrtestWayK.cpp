#include <iostream>
#include <vector>

using namespace std;

struct Edge {
    int u, v, w;
};

int main() {
    int n, m, k, s;
    cin >> n >> m >> k >> s;
    s--;
    vector<Edge> edges(m);
    int u, v, w;
    for (int i = 0; i < m; i++) {
        cin >> u >> v >> w;
        edges[i] = {--u, --v, w};
    }
    vector<vector<int>> dist(k + 1, vector<int>(n, 1e9));
    dist[0][s] = 0;
    for (int i = 0; i < k; i++) {
        for (Edge e : edges) {
            if (dist[i][e.u] < 1e9) {
                dist[i + 1][e.v] = std::min(dist[i + 1][e.v], dist[i][e.u] + e.w);
            }
        }
    }

    for (int i = 0; i < n; i++) {
        if (dist[k][i] == 1e9) {
            cout << -1 << endl;
        } else {
            cout << dist[k][i] << endl;
        }
    }
    return 0;
//    dist[s] = 0;
//    priority_queue<pair<int, int>, std::vector<std::pair<int, int>>, std::greater<>> queue;
//    queue.push({0, s});
//    while (!queue.empty()) {
//        int d_v = queue.top().first;
//        int v = queue.top().second;
//        queue.pop();
//        if (d_v != dist[v]) {
//            continue;
//        }
//        for (Edge e : edges[v]) {
//            if (dist[v] + e.w < dist[e.v]) {
//                dist[e.v] = dist[v] + e.w;
//                queue.push({dist[e.v], e.v});
//            }
//        }
//    }
}