package queue;

import java.util.Objects;

public abstract class AbstractQueue implements Queue {
    protected int size;

    protected abstract Object getHead();

    protected abstract Object getTail();

    protected abstract void updateHead();

    protected abstract void clearValues();

    protected abstract void addElement(final Object element);

    @Override
    public void enqueue(final Object element) {
        Objects.requireNonNull(element);
        addElement(element);
        size++;
    }

    @Override
    public int count(final Object element) {
        Objects.requireNonNull(element);
        Object cur;
        int cnt = 0;
        for (int i = 0; i < size; i++) {
            cur = dequeue();
            if (cur.equals(element)) cnt++;
            enqueue(cur);
        }
        return cnt;
    }

    @Override
    public Object element() {
        assert !isEmpty();
        return getHead();
    }

    @Override
    public Object dequeue() {
        assert !isEmpty();
        size--;
        Object element = getHead();
        updateHead();
        return element;
    }

    @Override
    public void clear() {
        size = 0;
        clearValues();
    }

    @Override
    public int size() {
        return size;
    }

    @Override
    public boolean isEmpty() {
        return size() == 0;
    }

    @Override
    public Object peek() {
        assert !isEmpty();
        return getTail();
    }
}
