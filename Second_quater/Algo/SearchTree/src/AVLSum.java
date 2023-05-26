import java.util.Scanner;

public class AVLSum {
    public static void main(String[] args) {
        SearchTree tree = new SearchTree();
        Scanner sc = new Scanner(System.in);
        int y = 0;
        while (sc.hasNext()) {
            String w = sc.next();
            switch (w) {
                case "delete" -> tree.delete(sc.nextInt());
                case "insert" -> tree.add(sc.nextInt());
                case "sum" -> tree.getSum(sc.nextInt(), sc.nextInt());
            }
            sc.nextLine();
//            tree.recursivePrint();
        }
    }

    private static class SearchTree {
        private Node head;

        private void add(final int num) {
            if (head == null) {
                head = new Node(num);
            } else {
                head = add(head, num);
            }
        }

        private Node add(final Node cur, final int num) {
            if (num > cur.value) {
                if (cur.right == null) {
                    cur.right = new Node(num);
                } else {
                    cur.right = add(cur.right, num);
                }
            } else if (num < cur.value) {
                if (cur.left == null) {
                    cur.left = new Node(num);
                } else {
                    cur.left = add(cur.left, num);
                }
            }
            return rebalance(cur);
        }

        private void delete(final int num) {
            head = delete(head, num);
        }

        private Node delete(Node cur, final int num) {
            if (cur == null) {
                return null;
            } else if (num > cur.value) {
                cur.right = delete(cur.right, num);
            } else if (num < cur.value) {
                cur.left = delete(cur.left, num);
            } else {
                if (cur.left == null || cur.right == null) {
                    cur = cur.left == null ? cur.right : cur.left;
                } else {
                    Node r = findRight(cur.left);
                    cur.value = r.value;
                    cur.left = delete(cur.left, r.value);
                }
            }
            if (cur != null) {
                cur = rebalance(cur);
            }
            return cur;
        }

        private Node findRight(final Node cur) {
            if (cur.right != null) {
                return findRight(cur.right);
            }
            return cur;
        }

        private Node LLRotate(Node a) {
            a.right = RRotate(a.right);
            a = LRotate(a);
            return a;
        }

        private Node LRotate(Node a) {
            Node b = a.right;
            a.right = b.left;
            b.left = a;
            updateH(a);
            updateH(b);
            return b;
        }

        private Node RRRotate(Node a) {
            a.left = LRotate(a.left);
            a = RRotate(a);
            return a;
        }

        private Node RRotate(Node a) {
            Node b = a.left;
            a.left = b.right;
            b.right = a;
            updateH(a);
            updateH(b);
            return b;
        }

        private Node rebalance(Node cur) {
            if (cur == null) {
                return cur;
            }
            updateH(cur);
            int balance = getH(cur.right) - getH(cur.left);
            if (balance > 1) {
                if (getH(cur.right.right) > getH(cur.right.left)) {
                    cur = LRotate(cur);
                } else {
                    cur = LLRotate(cur);
                }
            } else if (balance < -1) {
                if (getH(cur.left.left) > getH(cur.left.right)) {
                    cur = RRotate(cur);
                } else {
                    cur = RRRotate(cur);
                }
            }
            return cur;
        }

        private int getH(Node a) {
            return a == null ? 0 : a.H;
        }

        private void updateH(Node a) {
            a.H = 1 + Math.max(getH(a.left), getH(a.right));
            updateSum(a);
            updateMax(a);
            updateMin(a);
        }

        private void updateSum(Node a) {
            a.sum = a.value + getSum(a.left) + getSum(a.right);
        }

        private int getSum(Node a) {
            return a == null ? 0 : a.sum;
        }

        private void updateMin(Node a) {
            a.min = Math.min(a.value, Math.min(getMin(a.left), getMin(a.right)));
        }

        private int getMin(Node a) {
            return a == null ? 0 : a.min;
        }

        private void updateMax(Node a) {
            a.max = Math.max(a.value, Math.max(getMax(a.left), getMax(a.right)));
        }

        private int getMax(Node a) {
            return a == null ? 0 : a.max;
        }

        private void getSum(int l, int r) {
            System.out.println(getSum(head, l, r));
        }

        private int getSum(Node node, int l, int r) {
            if (node == null) {
                return 0;
            }

            if (l <= node.min && node.max <= r) {
                return node.sum;
            } else if (l <= node.min) {
                int sum = getSum(node.left) + getSum(node.right, l, r);
                if (l <= node.value && node.value <= r) {
                    sum += node.value;
                }
                return sum;
            } else if (node.max <= r) {
                int sum = getSum(node.left, l, r) + getSum(node.right);
                if (l <= node.value && node.value <= r) {
                    sum += node.value;
                }
                return sum;
            } else if (l <= node.value && node.value <= r) {
                return node.value + getSum(node.left, l, r) + getSum(node.right, l, r);
            } else if (l > node.value) {
                return getSum(node.right, l, r);
            } else {
                return getSum(node.left, l, r);
            }
        }

        private void recursivePrint() {
            recursivePrint(head);
        }

        private void recursivePrint(Node node) {
            if (node != null) {
                System.out.println(node.value + " " + node.H + " " + node.sum + " " + node.min + " " + node.max);
                recursivePrint(node.left);
                recursivePrint(node.right);
            }
        }

        private static class Node {
            private int value;
            private Node left;
            private Node right;
            private int H = 1;
            private int sum;
            private int min;
            private int max;

            private Node(final int val) {
                value = min = max = sum = val;
            }

            public boolean exists(final Node head, final int num) {
                if (head != null) {
                    if (head.value > num) {
                        return exists(head.left, num);
                    } else if (head.value < num) {
                        return exists(head.right, num);
                    } else {
                        return true;
                    }
                }
                return false;
            }
        }
    }
}
