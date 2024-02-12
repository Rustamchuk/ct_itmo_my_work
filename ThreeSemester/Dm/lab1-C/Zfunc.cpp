#include <iostream>
#include <vector>

using namespace std;

int main() {
    ios_base::sync_with_stdio(false);

    string s;
    cin >> s;
    int n = (int) s.length();
    vector<int> pi (n);
    for (int i=1, l=0, r=0; i<n; ++i) {
        if (i <= r)
            pi[i] = min (r-i+1, pi[i-l]);
        while (i+pi[i] < n && s[pi[i]] == s[i+pi[i]])
            ++pi[i];
        if (i+pi[i]-1 > r)
            l = i,  r = i+pi[i]-1;
        cout << pi[i] << " ";
    }
}