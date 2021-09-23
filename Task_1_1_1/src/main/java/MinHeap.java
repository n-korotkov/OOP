/**
 * An implementation of a binary min-heap.
 * Only supports <i>insert</i> and <i>extract</i> operations.
 */
public class MinHeap {

    private int[] data;
    private int size = 0;

    /**
     * Constructs a heap of fixed capacity.
     * @param capacity
     */
    public MinHeap(int capacity) {
        data = new int[capacity];
    }

    /**
     * The <i>sift-up</i> operation: moves the element at index <code>index</code> up the heap
     * until the heap property is met.
     * @param index
     */
    private void siftUp(int index) {
        int parentIndex = (index - 1) / 2;
        while (parentIndex >= 0 && data[index] < data[parentIndex]) {
            int temp = data[index];
            data[index] = data[parentIndex];
            data[parentIndex] = temp;

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
            if (rightChildIndex <= size && data[leftChildIndex] > data[rightChildIndex]) {
                chosenChildIndex = rightChildIndex;
            }

            if (data[index] > data[chosenChildIndex]) {
                int temp = data[index];
                data[index] = data[chosenChildIndex];
                data[chosenChildIndex] = temp;

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
     * Extracts the min element from the heap.
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
