package com.physmo.garnet.structure;

import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;

/**
 * A generic dynamic array that supports adding elements, resizing, sorting,
 * and clearing. It also provides an iterator for traversing its elements.
 * This class is specifically designed to avoid allocations which will
 * contribute to garbage collection.
 *
 * @param <T> the type of elements stored in the array
 */
public class Array<T> implements Iterable<T> {

    public T[] array;
    public int size;

    public Array(int capacity) {
        array = (T[]) new Object[capacity];
        size = 0;
    }

    public void add(T element) {
        if (size == array.length) doubleArrayCapacity();
        array[size++] = element;
    }

    private void doubleArrayCapacity() {
        T[] newArray = (T[]) new Object[array.length * 2];
        System.arraycopy(array, 0, newArray, 0, array.length);
        array = newArray;
    }

    public int getCapacity() {
        return array.length;
    }

    public void clear() {
        for (int i = 0; i < size; i++) {
            array[i] = null;
        }
        size = 0;
    }

    public int size() {
        return size;
    }

    public void sort(Comparator<T> comparator) {
        Arrays.sort(array, 0, size, comparator);
    }

    public T get(int index) {
        return array[index];
    }

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
                return null;
            }
        };
    }
}
