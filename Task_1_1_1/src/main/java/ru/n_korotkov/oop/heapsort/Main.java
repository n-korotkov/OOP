package ru.n_korotkov.oop.heapsort;

public class Main {

    /**
     * Prints an <code>int[]</code> in the form of an array literal.
     * @param arr the <code>int[]</code> to be printed
     */
    public static void printArray(int[] arr) {
        String[] stringArr = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            stringArr[i] = Integer.toString(arr[i]);
        }
        System.out.printf("{ %s }%n", String.join(", ", stringArr));
    }

    /**
     * Sorts <code>inputArray</code> in ascending order.
     * @param inputArray the array to be sorted
     */
    public static void heapSort(int[] inputArray) {
        MaxHeap heap = new MaxHeap(inputArray);
        for (int i = inputArray.length - 1; i >= 0; i--) {
            inputArray[i] = heap.extract();
        }
    }

    public static void main(String[] args) {
        int[] array = new int[args.length];
        try {
            for (int i = 0; i < args.length; i++) {
                array[i] = Integer.parseInt(args[i]);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: could not process parameter as integer");
            System.err.println(e.getMessage());
            return;
        }
        heapSort(array);
        printArray(array);
    }

}
