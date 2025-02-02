#include <iostream>
#include <vector>

using namespace std;

const int MAX = 1e9;

int main() {
    int n, s, f;
    cin >> n >> s >> f;
    n++;
    vector<vector<int>> edges(n, vector<int>(n));
    vector<int> dist(n, MAX);
    vector<int> visited(n, 0);
    for (int i = 1; i < n; i++) {
        for (int j = 1; j < n; j++) {
            cin >> edges[i][j];
            if (edges[i][j] == -1) {
                edges[i][j] = MAX;
            }
        }
    }
    dist[s] = 0;
    for (int i = 1; i < n; i++) {
        int min = 0;
        for (int j = 1; j < n; j++) {
            if (visited[j] == 0 && dist[j] < dist[min]) {
                min = j;
            }
        }
        if (min == 0) {
            break;
        }
        for (int j = 1; j < n; j++) {
            if (visited[j] == 0 && edges[min][j] != MAX && dist[min] + edges[min][j] < dist[j]) {
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
    if (dist[f] == MAX) {
        cout << -1 << endl;
        return 0;
    }
    cout << dist[f] << endl;
    return 0;
}