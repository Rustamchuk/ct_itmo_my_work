#include <iostream>
#include <vector>

using namespace std;

int main() {
    int n;
    cin >> n;
    n++;
    vector<int> arr(n);
    int l = -1, r = -1;
    for (int i = 1; i < n; i++) {
        int k;
        cin >> k;
        arr[k]++;
        if (arr[k] > 1) {
            if (l != -1) {
                l = -1;
                break;
            }
            l = k;
        }
    }
    if (l != -1) {
        for (int i = 1; i < n; i++) {
            if (arr[i] == 0) {
                if (r != -1) {
                    l = -1;
                    r = -1;
                    break;
                }
                r = i;
            }
        }
    }
    cout << l << " " << r;
    return 0;
}
