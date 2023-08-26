#include <algorithm>
#include <iostream>
#include <vector>


std::vector<char> automata[26][26];
size_t n, m;

bool check(char cr, std::string &word, int ind) {
    if (ind == word.length()) {
        return cr == '0';
    }

    if (cr == '0') {
        return false;
    }
    std::vector<char> cur = automata[cr - 'A'][word[ind] - 'a'];

    if (cur.empty()) {
        return false;
    }

    for (char i: cur) {
        if (check(i, word, ind + 1)) {
            return true;
        }
    }

    return false;
}

int main() {
    freopen("automaton.in", "r", stdin);
    char start;
    scanf("%zd %c\n", &n, &start);
    char a;
    std::string b, c;
    for (int i = 0; i < n; i++) {
        std::cin >> a >> c >> b;
        if (b.size() == 2) {
            automata[a - 'A'][b[0] - 'a'].push_back(b[1]);
        } else {
            automata[a - 'A'][b[0] - 'a'].push_back('0');
        }
    }
    freopen("automaton.out", "w", stdout);
    scanf("%zd", &m);
    std::string x;
    for (int i = 0; i < m; i++) {
        std::cin >> x;
        if (check(start, x, 0)) {
            printf("%s\n", "yes");
        } else {
            printf("%s\n", "no");
        }
    }
    return 0;
}