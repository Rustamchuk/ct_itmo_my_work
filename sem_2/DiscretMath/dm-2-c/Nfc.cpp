#include <fstream>
#include <iostream>
#include <map>
#include <string>
#include <vector>

using namespace std;

map<char, vector<string>> avt;
long long dp[26][100][100] = {0};

void counter(const string& word) {
    for (int len = 1; len <= word.length(); len++) {
        for (int i = 0; i + len - 1 < word.length(); i++) {
            int j = i + len - 1;
            for (char m = 0; m < 26; m++) {
                if (avt.find(m) == avt.end()) {
                    continue;
                }
                for (const string& rule : avt[m]) {
                    if (len != 1) {
                        if (rule.length() == 2) {
                            for (int k = i; k < j; k++) {
                                dp[m][i][j] += dp[rule[1] - 'A'][k + 1][j] * dp[rule[0] - 'A'][i][k];
                                dp[m][i][j] %= 1000000007;
                            }
                        }
                    } else {
                        if (rule.length() == 1 && rule[0] == word[i]) {
                            dp[m][i][j]++;
                        }
                    }
                }
            }
        }
    }
}

int main() {
    ifstream in("nfc.in");

    int n;
    char start;
    in >> n >> start;

    for (int i = 0; i < n; i++) {
        char a;
        string b;
        in >> a >> b >> b;
        avt[a - 'A'].push_back(b);
    }

    string word;
    in >> word;
    counter(word);

    ofstream out("nfc.out");
    out << dp[start - 'A'][0][word.length() - 1] << endl;
    in.close();
    out.close();
    return 0;
}
