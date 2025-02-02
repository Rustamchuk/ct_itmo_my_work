import java.util.Random;
import java.util.Scanner;

public class StartDecard {
    public static void main(String[] args) {
        Treap treap = new Treap();
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
        int n = sc.nextInt();
        for (int j = 1; j <= i; j++) {
            treap.pushBack(j);
        }
        for (int j = 0; j < n; j++) {
            treap.toStart(sc.nextLong() - 1, sc.nextLong() - 1);
        }
//        System.out.println(out);
        treap.print(treap.head);
    }

    private static class Treap {
        private Node head;

        private void print(Node node) {
            if (node == null) {
                return;
            }
            print(node.left);
            System.out.print(node.value + " ");
            print(node.right);
        }

        private void updateSize(Node a) {
            if (a != null) {
                a.size = 1 + size(a.left) + size(a.right);
            }
        }

        private long size(Node a) {
            return a == null ? 0 : a.size;
        }

        private Node merge(Node a, Node b) {
            if (a == null) return b;
            if (b == null) return a;
            if (a.priority > b.priority) {
                a.right = merge(a.right, b);
                updateSize(a);
                return a;
            }
            b.left = merge(a, b.left);
            updateSize(b);
            return b;
        }

        private Pair split(Node n, long k) {
            if (n != null) {
                Pair c;
                if (size(n.left) < k) {
                    c = split(n.right, k - size(n.left) - 1);
                    n.right = c.l;
                    c.l = n;
                } else {
                    c = split(n.left, k);
                    n.left = c.r;
                    c.r = n;
                }
                updateSize(c.l);
                updateSize(c.r);
                return c;
            }
            return new Pair();
        }

        private void pushBack(long value) {
            head = merge(head, new Node(value));
        }

        private void toStart(long l, long r) {
            Pair a = split(head, l);
            Pair b = split(a.r, r - l + 1);
            head = merge(merge(b.l, a.l), b.r);
        }

        private long get(long index) {
            Pair a = split(head, index);
            Pair b = split(a.r, 1);
            long res = b.l.value;
            head = merge(merge(a.l, b.l), b.r);
            return res;
        }

        private static class Node {
            private final long value;
            private final long priority;
            private long size;
            private Node left;
            private Node right;

            public Node(long key) {
                value = key;
                priority = new Random().nextLong();
                size = 1;
            }
        }

        private static class Pair {
            private Node l;
            private Node r;
        }
    }
}