package queue;

import java.util.Arrays;

public class ArrayQueue extends AbstractQueue {
    private Object[] queue = new Object[10];
    private int start;
    private int tail;

    // Pre: true
    // Post: queue[n' % length] = element && tail' = (tail + 1) % queue.length
    protected void addElement(final Object element) {
        boostQueue(size);
        if (tail == queue.length) tail = 0;
        queue[tail++] = element;
    }

    // Pre: true
    // Post: n' = n
    private void boostQueue(final int size) {
        if (queue.length <= size) {
            Object[] newQueue = new Object[size * 2];
            System.arraycopy(queue, start, newQueue, 0, queue.length - start);
            System.arraycopy(queue, 0, newQueue, queue.length - start, start);
            start = 0;
            tail = size;
            queue = Arrays.copyOf(newQueue, size * 2);
        }
    }

    // Pre: true
    // Post: R = a[start]
    protected Object getHead() {
        return queue[start];
    }

    // Pre: true
    // Post: start' = (start + 1) % queue.length
    protected void updateHead() {
        queue[start] = null;
        start++;
        if (start == queue.length) start = 0;
    }

    // Pre: true
    // Post: start' = tail' = 0
    protected void clearValues() {
        queue = new Object[10];
        start = tail = 0;
    }

    // Pre: true
    // Post: R = queue.String && n' = n
    public String toStr() {
        StringBuilder out = new StringBuilder();
        out.append("[");
        for (int i = 0; i < size; i++) {
            out.append(queue[calculatePos(i)]);
            if (i != size - 1)
                out.append(", ");
        }
        out.append("]");
        return out.toString();
    }

    public int calculatePos(int i) {
        int pos = i + start;
        if (pos >= queue.length)
            pos -= queue.length;
        return pos;
    }

    // Pre: true
    // Post: R = a[tail - 1]
    protected Object getTail() {
        if (tail == 0) return queue[queue.length - 1];
        return queue[tail - 1];
    }
}
