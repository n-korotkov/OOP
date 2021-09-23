import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MainTest {
    @Test
    void heapSortTest() {
        assertArrayEquals(Main.heapSort(
            new int[]{ 5, 4, 3, 2, 1 }),
            new int[]{ 1, 2, 3, 4, 5 }
        );
        assertArrayEquals(Main.heapSort(
            new int[]{}),
            new int[]{}
        );
        assertArrayEquals(Main.heapSort(
            new int[]{ 0 }),
            new int[]{ 0 }
        );
        assertArrayEquals(Main.heapSort(
            new int[]{ 1, 4, 8, 3, 6, 3, 5, 3, 6, 1, 4, 6, 3, 6, 2, 7 }),
            new int[]{ 1, 1, 2, 3, 3, 3, 3, 4, 4, 5, 6, 6, 6, 6, 7, 8 }
        );
        assertArrayEquals(Main.heapSort(
            new int[]{ 5, 9, 9, 5, 1, 3, 6, 1, 6, 1, 7, 8, 5, 3, 4, 5 }),
            new int[]{ 1, 1, 1, 3, 3, 4, 5, 5, 5, 5, 6, 6, 7, 8, 9, 9 }
        );
        assertArrayEquals(Main.heapSort(
            new int[]{ 8, 5, 1, 1, 3, 1, 3, 1, 4, 6, 5, 6, 6, 5, 9, 2 }),
            new int[]{ 1, 1, 1, 1, 2, 3, 3, 4, 5, 5, 5, 6, 6, 6, 8, 9 }
        );
        assertArrayEquals(Main.heapSort(
            new int[]{ 9, 9, 5, 5, 4, 2, 3, 2, 6, 9, 1, 1, 8, 6, 1, 5 }),
            new int[]{ 1, 1, 1, 2, 2, 3, 4, 5, 5, 5, 6, 6, 8, 9, 9, 9 }
        );
    }
}
