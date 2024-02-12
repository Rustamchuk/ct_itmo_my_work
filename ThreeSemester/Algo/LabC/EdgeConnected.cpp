#include <iostream>
#include <vector>

using namespace std;

vector<vector<int>> edges;
vector<pair<int, int>> lines;
vector<pair<int, int>> bufLines;
vector<int> tin, tup, used, buf, order;
int n, m;
int t, c = 0;

void dfs(int v, int p);

int main() {
    cin >> n >> m;
    n++;
    edges.resize(n, vector<int>());
    tin.resize(n);
    tup.resize(n);
    used.resize(n);
    buf.resize(m, 0);
    int u, v;
    for (int i = 0; i < m; i++) {
        cin >> u >> v;
        edges[u].push_back(v);
        edges[v].push_back(u);
    }
    for (int i = 1; i < n; i++) {
        if (used[i] == 0) {
            //            c++;
            dfs(i, -1);
        }
    }
    for (pair<int, int> e : bufLines) {
        for (int i = 0; i < m; i++) {
            if ((edges[e.first][i] == e.second || edges[e.second][i] == e.first) && buf[i] == 0) {
                buf[i] = c;
                break;
            }
        }
    }
    cout << c << endl;
    for (int i = 0; i < m; i++) {
        cout << buf[i] << " ";
    }
    cout << endl;
    return 0;
}

void dfs(int v, int p) {
    used[v] = 1;
    tin[v] = tup[v] = t++;
//    buf[v] = c;
//    order.push_back(v);
    int ret = 0;
    for (int u : edges[v]) {
        if (u == p) {
//            ret++;
//            if (ret > 1) {
//                tup[v] = min(tup[v], tin[u]);
//            }
            continue;
        }
        if (used[u] == 0) {
            lines.push_back({v, u});
            dfs(u, v);
            tup[v] = min(tup[v], tup[u]);
            if (tup[u] >= tin[v] && p != -1) {
                c++;
                while (lines.back() != make_pair(v, u)) {
                    bufLines.push_back(lines.back());
                    lines.pop_back();
                }
                bufLines.push_back(lines.back());
                lines.pop_back();
            }
            ret++;
            continue;
        }
        tup[v] = min(tup[v], tin[u]);
        if (tin[u] < tin[v]) {
            lines.push_back({v, u});
        }
    }

    if (p == -1 && ret > 1) {
        c++;
        while (!order.empty()) {
            bufLines.push_back(lines.back());
            order.pop_back();
        }
    }
}
