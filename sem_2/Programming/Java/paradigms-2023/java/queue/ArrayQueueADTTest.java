package queue;

public class ArrayQueueADTTest {
    public static void main(String[] args) {
        ArrayQueueADT queue1 = new ArrayQueueADT();
        ArrayQueueADT queue2 = new ArrayQueueADT();
        test(queue1);
        test(queue2);
    }

    public static void test(ArrayQueueADT queue) {
        for (int i = 0; i < 500; i++) {
            Object[] arrChecker = new Object[100000];
            int start = 0;
            int pos = 0;
            ArrayQueueADT.clear(queue);
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
                        ArrayQueueADT.enqueue(queue, value);
                        arrChecker[pos++] = value;
                        ansQueue = (int) ArrayQueueADT.peek(queue);
                        ansCorrect = value;
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case DEQUE -> {
                        if (start >= pos) break;
                        ansQueue = (int) ArrayQueueADT.dequeue(queue);
                        ansCorrect = (int) arrChecker[start++];
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case ELEMENT -> {
                        if (start >= pos) break;
                        ansQueue = (int) ArrayQueueADT.element(queue);
                        ansCorrect = (int) arrChecker[start];
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case SIZE -> {
                        ansQueue = ArrayQueueADT.size(queue);
                        ansCorrect = pos - start;
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case ISEMPTY -> {
                        ansQueue = ArrayQueueADT.isEmpty(queue);
                        ansCorrect = pos - start == 0;
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case CLEAR -> {
                        arrChecker = new Object[100000];
                        ArrayQueueADT.clear(queue);
                        start = 0;
                        pos = 0;
                        ansQueue = ArrayQueueADT.isEmpty(queue);
                        ansCorrect = true;
                        checkCorrect(ansQueue, ansCorrect, command);
                    }
                    case TOSTR -> {
                        ansQueue = ArrayQueueADT.toStr(queue);
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
                        ansQueue = ArrayQueueADT.peek(queue);
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
