#include <algorithm>
#include <iostream>
#include <set>
#include <unordered_set>
#include <vector>

using namespace std;

struct Node {
    int color;
    int cnt;
};

int n;
vector<int> tree[1000001];
Node nodes[1000001];
unordered_set<int> clrs[1000001];

void dfs(int v, unordered_set<int> &colors) {
    colors.insert(nodes[v].color);
    unordered_set<int> colors1;
    for (int child: tree[v]) {
        dfs(child, colors1);
        for (int i: colors1) {
            colors.insert(i);
        }
        colors1.clear();
    }
    nodes[v].cnt = colors.size();
}

void dfs(int v) {
    clrs[v].insert(nodes[v].color);
    for (int i = 0; i < tree[v].size(); i++) {
        dfs(tree[v][i]);
        if (clrs[v].size() < clrs[tree[v][i]].size()) {
            clrs[v].swap(clrs[tree[v][i]]);
        }
        for (int j : clrs[tree[v][i]]) {
            clrs[v].insert(j);
        }
        clrs[tree[v][i]].clear();
    }
    nodes[v].cnt = clrs[v].size();
}

void dfs() {
    //    unordered_set<int> colors;
    dfs(tree[0].back());
}


int main() {
    ios_base::sync_with_stdio(false);
    cin.tie(nullptr);

    cin >> n;
    int parent, c;

    for (int i = 1; i <= n; ++i) {
        cin >> parent >> c;
        nodes[i] = {c, 1};
        tree[parent].push_back(i);
    }

    dfs();

    for (int i = 1; i <= n; ++i) {
        cout << nodes[i].cnt << " ";
    }
    cout << endl;

    return 0;
}