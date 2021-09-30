package ru.n_korotkov.oop.heapsort;

import java.util.Arrays;

public class Main {

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
        System.out.println(Arrays.toString(new MaxHeap(array).sort()));
    }

}
