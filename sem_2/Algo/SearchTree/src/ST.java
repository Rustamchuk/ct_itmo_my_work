import java.util.Scanner;

public class ST {
    public static void main(String[] args) {
        SearchTree tree = new SearchTree();
        Scanner sc = new Scanner(System.in);
        int y = 0;
        while (sc.hasNext()) {
            String w = sc.next();
            switch (w) {
                case "exists" -> System.out.println(tree.exists(sc.nextInt()));
                case "delete" -> tree.delete(sc.nextInt());
                case "insert" -> tree.add(sc.nextInt());
                case "next" -> tree.getMin(sc.nextInt());
                case "prev" -> tree.getMax(sc.nextInt());
            }
            sc.nextLine();
        }
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

        private boolean exists(final int num) {
            return head != null && head.exists(head, num);
        }

        private void delete(final int num) {
            int i = delete(head, num);
            if (i == 0) {
                head = null;
            }
        }

        private int delete(final Node cur, final int num) {
            if (cur != null) {
                if (cur.value > num) {
                    int i = delete(cur.left, num);
                    if (i == 0) {
                        cur.left = null;
                    }
                } else if (cur.value < num) {
                    int i = delete(cur.right, num);
                    if (i == 0) {
                        cur.right = null;
                    }
                } else {
                    if (cur.left != null) {
                        Node r = cur.left;
                        if (r.right != null) {
                            r = takeRight(r);
                        } else {
                            cur.left = r.left;
                        }
                        cur.value = r.value;
                    } else if (cur.right != null) {
                        cur.value = cur.right.value;
                        cur.left = cur.right.left;
                        cur.right = cur.right.right;
                    } else {
                        return 0;
                    }
                }
            }
            return 1;
        }

        private Node takeRight(final Node cur) {
            if (cur.right.right != null) {
                return takeRight(cur.right);
            } else {
                Node a = cur.right;
                cur.right = cur.right.left;
                return a;
            }
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

        private static class Node {
            private int value;
            private Node left;
            private Node right;

            private Node(final int val) {
                value = val;
            }

            private void add(final int num) {
                if (num > value) {
                    if (right == null) {
                        right = new Node(num);
                    } else {
                        right.add(num);
                    }
                } else if (num < value) {
                    if (left == null) {
                        left = new Node(num);
                    } else {
                        left.add(num);
                    }
                }
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
