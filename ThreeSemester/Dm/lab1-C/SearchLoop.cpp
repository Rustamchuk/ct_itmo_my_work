#include <iostream>
#include <vector>

using namespace std;

int main() {
    ios_base::sync_with_stdio(false);

    string s;
    cin >> s;

    auto prefix_func = [](std::string_view text) {
        vector<int> pi(text.size() + 1);
        int k = pi[0] = pi[1] = 0;
        for (size_t pos = 1; pos < text.length(); ++pos) {
            while (k > 0 && text[k] != text[pos])
                k = pi[k];
            if (text[k] == text[pos])
                ++k;
            pi[pos + 1] = k;
        }
        return pi;
    };

    auto max_border = prefix_func(s)[s.size()];
    auto min_period = s.size() - max_border;
    if (s.size() % min_period == 0) {
        cout << min_period << endl;
    } else {
        cout << s.size() << endl;
    }
    return 0;
}