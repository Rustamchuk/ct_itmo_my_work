public class Sorted {
    public static void main(String[] args) {
        SearchTree tree = new SearchTree();
        tree.add(8);
        tree.add(4);
        tree.add(2);
        tree.add(3);
        tree.add(10);
        tree.add(6);
        tree.add(7);
        tree.sortRecursivePrint();
        System.out.println();
        tree.sortNotRecursivePrint();
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
                System.out.println(node.value);
                sortRecursivePrint(node.right);
            }
        }

        private void sortNotRecursivePrint() {
            Stack nodes = new Stack();
            Node cur = head;
            while (cur != null) {
                while (cur != null) {
                    nodes.push(cur);
                    cur = cur.left;
                }
                cur = (Node) nodes.pop();
                System.out.println(cur.value);
                cur = cur.right;
                while (cur == null) {
                    Object r = nodes.pop();
                    if (r == null) {
                        break;
                    }
                    cur = (Node) r;
                    System.out.println(cur.value);
                    cur = cur.right;
                }
            }
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

    private static class Stack {
        private Node head;

        private void push(Object val) {
            head = new Node(val, head);
        }

        private Object pop() {
            if (head == null) {
                return null;
            }
            Object res = head.value;
            head = head.prev;
            return res;
        }

        private class Node {
            private Object value;
            private Node prev;
            private Node(Object num, Node pr) {
                value = num;
                prev = pr;
            }
        }
    }
}
