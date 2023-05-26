import java.util.Arrays;
import java.util.Scanner;

public class MinAddSet {
    private static Tree[] tree;
    private static int size;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long[] input = new long[n];
        create(n);
        for (int i = 0; i < n; i++) {
//            set(i, i + 1, sc.nextLong(), 0, 0, size);
            input[i] = sc.nextLong();
        }
//        System.out.println(Arrays.toString(tree));
        build(input, 0, 0, size);
//        System.out.println(Arrays.toString(tree));
        while (sc.hasNext()) {
//            System.out.println(Arrays.toString(tree));
            String command = sc.next();
            if (command.equals("min")) {
                System.out.println(min(sc.nextInt() - 1, sc.nextInt(), 0, 0, size));
            } else if (command.equals("set")) {
                set(sc.nextInt() - 1, sc.nextInt(), sc.nextLong(), 0, 0, size);
//                for (Tree v:
//                     tree) {
//                    System.out.print(v.min + " ");
//                }
//                System.out.println();
            } else if (command.equals("add")) {
                add(sc.nextInt() - 1, sc.nextInt(), sc.nextLong(), 0, 0, size);
//                for (Tree v:
//                        tree) {
//                    System.out.print(v.min + " ");
//                }
//                System.out.println();
            }
//            for (Tree v: tree) {
//                System.out.print(v.min + " ");
//            }
//            System.out.println();
        }
    }

    public static void create(int n) {
        size = 1;
        while(size < n) size *= 2;
        tree = new Tree[size * 2 - 1];
        for (int i = 0; i < tree.length; i++) {
            tree[i] = new Tree();
            tree[i].min = Long.MAX_VALUE;
        }
    }

    public static void build(long[] input, int cur, int left, int right) {
        if (right - left == 1) {
            if (left < input.length) {
                tree[cur].min = input[left];
            }
        } else {
            int m = (left + right) / 2;
            build(input, 2 * cur + 1, left, m);
            build(input, 2 * cur + 2, m, right);
            tree[cur].min = Math.min(tree[2 * cur + 1].min, tree[2 * cur + 2].min);
        }
    }

    public static void push(int pos, int left, int right) {
        if (!tree[pos].changed || right - left == 1) return;
        if (tree[pos].set != Long.MIN_VALUE) {
            tree[2 * pos + 1].set = tree[pos].set;
            tree[2 * pos + 1].min = tree[pos].set;
            tree[2 * pos + 1].add = 0;
            tree[2 * pos + 2].add = 0;
            tree[2 * pos + 2].set = tree[pos].set;
            tree[2 * pos + 2].min = tree[pos].set;
        }
        if (tree[pos].add != 0) {
            tree[2 * pos + 1].add += tree[pos].add;
            tree[2 * pos + 1].min += tree[pos].add;
//            tree[2 * pos + 1].set = 0;
//            tree[2 * pos + 2].set = 0;
            tree[2 * pos + 2].add += tree[pos].add;
            tree[2 * pos + 2].min += tree[pos].add;
        }
        tree[pos].set = Long.MIN_VALUE;
        tree[pos].add = 0;
        tree[pos].changed = false;
        tree[2 * pos + 1].changed = true;
        tree[2 * pos + 2].changed = true;
    }

    public static long min(int start, int finish, int cur, int left, int right) {
        push(cur, left, right);
        if (start >= right || finish <= left) return Long.MAX_VALUE;
        if (start <= left && finish >= right)  {
//            System.out.println(right - left);
            return tree[cur].min;
        }

        int nw = left + (right - left) / 2;
        return Math.min(min(start, finish, cur * 2 + 1, left, nw), min(start, finish, cur * 2 + 2, nw, right));
    }

    public static void set(int start, int finish, long value, int cur, int left, int right) {
        push(cur, left, right);
        if (start >= right || finish <= left) { return; }
        if (start <= left && finish >= right) {
            tree[cur].set = value;
            tree[cur].add = 0;
            tree[cur].changed = true;
            tree[cur].min = value;
            return;
        }

        int nw = left + (right - left) / 2;
        set(start, finish, value, cur * 2 + 1, left, nw);
        set(start, finish, value, cur * 2 + 2, nw, right);
        tree[cur].min = Math.min(tree[cur * 2 + 1].min, tree[cur * 2 + 2].min);
    }

    public static void add(int start, int finish, long value, int cur, int left, int right) {
        push(cur, left, right);
        if (start >= right || finish <= left) { return; }
        if (start <= left && finish >= right) {
            tree[cur].add += value;
            tree[cur].changed = true;
            tree[cur].min += value;
            return;
        }

        int nw = left + (right - left) / 2;
        add(start, finish, value, cur * 2 + 1, left, nw);
        add(start, finish, value, cur * 2 + 2, nw, right);
        tree[cur].min = Math.min(tree[cur * 2 + 1].min, tree[cur * 2 + 2].min);
    }
}

class Tree {
    public long min;
    public long add;
    public long set = Long.MIN_VALUE;
    public boolean changed = false;
}
