#include <iostream>
#include <vector>

using namespace std;

vector<int> used;
vector<int> out;
vector<int> res;
vector<vector<int>> edges;

void dfs(int v);

int main() {
    int n, m, s;
    cin >> n >> m >> s;
    n++;
    used.resize(n, 0);
    edges.resize(n);
    res.resize(n);
    int u, v;
    for (int i = 0; i < m; i++) {
        cin >> u >> v;
        edges[u].push_back(v);
    }
    dfs(s);

    for (int t: out) {
        for (int k: edges[t]) {
            if (res[t] || !res[k]) {
                res[t] = 1;
            }
        }
    }

    if (res[s] == 0) {
        cout << "Second player wins" << endl;
        return 0;
    }
    cout << "First player wins" << endl;
    return 0;

//    for (int i = 1; i < n; ++i) {
//        if (!used[i]) {
//            std::cout << "Second player wins" << std::endl;
//            return 0;
//        }
//    }
//
//    std::cout << "First player wins" << std::endl;
//
//    return 0;
}

void dfs(int v) {
    used[v] = 1;
    for (int u: edges[v]) {
        if (used[u] == 0) {
            dfs(u);
        }
    }
    out.push_back(v);
}
