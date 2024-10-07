package com.physmo.garnet.toolkit;

import java.util.HashMap;
import java.util.Map;

/**
 * Service that stores cached string ID's for use in fast ID lookup.
 */


public enum StringIdBroker {

    INSTANCE;

    private final Map<String, Integer> idMap;
    private int nextId = 1;

    StringIdBroker() {
        idMap = new HashMap<>();
    }

    public static StringIdBroker getInstance() {
        return INSTANCE;
    }

    /**
     * Returns an integer that corresponds to the supplied string.
     * this is guaranteed to return the same number for a supplied string,
     * and no other string will result in that number.
     *
     * @param key
     * @return
     */
    public int getId(String key) {
        if (key == null || key.isEmpty()) {
            throw new IllegalArgumentException("Key cannot be null or empty");
        }

        if (!idMap.containsKey(key)) {
            idMap.put(key, nextId++);
        }

        return idMap.get(key);
    }

}