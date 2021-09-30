package ru.n_korotkov.oop.heapsort;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.Stream;

class MaxHeapTest {

    static Stream<int[]> intArrayProvider() {
        return Stream.of(
            new int[]{ 5, 4, 3, 2, 1 },
            new int[]{ },
            new int[]{ 0 },
            new int[]{ Integer.MAX_VALUE, Integer.MIN_VALUE, Integer.MAX_VALUE },
            new int[]{ 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1, 1 },
            new int[]{ 1, 4, 8, 3, 6, 3, 5, 3, 6, 1, 4, 6, 3, 6, 2, 7 },
            new int[]{ 5, 9, 9, 5, 1, 3, 6, 1, 6, 1, 7, 8, 5, 3, 4, 5 },
            new int[]{ 8, 5, 1, 1, 3, 1, 3, 1, 4, 6, 5, 6, 6, 5, 9, 2 },
            new int[]{ 9, 9, 5, 5, 4, 2, 3, 2, 6, 9, 1, 1, 8, 6, 1, 5 },
            new int[]{
                7, 3, 6, 3, 6, 4, 7, 9, 1, 4, 2, 4, 5, 4, 3, 2,
                4, 6, 2, 8, 4, 7, 2, 5, 7, 4, 7, 8, 3, 7, 5, 3,
                6, 3, 5, 3, 7
            }
        );
    }

    @ParameterizedTest
    @MethodSource("intArrayProvider")
    void heapSortTest(int[] sampleArray) {
        int[] sampleArraySorted = sampleArray.clone();
        Arrays.sort(sampleArraySorted);

        int[] sampleArrayHeapSorted = new MaxHeap(sampleArray).sort();
        assertArrayEquals(sampleArrayHeapSorted, sampleArraySorted);
    }

    @Test
    void heapIllegalCapacityTest() {
        assertThrows(IllegalArgumentException.class, () -> new MaxHeap(-10));
    }

    @Test
    void heapIllegalInputArrayTest() {
        assertThrows(IllegalArgumentException.class, () -> new MaxHeap(null));
    }

    @Test
    void heapZeroOverflowTest() {
        MaxHeap heapZero = new MaxHeap(0);
        assertThrows(IllegalStateException.class, () -> heapZero.insert(0));
    }
    
    @Test
    void heapFromArrayOverflowTest() {
        int[] sampleArray = { 1, 2, 3 };
        MaxHeap heapFromArray = new MaxHeap(sampleArray);
        assertThrows(IllegalStateException.class, () -> heapFromArray.insert(4));
    }

    @Test
    void heapFromCapacityOverflowTest() {
        int capacity = 3;
        MaxHeap heapFromCapacity = new MaxHeap(capacity);
        assertThrows(IllegalStateException.class, () -> {
            for (int i = 0; i <= capacity; i++) {
                heapFromCapacity.insert(0);
            }
        });
    }

    @Test
    void heapZeroUnderflowTest() {
        MaxHeap heapZero = new MaxHeap(0);
        assertThrows(IllegalStateException.class, () -> heapZero.extract());
    }

    @Test
    void heapFromArrayUnderflowTest() {
        int[] sampleArray = { 1, 2, 3 };
        MaxHeap heapFromArray = new MaxHeap(sampleArray);
        assertThrows(IllegalStateException.class, () -> {
            for (int i = 0; i <= sampleArray.length; i++) {
                heapFromArray.extract();
            }
        });
    }

    @Test
    void heapFromCapacityUnderflowTest() {
        int capacity = 3;
        MaxHeap heapFromCapacity = new MaxHeap(capacity);
        assertThrows(IllegalStateException.class, () -> heapFromCapacity.extract());
    }

}
