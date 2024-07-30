package info.kgeorgiy.ja.mikhailov.concurrent;

import info.kgeorgiy.java.advanced.concurrent.ListIP;
import info.kgeorgiy.java.advanced.concurrent.ScalarIP;
import info.kgeorgiy.java.advanced.mapper.ParallelMapper;

import java.util.*;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IterativeParallelism implements ScalarIP, ListIP {
    private final ParallelMapper parallelMapper;
    public IterativeParallelism() {
        this.parallelMapper = null;
    }
    public IterativeParallelism(ParallelMapper parallelMapper) {
        this.parallelMapper = parallelMapper;
    }

    private <T, R> R multiThread(final int numberOfThreads, final List<? extends T> data,
                          final Function<Stream<? extends T>, R> task,
                          final Function<Stream<? extends R>, R> taskToCollectAnswer) throws InterruptedException {
        int requiredNumberOfThreads = Math.min(numberOfThreads, data.size());
        List<Stream<? extends T>> parts = getParts(requiredNumberOfThreads, data);
        List<R> executionResult;
        if (parallelMapper != null) {
            executionResult = parallelMapper.map(task, parts);
        } else {
            List<Thread> threads = new ArrayList<>();
            executionResult = new ArrayList<>();
            for (int i = 0; i < requiredNumberOfThreads; i++) {
                executionResult.add(null);
                final int index = i;
                threads.add(new Thread(() -> executionResult.set(index, task.apply(parts.get(index)))));
            }
            threads.forEach(Thread::start);
            waitExecutionEnds(threads);
        }
        return taskToCollectAnswer.apply(executionResult.stream());
    }

    private <T> List<Stream<? extends T>> getParts(final int numberOfThreads, final List<? extends T> data) {
        List<Stream<? extends T>> partsOfData = new ArrayList<>();
        int dataPartLength = data.size() / numberOfThreads;
        int restOfData = data.size() % numberOfThreads;
        int usedRestElements = 0;
        for (int i = 0; i < numberOfThreads; i++) {
            final int startIndex = dataPartLength * i + usedRestElements;
            final int endIndex = startIndex + dataPartLength + (--restOfData < 0 ? 0 : 1);
            if (restOfData > -1) {
                usedRestElements++;
            }
            partsOfData.add(data.subList(startIndex, endIndex).stream());
        }
        return partsOfData;
    }

    private void waitExecutionEnds(List<Thread> threads) throws InterruptedException {
        InterruptedException interruptedException;
        try {
            for (Thread thread : threads) {
                thread.join();
            }
        } catch (InterruptedException globalException) {
            interruptedException = globalException;
            threads.forEach(Thread::interrupt);
            for (Thread thread : threads) {
                try {
                    thread.join();
                } catch (InterruptedException innerException) {
                    interruptedException.addSuppressed(innerException);
                }
            }
            throw interruptedException;
        }
    }

    private <T, R> R getResultOfMultiThreadTask(final int numberOfThreads, final List<? extends T> data,
                                                final Function<Stream<? extends T>, R> task,
                                                final Function<Stream<? extends R>, R> taskToCollectAnswer) throws InterruptedException {
        if (numberOfThreads < 1) {
            throw new InterruptedException("Unable to run task in " + numberOfThreads + " threads!");
        }
        if (data == null || data.stream().allMatch(Objects::isNull)) {
            throw new NullPointerException("Task launches only on non-null provided data!");
        }

        return multiThread(numberOfThreads, data, task, taskToCollectAnswer);
    }

    @Override
    public <T> T maximum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        if (values.size() == 0) {
            throw new NoSuchElementException("No elements in provided list, impossible to find maximum/minimum.");
        }
        return getResultOfMultiThreadTask(threads, values,
                stream -> stream.max(comparator).orElse(null),
                stream -> stream.max(comparator).orElse(null));
    }

    @Override
    public <T> T minimum(int threads, List<? extends T> values, Comparator<? super T> comparator) throws InterruptedException {
        return maximum(threads, values, Collections.reverseOrder(comparator));
    }

    @Override
    public <T> boolean all(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return getResultOfMultiThreadTask(threads, values,
                stream -> stream.allMatch(predicate),
                stream -> stream.allMatch(x -> x));
    }

    @Override
    public <T> boolean any(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return getResultOfMultiThreadTask(threads, values,
                stream -> stream.anyMatch(predicate),
                stream -> stream.anyMatch(x -> x));
    }

    @Override
    public <T> int count(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return getResultOfMultiThreadTask(threads, values,
                stream -> (int) stream.filter(predicate).count(),
                stream -> stream.mapToInt(Integer::intValue).sum());
    }

    @Override
    public String join(int threads, List<?> values) throws InterruptedException {
        return getResultOfMultiThreadTask(threads, values,
                stream -> stream.map(Object::toString).collect(Collectors.joining()),
                stream -> stream.collect(Collectors.joining()));
    }

    @Override
    public <T> List<T> filter(int threads, List<? extends T> values, Predicate<? super T> predicate) throws InterruptedException {
        return getResultOfMultiThreadTask(threads, values,
                stream -> stream.filter(predicate).collect(Collectors.toList()),
                stream -> stream.flatMap(Collection::stream).collect(Collectors.toList()));
    }

    @Override
    public <T, U> List<U> map(int threads, List<? extends T> values, Function<? super T, ? extends U> f) throws InterruptedException {
        return getResultOfMultiThreadTask(threads, values,
                stream -> stream.map(f).collect(Collectors.toList()),
                stream -> stream.flatMap(Collection::stream).collect(Collectors.toList()));
    }
}
