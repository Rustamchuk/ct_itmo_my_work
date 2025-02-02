import java.util.Random;
import java.util.Scanner;

public class SumDecard {
    public static void main(String[] args) {
        Treap treap = new Treap();
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
        long y = 0;
        long p = (long) Math.pow(10, 9);
        StringBuilder out = new StringBuilder();
        for (int j = 0; j < i; j++) {
            String w = sc.next();
            if (w.equals("+")) {
                treap.insert((sc.nextLong() + y) % p);
                y = 0;
            } else {
                y = treap.sum(sc.nextLong(), sc.nextLong());
                out.append(y).append(System.lineSeparator());
            }
            sc.nextLine();
        }
//        treap.print();
        System.out.println(out);
    }

    private static class Treap {
        private Node head;
        private Pair a;
        private Pair b;

        private void print() {
            print(head);
        }

        private void print(Node node) {
            if (node != null) {
                System.out.println(node.key + " " + node.priority);
                print(node.left);
                print(node.right);
            }
        }

        private Node merge(Node a, Node b) {
            if (a == null) return b;
            if (b == null) return a;
            if (a.priority > b.priority) {
                a.right = merge(a.right, b);
                updateSum(a);
                return a;
            }
            b.left = merge(a, b.left);
            updateSum(b);
            return b;
        }

        private void updateSum(Node a) {
            if (a != null) {
                a.sum = a.key + (a.left == null ? 0 : a.left.sum) + (a.right == null ? 0 : a.right.sum);
            }
        }

        private Pair split(Node n, long key) {
            if (n != null) {
                Pair c;
                if (n.key < key) {
                    c = split(n.right, key);
                    n.right = c.l;
                    c.l = n;
                } else {
                    c = split(n.left, key);
                    n.left = c.r;
                    c.r = n;
                }
                updateSum(n);
                return c;
            }
            return new Pair();
        }

        private void insert(long key) {
            if (contains(key)) {
                return;
            }
            a = split(head, key);
            head = merge(a.l, merge(new Node(key), a.r));
        }

        private boolean contains(long key) {
            a = split(head, key);
            b = split(a.r, key + 1);
            boolean res = b.l != null;
            head = merge(merge(a.l, b.l), b.r);
            return res;
        }

        private long sum(long l, long r) {
            a = split(head, l);
            b = split(a.r, r + 1);
            long sum = 0;
            if (b.l != null)
                sum = b.l.sum;
            head = merge(merge(a.l, b.l), b.r);
            return sum;
        }

        private long sum(Node a) {
            if (a != null) {
                return a.key + sum(a.left) + sum(a.right);
            }
            return 0;
        }

        private static class Node {
            private final long key;
            private final long priority;
            private Node left;
            private Node right;
            private long sum;

            public Node(long key) {
                this.key = key;
                sum = key;
                priority = new Random().nextLong();
            }
        }

        private static class Pair {
            private Node l;
            private Node r;
        }
    }
}