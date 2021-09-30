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
        if (capacity < 0)
            throw new IllegalArgumentException("Capacity must not be negative");

        data = new int[capacity];
    }

    /**
     * Constructs a heap containing all the elements of <code>arr</code>.
     * @param arr the array to heapify
     */
    public MaxHeap(int[] arr) {
        if (arr == null)
            throw new IllegalArgumentException("Input array must not be null");

        data = arr.clone();
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
        if (size == data.length)
            throw new IllegalStateException("This heap is full");

        data[size] = value;
        siftUp(size);
        size++;
    }

    /**
     * Extracts the max element from the heap.
     * @return the extracted element
     */
    public int extract() {
        if (size == 0)
            throw new IllegalStateException("This heap is empty");

        size--;
        int value = data[0];
        data[0] = data[size];
        siftDown(0);
        return value;
    }

    /**
     * Returns the sorted array holding the elements of this heap.
     * The heap should not be used afterwards.
     * @return the sorted array containing all the elements of this heap
     */
    public int[] sort() {
        while (size > 0) {
            size--;
            swap(0, size);
            siftDown(0);
        }
        return data;
    }

}
