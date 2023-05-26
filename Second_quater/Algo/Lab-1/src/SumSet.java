import java.util.Arrays;
import java.util.Scanner;

public class SumSet {
    private static long[] tree;
    private static int size;
    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        long[] input = new long[n];
        create(n);
        for (int i = 0; i < n; i++) {
            set(i, sc.nextLong(), 0, 0, size);
//            input[i] = sc.nextInt();
        }
//        build(input, 0, 0, size);
//        System.out.println(Arrays.toString(tree));
        while (sc.hasNext()) {
            String command = sc.next();
            if (command.equals("sum")) {
                System.out.println(sum(sc.nextInt() - 1, sc.nextInt(), 0, 0, size));
            } else if (command.equals("set")) {
                set(sc.nextInt() - 1, sc.nextLong(), 0, 0, size);
            }
        }
    }

    public static void create(int n) {
        size = 1;
        while(size < n) size *= 2;
        tree = new long[size * 2 - 1];
    }

//    public static void build(int[] input, int cur, int left, int right) {
//        if (right - left == 1) {
//            if (left < input.length) {
//                tree[cur] = input[left];
//            }
//        } else {
//            int m = (left + right) / 2;
//            build(input, 2 * cur + 1, left, m);
//            build(input, 2 * cur + 2, m, right);
//            tree[cur] = tree[2 * cur + 1] + tree[2 * cur + 2];
//        }
//    }

    public static long sum(int start, int finish, int cur, int left, int right) {
        if (start >= right || finish <= left) return 0;
        if (start <= left && finish >= right) return tree[cur];

        int nw = left + (right - left) / 2;
        return sum(start, finish, cur * 2 + 1, left, nw) + sum(start, finish, cur * 2 + 2, nw, right);
    }

    public static void set(int pos, long value, int cur, int left, int right) {
        if (right - left == 1) {
            tree[cur] = value;
            return;
        }
        int nw = left + (right - left) / 2;
        if (pos < nw)
            set(pos, value, cur * 2 + 1, left, nw);
        else
            set(pos, value, cur * 2 + 2, nw, right);
        tree[cur] = tree[cur * 2 + 1] + tree[cur * 2 + 2];
    }
}
