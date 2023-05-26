public class MinVal {
    public static void main(String[] args) {
        SearchTree tree = new SearchTree();
        tree.add(9);
        tree.add(7);
        tree.add(8);
        System.out.println(tree.getMin(8));
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

        private int getMin(int value) {
            return head.getMin(value);
        }

        private static class Node {
            private int value;
            private Node left;
            private Node right;

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

            private int getMin(int x) {
                if (x == this.value)
                    return x;
                if (x < this.value) {
                    if (left == null) {
                        return this.value;
                    }
                    int i = left.getMin(x);
                    return Math.min(i, this.value);
                }
                if (right == null) {
                    return Integer.MAX_VALUE;
                }
                int i = right.getMin(x);
                if (i < x) {
                    return Integer.MAX_VALUE;
                }
                return i;
            }
        }
    }
}
