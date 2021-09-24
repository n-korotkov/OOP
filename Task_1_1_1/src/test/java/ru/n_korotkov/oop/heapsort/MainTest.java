package ru.n_korotkov.oop.heapsort;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EmptySource;
import org.junit.jupiter.params.provider.MethodSource;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Arrays;
import java.util.stream.Stream;

class MainTest {

    static Stream<int[]> intArrayProvider() {
        return Stream.of(
            new int[]{ 5, 4, 3, 2, 1 },
            new int[]{ 0 },
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
    @EmptySource
    @MethodSource("intArrayProvider")
    void heapSortTest(int[] sampleArray) {
        int[] sampleArraySorted = sampleArray.clone();
        Arrays.sort(sampleArraySorted);

        Main.heapSort(sampleArray);
        assertArrayEquals(sampleArray, sampleArraySorted);
    }

}
