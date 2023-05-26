import java.util.Scanner;

public class DecardTree {
    public static void main(String[] args) {
        Treap treap = new Treap();
        Scanner sc = new Scanner(System.in);
        int i = sc.nextInt();
        for (int j = 1; j <= i; j++) {
            treap.insert(sc.nextInt(), sc.nextInt(), j);
        }
        System.out.println("YES");
        treap.print();
    }

    private static class Treap {
        private Node head;

        private void print() {
            print(head, 0);
        }

        private void print(Node node, int n) {
            if (node != null) {
                System.out.println(n +
                        " " + (node.left != null ? node.left.i : 0) +
                        " " + (node.right != null ? node.right.i : 0));
                print(node.left, node.i);
                print(node.right, node.i);
            }
        }

        private Node merge(Node a, Node b) {
            if (a == null || b == null) {
                return a == null ? b : a;
            }
            if (a.priority > b.priority) {
                a.right = merge(a.right, b);
                return a;
            }
            b.left = merge(a, b.left);
            return b;
        }

        private Node[] split(Node n, int key) {
            if (n != null) {
                if (n.key < key) {
                    Node[] a = split(n.right, key);
                    n.right = a[0];
                    return new Node[]{n, a[1]};
                } else {
                    Node[] b = split(n.left, key);
                    n.left = b[1];
                    return new Node[]{b[0], n};
                }
            }
            return new Node[]{null, null};
        }

        private void insert(int key, int priority, int i) {
            Node less, greater;
            Node[] a = split(head, key);
            less = a[0];
            greater = a[1];
            head = merge(merge(less, new Node(key, priority, i)), greater);
        }

        private static class Node {
            private int key;
            private int priority;
            private int i;
            private Node left;
            private Node right;

            public Node(int key, int priority, int i) {
                this.key = key;
                this.priority = priority;
                this.i = i;
            }
        }
    }
}
