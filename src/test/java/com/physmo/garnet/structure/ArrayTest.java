package com.physmo.garnet.structure;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

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
    void removeIfReturnsFalseAndPreservesItemsWhenNothingMatches() {
        Array<String> array = new Array<>(5);
        array.add("A");
        array.add("B");
        array.add("C");

        boolean removed = array.removeIf(s -> s.equals("X"));

        assertFalse(removed);
        assertEquals(3, array.size());
        assertEquals("A", array.get(0));
        assertEquals("B", array.get(1));
        assertEquals("C", array.get(2));
    }

    @Test
    void removeIfReturnsTrueAndRemovesAdjacentMatchingItems() {
        Array<String> array = new Array<>(6);
        array.add("A");
        array.add("X");
        array.add("X");
        array.add("B");
        array.add("X");
        array.add("C");

        boolean removed = array.removeIf(s -> s.equals("X"));

        assertTrue(removed);
        assertEquals(3, array.size());
        assertEquals("A", array.get(0));
        assertEquals("B", array.get(1));
        assertEquals("C", array.get(2));
    }

    @Test
    void removeIfRejectsNullPredicate() {
        Array<String> array = new Array<>(1);

        assertThrows(NullPointerException.class, () -> array.removeIf(null));
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

    @Test
    void addAllAppendsItemsFromAnotherArray() {
        Array<String> array = new Array<>(2);
        Array<String> other = new Array<>(2);
        array.add("A");
        array.add("B");
        other.add("C");
        other.add("D");

        array.addAll(other);

        assertEquals(4, array.size());
        assertEquals("A", array.get(0));
        assertEquals("B", array.get(1));
        assertEquals("C", array.get(2));
        assertEquals("D", array.get(3));
    }

    @Test
    void containsAndIndexOfReportPresentAndAbsentItems() {
        Array<String> array = new Array<>(3);
        array.add("A");
        array.add("B");
        array.add("C");

        assertTrue(array.contains("B"));
        assertFalse(array.contains("X"));
        assertEquals(1, array.indexOf("B"));
        assertEquals(-1, array.indexOf("X"));
    }

    @Test
    @SuppressWarnings({"rawtypes", "unchecked"})
    void sortOnlySortsActiveRange() {
        Array array = new Array(5);
        array.add(3);
        array.add(1);
        array.add(2);
        array.array[3] = -100;
        array.array[4] = -200;

        array.sort((a, b) -> ((Integer) a).compareTo((Integer) b));

        assertEquals(1, array.get(0));
        assertEquals(2, array.get(1));
        assertEquals(3, array.get(2));
        assertEquals(-100, array.array[3]);
        assertEquals(-200, array.array[4]);
    }

    @Test
    void setAtReplacesExistingElement() {
        Array<String> array = new Array<>(2);
        array.add("A");
        array.add("B");

        array.setAt(1, "C");

        assertEquals("A", array.get(0));
        assertEquals("C", array.get(1));
    }

    @Test
    void setAtRejectsNegativeAndSizeIndexes() {
        Array<String> array = new Array<>(2);
        array.add("A");
        array.add("B");

        assertThrows(ArrayIndexOutOfBoundsException.class, () -> array.setAt(-1, "X"));
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> array.setAt(2, "X"));
    }

    @Test
    void setAtRejectsNullElement() {
        Array<String> array = new Array<>(1);
        array.add("A");

        assertThrows(NullPointerException.class, () -> array.setAt(0, null));
    }

    @Test
    void iteratorReturnsNullAfterExhaustion() {
        Array<String> array = new Array<>(1);
        array.add("A");
        Iterator<String> iterator = array.iterator();

        assertEquals("A", iterator.next());
        assertFalse(iterator.hasNext());
        assertNull(iterator.next());
    }
}
