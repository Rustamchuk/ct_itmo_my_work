#include <algorithm>
#include <fstream>
#include <iostream>
#include <map>
#include <set>
#include <string>
#include <vector>

using namespace std;

set<char> eps;

void checkEps(char key, const vector<string>& rel, bool &change) {
    for (const string &cr: rel) {
        bool res = true;
        for (char c: cr) {
            if (c < 'A' || c > 'Z' || !eps.count(c)) {
                res = false;
                break;
            }
        }

        if (res) {
            eps.insert(key);
            change = true;
        }
    }
}

int main() {
    ifstream input("epsilon.in");

    int n;
    char start;
    input >> n >> start;

    map<char, vector<string>> avt;
    for (int i = 0; i < n; ++i) {
        char A;
        string arrow, B;
        input >> A >> arrow;
        getline(input, B);
        if (B[0] == ' ') {
            string C;
            for (int j = 1; j < B.length(); j++) {
                C += B[j];
            }
            B = C;
        }
        avt[A].push_back(B);
        if (B.empty()) {
            eps.insert(A);
        }
    }

    bool changed = true;
    while (changed) {
        changed = false;

        for (const auto &relate: avt) {
            if (eps.count(relate.first)) {
                continue;
            }

            checkEps(relate.first, relate.second, changed);
        }
    }

    ofstream output("epsilon.out");
    for (char nonterminal: eps) {
        output << nonterminal << ' ';
    }
    output << '\n';

    input.close();
    output.close();

    return 0;
}