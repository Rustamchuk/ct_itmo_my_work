import java.util.Arrays;
import java.util.Scanner;

public class MaxsubMultSet {
    private static TreeSubSegment[] tree;
    private static int size;
    public static void main(String[] args) {
        test();
    }

    public static void test() {
        test:
        for (int i = 0; i < 10000000; i++) {
            System.out.println(i);
            int n = (int) (Math.random() * 100) + 1;
            long[] arr = new long[n];
            for (int j = 0; j < n; j++) {
                arr[j] = (int) (Math.random() * 400) - 200;
            }
            create(n);
            build(arr, 0, 0, size);
            for (int j = 0; j < 10000; j++) {
                int command = (int) (Math.random() * 2);

                int left = (int) (Math.random() * n);
                int right = (int) (Math.random() * n);
                if (left > right) {
                    int a = left;
                    left = right;
                    right = a;
                } else if (left == right) {
                    right++;
                }

                if (command == 0) { // max
                    long[] dp = new long[right - left];
                    dp[0] = arr[left];
                    long max = arr[left];
                    for (int k = 1; k < dp.length; k++) {
                        dp[k] = Math.max(arr[left + k], dp[k - 1] + arr[left + k]);
                        if (dp[k] > max) max = dp[k];
                    }

                    long treeAns = max(left, right);
                    if (treeAns != max) {
                        System.out.println(Arrays.toString(arr));
                        System.out.println(Arrays.toString(dp));
                        System.out.println("Exprect " + max);
                        System.out.println("Ectual " + treeAns);
                        System.out.println("Brackets " + left + " " + right);
                        for (TreeSubSegment v: tree) {
                            System.out.print("(" + v.sum + " " + v.maxPref + " " + v.maxSuff + " " + v.maxSub + "|" +
                                    v.minPref + " " + v.minSuff + " " + v.minSub + ")");
                        }
                        System.out.println();
                        break test;
                    }
                } else if (command == 1) { // set
                    long value = (long) (Math.random() * 200);
                    for (int k = left; k < right; k++) {
                        arr[k] = value;
                    }
                    set(left, right, value, 0, 0, size);
                } else { // multy
                    for (int k = left; k < right; k++) {
                        arr[k] *= -1;
                    }

                    multy(left, right, -1, 0, 0, size);
                }
            }
        }
    }

    public static void create(int n) {
        size = 1;
        while(size < n) size *= 2;
        tree = new TreeSubSegment[size * 2 - 1];
        for (int i = 0; i < tree.length; i++) {
            tree[i] = new TreeSubSegment();
        }
    }

    public static void build(long[] input, int cur, int left, int right) {
        if (right - left == 1) {
            if (left < input.length) {
                tree[cur].sum = input[left];
            }
        } else {
            int m = (left + right) / 2;
            build(input, 2 * cur + 1, left, m);
            build(input, 2 * cur + 2, m, right);
            combine(cur);
        }
    }

    public static void combine(int cur) {
        int l = cur * 2 + 1;
        int r = cur * 2 + 2;
        if (tree[l].sum != Long.MIN_VALUE / 2 && tree[r].sum != Long.MIN_VALUE / 2)
            tree[cur].sum = tree[l].sum + tree[r].sum;
        else if (tree[l].sum != Long.MIN_VALUE / 2)
            tree[cur].sum = tree[l].sum;
        else if (tree[r].sum != Long.MIN_VALUE / 2)
            tree[cur].sum = tree[r].sum;


        tree[cur].maxPref = Math.max(tree[l].sum, Math.max(tree[l].maxPref, tree[l].sum + tree[r].maxPref));
        tree[cur].maxSuff = Math.max(tree[r].sum, Math.max(tree[r].maxSuff, tree[r].sum + tree[l].maxSuff));
        tree[cur].maxSub = Math.max(tree[l].maxSuff + tree[r].maxPref,
                Math.max(maxFour(l), maxFour(r)));

        tree[cur].minPref = Math.min(tree[l].sum, Math.min(tree[l].minPref, tree[l].sum + tree[r].minPref));
        tree[cur].minSuff = Math.min(tree[r].sum, Math.min(tree[r].minSuff, tree[r].sum + tree[l].minSuff));
        tree[cur].minSub = Math.min(tree[l].minSuff + tree[r].minPref,
                Math.min(minFour(l), minFour(r)));
    }

    public static TreeSubSegment combine(TreeSubSegment left, TreeSubSegment right) {
        TreeSubSegment ans = new TreeSubSegment();
        if (right.sum == Long.MIN_VALUE / 2) return left; // если бесконечный член
        if (left.sum == Long.MIN_VALUE / 2) return right; // если бесконечный член
        ans.sum = left.sum + right.sum;
        ans.maxPref = Math.max(left.maxPref, Math.max(left.sum + right.maxPref, left.sum));
        ans.maxSuff = Math.max(right.maxSuff, Math.max(right.sum + left.maxSuff, right.sum));
        ans.maxSub = Math.max(Math.max(maxFour(left), maxFour(right)), left.maxSuff + right.maxPref);
        return ans;
    }

    public static void push(int pos, int left, int right) {
        if (right - left == 1 || (tree[pos].set == Long.MIN_VALUE && tree[pos].multy == 1)) return;
        if (tree[pos].set != Long.MIN_VALUE) {
            long value = tree[pos].set * (right - left);
            int cur = 2 * pos + 1;
            tree[cur].sum = value / 2;
            if (right - left == 2) {
                tree[cur].maxPref = tree[cur].maxSuff = tree[cur].maxSub = Long.MIN_VALUE / 2;
            } else {
                if (tree[pos].set >= 0) {
                    tree[cur].maxPref = tree[cur].maxSuff = value / 2 - tree[pos].set;
                    tree[cur].maxSub = value / 2 - tree[pos].set * 2;

                    tree[cur].minPref = tree[cur].minSuff = tree[cur].minSub = tree[pos].set;
                } else {
                    tree[cur].maxPref = tree[cur].maxSuff = tree[cur].maxSub = tree[pos].set;

                    tree[cur].minPref = tree[cur].minSuff = value / 2 - tree[pos].set;
                    tree[cur].minSub = value / 2 - tree[pos].set * 2;
                }
            }
            tree[cur].multy = 1;
            tree[cur].set = tree[pos].set;

            cur += 1;
            tree[cur].sum = value / 2;
            if (right - left == 2) {
                tree[cur].maxPref = tree[cur].maxSuff = tree[cur].maxSub = Long.MIN_VALUE / 2;
            } else {
                if (tree[pos].set >= 0) {
                    tree[cur].maxPref = tree[cur].maxSuff = value / 2 - tree[pos].set;
                    tree[cur].maxSub = value / 2 - tree[pos].set * 2;

                    tree[cur].minPref = tree[cur].minSuff = tree[cur].minSub = tree[pos].set;
                } else {
                    tree[cur].maxPref = tree[cur].maxSuff = tree[cur].maxSub = tree[pos].set;

                    tree[cur].minPref = tree[cur].minSuff = value / 2 - tree[pos].set;
                    tree[cur].minSub = value / 2 - tree[pos].set * 2;
                }
            }
            tree[cur].multy = 1;
            tree[cur].set = tree[pos].set;
        }
        if (tree[pos].multy != 1) {
            tree[2 * pos + 1].sum *= tree[pos].multy;
            swapMaxMin(2 * pos + 1);
            tree[2 * pos + 1].multy = tree[pos].multy;

            tree[2 * pos + 2].sum *= tree[pos].multy;
            swapMaxMin(2 * pos + 2);
            tree[2 * pos + 2].multy = tree[pos].multy;
            tree[pos].multy = 1;
        }
        tree[pos].set = Long.MIN_VALUE;
    }

    public static long maxFour(int cur) {
        return Math.max(Math.max(tree[cur].sum, tree[cur].maxSub),
                Math.max(tree[cur].maxSuff, tree[cur].maxPref));
    }

    public static long maxFour(TreeSubSegment tr) {
        return Math.max(Math.max(tr.sum, tr.maxSub),
                Math.max(tr.maxSuff, tr.maxPref));
    }

    public static long minFour(int cur) {
        return Math.min(Math.min(tree[cur].sum, tree[cur].minSub),
                Math.min(tree[cur].minSuff, tree[cur].minPref));
    }

    public static TreeSubSegment max(int start, int finish, int cur, int left, int right) {
        push(cur, left, right);
        if (start >= right || finish <= left) return new TreeSubSegment();
        if (start <= left && finish >= right) {
            return tree[cur];
        }

        int nw = left + (right - left) / 2;
        TreeSubSegment pref = max(start, finish, cur * 2 + 1, left, nw);
        TreeSubSegment suff = max(start, finish, cur * 2 + 2, nw, right);
        return combine(pref, suff);
    }

    public static long max(int start, int finish) {
        TreeSubSegment tree = max(start, finish, 0, 0, size);
        return Math.max(Math.max(tree.maxSuff, tree.maxPref), Math.max(tree.sum, tree.maxSub));
    }

    public static void set(int start, int finish, long value, int cur, int left, int right) {
        push(cur, left, right);
        if (start >= right || finish <= left) { return; }
        if (start <= left && finish >= right) {
            tree[cur].set = value;
            tree[cur].multy = 1;
            tree[cur].sum = value * (right - left);

            if (right - left <= 1) {
                tree[cur].maxPref = tree[cur].maxSuff = tree[cur].maxSub = Long.MIN_VALUE / 2; // в листе всегда бесконечность
            } else {
                if (value >= 0) {
                    tree[cur].maxPref = tree[cur].maxSuff = value * (right - left) - value;
                    tree[cur].maxSub = value * (right - left) - value * 2;

                    tree[cur].minPref = tree[cur].minSuff = tree[cur].minSub = value;
                } else {
                    tree[cur].maxPref = tree[cur].maxSuff = tree[cur].maxSub = value;

                    tree[cur].minPref = tree[cur].minSuff = value * (right - left) - value;
                    tree[cur].minSub = value * (right - left) - value * 2;
                }
            }
            return;
        }

        int nw = left + (right - left) / 2;
        set(start, finish, value, cur * 2 + 1, left, nw);
        set(start, finish, value, cur * 2 + 2, nw, right);
        combine(cur);
    }

    public static void multy(int start, int finish, long value, int cur, int left, int right) {
        push(cur, left, right);
        if (start >= right || finish <= left) { return; }
        if (start <= left && finish >= right) {
            tree[cur].multy *= value;

            if (right - left <= 1) {
                tree[cur].sum *= -1;
                return;
            }
            tree[cur].sum = -1 * (tree[cur * 2 + 1].sum + tree[cur * 2 + 2].sum);
            swapMaxMin(cur);
            return;
        }

        int nw = left + (right - left) / 2;
        multy(start, finish, value, cur * 2 + 1, left, nw);
        multy(start, finish, value, cur * 2 + 2, nw, right);
        combine(cur);
    }

    public static void swapMaxMin(int cur) {
        long oldMax = tree[cur].maxPref;
        tree[cur].maxPref = tree[cur].minPref * -1;
        tree[cur].minPref = oldMax * -1;
        oldMax = tree[cur].maxSuff;
        tree[cur].maxSuff = tree[cur].minSuff * -1;
        tree[cur].minSuff = oldMax * -1;
        oldMax = tree[cur].maxSub;
        tree[cur].maxSub = tree[cur].minSub * -1;
        tree[cur].minSub = oldMax * -1;
    }
}

class TreeSubSegment {
    public long sum = Long.MIN_VALUE / 2;
    public long maxSuff = Long.MIN_VALUE / 2;
    public long maxPref = Long.MIN_VALUE / 2;
    public long maxSub = Long.MIN_VALUE / 2;

    public long minSuff = Long.MAX_VALUE / 2;
    public long minPref = Long.MAX_VALUE / 2;
    public long minSub = Long.MAX_VALUE / 2;
    public int multy = 1;
    public long set = Long.MIN_VALUE;
}
