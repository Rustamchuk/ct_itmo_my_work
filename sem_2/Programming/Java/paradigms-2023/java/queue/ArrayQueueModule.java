package queue;

import java.util.Arrays;
import java.util.Objects;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i=start..size: a[i % queue.length] != null && 0 <= start && tail && size < queue.length()
// Inv: forall i=(tail < start ? tail : 1)..start: a[i] = null
// Inv: forall i=tail..(tail < start ? start : queue.length): a[i] = null
// Let: immutable(k): forall i=1..k: a'[i] = a[i] !
// Let: n = size
public class ArrayQueueModule {
    private static Object[] queue = new Object[10];
    private static int start;
    private static int size;
    private static int tail;

    // Pre: element != null
    // Post: n' = n + 1 && queue[n' % length] = element && tail' = (tail + 1) % queue.length && immutable(size)
    public static void enqueue(final Object element) {
        Objects.requireNonNull(element);
        boostQueue(size);
        if (tail == queue.length) tail = 0;
        size++;
        queue[tail++] = element;
    }

    // Pre: true
    // Post: n' = n && immutable(size)
    private static void boostQueue(final int size) {
        if (queue.length <= size) {
            Object[] newQueue = new Object[size * 2];
            System.arraycopy(queue, start, newQueue, 0, queue.length - start);
            System.arraycopy(queue, 0, newQueue, queue.length - start, start);
            start = 0;
            tail = size;
            queue = Arrays.copyOf(newQueue, size * 2);
        }
    }

    // Pre: n > 0
    // Post: R = a[start] && n' = n && immutable(size)
    public static Object element() {
        assert !isEmpty();
        return queue[start];
    }

    // Pre: n > 0
    // Post: start' = (start + 1) % queue.length && immutable(size) && n' = n - 1
    public static Object dequeue() {
        assert !isEmpty();
        Object element = queue[start];
        queue[start] = null;
        start++;
        size--;
        if (start == queue.length) start = 0;
        return element;
    }

    // Pre: true
    // Post: R = n && n' = n && immutable(size)
    public static int size() {
        return size;
    }

    // Pre: true
    // Post: R = (n == 0) && n' = n && immutable(size)
    public static boolean isEmpty() {
        return size() == 0;
    }

    // Pre: true
    // Post: n' = 0 && start' = size' = tail' = 0
    public static void clear() {
        queue = new Object[10];
        start = size = tail = 0;
    }

    // Pre: true
    // Post: R = queue.String && n' = n && immutable(size)
    public static String toStr() {
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

    public static int calculatePos(int i) {
        int pos = i + start;
        if (pos >= queue.length)
            pos -= queue.length;
        return pos;
    }

    // Pre: n > 0
    // Post: R = a[tail - 1] && immutable(size) && n' = n
    public static Object peek() {
        assert !isEmpty();
        if (tail == 0) return queue[queue.length - 1];
        return queue[tail - 1];
    }  // – вернуть последний элемент в очереди;
}
