package com.physmo.garnet.toolkit;

import java.util.HashMap;
import java.util.Map;

/**
 * The {@code StringIdBroker} is an enumeration that provides a mechanism to
 * convert strings into unique integer identifiers. This ensures that each string
 * corresponds to a specific integer, and the same string will always produce
 * the same integer.
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
     * Retrieves the unique integer identifier associated with the provided string key.
     *
     * @param key The string key for which the unique identifier is to be retrieved.
     *            Must not be null or empty.
     * @return The unique integer identifier associated with the provided key.
     * @throws IllegalArgumentException if the key is null or empty.
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