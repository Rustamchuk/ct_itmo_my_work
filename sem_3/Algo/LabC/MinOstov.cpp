#include <iostream>
#include <algorithm>
#include <vector>

using namespace std;

class Edge {
public:
    int u, v;
    long long w;
};

int compare(Edge a, Edge b);
int getDSU(int v);

vector<int> parent, sizes;

int main() {
    int n, m;
    vector<Edge> graph;
    cin >> n >> m;
    n++;
    graph.resize(m);
    parent.resize(n);
    sizes.resize(n);

    for (int i = 0; i < m; ++i) {
        int u, v;
        long long w;
        cin >> u >> v >> w;
        graph[i].u = u;
        graph[i].v = v;
        graph[i].w = w;
    }

    sort(graph.begin(), graph.begin() + m, compare);

    for (int i = 1; i < n; ++i) {
        parent[i] = i;
        sizes[i] = i;
    }

    long long res = 0;
    int u, v, x, y;
    long long w;
    for (Edge e : graph) {
        if (getDSU(e.u) != getDSU(e.v)) {
            res += e.w;
            x = getDSU(e.u);
            y = getDSU(e.v);
            if (x != y) {
                if (sizes[x] < sizes[y]) {
                    swap(x, y);
                }
                parent[y] = x;
                if (sizes[x] == sizes[y]) {
                    sizes[x]++;
                }
            }
        }
    }
    cout << res;
    return 0;
}

int compare(Edge a, Edge b) {
    return (a.w < b.w);
}

int getDSU(int v) {
    if (v != parent[v])
        parent[v] = getDSU(parent[v]);
    return parent[v];
}
