package info.kgeorgiy.ja.nazarov.iterative;

import info.kgeorgiy.java.advanced.iterative.NewScalarIP;
import info.kgeorgiy.java.advanced.iterative.ScalarIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.BinaryOperator;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Implementation of the {@link NewScalarIP} interface. Provides the ability to produce
 * different manipulations on the list
 */
public class IterativeParallelism implements NewScalarIP {
    /**
     * mapper
     */
    private final ParallelMapper mapper;

    /**
     * The constructor for {@code IterativeParallelism}.
     * Creates an instance of the {@code IterativeParallelism} class that can be used
     * to manipulations of lists in parallel.
     *
     * @param mapper {@link ParallelMapperImpl} class. Get mapper
     */
    public IterativeParallelism(ParallelMapper mapper) {
        this.mapper = mapper;
    }

    /**
     * Execute given operations by threads, change List values.
     *
     * @param threads      Number of threads for working.
     * @param step         Number of step for working.
     * @param values       List data to change.
     * @param operation    Operation which every thread has to execute.
     * @param resOperation Operation to collect res after threads.
     * @param <T>          List type.
     * @param <R>          Return type.
     * @return Result after operations
     * @throws InterruptedException Error threads.
     */
    private <T, R> R parallelOperation(
            int threads,
            int step,
            List<? extends T> values,
            Function<List<? extends T>, R> operation,
            Function<List<R>, R> resOperation
    ) throws InterruptedException {
        List<List<T>> sharedList = shareList(threads, step, values);

        if (mapper != null) {
            List<R> results = mapper.map(operation, sharedList);
            return resOperation.apply(results);
        }

        List<Thread> threadList = new ArrayList<>();
        List<R> result = new ArrayList<>(Collections.nCopies(threads, null));

        for (int i = 0; i < sharedList.size(); i++) {
            final int index = i;
            List<? extends T> list = sharedList.get(i);
            Thread thread = new Thread(() -> result.set(index, operation.apply(list)));
            thread.start();
            threadList.add(thread);
        }

        for (Thread thread : threadList) {
            thread.join();
        }
        return resOperation.apply(result.stream().filter(Objects::nonNull).toList());
    }

    /**
     * Split given list into thread lists
     *
     * @param threads Number of threads for working.
     * @param step    Number of step for working.
     * @param values  List data to change.
     * @param <T>     List type.
     * @return Result List of lists, result of sharing
     * @throws IllegalArgumentException In case invalid threads count.
     */
    private <T> List<List<T>> shareList(int threads, int step, List<? extends T> values) {
        if (threads < 1) {
            throw new IllegalArgumentException("expected threads > 0 to share list");
        }
        if (values == null) {
            throw new NullPointerException("list is null");
        }
        if (step <= 0) {
            throw new IllegalArgumentException("illegal step argument");
        }

        List<List<T>> sharedList = new ArrayList<>();

        int j = 0;
        for (int i = 0; i < values.size(); i += step) {
            if (j / threads == 0) {
                sharedList.add(new ArrayList<>());
            }
            sharedList.get(j % threads).add(values.get(i));
            j++;
        }
        return sharedList;
    }

    /**
     * The default constructor for {@code IterativeParallelism}.
     * Creates an instance of the {@code IterativeParallelism} class that can be used
     * to manipulations of lists in parallel.
     */
    public IterativeParallelism() {
        super();
        this.mapper = null;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator, int step) throws InterruptedException {
        return parallelOperation(threads, step, values,
                list -> Collections.max(list, comparator),
                list -> Collections.max(list, comparator));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator, int step) throws InterruptedException {
        return maximum(threads, values, comparator.reversed(), step);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate, int step) throws InterruptedException {
        return !any(threads, values, predicate.negate(), step);
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate, int step) throws InterruptedException {
        return parallelOperation(threads, step, values,
                list -> list.stream().anyMatch(predicate),
                list -> list.stream().anyMatch(t -> t.equals(true)));
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T> int count(int threads, List<? extends T> values, Predicate<? super T> predicate, int step) throws InterruptedException {
        return parallelOperation(threads, step, values,
                list -> (int) list.stream().filter(predicate).count(),
                results -> results.stream().reduce(0, Integer::sum));
    }
}
