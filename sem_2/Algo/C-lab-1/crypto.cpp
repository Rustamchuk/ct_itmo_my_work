#include <iostream>
#include <vector>

int r;
int size;

struct matrix {
    long long a;
    long long b;
    long long c;
    long long d;
    bool valid = true;

    matrix() {

    }

    matrix(bool state) {
        valid = state;
    }

    void set(long long a, long long b, long long c, long long d) {
        this->a = a;
        this->b = b;
        this->c = c;
        this->d = d;
    }

    void muptiply(matrix first, matrix second) {
        if (not(first.valid)) {
            set(second.a, second.b, second.c, second.d);
        } else if (not(second.valid)) {
            set(first.a, first.b, first.c, first.d);
        } else {
            set((first.a * second.a + first.b * second.c) % r,
                (first.a * second.b + first.b * second.d) % r,
                (first.c * second.a + first.d * second.c) % r,
                (first.c * second.b + first.d * second.d) % r);
        }
    }
};

std::vector<matrix> tree;

void create(int n) {
    size = 1;
    while (size < n) size *= 2;
    tree.resize(size * 2 - 1);
    for (int i = 0; i < tree.size(); ++i) {
        tree[i] = *new matrix;
    }
}

void build(std::vector<matrix> input, int cur, int left, int right) {
    if (right - left == 1) {
        if (left < input.size()) {
            tree[cur] = input[left];
        }
    } else {
        int m = (left + right) / 2;
        build(input, 2 * cur + 1, left, m);
        build(input, 2 * cur + 2, m, right);
        tree[cur].muptiply(tree[cur * 2 + 1], tree[cur * 2 + 2]);
    }
}

matrix multiply(int start, int finish, int cur, int left, int right) {
    if (start >= right || finish <= left) {
        return *new matrix(false);
    }
    if (start <= left && finish >= right) {
        return tree[cur];
    }

    int nw = left + (right - left) / 2;
    matrix n = new matrix();
    n.muptiply(multiply(start, finish, cur * 2 + 1, left, nw),
               multiply(start, finish, cur * 2 + 2, nw, right));
    return n;
}

int main() {
    std::ios_base::sync_with_stdio(false);
    std::cin.tie(nullptr);
    std::cout.tie(nullptr);

    freopen("crypto.in", "r", stdin);
    freopen("crypto.out", "w", stdout);
    int n, m;
    scanf("%i %i %i", &r, &n, &m);

    std::vector<matrix> input;
    input.resize(n);

    long long a, b, c, d;

    create(n);
    for (int i = 0; i < n; ++i) {
        scanf("%lli %lli %lli %lli", &a, &b, &c, &d);
        input[i] = new matrix;
        input[i].set(a, b, c, d);
    }
    build(input, 0, 0, size);
//    for (int i = 0; i < n; ++i) {
//        printf("%lli %lli\n%lli %lli\n----\n", tree[i].a, tree[i].b, tree[i].c, tree[i].d);
//    }
    int s, f;
    for (int i = 0; i < m; ++i) {
        scanf("%i %i", &s, &f);
        s -= 1;
        matrix cur = multiply(s, f, 0, 0, size);
        printf("%lli %lli\n%lli %lli\n\n", cur.a, cur.b, cur.c, cur.d);
    }

    return 0;
}
