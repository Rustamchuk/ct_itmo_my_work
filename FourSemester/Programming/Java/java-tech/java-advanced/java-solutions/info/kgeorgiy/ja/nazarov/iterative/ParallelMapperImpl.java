package info.kgeorgiy.ja.nazarov.iterative;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

/**
 * Implementation of the {@link ParallelMapper} interface. Provides the ability to produce
 * different manipulations in parallel mode
 */
public class ParallelMapperImpl implements ParallelMapper {
    /**
     * threads list
     */
    private final List<Thread> workers;

    /**
     * tasks storage
     */
    private final Tasks tasks;

    /**
     * The constructor for {@code ParallelMapperImpl}.
     * Creates an instance of the {@code ParallelMapperImpl} class that can be used
     * to manipulations in parallel.
     *
     * @param threads thread count for parallel executing
     */
    public ParallelMapperImpl(int threads) {
        if (threads < 0) {
            throw new IllegalArgumentException();
        }

        workers = new ArrayList<>(threads);
        tasks = new Tasks();

        for (int i = 0; i < threads; i++) {
            Thread worker = new Thread(this::processTask);
            worker.start();
            workers.add(worker);
        }
    }

    /**
     * Method to get task and start solving. If thread in not interrupted
     */
    private void processTask() {
        try {
            while (!Thread.interrupted()) {
                Runnable task;
                synchronized (tasks) {
                    task = tasks.getTask();
                }
                try {
                    task.run();
                } catch (RuntimeException ignored) {
                }
            }
        } catch (InterruptedException ignored) {
        }
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public <T, R> List<R> map(Function<? super T, ? extends R> f, List<? extends T> args) throws InterruptedException {
        List<R> results = new ArrayList<>(Collections.nCopies(args.size(), null));
        Counter argLen = new Counter(args.size());

        for (int i = 0; i < args.size(); i++) {
            final int index = i;
            synchronized (tasks) {
                tasks.addTask(() -> {
                    results.set(index, f.apply(args.get(index)));
                    synchronized (argLen) {
                        argLen.reduce();
                    }
                });
                tasks.notify();
            }
        }

        synchronized (argLen) {
            try {
                argLen.waitZero();
            } catch (InterruptedException ignored) {
            }
        }

        return results;
    }

    /**
     * {@inheritDoc}
     */
    @Override
    public void close() {
        workers.forEach(Thread::interrupt);
        for (Thread worker : workers) {
            try {
                worker.join();
            } catch (InterruptedException ignored) {
            }
        }
    }

    /**
     * Class, list Tasks to solve in parallel
     */
    private static class Tasks {
        /**
         * tasks list
         */
        private final Queue<Runnable> tasks;

        /**
         * The constructor for {@code Tasks}.
         * Creates an instance of the {@code Tasks} class that can be used
         * to keep tasks.
         */
        public Tasks() {
            tasks = new LinkedList<>();
        }

        /**
         * Method to get task. Wait appearance and return
         *
         * @return task for solve
         * @throws InterruptedException if interrupt
         */
        public Runnable getTask() throws InterruptedException {
            while (tasks.isEmpty()) {
                wait();
            }
            return tasks.poll();
        }

        /**
         * Method to add task. And notify
         *
         * @param task Task to add in list.
         */
        public void addTask(Runnable task) {
            tasks.add(task);
            notify();
        }
    }

    /**
     * Class, args counter, to count necessary solving tasks
     */
    private static class Counter {
        /**
         * count of args cur
         */
        private int count;

        /**
         * The constructor for {@code Counter}.
         * Creates an instance of the {@code Counter} class that can be used
         * to count tasks for solve.
         *
         * @param count args count
         */
        public Counter(int count) {
            this.count = count;
        }

        /**
         * Method to reduce count. And notify, if empty
         */
        public void reduce() {
            count--;
            if (isZero()) {
                this.notify();
            }
        }

        /**
         * if arg count in zero
         *
         * @return true if zero
         */
        public boolean isZero() {
            return count == 0;
        }

        /**
         * Wait for all solving arguments.
         *
         * @throws InterruptedException for interrupt
         */
        public void waitZero() throws InterruptedException {
            while (!isZero()) {
                wait();
            }
        }
    }
}
