package info.kgeorgiy.ja.mikhailov.concurrent;

import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;

public class ParallelMapperImpl implements ParallelMapper {

    private final List<Thread> threads = new ArrayList<>();
    private final SynchronizedQueue<Task<?, ?>> taskQueue = new SynchronizedQueue<>();

    public ParallelMapperImpl(final int threadsCount) {
        for (int i = 0; i < threadsCount; i++) {
            // :NOTE: одна и та же лямбда создается заново
            threads.add(new Thread(() -> {
                try {
                    while (true) {
                        final Task<?, ?> task;
                        final Runnable runnable;
                        // :NOTE: этот код является логикой очереди, а не воркера
                        synchronized (taskQueue) {
                            task = taskQueue.peek();
                            runnable = task.getSubtask();
                            if (runnable == null) {
                                taskQueue.poll();
                                continue;
                            }
                        }
                        runnable.run();
                    }
                } catch (final InterruptedException ignored) {
                    // :NOTE: игнорирование ошибок
                }
                // :NOTE: нужно вернуть на место флаг interrupt'а
            }));
        }
        threads.forEach(Thread::start);
    }

    private static class Task<T, R> {
        private final Function<? super T, ? extends R> f;
        private final SynchronizedQueue<T> queue;
        private final List<R> result;
        private RuntimeException runtimeException;
        private int runningSubtasks;
        private int completedSubtasks;
        private boolean isStopped;

        private Task(final Function<? super T, ? extends R> f, final List<? extends T> args) {
            this.f = f;
            this.queue = new SynchronizedQueue<>();
            for (T arg : args) {
                this.queue.add(arg);
            }
            this.result = new ArrayList<>();
            for (int i = 0; i < args.size(); i++) {
                this.result.add(null);
            }

            this.runningSubtasks = 0;
            this.completedSubtasks = 0;
            this.isStopped = false;
        }

        private synchronized void appendException(final RuntimeException e) {
            if (runtimeException == null) {
                runtimeException = e;
                return;
            }
            runtimeException.addSuppressed(e);
        }

        private void setToResult(final int index, final T a) {
            try {
                R currResult = f.apply(a);
                synchronized (this) {
                    result.set(index, currResult);
                }
            } catch (RuntimeException e) {
                synchronized (this) {
                    completedSubtasks += result.size() - runningSubtasks;
                    runningSubtasks = result.size();
                }
                appendException(e);
            }
            synchronized (this) {
                completedSubtasks++;
                if (completedSubtasks == result.size()) {
                    isStopped = true;
                    notify();
                }
            }
        }

        private synchronized Runnable getSubtask() {
            if (runningSubtasks == result.size()) {
                return null;
            }
            final T a = this.queue.poll();
            final int index = runningSubtasks;
            runningSubtasks++;
            return () -> setToResult(index, a);
        }

        private synchronized List<R> getResult() throws InterruptedException {
            while (!isStopped) {
                wait();
            }
            if (runtimeException != null) {
                throw runtimeException;
            }
            return result;
        }
    }

    private static class SynchronizedQueue<T> {
        private final Queue<T> queue;

        private SynchronizedQueue() {
            this.queue = new ArrayDeque<>();
        }

        private synchronized void add(T e) {
            queue.add(e);
        }

        private synchronized void notifyAdd(T e) {
            queue.add(e);
            notifyAll();
        }

        private synchronized T peek() throws InterruptedException {
            while (queue.isEmpty()) {
                wait();
            }
            return queue.peek();
        }

        private synchronized T poll() {
            return queue.poll();
        }
    }

    @Override
    public <T, R> List<R> map(final Function<? super T, ? extends R> f, final List<? extends T> args) throws InterruptedException {
        Task<T, R> currTask = new Task<>(f, args);
        this.taskQueue.notifyAdd(currTask);
        return currTask.getResult();
    }

    @Override
    public void close() {
        threads.forEach(Thread::interrupt);
        for (Thread thread : threads) {
            while (true) {
                try {
                    thread.join();
                    break;
                } catch (InterruptedException ignored1) {
                }
            }
        }
    }
}