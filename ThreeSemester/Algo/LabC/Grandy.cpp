#include <iostream>
#include <vector>
#include <algorithm>

using namespace std;

vector<vector<int>> edges;
vector<int> grundy;
int n, m;

void dfs(int v);

int main() {
    cin >> n >> m;
    n++;
    edges.resize(n);
    grundy.resize(n);
    edges[0].resize(n, 0);
    int u, v;
    for (int i = 0; i < m; i++) {
        cin >> u >> v;
        edges[u].push_back(v);
    }
    for (int i = 1; i < n; i++) {
        if (edges[0][i] == 0) {
            dfs(i);
        }
    }
    for (int i = 1; i < n; i++) {
        cout << grundy[i] << endl;
    }
    return 0;
}

void dfs(int v) {
    edges[0][v] = 1;
    vector<int> gv;
    for (int u : edges[v]) {
        if (edges[0][u] == 0) {
            dfs(u);
        }
        gv.push_back(grundy[u]);
    }
    sort(gv.begin(), gv.end());
    gv.erase(unique(gv.begin(), gv.end()), gv.end());
    for (int i = 0; i <= gv.size(); i++) {
        if (i == gv.size() || gv[i] != i) {
            grundy[v] = i;
            break;
        }
    }
}
