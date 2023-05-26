import java.util.TreeMap;

public class Checked {
    public static void main(String[] args) {
        SearchTree tree = new SearchTree();
        tree.add(8);
        tree.add(4);
        tree.add(2);
        tree.add(3);
        tree.add(10);
        tree.add(6);
        tree.add(7);
        System.out.println(tree.checkCorrect());
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

        private boolean checkCorrect() {
            return checkCorrect(head, Integer.MIN_VALUE, Integer.MAX_VALUE);
        }

        public boolean checkCorrect(Node node, int minVal, int maxVal) {
            if (node == null) {
                return true;
            }
            if (node.value < minVal || node.value > maxVal) {
                return false;
            }
            return checkCorrect(node.left, minVal, node.value) &&
                    checkCorrect(node.right, node.value, maxVal);
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
        }
    }
}
