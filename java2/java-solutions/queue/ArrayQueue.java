package queue;

import java.util.Arrays;
import java.util.Objects;

public class ArrayQueue extends AbstractQueue{
    Object[] array = new Object[5];
    int take = 0;
    int write = 0;

    @Override
    protected void angelEnqueue(Object element) {
        Objects.requireNonNull(element);
        if (size == array.length) {
            copy();
        } else if (write == array.length) {
            write = 0;
        }
        array[write++] = element;
    }

    private void copy () {
        if (write == take) {
            Object[] a = new Object[size];
            int index = 0;
            for (int i = take; i < size; i++) {
                a[index++] = array[i];
            }
            for (int i = 0; i < take; i++) {
                a[index++] = array[i];
            }
            array = Arrays.copyOf(a, size * 2);
            take = 0;
            write = size;
        } else {
            array = Arrays.copyOf(array, size * 2);
        }
    }

    @Override
    protected Object angelElement() {
        if (take == array.length) {
            take = 0;
        }
        return array[take];
    }

    @Override
    protected void angelRemoveElement() {
        array[take] = null;
        take++;
    }

    @Override
    protected void angelClear() {
        take = 0;
        write = 0;
        Arrays.fill(array, null);
    }
}
