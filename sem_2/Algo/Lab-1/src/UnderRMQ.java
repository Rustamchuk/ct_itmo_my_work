import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Scanner;

public class UnderRMQ {
    private static UnderTree[] tree;
    private static int size;

    private static boolean stop = false;
    private static boolean finish = false;

    public static void main(String[] args) throws IOException {
        Scanner sc = new Scanner(new File("rmq.in"));
        PrintWriter consoleOutput = new PrintWriter("rmq.out");
        int n = sc.nextInt();
        int m = sc.nextInt();
        create(n);
//        System.out.println(Arrays.toString(tree));
//        System.out.println(Arrays.toString(tree));
        int[][] coms = new int[m][3];
        for (int i = 0; i < m; i++) {
            coms[i][0] = sc.nextInt() - 1;
            coms[i][1] = sc.nextInt();
            coms[i][2] = sc.nextInt();
            set(coms[i][0], coms[i][1], coms[i][2], 0, 0, size);
        }
        for (UnderTree t :
                tree) {
            System.out.println(t.value + " " + t.changed);
        }
        StringBuilder a = new StringBuilder();
        if (stop) {
            consoleOutput.println("inconsistent");
        } else {
            for (int i = 0; i < n; i++) {
                a.append(get(i, 0, 0, size)).append(" ");
                System.out.println();
                for (UnderTree t :
                        tree) {
                    System.out.println(t.value + " " + t.changed);
                }
                if (stop) {
                    consoleOutput.println("inconsistent");
                    break;
                }
            }
            if (!stop) {
                for (int i = 0; i < m; i++) {
                    if (min(coms[i][0], coms[i][1], 0, 0, size) != coms[i][2]) {
                        stop = true;
                        consoleOutput.println("inconsistent");
                        break;
                    }
                }
                if (!stop) {
                    finish = true;
                    for (int i = 0; i < n; i += 2) {
                        push(i, 0, size);
                        if (stop) {
                            consoleOutput.println("inconsistent");
                            break;
                        }
                    }
                    if (!stop) {
                        consoleOutput.println("consistent");
                        consoleOutput.println(a);
                    }
                }
            }
        }
        consoleOutput.close();
    }

    public static void create(int n) {
        size = 1;
        while(size < n) size *= 2;
        tree = new UnderTree[size * 2 - 1];
        for (int i = 0; i < tree.length; i++) {
            tree[i] = new UnderTree();
        }
    }

    public static void push(int pos, int left, int right) {
        if (right - left == 1) return;
        if (!tree[pos].changed) return;
        int l = 2 * pos + 1;
        int r = 2 * pos + 2;
        if (finish) {
            if (tree[pos].value != Math.min(tree[l].value, tree[r].value)) {
                stop = true;
            }
            return;
        }
        if (!tree[l].changed) {
            tree[l].value = tree[pos].value;
            tree[l].changed = true;
            tree[l].pushCheck = true;
        }
        if (!tree[r].changed) {
            tree[r].value = tree[pos].value;
            tree[r].changed = true;
            tree[r].pushCheck = true;
        }
        if (tree[pos].value != Math.min(tree[l].value, tree[r].value)) {
            if (tree[pos].pushCheck) tree[pos].value = Math.min(tree[l].value, tree[r].value);
            else stop = true;
        }
    }

    public static long get(int pos, int cur, int left, int right) {
        push(cur, left, right);
        if (right - left == 1) {
            return tree[cur].value;
        }
        int nw = left + (right - left) / 2;
        if (pos < nw)
            return get(pos, cur * 2 + 1, left, nw);
        else
            return get(pos, cur * 2 + 2, nw, right);
    }

    public static int min(int start, int finish, int cur, int left, int right) {
        if (start >= right || finish <= left) return Integer.MAX_VALUE;
        if (start <= left && finish >= right) return tree[cur].value;

        int nw = left + (right - left) / 2;
        return Math.min(min(start, finish, cur * 2 + 1, left, nw), min(start, finish, cur * 2 + 2, nw, right));
    }

    public static void set(int start, int finish, int value, int cur, int left, int right) {
        if (start >= right || finish <= left) { return; }
        if (start <= left && finish >= right) {
//            System.out.println(start + " " + finish + " " + left + " " + right);
          if (start == left && finish == right) {
                if (tree[cur].reallyChanged && tree[cur].value != value) {
                    stop = true;
                } else {
                    tree[cur].reallyChanged = true;
                }
            } else if (tree[cur].reallyChanged) {
                return;
            }
//            if (!tree[cur].changed) {
            tree[cur].changed = true;
            tree[cur].value = value;
            return;
//            }
//            return tree[cur].value;
        }

        int nw = left + (right - left) / 2;
        set(start, finish, value, cur * 2 + 1, left, nw);
        set(start, finish, value, cur * 2 + 2, nw, right);
        if (!tree[cur].changed)
            tree[cur].value = Math.min(tree[cur * 2 + 1].value, tree[cur * 2 + 1].value);
    }

    private static class UnderTree {
        private int value = Integer.MAX_VALUE;
        private boolean changed = false;
        private boolean reallyChanged = false;
        private boolean pushCheck = false;
    }
}