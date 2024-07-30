package queue;

/*
Model: a[0]..a[n - 1]
Invariant: for i=0..n: a[i] != null

Let n = a.length
*/

import java.util.Arrays;
import java.util.Objects;

public class ArrayQueueADT {
    private Object[] array;
    private int size = 0;
    private int write = 0;
    private int take = 0;

    public ArrayQueueADT () {
        array = new Object[5];
    }

    public static ArrayQueueADT create () {
        final ArrayQueueADT queue = new ArrayQueueADT();
        queue.array = new Object[5];
        return queue;
    }

    //Pred: x != null
    //Post: n' = n + 1 && a[n' - 1] == x && for i=0..n: a'[i] == a[i]
    public static void enqueue (ArrayQueueADT queue, final Object element) {
        Objects.requireNonNull(element);
        if (queue.size == queue.array.length) {
            copy(queue);
        } else if (queue.write == queue.array.length) {
            queue.write = 0;
        }
        queue.array[queue.write++] = element;
        queue.size++;
    }

    private static void copy (ArrayQueueADT queue) {
        if (queue.write == queue.take) {
            Object[] a = new Object[queue.size];
            int index = 0;
            for (int i = queue.take; i < queue.size; i++) {
                a[index++] = queue.array[i];
            }
            for (int i = 0; i < queue.take; i++) {
                a[index++] = queue.array[i];
            }
            queue.array = Arrays.copyOf(a, queue.size * 2);
            queue.take = 0;
            queue.write = queue.size;
        } else {
            queue.array = Arrays.copyOf(queue.array, queue.size * 2);
        }
    }

    //Pred: n > 0
    //Post: R == a[0] && n' == n && for i=0..n': a'[i] == a[i]
    public static Object element (ArrayQueueADT queue) {
        if (queue.take == queue.array.length) {
            queue.take = 0;
        }
        return queue.array[queue.take];
    }

    //Pred: n > 0
    //Post: R == a[0] && n' = n - 1 && for i=0..n': a'[i] == a[i + 1]
    public static Object dequeue (ArrayQueueADT queue) {
        if (queue.take == queue.array.length) {
            queue.take = 0;
        }
        Object result = queue.array[queue.take];
        queue.array[queue.take] = null;
        queue.take++;
        queue.size--;
        return result;
    }

    //Pred: true
    //Post: R == n && n' == n && for i=0..n': a'[i] == a[i]
    public static int size (ArrayQueueADT queue) {
        return queue.size;
    }

    //Pred: true
    //Post: R == (n == 0) && n' == n && for i=0..n': a'[i] == a[i]
    public static boolean isEmpty (ArrayQueueADT queue) {
        return queue.size == 0;
    }

    //Pred: true
    //Post: n' == 0
    public static void clear (ArrayQueueADT queue) {
        queue.size = 0;
        queue.take = 0;
        queue.write = 0;
        Arrays.fill(queue.array, null);
    }

    //Pred: x != null
    //Post: n' == n && R = (count = (for i=0..n': if a'[i] == x: count++)) && for i=0..n': a'[i] == a[i]
    public static int count (ArrayQueueADT queue, final Object x) {
        Objects.requireNonNull(x);
        int count = 0;
        for (int i = 0; i < queue.array.length; i++) {
            if (queue.array[i] == null) {
                continue;
            }
            if (queue.array[i].equals(x)) {
                count++;
            }
        }
        return count;
    }
}