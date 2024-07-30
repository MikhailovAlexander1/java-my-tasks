package queue;

/*
Model: a[1]..a[n]
Invariant = for i=1..n: a[i] != null
 */

import java.util.function.Predicate;

public interface Queue {
    //Pred: element != null;
    //Post: n' = n + 1 && a[n'] == element && for i=1..n: a'[i] == a[i]
    void enqueue(final Object element);

    //Pred: n > 0
    //Post: R = a[1] && n' == n && for i=1..n: a'[i] == a[i]
    Object element();

    //Pred: n > 0;
    //Post: R = a[1] && n' = n - 1 && for i=1..n: a'[i] == a[i + 1]
    Object dequeue();

    //Pred: true
    //Post: R = n && n' == n && for i=1..n: a'[i] == a[i]
    int size();

    //Pred: true
    //Post: R = (n == 0) && n' == n && for i=1..n: a'[i] == a[i]
    boolean isEmpty();

    //Pred: true
    //Post: n' == 0
    void clear();

    //Pred: element != null
    //Post: R = count: (for i=1..n: if a[i] == element: count++) && n' == n && for i=1..n: a'[i] == a[i]
    int count(Object element);

    //Pred: predicate != null
    //Post: R = count: (for i=1..n: if predicate.test(a[i]): count++) && n' == n && for i=1..n: a'[i] == a[i]
    int countIf (Predicate<Object> predicate);
}
