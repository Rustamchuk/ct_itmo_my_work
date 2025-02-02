#include <iostream>
#include <string>
#include <cstring>
#include <algorithm>

int main() {
    std::string s, t;
    int k;
    std::cin >> k;
    for (int i = 1; i <= k; ++i) {
        std::cin >> t;
        s += t + (char)(i + '0');
    }
    int n = s.length(); // длина строки

    // константы
    const int maxlen = 10000 + 5; // максимальная длина строки
    const int alphabet = 256; // размер алфавита, <= maxlen
    int p[maxlen], cnt[maxlen], c[maxlen];
    memset (cnt, 0, alphabet * sizeof(int));
    for (int i=0; i<n; ++i)
        ++cnt[s[i]];
    for (int i=1; i<alphabet; ++i)
        cnt[i] += cnt[i-1];
    for (int i=0; i<n; ++i)
        p[--cnt[s[i]]] = i;
    c[p[0]] = 0;
    int classes = 1;
    for (int i=1; i<n; ++i) {
        if (s[p[i]] != s[p[i-1]])  ++classes;
        c[p[i]] = classes-1;
    }
    int pn[maxlen], cn[maxlen];
    for (int h=0; (1<<h)<n; ++h) {
        for (int i=0; i<n; ++i) {
            pn[i] = p[i] - (1<<h);
            if (pn[i] < 0)  pn[i] += n;
        }
        memset (cnt, 0, classes * sizeof(int));
        for (int i=0; i<n; ++i)
            ++cnt[c[pn[i]]];
        for (int i=1; i<classes; ++i)
            cnt[i] += cnt[i-1];
        for (int i=n-1; i>=0; --i)
            p[--cnt[c[pn[i]]]] = pn[i];
        cn[p[0]] = 0;
        classes = 1;
        for (int i=1; i<n; ++i) {
            int mid1 = (p[i] + (1<<h)) % n,  mid2 = (p[i-1] + (1<<h)) % n;
            if (c[p[i]] != c[p[i-1]] || c[mid1] != c[mid2])
                ++classes;
            cn[p[i]] = classes-1;
        }
        memcpy (c, cn, n * sizeof(int));
    }
    int v = 0;
    int lcp[maxlen];
    for (int i = 0; i<n; ++i) {
        if (c[i] != n-1) {
            int j = p[c[i]+1];
            while (s[i+v] == s[j+v]) ++v;
            lcp[c[i]] = v;
            if (v) --v;
        }
    }
    return 0;
}

//#include <iostream>
//#include <map>
//
//using namespace std;
//
//struct state {
//    int len, link;
//    map<char, int> next;
//};
//
//const int MAXLEN = 10000;
//state st[MAXLEN * 2];
//int sz, last;
//
//void sa_init() {
//    sz = last = 0;
//    st[0].len = 0;
//    st[0].link = -1;
//    sz++;
//}
//
//void sa_extend(char c) {
//    int cur = sz++;
//    st[cur].len = st[last].len + 1;
//    int p;
//    for (p = last; p != -1 && !st[p].next.count(c); p = st[p].link)
//        st[p].next[c] = cur;
//    if (p == -1)
//        st[cur].link = 0;
//    else {
//        int q = st[p].next[c];
//        if (st[p].len + 1 == st[q].len)
//            st[cur].link = q;
//        else {
//            int clone = sz++;
//            st[clone].len = st[p].len + 1;
//            st[clone].next = st[q].next;
//            st[clone].link = st[q].link;
//            for (; p != -1 && st[p].next[c] == q; p = st[p].link)
//                st[p].next[c] = clone;
//            st[q].link = st[cur].link = clone;
//        }
//    }
//    last = cur;
//}
//
//string lcs(string s, string t) {
//    sa_init();
//    for (int i = 0; i < (int) s.length(); ++i) {
//        sa_extend(s[i]);
//    }
//
//    int v = 0, l = 0,
//        best = 0, bestpos = 0;
//    for (int i = 0; i < (int) t.length(); ++i) {
//        while (v && !st[v].next.count(t[i])) {
//            v = st[v].link;
//            l = st[v].len;
//        }
//        if (st[v].next.count(t[i])) {
//            v = st[v].next[t[i]];
//            ++l;
//        }
//        if (l > best)
//            best = l, bestpos = i;
//    }
//    return t.substr(bestpos - best + 1, best);
//}
//
//int main() {
//    string s, t;
//    cin >> s;
//    cin >> t;
//    cout << lcs(s, t);
//    return 0;
//}
