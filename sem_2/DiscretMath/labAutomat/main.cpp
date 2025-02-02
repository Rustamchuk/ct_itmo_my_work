#include <iostream>
#include <string>
#include <vector>
#include <fstream>
using namespace std;

struct Node {
    vector<vector<int>> next;

    Node() {
        next.resize(26);
    }
};

bool find(vector<int> v, int f)
{
    for (int i = 0; i < v.size(); i++)
    {
        if (v[i] == f)
        {
            return true;
        }
    }
    return false;
}

void dfs(int state, int pos, const string &line, const vector<Node> &nodes, vector<int> &chain, bool &ok, const vector<int> &stat) {
    if (pos == line.size()) {
        if (find(stat, state)) {
            ok = true;
        }
        return;
    }

    if (find(chain, state)) {
        return;
    }

    chain.push_back(state);

    char next_char = line[pos];
    for (int next_state: nodes[state].next[next_char - 'a']) {
        dfs(next_state, pos + 1, line, nodes, chain, ok, stat);
        if (ok) {
            return;
        }
    }
}

int main() {
    freopen("problem2.in", "r", stdin);
    freopen("problem2.out", "w", stdout);
    string line;
    int vars, n, end;
    cin >> line;
    cin >> vars >> n >> end;
    vector<Node> nodes(vars);
    vector<int> stat;
    vector<int> chain;
    bool found = false;
    for (int i = 0; i < vars; i++) {
        nodes.emplace_back();
    }
    for (int i = 0; i < end; i++) {
        int state;
        cin >> state;
        stat.push_back(state);
    }
    for (int i = 0; i < n; i++) {
        int p, t, l;
        cin >> p >> t;
        char c;
        cin >> c;
        l = c - 'a';
        nodes[p - 1].next[l].push_back(t - 1);
    }
    dfs(1, 0, line, nodes, chain, found, stat);

    if (!found) {
        cout << "Rejects";
    } else {
        cout << "Accepts";
    }
    return 0;
}