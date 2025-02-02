#include <iostream>

int main() {
    std::string haystack = "mississippi";
    std::string needle = "issip";

    int n = haystack.length();
    int k = needle.length();
    int j = 0;
    int i = 0;
    for (; i < n; i++) {
        if (j >= k) {
            break;
        }
        if (haystack[i] == needle[j]) {
            j++;
        } else if (j != 0) {
            i -= j;
            j = 0;
        }
    }
    if (j >= k) {
        std::cout << i - k;
        return i - k;
    }
    std::cout << -1;
    return -1;
}
