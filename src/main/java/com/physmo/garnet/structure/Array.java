package com.physmo.garnet.structure;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.function.Predicate;

/**
 * A generic dynamic array that supports adding elements, resizing, sorting, and
 * clearing. This class is designed for allocation-conscious code paths that can
 * reuse an instance across frames.
 *
 * <p>For frame loops that must avoid garbage collection, allocate and size this
 * array outside the loop, call {@link #clear()} for reuse, and iterate by index
 * over {@link #array} and {@link #size}. Methods that grow the backing array,
 * create iterators, use freshly-created predicates, or delegate to object-array
 * sorting may still allocate.
 *
 * @param <T> the type of elements stored in the array
 */
public class Array<T> implements Iterable<T> {

    public T[] array;
    public int size;

    /**
     * Constructs an Array with a specified initial capacity. The array is initialized
     * to hold elements of type T, and the initial size of the array is set to 0.
     *
     * @param capacity the initial capacity of the array
     *                 (i.e., the maximum number of elements it can hold before resizing).
     * @throws IllegalArgumentException if the capacity is negative
     */
    public Array(int capacity) {
        if (capacity < 0) {
            throw new IllegalArgumentException("Capacity cannot be negative: " + capacity);
        }
        array = (T[]) new Object[capacity];
        size = 0;
    }

    /**
     * Returns the current capacity of the underlying array.
     *
     * @return the total number of elements the array can hold.
     */
    public int getCapacity() {
        return array.length;
    }

    /**
     * Removes all elements from the array. After calling this method, the array will
     * be empty, and its size will be reset to zero. The elements in the array will
     * be set to null.
     *
     * <p>This method does not allocate and is the preferred way to reuse an
     * {@code Array} across frames.
     */
    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }

    /**
     * Removes all elements from the array that satisfy the given predicate.
     *
     * <p>This method does not allocate internally and clears removed references from
     * the inactive tail. Creating the predicate at the call site, for example with a
     * capturing lambda inside a frame loop, may still allocate.
     *
     * @param filter a predicate used to determine which elements should be removed.
     *               The predicate is applied to each element, and elements for which
     *               the predicate returns true are removed.
     * @return true if any elements were removed as a result of the operation,
     * false otherwise.
     */
    public boolean removeIf(Predicate<? super T> filter) {
        Objects.requireNonNull(filter);
        boolean removed = false;

        int oldSize = size;
        int writePos = 0;
        for (int readPos = 0; readPos < oldSize; readPos++) {
            T element = array[readPos];
            if (filter.test(element)) {
                removed = true;
                continue;
            }
            array[writePos++] = element;
        }
        for (int i = writePos; i < oldSize; i++) {
            array[i] = null;
        }
        size = writePos;
        return removed;
    }

    /**
     * Adds all elements from the specified list to the array.
     *
     * <p>This method may allocate if the backing array must grow. Pre-size the
     * target array before frame-loop use.
     *
     * @param list the list containing elements to be added to this array
     * @throws NullPointerException if the specified list is null
     *                              or any list element is null
     */
    public void addAll(List<T> list) {
        Objects.requireNonNull(list, "List cannot be null");
        ensureCapacity(size + list.size());
        for (int i = 0; i < list.size(); i++) add(list.get(i));
    }

    private void ensureCapacity(int minimumCapacity) {
        while (minimumCapacity > array.length) {
            doubleArrayCapacity();
        }
    }

    /**
     * Adds the specified element to the array. If the underlying array's capacity
     * is reached, it is automatically doubled before adding the element.
     *
     * <p>This method is allocation-free only while {@link #size} is less than
     * {@link #getCapacity()}. Capacity growth allocates a new backing array.
     *
     * @param element the element to be added to the array
     * @throws NullPointerException if the element is null
     */
    public void add(T element) {
        Objects.requireNonNull(element, "Element cannot be null");
        if (size == array.length) doubleArrayCapacity();
        array[size++] = element;
    }

    private void doubleArrayCapacity() {
        int newCapacity = Math.max(1, array.length * 2);
        array = Arrays.copyOf(array, newCapacity);
    }

    /**
     * Adds all elements from another {@link Array} to this array.
     *
     * <p>This method may allocate if the backing array must grow. Pre-size the
     * target array before frame-loop use.
     *
     * @param list the source array whose elements will be added
     * @throws NullPointerException if the specified array is null
     *                              or any source element is null
     */
    public void addAll(Array<T> list) {
        Objects.requireNonNull(list, "Array cannot be null");
        int sourceSize = list.size();
        ensureCapacity(size + sourceSize);
        for (int i = 0; i < sourceSize; i++) add(list.get(i));
    }

    /**
     * Returns the current number of elements stored in the array.
     *
     * @return the number of elements currently present in the array.
     */
    public int size() {
        return size;
    }

    /**
     * Checks if the array is empty.
     *
     * @return true if the array contains no elements, false otherwise.
     */
    public boolean isEmpty() {
        return size == 0;
    }

    /**
     * Retrieves the element at the specified index from the array.
     *
     * @param index the position of the element to retrieve, zero-based.
     *              Must be within the range `0` to `size - 1`, where `size` is
     *              the number of elements currently stored in the array.
     * @return the element of type T stored at the specified index.
     * @throws ArrayIndexOutOfBoundsException if the index is out of bounds
     *                                        (i.e., less than 0 or greater than or equal to the current size
     *                                        of the array).
     */
    public T get(int index) {
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        return array[index];
    }

    /**
     * Sorts the elements in the array using the specified comparator.
     * The sorting is performed on the internal array from the beginning
     * of the array up to the current size of the array.
     *
     * <p>This delegates to {@link Arrays#sort(Object[], int, int, Comparator)}, which
     * may allocate temporary storage for object-array sorting. Avoid sorting in
     * allocation-sensitive frame loops unless benchmarked for the target workload.
     *
     * @param comparator the comparator to determine the order of the array.
     *                   A {@code null} comparator indicates that the elements'
     *                   natural ordering should be used.
     */
    public void sort(Comparator<T> comparator) {
        Arrays.sort(array, 0, size, comparator);
    }

    /**
     * Checks whether the specified element is present in the array.
     *
     * @param element the element to check for presence in the array. Must be of type T.
     * @return true if the element is found in the array, false otherwise.
     * @throws NullPointerException if the element is null
     */
    public boolean contains(T element) {
        Objects.requireNonNull(element, "Element cannot be null");
        for (int i = 0; i < size; i++) {
            if (element.equals(array[i])) return true;
        }
        return false;
    }

    /**
     * Retrieves the index of the specified element in the array.
     *
     * @param element the element to search for in the array. Must be of type T.
     * @return the zero-based index of the element if found; -1 otherwise.
     * @throws NullPointerException if the element is null
     */
    public int indexOf(T element) {
        Objects.requireNonNull(element, "Element cannot be null");
        for (int i = 0; i < size; i++) {
            if (element.equals(array[i])) return i;
        }
        return -1;
    }

    /**
     * Sets the specified element at the given index in the array.
     *
     * @param index   the zero-based index where the element should be set.
     *                Must be within the range `0` to `size - 1`.
     * @param element the element of type T to set at the specified index.
     * @throws ArrayIndexOutOfBoundsException if the index is out of bounds
     *                                        (i.e., less than 0 or greater than or equal to the current size
     *                                        of the array).
     * @throws NullPointerException           if the provided element is null.
     */
    public void setAt(int index, T element) {
        if (index < 0 || index >= size) {
            throw new ArrayIndexOutOfBoundsException("Index out of bounds: " + index);
        }
        Objects.requireNonNull(element, "Element cannot be null");
        array[index] = element;
    }

    /**
     * Returns an iterator over elements of type T in the array.
     * The iterator allows sequential access to the elements stored in the array.
     *
     * <p>This method allocates an iterator. Enhanced {@code for} loops call this
     * method, so prefer indexed loops in allocation-sensitive frame paths.
     *
     * @return an Iterator of type T that allows traversal of the array's elements.
     */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            int index = 0;

            @Override
            public boolean hasNext() {
                return index < size;
            }

            @Override
            public T next() {
                if (hasNext()) {
                    return array[index++];
                }
                throw new NoSuchElementException();
            }
        };
    }
}
