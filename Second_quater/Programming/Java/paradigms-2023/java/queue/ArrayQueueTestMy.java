package queue;

public class ArrayQueueTestMy {
    public static void main(String[] args) {
        ArrayQueue queue1 = new ArrayQueue();
        ArrayQueue queue2 = new ArrayQueue();
        test(queue1);
        test(queue2);
    }

    public static void test(ArrayQueue queue) {
        for (int i = 0; i < 500; i++) {
            Object[] arrChecker = new Object[100000];
            queue.clear();
            int start = 0;
            int pos = 0;

            Object ansQueue;
            Object ansCorrect;

            int value;

            for (int j = 0; j < 500; j++) {
                System.out.println(i + " " + j);
                Commands command = Commands.values()[(int) (Math.random() * 8)];
                switch (command) {
                    case ENQUE -> {
                        if (pos >= arrChecker.length) break;
                        value = (int) (Math.random() * 200);
                        queue.enqueue(value);
                        arrChecker[pos++] = value;
                        ansQueue = (int) queue.peek();
                        ansCorrect = value;
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case DEQUE -> {
                        if (start >= pos) break;
                        ansQueue = queue.dequeue();
                        ansCorrect = arrChecker[start++];
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case ELEMENT -> {
                        if (start >= pos) break;
                        ansQueue = (int) queue.element();
                        ansCorrect = (int) arrChecker[start];
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case SIZE -> {
                        ansQueue = queue.size();
                        ansCorrect = pos - start;
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case ISEMPTY -> {
                        ansQueue = queue.isEmpty();
                        ansCorrect = pos - start == 0;
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case CLEAR -> {
                        arrChecker = new Object[100000];
                        queue.clear();
                        start = 0;
                        pos = 0;
                        ansQueue = queue.isEmpty();
                        ansCorrect = true;
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case TOSTR -> {
                        ansQueue = queue.toStr();
                        StringBuilder out = new StringBuilder();
                        out.append("[");
                        for (int k = start; k < pos; k++) {
                            out.append(arrChecker[k]);
                            if (k != pos - 1)
                                out.append(", ");
                        }
                        out.append("]");
                        ansCorrect = out.toString();
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case PEEK -> {
                        if (start >= pos) break;
                        ansQueue = (int) queue.peek();
                        ansCorrect = (int) arrChecker[pos - 1];
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                }
            }
        }
    }

    public static void checkCorrect(Object ansQueue, Object ansCorrect, Object method) {
        if (!ansCorrect.equals(ansQueue)) System.out.println("WRONG " + method + " " + ansQueue + " CORRECT " + ansCorrect);
    }
}
