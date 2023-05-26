import java.util.Scanner;

public class ChinaWall {
    public static Walls[] tree;
    public static int size;

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        int n = sc.nextInt();
        int m = sc.nextInt();
        create(n);
        build(0, 0, size);
        for (int i = 0; i < m; i++) {
            String command = sc.next();
            if (command.equals("attack")) {
                Walls out = min(sc.nextInt() - 1, sc.nextInt(), 0, 0, size);
                System.out.println(out.h + " " + (out.index + 1));
            } else if (command.equals("defend")) {
                set(sc.nextInt() - 1, sc.nextInt(), sc.nextLong(), 0, 0, size);
            }
        }
    }

    public static void create(int n) {
        size = 1;
        while (size < n) size *= 2;
        tree = new Walls[size * 2 - 1];
        for (int i = 0; i < tree.length; i++) {
            tree[i] = new Walls();
        }
    }

    public static void build(int cur, int left, int right) {
        if (right - left == 1) {
            tree[cur].index = left;
        } else {
            int m = (left + right) / 2;
            build(2 * cur + 1, left, m);
            build(2 * cur + 2, m, right);
            tree[cur].index = tree[cur * 2 + 1].index;
        }
    }

    public static void push(int pos, int left, int right) {
        if (!tree[pos].changed || right - left == 1) return;
        int l = 2 * pos + 1;
        int r = 2 * pos + 2;
        if (tree[l].h < tree[pos].h) {
            tree[l].h = tree[pos].h;
            tree[l].changed = true;
        }
        if (tree[r].h < tree[pos].h) {
            tree[r].h = tree[pos].h;
            tree[r].changed = true;
        }
        tree[pos].changed = false;
    }

    public static Walls min(int start, int finish, int cur, int left, int right) {
        push(cur, left, right);
        if (start >= right || finish <= left) {
            Walls a = new Walls();
            a.h = Long.MAX_VALUE;
            return a;
        }
        if (start <= left && finish >= right) {
            return tree[cur];
        }

        int nw = left + (right - left) / 2;
        Walls l = min(start, finish, cur * 2 + 1, left, nw);
        Walls r = min(start, finish, cur * 2 + 2, nw, right);
        if (l.h > r.h) {
            return r;
        } else {
            return l;
        }
    }

    public static void set(int start, int finish, long value, int cur, int left, int right) {
        push(cur, left, right);
        if (start >= right || finish <= left) {
            return;
        }
        if (start <= left && finish >= right) {
            if (tree[cur].h < value) {
                tree[cur].h = value;
                tree[cur].changed = true;
            }
            return;
        }

        int nw = left + (right - left) / 2;
        set(start, finish, value, cur * 2 + 1, left, nw);
        set(start, finish, value, cur * 2 + 2, nw, right);
        Walls a = tree[cur * 2 + 1].h < tree[cur * 2 + 2].h ? tree[cur * 2 + 1] : tree[cur * 2 + 2];
        tree[cur].h = a.h;
        tree[cur].index = a.index;
    }

    private static class Walls {
        private long h = 0;
        private boolean changed = false;
        private int index = 0;
    }
}
