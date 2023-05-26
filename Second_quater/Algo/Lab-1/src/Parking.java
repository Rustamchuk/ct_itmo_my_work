import java.io.*;
import java.util.Arrays;
import java.util.Scanner;

public class Parking {
    private static Place[] tree;
    private static int size;
    private static int n;

    public static void main(String[] args) throws IOException {
//        Scanner sc = new Scanner(System.in);
//        Scanner sc = new Scanner(new File("parking.in"));
        BufferedReader reader = new BufferedReader(new FileReader("parking.in"));
        PrintWriter consoleOutput = new PrintWriter("parking.out");
        String in = reader.readLine();

        int v = 0;
        while (in.charAt(v) != ' ') v++;
        n = Integer.parseInt(in.substring(0, v));
        int m = Integer.parseInt(in.substring(v + 1));

        create(n);
        build(0, 0, size);

        for (int i = 0; i < m; i++) {
            String command = reader.readLine();
            if (command.contains("enter")) {
                int c = Integer.parseInt(command.substring(6)) - 1;

            } else {
                int c = Integer.parseInt(command.substring(5)) - 1;
            }
        }
        consoleOutput.flush();
        consoleOutput.close();
    }

    public static void create(int n) {
        size = 1;
        while(size < n) size *= 2;
        tree = new Place[size * 2 - 1];
        for (int i = 0; i < tree.length; i++) {
            tree[i] = new Place();
        }
    }

    public static void build(int cur, int left, int right) {
        if (right - left == 1) {
            if (left < n) {
                tree[cur].ind = left;
            }
        } else {
            int m = (left + right) / 2;
            build(2 * cur + 1, left, m);
            build(2 * cur + 2, m, right);
            tree[cur] = tree[2 * cur + 2];
        }
    }

    public static void enter(int pos, int cur, int left, int right) {
        if (tree[cur].ind == pos) {
            tree[cur].empty = false;
            return;
        }
        if (right - left == 1) {
            if (tree[cur].empty) {

            }
        }
        int nw = left + (right - left) / 2;
        if (pos < nw)
            enter(pos, cur * 2 + 1, left, nw);
        else
            enter(pos, cur * 2 + 2, nw, right);
        tree[cur].ind = Math.min(tree[cur * 2 + 1].ind, tree[cur * 2 + 2].ind);
    }

    public static void exit(int pos, int cur, int left, int right) {
        if (right - left == 1) {
            tree[cur].empty = true;
            return;
        }
        int nw = left + (right - left) / 2;
        if (pos < nw)
            exit(pos, cur * 2 + 1, left, nw);
        else
            exit(pos, cur * 2 + 2, nw, right);
        if (!tree[cur * 2 + 1].empty) {
            tree[cur].ind = tree[2 * cur + 2].ind;
            return;
        }
        if (!tree[cur * 2 + 2].empty) {
            tree[cur].ind = tree[2 * cur + 1].ind;
            return;
        }
        tree[cur].ind = Math.max(tree[cur * 2 + 1].ind, tree[cur * 2 + 2].ind);
    }

    private static class Place {
        private int ind;
        private boolean empty = true;
    }
}