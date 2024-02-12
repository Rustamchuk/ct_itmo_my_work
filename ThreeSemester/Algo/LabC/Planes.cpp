#include <iostream>
#include <vector>

using namespace std;

vector<vector<int>> edges, g;
int n;

void dfs(int v);
void dfs1(int v);

int main() {
    cin >> n;
    n++;
    edges.resize(n, vector<int>(n, 0));
    g.resize(n, vector<int>(n, 0));
    for (int i = 1; i < n; i++) {
        for (int j = 1; j < n; j++) {
            cin >> edges[i][j];
        }
    }
    int l = 0;
    int r = 1e9;
    while (l < r) {
        int m = (l + r) / 2;
        for (int i = 1; i < n; i++) {
            for (int j = 1; j < n; j++) {
                if (edges[i][j] <= m) {
                    g[i][j] = 1;
                    continue;
                }
                g[i][j] = 0;
            }
        }
        std::fill(edges[0].begin(), edges[0].end(), 0);
        dfs(1);
        int f = 1;
        for (int i = 1; i < n; i++) {
            if (edges[0][i] == 0) {
                f = 0;
                break;
            }
        }
        if (f == 1) {
            f = 0;
            std::fill(edges[0].begin(), edges[0].end(), 0);
            dfs1(1);
            for (int i = 1; i < n; i++) {
                if (edges[0][i] == 0) {
                    f = 1;
                    break;
                }
            }
        } else {
            f = 1;
        }
        if (f == 0) {
            r = m;
        } else {
            l = m + 1;
        }
    }
    cout << l;
    return 0;
}

void dfs(int v) {
    edges[0][v] = 1;
    for (int i = 1; i < n; i++) {
        if (g[v][i] == 1 && edges[0][i] == 0) {
            dfs(i);
        }
    }
}

void dfs1(int v) {
    edges[0][v] = 1;
    for (int i = 1; i < n; i++) {
        if (g[i][v] == 1 && edges[0][i] == 0) {
            dfs1(i);
        }
    }
}
