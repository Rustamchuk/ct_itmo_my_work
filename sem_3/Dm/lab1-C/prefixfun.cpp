#include <iostream>
#include <vector>

using namespace std;

int main() {
    ios_base::sync_with_stdio(false);

    string s;
    cin >> s;
    int n = (int) s.length();
    vector<int> pi (n);
    cout << 0 << " ";
    for (int i=1; i<n; ++i) {
        int j = pi[i-1];
        while (j > 0 && s[i] != s[j])
            j = pi[j-1];
        if (s[i] == s[j])  ++j;
        pi[i] = j;
        cout << pi[i] << " ";
    }
}