import java.util.Arrays;
import java.util.Scanner;

public class GoodDays2 {
    public static int size;
    public static int n;
    public static minTree[] tree;
    public static long max = Long.MIN_VALUE;
    public static long l = -1;
    public static long r = -1;

    public static void main(String[] args) {
        max = Long.MIN_VALUE;
        l = -1;
        r = -1;
        Scanner sc = new Scanner(System.in);
        n = sc.nextInt();
        long[] into = new long[n];
        int parent;
        for (int i = 0; i < n; i++) {
            into[i] = sc.nextLong();
        }
        create(n);
        build(into, 0, 0, size);
        searchMax( 0, n);
        System.out.println(max);
        System.out.println(l + " " + r);
    }

    public static void searchMax(int left, int right) {
        if (right - left == 1) {
            long res = sum(left, right, 0, 0, size) * min(left, right, 0, 0, size).min;

            if (max < res) {
                max = res;
                l = left + 1;
                r = right;
            }
            return;
        }
        if (left >= right || right > n) return;
        minTree min = min(left, right, 0, 0, size);
        long res = min.min * sum(left, right, 0, 0, size);
        if (max < res) {
            max = res;
            l = left + 1;
            r = right;
        }
        int nw = min.index;
        searchMax(left, nw);
        searchMax(nw + 1, right);

    }

    public static void create(int n) {
        size = 1;
        while(size < n) size *= 2;
        tree = new minTree[size * 2 - 1];
        for (int i = 0; i < tree.length; i++) {
            tree[i] = new minTree();
        }
    }

    public static void build(long[] input, int cur, int left, int right) {
        if (right - left == 1) {
            if (left < input.length) {
                tree[cur].min = input[left];
                tree[cur].index = left;
                tree[cur].sum = input[left];
            }
        } else {
            int m = (left + right) / 2;
            build(input, 2 * cur + 1, left, m);
            build(input, 2 * cur + 2, m, right);
            min2(tree[cur], tree[2 * cur + 1], tree[2 * cur + 2]);
            tree[cur].sum = tree[2 * cur + 1].sum + tree[2 * cur + 2].sum;
        }
    }

    public static void min2(minTree set, minTree left, minTree right) {
        if (left.min > right.min) {
            set.min = right.min;
            set.index = right.index;
        } else {
            set.min = left.min;
            set.index = left.index;
        }
    }

    public static minTree min(int start, int finish, int cur, int left, int right) {
        if (start >= right || finish <= left) return new minTree();
        if (start <= left && finish >= right) return tree[cur];

        int nw = left + (right - left) / 2;
        minTree l = min(start, finish, cur * 2 + 1, left, nw);
        minTree r = min(start, finish, cur * 2 + 2, nw, right);
        if (l.min > r.min) {
            return r;
        } else {
            return l;
        }
    }

    public static long sum(int start, int finish, int cur, int left, int right) {
        if (start >= right || finish <= left) return 0;
        if (start <= left && finish >= right) return tree[cur].sum;

        int nw = left + (right - left) / 2;
        return sum(start, finish, cur * 2 + 1, left, nw) +
        sum(start, finish, cur * 2 + 2, nw, right);
    }

    private static class minTree {
        private long min = Long.MAX_VALUE;
        private long sum = 0;
        private int index;
    }
}
