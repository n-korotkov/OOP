package ru.n_korotkov.oop.heapsort;

/**
 * An implementation of a binary max-heap.
 * Only supports <i>insert</i> and <i>extract</i> operations.
 */
public class MaxHeap {

    private int[] data;
    private int size = 0;

    /**
     * Constructs a heap of fixed capacity.
     * @param capacity
     */
    public MaxHeap(int capacity) {
        data = new int[capacity];
    }

    /**
     * Constructs a heap from <code>arr</code>.
     * The resulting <code>MaxHeap</code> contains all the elements of <code>arr</code> and
     * occupies <code>arr</code> to store them.
     * @param arr the array to heapify
     */
    public MaxHeap(int[] arr) {
        data = arr;
        size = arr.length;
        for (int index = size / 2 - 1; index >= 0; index--) {
            siftDown(index);
        }
    }

    /**
     * Swaps elements at indices <code>index1</code> and <code>index2</code>.
     * @param index1
     * @param index2
     */
    private void swap(int index1, int index2) {
        int temp = data[index1];
        data[index1] = data[index2];
        data[index2] = temp;
    }

    /**
     * The <i>sift-up</i> operation: moves the element at index <code>index</code> up the heap
     * until the heap property is met.
     * @param index
     */
    private void siftUp(int index) {
        int parentIndex = (index - 1) / 2;
        while (parentIndex >= 0 && data[index] > data[parentIndex]) {
            swap(index, parentIndex);
            index = parentIndex;
            parentIndex = (index - 1) / 2;
        }
    }

    /**
     * The <i>sift-down</i> operation: moves the element at index <code>index</code> down the heap
     * until the heap property is met.
     * @param index
     */
    private void siftDown(int index) {
        while (index * 2 + 1 < size) {
            int leftChildIndex = index * 2 + 1, rightChildIndex = index * 2 + 2;
            int chosenChildIndex = leftChildIndex;
            if (rightChildIndex < size && data[leftChildIndex] < data[rightChildIndex]) {
                chosenChildIndex = rightChildIndex;
            }

            if (data[index] < data[chosenChildIndex]) {
                swap(index, chosenChildIndex);
                index = chosenChildIndex;
            } else {
                return;
            }
        }
    }

    /**
     * Inserts a new element into the heap.
     * @param value
     */
    public void insert(int value) {
        data[size] = value;
        siftUp(size);
        size++;
    }

    /**
     * Extracts the max element from the heap.
     * @return the extracted element
     */
    public int extract() {
        size--;
        int value = data[0];
        data[0] = data[size];
        siftDown(0);
        return value;
    }

}
