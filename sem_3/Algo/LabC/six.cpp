#include <iostream>
#include <vector>

using namespace std;

int main() {
    cin.tie(nullptr);
    ios_base::sync_with_stdio(false);

    int t;
    cin >> t;
    //    cout << a + b << '\n';
    for (int i = 0; i < t; i++) {
        int n, m;
        cin >> n >> m;
        vector<vector<int>> book(n, vector<int>(m));
        int supMin = 100;
        int xx, yy = 0;
        for (int k = 0; k < n; k++) {
            string str;
            cin >> str;
            for (int j = 0; j < m; j++) {
                book[k][j] = str[j] - '0';
                if (supMin > book[k][j]) {
                    supMin = book[k][j];
                    xx = k;
                    yy = j;
                }
            }
        }
        int minn2 = 100;
        int x2, y2 = 0;
        for (int k = 0; k < n; k++) {
            for (int j = 0; j < m; j++) {
                if (minn2 > book[k][j] && (k != xx || j != yy)) {
                    minn2 = book[k][j];
                    x2 = k;
                    y2 = j;
                }
            }
        }
        int maxx = 0;
        int x, y = 0;
        if (x2 == xx) {
            for (int j = 0; j < m; j++) {
                int minn = 100;
                for (int l = 0; l < n; l++) {
                    for (int k = 0; k < m; k++) {
                        if (minn > book[l][k] && l != xx && k != j) {
                            minn = book[l][k];
                        }
                    }
                }
                if (minn > maxx) {
                    maxx = minn;
                    x = xx;
                    y = j;
                }
            }
        } else if (y2 == yy) {
            for (int k = 0; k < n; k++) {
                int minn = 100;
                for (int l = 0; l < n; l++) {
                    for (int h = 0; h < m; h++) {
                        if (minn > book[l][h] && l != k && h != yy) {
                            minn = book[l][h];
                        }
                    }
                }
                if (minn > maxx) {
                    maxx = minn;
                    x = k;
                    y = y2;
                }
            }
        } else {
            int minn = 100;
            for (int l = 0; l < n; l++) {
                for (int k = 0; k < m; k++) {
                    if (minn > book[l][k] && l != x2 && k != yy) {
                        minn = book[l][k];
                    }
                }
            }
            if (minn > maxx) {
                maxx = minn;
                x = x2;
                y = yy;
            }
            minn = 100;
            for (int l = 0; l < n; l++) {
                for (int k = 0; k < m; k++) {
                    if (minn > book[l][k] && l != xx && k != y2) {
                        minn = book[l][k];
                    }
                }
            }
            if (minn > maxx) {
                maxx = minn;
                x = xx;
                y = y2;
            }
        }
        cout << x+1 << " " << y+1 << "\n";
    }
}