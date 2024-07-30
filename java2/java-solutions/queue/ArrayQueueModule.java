package queue;

/*
Model: a[0]..a[n - 1]
Invariant: for i=0..n: a[i] != null

Let n = a.length
*/

import java.util.Arrays;
import java.util.Objects;

public class ArrayQueueModule {
    private static Object[] array = new Object[5];
    private static int size = 0;
    private static int write = 0;
    private static int take = 0;

    //Pred: x != null
    //Post: n' = n + 1 && a[n' - 1] == x && for i=0..n: a'[i] == a[i]
    public static void enqueue (final Object element) {
        Objects.requireNonNull(element);
        if (size == array.length) {
            copy();
        } else if (write == array.length) {
            write = 0;
        }
        array[write++] = element;
        size++;
    }

    private static void copy () {
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

    //Pred: n > 0
    //Post: R == a[0] && n' == n && for i=0..n': a'[i] == a[i]
    public static Object element () {
        if (take == array.length) {
            take = 0;
        }
        return array[take];
    }

    //Pred: n > 0
    //Post: R == a[0] && n' = n - 1 && for i=0..n': a'[i] == a[i + 1]
    public static Object dequeue () {
        if (take == array.length) {
            take = 0;
        }
        Object result = array[take];
        array[take] = null;
        take++;
        size--;
        return result;
    }

    //Pred: true
    //Post: R == n && n' == n && for i=0..n': a'[i] == a[i]
    public static int size () {
        return size;
    }

    //Pred: true
    //Post: R == (n == 0) && n' == n && for i=0..n': a'[i] == a[i]
    public static boolean isEmpty () {
        return size == 0;
    }

    //Pred: true
    //Post: n' == 0
    public static void clear () {
        write = 0;
        take = 0;
        size = 0;
        Arrays.fill(array, null);
    }

    //Pred: x != null
    //Post: n' == n && R = (count = (for i=0..n': if a'[i] == x: count++)) && for i=0..n': a'[i] == a[i]
    public static int count (final Object x) {
        Objects.requireNonNull(x);
        int count = 0;
        for (Object o : array) {
            if (o == null || !o.equals(x)) {
                continue;
            }
            count++;
        }
        return count;
    }
}