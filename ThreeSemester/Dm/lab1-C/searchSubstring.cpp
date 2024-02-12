#include <iostream>
#include <vector>

using namespace std;

int main() {
    ios_base::sync_with_stdio(false);

    string p, t;
    cin >> p >> t;
    string s = p + "#" + t;
    size_t n = s.length();
    size_t pl = p.length();
    size_t tl = t.length();

    vector<int> prefix(n);
    prefix[0] = 0;
    for (int i = 1; i < n; ++i) {
        int j = prefix[i - 1];
        while (true) {
            if (j <= 0 || s[i] == s[j]) {
                break;
            }
            j = prefix[j - 1];
        }
        if (s[i] == s[j]) {
            ++j;
        }
        prefix[i] = j;
    }
    vector<int> res;

    for (int i = pl; i < pl + 1 + tl; ++i) {
        if (prefix[i] == pl) {
            res.push_back(i - pl * 2 + 1);
        }
    }

    cout << res.size() << "\n";
    for (int i = 0; i < res.size(); ++i) {
        cout << res[i] << " ";
    }

    return 0;
}