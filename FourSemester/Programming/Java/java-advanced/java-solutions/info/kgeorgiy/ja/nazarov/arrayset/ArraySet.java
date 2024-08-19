package info.kgeorgiy.ja.nazarov.arrayset;

import com.sun.source.tree.Tree;

import java.lang.reflect.Array;
import java.util.*;

public class ArraySet<T> extends AbstractSet<T> implements SortedSet<T> {
    private final List<T> array;
    private final Comparator<? super T> comparator;

    public ArraySet() {
        // :TO-IMPROVE: List.of()
        this(Collections.emptyList());
    }

    public ArraySet(final Collection<? extends T> array) {
        this(array, null);
    }

    public ArraySet(final Collection<? extends T> array, final Comparator<? super T> comparator) {
        TreeSet<T> set = new TreeSet<>(comparator);
        set.addAll(array);
        // :NOTE: maybe not array
        this.array = Collections.unmodifiableList(new ArrayList<>(set));
        this.comparator = comparator;
    }

    @Override
    public Comparator<? super T> comparator() {
        return comparator;
    }

    @Override
    public SortedSet<T> subSet(T fromElement, T toElement) {
        return takeSubSet(fromElement, toElement);
    }

    @Override
    public SortedSet<T> headSet(T toElement) {
        return takeSubSet(null, toElement);
    }

    @Override
    public SortedSet<T> tailSet(T fromElement) {
        return takeSubSet(fromElement, null);
    }

    private SortedSet<T> takeSubSet(T start, T finish) {
        int l = searchNumber(start, 0);
        int r = searchNumber(finish, size());
        if (start != null && finish != null && comparator.compare(start, finish) > 0) {
            throw new IllegalArgumentException();
        }
        return new ArraySet<>(array.subList(l, r), comparator);
    }

    private int searchNumber(T element, int ind) {
        if (element != null) {
            ind = binarySearch(element);
            if (ind < 0) {
                ind = -1 - ind;
            }
        }
        return ind;
    }

    private int binarySearch(T element) {
        return Collections.binarySearch(array, element, comparator);
    }

    @Override
    public T first() {
        checkEmpty();
        return array.get(0);
    }

    @Override
    public T last() {
        checkEmpty();
        return array.get(array.size() - 1);
    }

    private void checkEmpty() {
        if (isEmpty()) {
            // :TO-IMPROVE: message
            throw new NoSuchElementException("error: request to empty list. Impossible operation");
        }
    }

    @Override
    public int size() {
        return array.size();
    }

    @Override
    public boolean isEmpty() {
        // :NOTE: isEmpty
        return array.isEmpty();
    }

    @Override
    @SuppressWarnings("unchecked")
    public boolean contains(Object o) {
        T obj = (T) o;
        return binarySearch(obj) >= 0;
    }

    @Override
    public Iterator<T> iterator() {
        return array.iterator();
    }
}
