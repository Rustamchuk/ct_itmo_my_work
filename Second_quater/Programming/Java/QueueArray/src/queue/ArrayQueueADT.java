package queue;

import java.util.Arrays;
import java.util.Objects;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i=start..size: a[i % queue.length] != null && 0 <= start && tail && size < queue.length()
// Inv: forall i=(tail < start ? tail : 1)..start: a[i] = null
// Inv: forall i=tail..(tail < start ? start : queue.length): a[i] = null
// Let: immutable(k): forall i=1..k: a'[i] = a[i]
// Let: n = size
public class ArrayQueueADT {
    private static Object[] queue = new Object[10];
    private static int start;
    private static int size;
    private static int tail;

    // Pre: element != null && queue != null
    // Post: n' = n + 1 && queue[n' % length] = element && tail' = (tail + 1) % queue.length && immutable(size)
    public static void enqueue(final ArrayQueueADT queue, final Object element) {
        Objects.requireNonNull(queue);
        Objects.requireNonNull(element);
        boostQueue(queue, queue.size);
        if (queue.tail == queue.queue.length) queue.tail = 0;
        queue.size++;
        queue.queue[queue.tail++] = element;
    }

    // Pre:  queue != null
    // Post: n' = n && immutable(size)
    private static void boostQueue(final ArrayQueueADT queue, final int size) {
        Objects.requireNonNull(queue);
        if (queue.queue.length <= size) {
            Object[] newQueue = new Object[size * 2];
            System.arraycopy(queue.queue, queue.start, newQueue, 0, queue.queue.length - queue.start);
            System.arraycopy(queue.queue, 0, newQueue, queue.queue.length - queue.start, queue.start);
            queue.start = 0;
            queue.tail = size;
            queue.queue = Arrays.copyOf(newQueue, size * 2);
        }
    }

    // Pre: n > 0 && queue != null
    // Post: R = a[start] && n' = n && immutable(size)
    public static Object element(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert !isEmpty(queue);
        return queue.queue[queue.start];
    }

    // Pre: n > 0 && queue != null
    // Post: start' = (start + 1) % queue.length && immutable(size) && n' = n - 1
    public static Object dequeue(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert !isEmpty(queue);
        Object element = queue.queue[queue.start];
        queue.queue[queue.start] = null;
        queue.start++;
        queue.size--;
        if (queue.start == queue.queue.length) queue.start = 0;
        return element;
    }

    // Pre: queue != null
    // Post: R = n && n' = n && immutable(size)
    public static int size(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        return queue.size;
    }

    // Pre: queue != null
    // Post: R = (n == 0) && n' = n && immutable(size)
    public static boolean isEmpty(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        return size(queue) == 0;
    }

    // Pre: queue != null
    // Post: n' = 0 && start' = size' = tail' = 0
    public static void clear(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        queue.start = queue.size = queue.tail = 0;
    }

    // Pre: queue != null
    // Post: R = queue.String && n' = n && immutable(size)
    public static String toStr(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        StringBuilder out = new StringBuilder();
        out.append("[");
        for (int i = 0; i < size; i++) {
            out.append(queue.queue[calculatePos(queue, i)]);
            if (i != queue.size - 1)
                out.append(", ");
        }
        out.append("]");
        return out.toString();
    }

    public static int calculatePos(final ArrayQueueADT queue, int i) {
        int pos = i + queue.start;
        if (pos >= queue.queue.length)
            pos -= queue.queue.length;
        return pos;
    }

    // Pre: n > 0 && queue != null
    // Post: R = a[tail - 1] && immutable(size) && n' = n
    public static Object peek(final ArrayQueueADT queue) {
        Objects.requireNonNull(queue);
        assert !isEmpty(queue);
        if (queue.tail == 0) return queue.queue[queue.queue.length - 1];
        return queue.queue[queue.tail - 1];
    }  // – вернуть последний элемент в очереди;
}
