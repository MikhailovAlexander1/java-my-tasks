package info.kgeorgiy.ja.mikhailov.arrayset;

import java.util.*;

public class ArraySet<E> extends AbstractSet<E> implements SortedSet<E> {

    private final List<E> elements;
    private final Comparator<? super E> comparator;

    public ArraySet() {
        this(null, null);
    }

    public ArraySet(final Comparator<? super E> comparator) {
        this(null, comparator);
    }

    public ArraySet(final Collection<E> collection) {
        this(collection, null);
    }

    public ArraySet(final Collection<E> collection, final Comparator<? super E> comparator) {
        this.comparator = comparator;
        if (collection != null) {
            TreeSet<E> support = new TreeSet<>(this.comparator);
            support.addAll(collection);
            this.elements = new ArrayList<>(support);
        } else {
            this.elements = Collections.emptyList();
        }
    }

    @Override
    public Iterator<E> iterator() {
        return Collections.unmodifiableList(elements).iterator();
    }

    @Override
    public int size() {
        return elements.size();
    }

    @Override
    public Comparator<? super E> comparator() {
        return comparator;
    }

    public boolean contains(Object el) {
        @SuppressWarnings("unchecked")
        E obj = (E) el;
        return Collections.binarySearch(elements, obj, comparator) >= 0;
    }

    private int binarySearch(final E el) {
        final int position = Collections.binarySearch(elements, el, comparator);
        return (position > -1) ? position : (-position - 1);
    }

    @Override
    public SortedSet<E> subSet(E fromElement, E toElement) {
        if (comparator != null && comparator.compare(fromElement, toElement) > 0) {
            throw new IllegalArgumentException("Incorrect method arguments: " +
                    "fromElement > toElement by Comparator: " + comparator + ".");
        }
        return new ArraySet<>(elements.subList(binarySearch(fromElement), binarySearch(toElement)), comparator);
    }

    @Override
    public SortedSet<E> headSet(E toElement) {
        int position = binarySearch(toElement);
        return new ArraySet<>(elements.subList(0, position), comparator);
    }

    @Override
    public SortedSet<E> tailSet(E fromElement) {
        int position = binarySearch(fromElement);
        return new ArraySet<>(elements.subList(position, elements.size()), comparator);
    }

    private E checkIsEmptyAndGet(int position) {
        if (elements.isEmpty()) {
            throw new NoSuchElementException("ArraySet is empty.");
        }
        return elements.get(position);
    }

    @Override
    public E first() {
        return checkIsEmptyAndGet(0);
    }

    @Override
    public E last() {
        return checkIsEmptyAndGet(size() - 1);
    }
}
