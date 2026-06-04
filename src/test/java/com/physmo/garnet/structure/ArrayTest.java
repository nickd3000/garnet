package com.physmo.garnet.structure;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayTest {

    @Test
    void capacityDoublesWhenFull() {
        Array<String> array = new Array<>(5);

        for (int i = 0; i < 6; i++) {
            array.add("A");
        }

        assertEquals(6, array.size());
        assertEquals(10, array.getCapacity());
    }

    @Test
    void clearRemovesAllItems() {
        Array<String> array = new Array<>(5);

        array.add("A");
        array.add("B");
        array.add("C");
        array.add("D");
        array.clear();

        assertEquals(0, array.size());
    }

    @Test
    void itemsCanBeAddedAndRetrieved() {
        Array<String> array = new Array<>(5);

        array.add("A");
        array.add("B");
        array.add("C");

        assertEquals("A", array.get(0));
        assertEquals("B", array.get(1));
        assertEquals("C", array.get(2));
    }

    @Test
    void iteratorReturnsItemsInInsertionOrder() {
        Array<String> array = new Array<>(5);
        array.add("A");
        array.add("B");
        array.add("C");

        int count = 0;
        String[] strings = new String[5];
        for (String str : array) {
            strings[count++] = str;
        }

        assertEquals("A", strings[0]);
        assertEquals("B", strings[1]);
        assertEquals("C", strings[2]);
        assertEquals(3, count);
    }

    @Test
    void removeIfRemovesMatchingItems() {
        Array<String> array = new Array<>(5);
        array.add("A");
        array.add("X");
        array.add("B");
        array.add("X");
        array.add("C");

        int startingSize = array.size();
        array.removeIf(s -> s.equals("X"));

        assertEquals(5, startingSize);
        assertEquals(3, array.size());
        assertEquals("A", array.get(0));
        assertEquals("B", array.get(1));
        assertEquals("C", array.get(2));
    }

    @Test
    void addAllAppendsListItems() {
        Array<String> array = new Array<>(5);
        List<String> list = new ArrayList<>();

        array.add("A");
        array.add("B");
        array.add("C");
        list.add("D");
        list.add("E");
        list.add("F");

        int startingSize = array.size();
        array.addAll(list);

        assertEquals(3, startingSize);
        assertEquals(6, array.size());
        assertEquals("A", array.get(0));
        assertEquals("B", array.get(1));
        assertEquals("C", array.get(2));
        assertEquals("D", array.get(3));
        assertEquals("E", array.get(4));
        assertEquals("F", array.get(5));
    }
}
