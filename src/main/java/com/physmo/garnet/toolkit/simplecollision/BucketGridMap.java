package com.physmo.garnet.toolkit.simplecollision;

import com.physmo.garnet.structure.Array;

import java.util.Arrays;
import java.util.List;


public class BucketGridMap {
    // TODO: this must be rewritten to use basic arrays!

    int[] keys = new int[100];
    Array<List<Object>> objects = new Array<>(100);

    int size = 0;


    public int[] keySet() {
        int[] subset = new int[size];
        for (int i = 0; i < size; i++) {
            subset[i] = keys[i];
        }
        return subset;
    }

    public void clear() {
        Arrays.fill(keys, -1);
        size = 0;
        objects.clear();
    }

    public int size() {
        return size;
    }

    public boolean containsKey(int key) {
        return getKeyIndex(key) != -1;
    }

    public int getKeyIndex(int key) {
        for (int i = 0; i < size; i++) {
            if (keys[i] == key) return i;
        }
        return -1;
    }

    public List<Object> get(int key) {
        int index = getKeyIndex(key);
        if (index == -1) return null;

        return objects.get(index);
    }

    public void put(int key, List<Object> value) {
        int index = getKeyIndex(key);

        if (index != -1) {
            // Key exists
            objects.setAt(index, value);
        } else {
            if (keys.length == size) doubleKeySetSize();
            keys[size++] = key;
            objects.add(value);
        }


        //if (keys.size > 1000) System.out.println("BucketGridMap is getting big " + keys.size);
    }

    public void doubleKeySetSize() {
        int[] newKeys = new int[keys.length * 2];
        System.arraycopy(keys, 0, newKeys, 0, keys.length);
        keys = newKeys;
    }
}
