#include <iostream>
#include <fstream>
#include <vector>
#include <cstring>

using namespace std;

const int MOD = 1000000007;
int l;

int find(int pos, int count[][l], int arr[], vector<int> a[]) {
    if (count[pos][l] != -4) {
        return count[pos][l];
    }

    if (l != 0) {
        count[pos][l] = 0;
        for (int i = 0; i < a[pos].size(); i++) {
            count[pos][l] += find(a[pos][i], l - 1, count, arr, a);
            count[pos][l] %= MOD;
        }
        return count[pos][l];
    }
    else {
        count[pos][l] = arr[pos];
        return count[pos][l];
    }
}

int main() {
    ifstream in("problem4.in");
    ofstream out("problem4.out");

    int n, m, k;
    in >> n >> m >> k >> l;
    n++;

    int states[k];
    for (int i = 0; i < k; i++) {
        in >> states[i];
    }

    int fstates[n];
    memset(fstates, 0, sizeof(fstates));
    for (int i = 0; i < k; i++) {
        fstates[states[i]] = 1;
    }

    vector<int> mas[n];
    for (int i = 0; i < m; i++) {
        int a, b;
        string c;
        in >> a >> b >> c;
        mas[a].push_back(b);
    }
    in.close();

    int count[n][l];
    memset(count, -4, sizeof(count));
    int x = find(1, count, fstates, mas);

    out << x << endl;
    out.close();

    return 0;
}