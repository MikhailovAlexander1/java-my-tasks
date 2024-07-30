package queue;

import java.util.Objects;
import java.util.function.Predicate;

public abstract class AbstractQueue implements Queue {
    protected int size;

    protected abstract void angelEnqueue(final Object element);

    public void enqueue(final Object element) {
        Objects.requireNonNull(element);
        angelEnqueue(element);
        size++;
    }

    protected abstract Object angelElement();

    public Object element() {
        assert size > 0;
        return angelElement();
    }

    protected abstract void angelRemoveElement();

    public Object dequeue() {
        assert size > 0;
        Object result = element();
        angelRemoveElement();
        size--;
        return result;
    }

    public int size() {
        return size;
    }

    public boolean isEmpty() {
        return size == 0;
    }

    protected abstract void angelClear();

    public void clear() {
        size = 0;
        angelClear();
    }

    public int count(Object element) {
        Objects.requireNonNull(element);
        int count = 0;
        for (int i = 0; i < size; i++) {
            Object x = dequeue();
            if (element.equals(x)) {
                count++;
            }
            enqueue(x);
        }
        return count;
    }

    public int countIf(Predicate<Object> predicate) {
        Objects.requireNonNull(predicate);
        int count = 0;
        for (int i = 0; i < size; i++) {
            Object x = dequeue();
            if (predicate.test(x)) {
                count++;
            }
            enqueue(x);
        }
        return count;
    }
}
