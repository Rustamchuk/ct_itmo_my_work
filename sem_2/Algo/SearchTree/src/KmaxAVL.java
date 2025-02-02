import java.util.Scanner;

public class KmaxAVL {
    public static void main(String[] args) {
        SearchTree tree = new SearchTree();
        Scanner sc = new Scanner(System.in);
        int y = 0;
        while (sc.hasNext()) {
            int w = sc.nextInt();
            switch (w) {
                case 1 -> {
                    tree.add(sc.nextInt());
                }
                case 0 -> {
                    System.out.println(tree.kMax(sc.nextInt()));
                }
                case -1 -> {
                    tree.delete(sc.nextInt());
                }
            }
//            String w = sc.next();
//            switch (w) {
//                case "exists" -> System.out.println(tree.exists(sc.nextInt()));
//                case "delete" -> tree.delete(sc.nextInt());
//                case "insert" -> tree.add(sc.nextInt());
//                case "next" -> tree.getMin(sc.nextInt());
//                case "prev" -> tree.getMax(sc.nextInt());
//            }
//            sc.nextLine();
//            tree.sortRecursivePrint();
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
            updateSize(a);
        }

        private void updateSize(Node a) {
            a.size = 1 + (a.left != null ? a.left.size : 0) + (a.right != null ? a.right.size : 0);
        }

        private void getMin(int value) {
            if (head != null) {
                int i = head.getMin(value);
                if (i != Integer.MAX_VALUE) {
                    System.out.println(i);
                    return;
                }
            }
            System.out.println("none");
        }

        private void getMax(int value) {
            if (head != null) {
                int i = head.getMax(value);
                if (i != Integer.MIN_VALUE) {
                    System.out.println(i);
                    return;
                }
            }
            System.out.println("none");
        }

        private boolean exists(final int num) {
            return head != null && head.exists(head, num);
        }

        private void sortRecursivePrint() {
            sortRecursivePrint(head);
        }

        private void sortRecursivePrint(Node node) {
            if (node != null) {
                System.out.println(node.value + " " + node.H + " " + node.size);
                sortRecursivePrint(node.left);
                sortRecursivePrint(node.right);
            }
        }

        public int kMax(final int n) {
            return kMax(head, n);
        }

        private int kMax(Node cur, final int n) {
            if (cur != null) {
                if (cur.right != null && cur.right.size >= n) {
                    return kMax(cur.right, n);
                } else if (cur.left != null) {
                    if (cur.size - cur.left.size == n) {
                        return cur.value;
                    }
                    return kMax(cur.left, n - cur.size + cur.left.size);
                } else {
                    if (cur.size == n) {
                        return cur.value;
                    }
                }
            }
            return Integer.MAX_VALUE;
        }

        private static class Node {
            private int value;
            private Node left;
            private Node right;
            private int H = 1;
            private int size = 1;

            private Node(final int val) {
                value = val;
            }

            private int getMin(int x) {
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

            public boolean exists(final Node head, final int num) {
                if (head != null) {
                    if (head.value > num) {
                        return exists(head.left, num);
                    } else if (head.value < num) {
                        return exists(head.right, num);
                    } else {
                        return true;
                    }
                } return false;
            }

            public int getMax(int value) {
                if (value > this.value) {
                    if (right == null) {
                        return this.value;
                    }
                    int i = right.getMax(value);
                    return Math.max(i, this.value);
                }
                if (left == null) {
                    return Integer.MIN_VALUE;
                }
                int i = left.getMax(value);
                if (i > value) {
                    return Integer.MIN_VALUE;
                }
                return i;
            }
        }
    }
}
