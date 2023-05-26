import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Crypto {
    private static matrix[] tree;
    private static int size;
    private static int r;

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(new File("crypto.in"));
        PrintWriter consoleOutput = new PrintWriter("crypto.out");
//        Scanner sc = new Scanner(System.in);
        r = sc.nextInt();
        int n = sc.nextInt();
        int m = sc.nextInt();
        matrix[] input = new matrix[n];
        create(n);
        for (int i = 0; i < n; i++) {
            input[i] = new matrix();
            input[i].set(sc.nextLong(), sc.nextLong(), sc.nextLong(), sc.nextLong());
//            input[i] = sc.nextInt();
        }
        build(input, 0, 0, size);
//        System.out.println(Arrays.toString(tree));
        for (int i = 0; i < m; i++) {
            consoleOutput.append(multiply(sc.nextInt() - 1, sc.nextInt(), 0, 0, size).toString());
            consoleOutput.append(System.lineSeparator());
            consoleOutput.append(System.lineSeparator());
        }
        consoleOutput.flush();
        consoleOutput.close();

    }

    public static void create(int n) {
        size = 1;
        while (size < n) size *= 2;
        tree = new matrix[size * 2 - 1];
        for (int i = 0; i < tree.length; i++) {
            tree[i] = new matrix();
        }
    }

    public static void build(matrix[] input, int cur, int left, int right) {
        if (right - left == 1) {
            if (left < input.length) {
                tree[cur] = input[left];
            }
        } else {
            int m = (left + right) / 2;
            build(input, 2 * cur + 1, left, m);
            build(input, 2 * cur + 2, m, right);
            tree[cur].muptiply(tree[cur * 2 + 1], tree[cur * 2 + 2]);
        }
    }

//    public static void set(int pos, matrix value, int cur, int left, int right) {
//        if (right - left == 1) {
//            tree[cur] = value;
//            return;
//        }
//        int nw = left + (right - left) / 2;
//        if (pos < nw)
//            set(pos, value, cur * 2 + 1, left, nw);
//        else
//            set(pos, value, cur * 2 + 2, nw, right);
//        tree[cur].muptiply(tree[cur * 2 + 1], tree[cur * 2 + 2]);
//    }

    public static matrix multiply(int start, int finish, int cur, int left, int right) {
        if (start >= right || finish <= left) {
            return null;
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

    private static class matrix {
        private long a;
        private long b;
        private long c;
        private long d;

        private void set(long a, long b, long c, long d) {
            this.a = a;
            this.b = b;
            this.c = c;
            this.d = d;
        }

        private void muptiply(matrix first, matrix second) {
            if (first == null) {
                set(second.a, second.b, second.c, second.d);
            }
            else if (second == null) {
                set(first.a, first.b, first.c, first.d);
            }
            else {
                set((first.a * second.a + first.b * second.c) % r,
                        (first.a * second.b + first.b * second.d) % r,
                        (first.c * second.a + first.d * second.c) % r,
                        (first.c * second.b + first.d * second.d) % r);
            }
        }

        @Override
        public String toString() {
            return (a + " " + b + System.lineSeparator() + c + " " + d);
        }
    }
}
