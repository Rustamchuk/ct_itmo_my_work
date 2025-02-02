package queue;

// Model: a[1]..a[n]
// Inv: n >= 0 && forall i=1..size: a[i % queue.length] != null && 0 <= start && tail && size < queue.length()
// Let: immutable(k): forall i=1..k: a'[i] = a[i]
// Let: n = size
public interface Queue {
    // Pred: element != null
    // Post: n' = n + 1 && arr[n'] = element && immutable(n)
    void enqueue(final Object element);

    // Pred: n > 0
    // Post: R = arr[start] && n' = n && immutable(n)
    Object element();

    // Pred: n > 0
    // Post: R = arr[start] && n' = n && immutable(n)
    Object dequeue();

    // Pred: true
    // Post: n' = 0
    void clear();

    // Pred: true
    // Post: R = n && n' = n && immutable(n)
    int size();

    // Pred: true
    // Post: R = n == 0 && immutable(n)
    boolean isEmpty();

    // Pred: n > 0
    // Post: R = arr[n] && n' = n && immutable(n)
    Object peek();

    // Pred: element != null
    // Post: R = cnt (for i=1..n if queue[i] == element cnt++)
    int count(final Object element);
}
