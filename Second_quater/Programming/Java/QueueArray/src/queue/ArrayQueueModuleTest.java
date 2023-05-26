package queue;

public class ArrayQueueModuleTest {
    public static void main(String[] args) {
        for (int i = 0; i < 10000; i++) {
            Object[] arrChecker = new Object[100000];
            ArrayQueueModule.clear();
            int start = 0;
            int pos = 0;

            Object ansQueue;
            Object ansCorrect;

            int value;

            for (int j = 0; j < 10000; j++) {
                System.out.println(i + " " + j);
                Commands command = Commands.values()[(int) (Math.random() * 8)];
                switch (command) {
                    case ENQUE -> {
                        if (pos >= arrChecker.length) break;
                        value = (int) (Math.random() * 200);
                        ArrayQueueModule.enqueue(value);
                        arrChecker[pos++] = value;
                        ansQueue = ArrayQueueModule.peek();
                        ansCorrect = value;
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case DEQUE -> {
                        if (start >= pos) break;
                        ansQueue = ArrayQueueModule.dequeue();
                        ansCorrect = arrChecker[start++];
                        arrChecker[start - 1] = 0;
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case ELEMENT -> {
                        if (start >= pos) break;
                        ansQueue = ArrayQueueModule.element();
                        ansCorrect = arrChecker[start];
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case SIZE -> {
                        ansQueue = ArrayQueueModule.size();
                        ansCorrect = pos - start;
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case ISEMPTY -> {
                        ansQueue = ArrayQueueModule.isEmpty();
                        ansCorrect = pos - start == 0;
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case CLEAR -> {
                        arrChecker = new Object[100000];
                        ArrayQueueModule.clear();
                        start = 0;
                        pos = 0;
                        ansQueue = ArrayQueueModule.isEmpty();
                        ansCorrect = true;
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case TOSTR -> {
                        ansQueue = ArrayQueueModule.toStr();
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
                        ansQueue = ArrayQueueModule.peek();
                        ansCorrect = arrChecker[pos - 1];
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
