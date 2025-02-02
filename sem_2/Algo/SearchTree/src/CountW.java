import java.util.TreeMap;

public class CountW {
    public static void main(String[] args) {
        SearchTree tree = new SearchTree();
        tree.add(8);
        tree.add(4);
        tree.add(2);
        tree.add(3);
        tree.add(10);
        tree.add(6);
        tree.add(7);
        tree.countW();
        tree.sortRecursivePrint();
        System.out.println(tree.countLess(7));
    }

    private static class SearchTree {
        private Node head;

        private void add(final int num) {
            if (head == null) {
                head = new Node(num);
            } else {
                head.add(num);
            }
        }

        private void sortRecursivePrint() {
            sortRecursivePrint(head);
        }

        private void sortRecursivePrint(Node node) {
            if (node != null) {
                sortRecursivePrint(node.left);
                System.out.println(node.value + " " + node.w);
                sortRecursivePrint(node.right);
            }
        }

        private void countW() {
            head.w += countW(head.left) + countW(head.right);
        }

        private int countW(Node node) {
            if (node != null) {
                node.w += countW(node.left) + countW(node.right);
                return node.w;
            }
            return 0;
        }

        private int countLess(int x) {
            return head.countLess(x);
        }

        private static class Node {
            private int value;
            private Node left;
            private Node right;
            private int w = 1;

            private Node(final int val) {
                value = val;
            }

            private void add(final int num) {
                if (num >= value) {
                    if (right == null) {
                        right = new Node(num);
                    } else {
                        right.add(num);
                    }
                } else {
                    if (left == null) {
                        left = new Node(num);
                    } else {
                        left.add(num);
                    }
                }
            }

            private int countLess(int x) {
                if (value == x) {
                    return left == null ? 0 : left.w;
                }
                if (value > x) {
                    return left == null ? 0 : left.countLess(x);
                } else {
                    return (left == null ? 0 : left.w) + 1 + (right == null ? 0 : right.countLess(x));
                }
            }
        }
    }
}
