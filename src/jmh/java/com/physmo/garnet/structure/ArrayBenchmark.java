package com.physmo.garnet.structure;

import org.openjdk.jmh.annotations.Benchmark;
import org.openjdk.jmh.annotations.BenchmarkMode;
import org.openjdk.jmh.annotations.Fork;
import org.openjdk.jmh.annotations.Level;
import org.openjdk.jmh.annotations.Measurement;
import org.openjdk.jmh.annotations.Mode;
import org.openjdk.jmh.annotations.OutputTimeUnit;
import org.openjdk.jmh.annotations.Param;
import org.openjdk.jmh.annotations.Scope;
import org.openjdk.jmh.annotations.Setup;
import org.openjdk.jmh.annotations.State;
import org.openjdk.jmh.annotations.Warmup;
import org.openjdk.jmh.infra.Blackhole;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.function.Predicate;

/**
 * JMH benchmarks for {@link Array}. Run with:
 * <pre>{@code
 * mvn -Pbenchmarks -DskipTests package
 * java -jar target/benchmarks.jar ArrayBenchmark -prof gc -wi 5 -i 10 -f 3
 * }</pre>
 */
@BenchmarkMode(Mode.AverageTime)
@OutputTimeUnit(TimeUnit.NANOSECONDS)
@Warmup(iterations = 3, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Measurement(iterations = 5, time = 200, timeUnit = TimeUnit.MILLISECONDS)
@Fork(1)
public class ArrayBenchmark {

    @Benchmark
    public Array<Integer> currentAddPresized(ArrayData data) {
        Array<Integer> array = new Array<>(data.size);
        for (int i = 0; i < data.size; i++) {
            array.add(data.values[i]);
        }
        return array;
    }

    @Benchmark
    public LegacyArray<Integer> legacyAddPresized(ArrayData data) {
        LegacyArray<Integer> array = new LegacyArray<>(data.size);
        for (int i = 0; i < data.size; i++) {
            array.add(data.values[i]);
        }
        return array;
    }

    @Benchmark
    public ArrayList<Integer> arrayListAddPresized(ArrayData data) {
        ArrayList<Integer> list = new ArrayList<>(data.size);
        for (int i = 0; i < data.size; i++) {
            list.add(data.values[i]);
        }
        return list;
    }

    @Benchmark
    public Array<Integer> currentAddWithResize(ArrayData data) {
        Array<Integer> array = new Array<>(1);
        for (int i = 0; i < data.size; i++) {
            array.add(data.values[i]);
        }
        return array;
    }

    @Benchmark
    public LegacyArray<Integer> legacyAddWithResize(ArrayData data) {
        LegacyArray<Integer> array = new LegacyArray<>(1);
        for (int i = 0; i < data.size; i++) {
            array.add(data.values[i]);
        }
        return array;
    }

    @Benchmark
    public int currentClearAndReuse(ArrayData data) {
        data.currentScratch.clear();
        for (int i = 0; i < data.size; i++) {
            data.currentScratch.add(data.values[i]);
        }
        return data.currentScratch.size();
    }

    @Benchmark
    public int legacyClearAndReuse(ArrayData data) {
        data.legacyScratch.clear();
        for (int i = 0; i < data.size; i++) {
            data.legacyScratch.add(data.values[i]);
        }
        return data.legacyScratch.size();
    }

    @Benchmark
    public int currentIndexedLoop(ArrayData data) {
        int sum = 0;
        for (int i = 0; i < data.currentFull.size(); i++) {
            sum += data.currentFull.get(i);
        }
        return sum;
    }

    @Benchmark
    public int currentDirectIndexedLoop(ArrayData data) {
        int sum = 0;
        for (int i = 0; i < data.currentFullSize; i++) {
            sum += (Integer) data.currentFullBacking[i];
        }
        return sum;
    }

    @Benchmark
    public int legacyIndexedLoop(ArrayData data) {
        int sum = 0;
        for (int i = 0; i < data.legacyFull.size(); i++) {
            sum += data.legacyFull.get(i);
        }
        return sum;
    }

    @Benchmark
    public int arrayListIndexedLoop(ArrayData data) {
        int sum = 0;
        for (int i = 0; i < data.arrayListFull.size(); i++) {
            sum += data.arrayListFull.get(i);
        }
        return sum;
    }

    @Benchmark
    public int currentEnhancedForLoop(ArrayData data) {
        int sum = 0;
        for (Integer value : data.currentFull) {
            sum += value;
        }
        return sum;
    }

    @Benchmark
    public int legacyEnhancedForLoop(ArrayData data) {
        int sum = 0;
        for (Integer value : data.legacyFull) {
            sum += value;
        }
        return sum;
    }

    @Benchmark
    public boolean currentContainsHit(ArrayData data) {
        return data.currentFull.contains(data.hitValue);
    }

    @Benchmark
    public boolean legacyContainsHit(ArrayData data) {
        return data.legacyFull.contains(data.hitValue);
    }

    @Benchmark
    public boolean arrayListContainsHit(ArrayData data) {
        return data.arrayListFull.contains(data.hitValue);
    }

    @Benchmark
    public boolean currentContainsMiss(ArrayData data) {
        return data.currentFull.contains(data.missValue);
    }

    @Benchmark
    public boolean legacyContainsMiss(ArrayData data) {
        return data.legacyFull.contains(data.missValue);
    }

    @Benchmark
    public boolean arrayListContainsMiss(ArrayData data) {
        return data.arrayListFull.contains(data.missValue);
    }

    @Benchmark
    public int currentIndexOfHit(ArrayData data) {
        return data.currentFull.indexOf(data.hitValue);
    }

    @Benchmark
    public int legacyIndexOfHit(ArrayData data) {
        return data.legacyFull.indexOf(data.hitValue);
    }

    @Benchmark
    public int arrayListIndexOfHit(ArrayData data) {
        return data.arrayListFull.indexOf(data.hitValue);
    }

    @Benchmark
    public int currentIndexOfMiss(ArrayData data) {
        return data.currentFull.indexOf(data.missValue);
    }

    @Benchmark
    public int legacyIndexOfMiss(ArrayData data) {
        return data.legacyFull.indexOf(data.missValue);
    }

    @Benchmark
    public int arrayListIndexOfMiss(ArrayData data) {
        return data.arrayListFull.indexOf(data.missValue);
    }

    @Benchmark
    public int currentRemoveIfNone(RemoveIfData data) {
        data.populateCurrent();
        data.current.removeIf(data.removeNone);
        return data.current.size();
    }

    @Benchmark
    public int legacyRemoveIfNone(RemoveIfData data) {
        data.populateLegacy();
        data.legacy.removeIf(data.removeNone);
        return data.legacy.size();
    }

    @Benchmark
    public int currentRemoveIfSparse(RemoveIfData data) {
        data.populateCurrent();
        data.current.removeIf(data.removeSparse);
        return data.current.size();
    }

    @Benchmark
    public int legacyRemoveIfSparse(RemoveIfData data) {
        data.populateLegacy();
        data.legacy.removeIf(data.removeSparse);
        return data.legacy.size();
    }

    @Benchmark
    public int currentRemoveIfDense(RemoveIfData data) {
        data.populateCurrent();
        data.current.removeIf(data.removeDense);
        return data.current.size();
    }

    @Benchmark
    public int legacyRemoveIfDense(RemoveIfData data) {
        data.populateLegacy();
        data.legacy.removeIf(data.removeDense);
        return data.legacy.size();
    }

    @Benchmark
    public Array<Integer> currentAddAllArray(ArrayData data) {
        Array<Integer> target = new Array<>(data.size);
        target.addAll(data.currentFull);
        return target;
    }

    @Benchmark
    public LegacyArray<Integer> legacyAddAllArray(ArrayData data) {
        LegacyArray<Integer> target = new LegacyArray<>(data.size);
        target.addAll(data.legacyFull);
        return target;
    }

    @Benchmark
    public Array<Integer> currentAddAllList(ArrayData data) {
        Array<Integer> target = new Array<>(data.size);
        target.addAll(data.arrayListFull);
        return target;
    }

    @Benchmark
    public ArrayList<Integer> arrayListAddAllList(ArrayData data) {
        ArrayList<Integer> target = new ArrayList<>(data.size);
        target.addAll(data.arrayListFull);
        return target;
    }

    @Benchmark
    public Array<Integer> currentSort(ArrayData data) {
        Array<Integer> array = new Array<>(data.size);
        for (int i = data.size - 1; i >= 0; i--) {
            array.add(data.values[i]);
        }
        array.sort(Comparator.naturalOrder());
        return array;
    }

    @Benchmark
    public LegacyArray<Integer> legacySort(ArrayData data) {
        LegacyArray<Integer> array = new LegacyArray<>(data.size);
        for (int i = data.size - 1; i >= 0; i--) {
            array.add(data.values[i]);
        }
        array.sort(Comparator.naturalOrder());
        return array;
    }

    @Benchmark
    public void currentIteratorAllocationOnly(ArrayData data, Blackhole blackhole) {
        blackhole.consume(data.currentFull.iterator());
    }

    @Benchmark
    public void legacyIteratorAllocationOnly(ArrayData data, Blackhole blackhole) {
        blackhole.consume(data.legacyFull.iterator());
    }

    @Benchmark
    public int garnetArrayFrameWorkload(FrameWorkloadData data) {
        int checksum = 0;
        Array<Integer> array = data.current;
        Object[] backing = data.currentBacking;

        for (int frame = 0; frame < data.frames; frame++) {
            data.removeOffset = frame;
            array.clear();

            for (int i = 0; i < data.size; i++) {
                array.add(data.values[(i + frame) & data.valueMask]);
            }

            for (int i = 0; i < array.size; i++) {
                checksum += (Integer) backing[i];
            }

            array.removeIf(data.removeSparse);

            for (int i = 0; i < array.size; i++) {
                Integer value = (Integer) backing[i];
                checksum ^= value;
                checksum += value * 31;
            }

            if (array.contains(data.hitValue)) {
                checksum++;
            }
            checksum += array.indexOf(data.missValue);
        }

        return checksum;
    }

    @Benchmark
    public int arrayListFrameWorkload(FrameWorkloadData data) {
        int checksum = 0;
        ArrayList<Integer> list = data.arrayList;

        for (int frame = 0; frame < data.frames; frame++) {
            data.removeOffset = frame;
            list.clear();

            for (int i = 0; i < data.size; i++) {
                list.add(data.values[(i + frame) & data.valueMask]);
            }

            for (int i = 0; i < list.size(); i++) {
                checksum += list.get(i);
            }

            list.removeIf(data.removeSparse);

            for (int i = 0; i < list.size(); i++) {
                Integer value = list.get(i);
                checksum ^= value;
                checksum += value * 31;
            }

            if (list.contains(data.hitValue)) {
                checksum++;
            }
            checksum += list.indexOf(data.missValue);
        }

        return checksum;
    }

    @State(Scope.Thread)
    public static class ArrayData {
        @Param({"128", "4096"})
        int size;

        Integer[] values;
        Integer hitValue;
        Integer missValue;
        Array<Integer> currentFull;
        LegacyArray<Integer> legacyFull;
        ArrayList<Integer> arrayListFull;
        Object[] currentFullBacking;
        int currentFullSize;
        Array<Integer> currentScratch;
        LegacyArray<Integer> legacyScratch;

        @Setup(Level.Trial)
        public void setUp() {
            values = new Integer[size];
            for (int i = 0; i < size; i++) {
                values[i] = i;
            }
            hitValue = values[size / 2];
            missValue = -1;
            currentFull = new Array<>(size);
            legacyFull = new LegacyArray<>(size);
            arrayListFull = new ArrayList<>(size);
            currentScratch = new Array<>(size);
            legacyScratch = new LegacyArray<>(size);
            for (Integer value : values) {
                currentFull.add(value);
                legacyFull.add(value);
                arrayListFull.add(value);
            }
            Array<?> rawCurrentFull = currentFull;
            currentFullBacking = rawCurrentFull.array;
            currentFullSize = currentFull.size;
        }
    }

    @State(Scope.Thread)
    public static class FrameWorkloadData {
        @Param({"512", "4096"})
        int size;

        @Param({"640"})
        int frames;

        Integer[] values;
        int valueMask;
        Integer hitValue;
        Integer missValue;
        Array<Integer> current;
        Object[] currentBacking;
        ArrayList<Integer> arrayList;
        int removeOffset;
        Predicate<Integer> removeSparse = value -> ((value + removeOffset) & 15) == 0;

        @Setup(Level.Trial)
        public void setUp() {
            int valueCount = 1;
            while (valueCount < size + frames) {
                valueCount <<= 1;
            }
            valueMask = valueCount - 1;
            values = new Integer[valueCount];
            for (int i = 0; i < valueCount; i++) {
                values[i] = i;
            }
            hitValue = values[valueCount / 2];
            missValue = -1;
            current = new Array<>(size);
            currentBacking = ((Array<?>) current).array;
            arrayList = new ArrayList<>(size);
        }
    }

    @State(Scope.Thread)
    public static class RemoveIfData {
        @Param({"128", "4096"})
        int size;

        Integer[] values;
        Array<Integer> current;
        LegacyArray<Integer> legacy;
        Predicate<Integer> removeNone = value -> value < 0;
        Predicate<Integer> removeSparse = value -> value % 16 == 0;
        Predicate<Integer> removeDense = value -> value % 2 == 0;

        @Setup(Level.Trial)
        public void setUpTrial() {
            values = new Integer[size];
            for (int i = 0; i < size; i++) {
                values[i] = i;
            }
            current = new Array<>(size);
            legacy = new LegacyArray<>(size);
        }

        void populateCurrent() {
            current.clear();
            for (Integer value : values) {
                current.add(value);
            }
        }

        void populateLegacy() {
            legacy.clear();
            for (Integer value : values) {
                legacy.add(value);
            }
        }
    }

    /**
     * Copy of the pre-correctness-fix Array behavior for before/after benchmark
     * comparisons. Keep this class local to benchmarks; production code should use
     * {@link Array}.
     */
    public static class LegacyArray<T> implements Iterable<T> {
        public T[] array;
        public int size;

        public LegacyArray(int capacity) {
            array = (T[]) new Object[capacity];
            size = 0;
        }

        private void doubleArrayCapacity() {
            T[] newArray = (T[]) new Object[array.length * 2];
            System.arraycopy(array, 0, newArray, 0, array.length);
            array = newArray;
        }

        public void clear() {
            for (int i = 0; i < size; i++) {
                array[i] = null;
            }
            size = 0;
        }

        public boolean removeIf(Predicate<? super T> filter) {
            boolean removed = false;
            int readPos = 0;
            int writePos = 0;
            for (int i = 0; i < size; i++) {
                if (filter.test(array[i])) {
                    readPos++;
                    removed = true;
                    continue;
                }
                array[writePos++] = array[readPos++];
            }
            size = writePos;
            return removed;
        }

        public void addAll(List<T> list) {
            for (int i = 0; i < list.size(); i++) add(list.get(i));
        }

        public void addAll(LegacyArray<T> list) {
            for (int i = 0; i < list.size(); i++) add(list.get(i));
        }

        public void add(T element) {
            if (size == array.length) doubleArrayCapacity();
            array[size++] = element;
        }

        public int size() {
            return size;
        }

        public void sort(Comparator<T> comparator) {
            Arrays.sort(array, 0, size, comparator);
        }

        public boolean contains(T element) {
            for (int i = 0; i < size; i++) {
                if (array[i].equals(element)) return true;
            }
            return false;
        }

        public int indexOf(T element) {
            for (int i = 0; i < size; i++) {
                if (array[i].equals(element)) return i;
            }
            return -1;
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
}
