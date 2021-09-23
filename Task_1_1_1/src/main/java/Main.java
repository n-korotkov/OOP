public class Main {

    /**
     * Prints an <code>int[]</code> in the form of an array literal.
     * @param arr the <code>int[]</code> to be printed
     */
    public static void printArray(int[] arr) {
        System.out.print("{");
        if (arr.length > 0) {
            System.out.printf(" %d", arr[0]);
        }
        for (int i = 1; i < arr.length; i++) {
            System.out.printf(", %d", arr[i]);
        }
        System.out.printf(" }%n");
    }

    /**
     * Sorts <code>inputArray</code> in ascending order and returns the resulting array.
     * <code>inputArray</code> remains unchanged.
     * @param inputArray the array to be sorted
     * @return the sorted array
     */
    public static int[] heapSort(int[] inputArray) {
        MinHeap heap = new MinHeap(inputArray.length);
        for (int i : inputArray) {
            heap.insert(i);
        }
        int[] outputArray = new int[inputArray.length];
        for (int i = 0; i < inputArray.length; i++) {
            outputArray[i] = heap.extract();
        }
        return outputArray;
    }

    public static void main(String[] args) {
        int[] array = new int[args.length];
        try {
            for (int i = 0; i < args.length; i++) {
                array[i] = Integer.parseInt(args[i]);
            }
        } catch (NumberFormatException e) {
            System.err.println("Error: could not process parameters as integers");
            return;
        }
        int[] sortedArray = heapSort(array);
        printArray(sortedArray);
    }

}