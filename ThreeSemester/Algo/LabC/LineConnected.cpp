#include <iostream>
#include <vector>

using namespace std;

vector<vector<int>> edges;
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
    buf.resize(n);
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
    cout << c << endl;
    for (int i = 1; i < n; i++) {
        cout << buf[i] << " ";
    }
    cout << endl;
    return 0;
}

void dfs(int v, int p) {
    used[v] = 1;
    tin[v] = tup[v] = t++;
    buf[v] = c;
    order.push_back(v);
    int ret = 0;
    for (int u : edges[v]) {
        if (u == p) {
            ret++;
            if (ret > 1) {
                tup[v] = min(tup[v], tin[u]);
            }
            continue;
        }
        if (used[u] == 0) {
            dfs(u, v);
            tup[v] = min(tup[v], tup[u]);
            continue;
        }
        tup[v] = min(tup[v], tin[u]);
    }

    if (tup[v] == tin[v]) {
        c++;
        while (true) {
            int x = order.back();
            buf[x] = c;
            order.pop_back();
            if (x == v) {
                break;
            }
        }
    }
}
