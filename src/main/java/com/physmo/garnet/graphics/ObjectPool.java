package com.physmo.garnet.graphics;

import java.lang.reflect.Array;

/**
 * A generic object pool implementation that allows for efficient reuse of objects.
 * The ObjectPool class maintains a pool of reusable objects to minimize object creation
 * overhead by recycling objects instead of creating new ones each time.
 *
 * @param <T> the type of object to be pooled in this ObjectPool instance
 */
public class ObjectPool<T> {

    T[] pool;
    int head;
    Class<T> clazz;
    PoolObjectCreator poolObjectCreator;

    public ObjectPool(Class<T> clazz, PoolObjectCreator poolObjectCreator) {
        this.clazz = clazz;
        this.poolObjectCreator = poolObjectCreator;
        int startingCapacity = 50;
        this.head = -1;
        pool = (T[]) Array.newInstance(clazz, startingCapacity);
    }

    /**
     * Returns an object to the pool so it can be reused.
     * The pool is expanded automatically if it is full.
     *
     * @param object the object to return to the pool
     */
    public void releaseObject(T object) {
        if (head == pool.length - 2) expandPool();
        pool[++head] = object;
    }

    /**
     * Doubles the capacity of the internal pool array.
     * Called automatically by {@link #releaseObject} when the pool is full.
     */
    public void expandPool() {
        T[] newPool = (T[]) Array.newInstance(clazz, pool.length * 2);
        System.arraycopy(pool, 0, newPool, 0, pool.length);
        pool = newPool;
        // System.out.println("Expanding pool to " + pool.length + "  head:" + head);
    }

    /**
     * Returns a pooled object for reuse, or creates a new one via the
     * {@link PoolObjectCreator} if the pool is empty.
     *
     * @return a reusable object of type {@code T}
     */
    public T getFreeObject() {
        if (head >= 0) {
            return pool[head--];
        }
        return (T) poolObjectCreator.createNew();
    }

    @Override
    public String toString() {
        return "ObjectPool{" +
                "pool size=" + pool.length +
                ", head=" + head + "}";
    }
}
