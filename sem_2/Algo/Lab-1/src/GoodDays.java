import java.util.Arrays;
import java.util.Scanner;

public class GoodDays {
    private static GoodTree[] tree;
    private static int size;
    private static long[] into;

    public static void main(String[] args) {
//        Scanner sc = new Scanner(System.in);
//        int n = sc.nextInt();
//        into = new long[] {7, 0, 5, 7, 7, 1, 1};
//        for (int i = 0; i < n; i++) {
//            into[i] = sc.nextInt();
//        }
//        create(6);
//        build(into, 0, 0, size);
//        Info res = max(0, size);
//        System.out.println(res.value);
//        System.out.println(res.x + " " + res.y);
//        for (GoodTree node :
//             tree) {
//            System.out.println(node.sum.toString() + " " + node.maxPref.toString() + " " + node.maxSuff.toString() + " " + node.maxSub.toString());
//        }
        test();
    }

    public static void test() {
        test:
        for (int i = 0; i < 10000000; i++) {
            System.out.println(i);
            int n = (int) (Math.random() * 8) + 1;
            long[] arr = new long[n];
            for (int j = 0; j < n; j++) {
                arr[j] = (int) (Math.random() * 8);
            }
            into = Arrays.copyOf(arr, n);
            create(n);
            build(arr, 0, 0, size);
            int command = (int) (Math.random() * 2);

            if (command == 0) { // max
                long[] dp = new long[arr.length];
                long max = arr[0] * arr[0];
                long min;
                long sum;
                for (int k = 0; k < n; k++) {
                    for (int j = k; j < n; j++) {
                        min = Long.MAX_VALUE;
                        sum = 0;
                        for (int l = k; l <= j; l++) {
                            sum += arr[l];
                            if (min > arr[l]) min = arr[l];
                        }
                        sum *= min;
                        if (sum > max) max = sum;
                    }
                    if (k == n - 1) {
                        sum = arr[k] * arr[k];
                        if (sum > max) max = sum;
                    }
                }

                Info treeAns = max(0, n);
                if (treeAns.value != max) {
//                    System.out.println(Arrays.toString(st));
                    System.out.println(Arrays.toString(arr));
                    System.out.println(Arrays.toString(dp));
                    System.out.println("Expect " + max);
                    System.out.println("Actual " + treeAns.value + " " + treeAns.x + " " + treeAns.y);
                    System.out.println("Brackets " + 0 + " " + n);
                    for (GoodTree node : tree) {
                        System.out.println(node.sum.toString() + " " + node.maxPref.toString() + " " + node.maxSuff.toString() + " " + node.maxSub.toString());
                    }
                    System.out.println();
                    break test;
                }

            }
        }
    }

    public static void create(int n) {
        size = 1;
        while (size < n) size *= 2;
        tree = new GoodTree[size * 2 - 1];
        for (int i = 0; i < tree.length; i++) {
            tree[i] = new GoodTree();
        }
    }

    public static void build(long[] input, int cur, int left, int right) {
        if (right - left == 1) {
            if (left < input.length) {
                tree[cur].sum.value = input[left] * input[left];
                tree[cur].sum.sum = input[left];
                tree[cur].sum.minValue = input[left];
                tree[cur].sum.x = left + 1;
                tree[cur].sum.y = right;
                tree[cur].maxPref.x = left + 1;
                tree[cur].maxPref.y = right;
                tree[cur].maxSuff.x = left + 1;
                tree[cur].maxSuff.y = right;
                tree[cur].maxSub.x = left + 1;
                tree[cur].maxSub.y = right;
                tree[cur].bestOfPref = tree[cur].bestOfSuf = tree[cur].sum;
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
        tree[cur].sum = makeInfo(tree[l].sum, tree[r].sum);
        tree[cur].maxPref = maxTwo(tree[l].sum, maxTwo(tree[l].maxPref, makeInfo(tree[l].sum, tree[r].maxPref)));
        tree[cur].maxSuff = maxTwo(tree[r].sum, maxTwo(tree[r].maxSuff, makeInfo(tree[l].maxSuff, tree[r].sum)));
        tree[cur].maxSub = maxTwo(makeInfo(tree[l].bestOfSuf, tree[r].bestOfPref), maxTwo(makeInfo(tree[l].maxSuff, tree[r].maxPref),
                maxTwo(maxFour(l), maxFour(r))));
        tree[cur].bestOfPref = maxTwoMins(tree[l].bestOfPref, tree[l].sum);
        tree[cur].bestOfSuf = maxTwoMins(tree[r].bestOfSuf, tree[r].sum);
    }

    private static Info maxTwoMins(Info left, Info right) {
        if (left.minValue > right.minValue) {
            return left;
        }
        return right;
    }

    private static Info minTwoMins(Info left, Info right) {
        if (left.minValue >= right.minValue) {
            return right;
        }
        return left;
    }

    public static Info maxTwo(Info left, Info right) {
        if (left.value > right.value) {
            return left;
        }
        return right;
    }

    public static Info makeInfo(Info left, Info right) {
        if (left.sum == Long.MIN_VALUE / 2) return right;
        if (right.sum == Long.MIN_VALUE / 2) return left;
        Info inf = new Info();
        inf.sum = minChecker(left.sum) + minChecker(right.sum);
        inf.minValue = Math.min(left.minValue, right.minValue);
        inf.value = inf.sum * inf.minValue;
        inf.x = left.x == -1 ? right.x : left.x;
        inf.y = right.y == -1 ? left.y : right.y;
        return inf;
    }

    public static long minChecker(long value) {
        if (value == Long.MIN_VALUE / 2) return 0;
        return value;
    }

    public static GoodTree combine(GoodTree left, GoodTree right) {
        GoodTree ans = new GoodTree();
        if (right.sum.value == Long.MIN_VALUE / 2) return left; // если бесконечный член
        if (left.sum.value == Long.MIN_VALUE / 2) return right; // если бесконечный член
        ans.sum = makeInfo(left.sum, right.sum);
        ans.maxPref = maxTwo(left.maxPref, maxTwo(makeInfo(left.sum, right.maxPref), left.sum));
        ans.maxSuff = maxTwo(right.maxSuff, maxTwo(makeInfo(left.maxSuff, right.sum), right.sum));
        ans.maxSub = maxTwo(maxTwo(maxFour(left), maxFour(right)), makeInfo(left.maxSuff, right.maxPref));
        return ans;
    }

    public static Info maxFour(int cur) {
        return maxTwo(maxTwo(tree[cur].sum, tree[cur].maxSub),
                maxTwo(tree[cur].maxSuff, tree[cur].maxPref));
    }

    public static Info maxFour(GoodTree tr) {
        return maxTwo(maxTwo(tr.sum, tr.maxSub),
                maxTwo(tr.maxSuff, tr.maxPref));
    }

    public static GoodTree max(int start, int finish, int cur, int left, int right) {
        if (start >= right || finish <= left) return new GoodTree();
        if (start <= left && finish >= right) {
            return tree[cur];
        }

        int nw = left + (right - left) / 2;
        GoodTree pref = max(start, finish, cur * 2 + 1, left, nw);
        GoodTree suff = max(start, finish, cur * 2 + 2, nw, right);
        return combine(pref, suff);
    }

    public static Info max(int start, int finish) {
        GoodTree tree = max(start, finish, 0, 0, size);
        Info ans = maxTwo(maxTwo(tree.maxSuff, tree.maxPref), maxTwo(tree.sum, tree.maxSub));
        return ans;
    }

    private static class GoodTree {
        public Info sum = new Info();
        public Info maxSuff = new Info();
        public Info maxPref = new Info();
        public Info maxSub = new Info();
        public Info bestOfPref = new Info();
        public Info bestOfSuf = new Info();
    }

    private static class Info {
        public long value = Long.MIN_VALUE / 2;
        public long sum = Long.MIN_VALUE / 2;
        public long minValue = Long.MAX_VALUE / 2;
        public int x = -1;
        public int y = -1;

        @Override
        public String toString() {
            return "(" + value + " " + sum + " " + minValue + " " + x + " " + y + ")";
        }
    }
}
