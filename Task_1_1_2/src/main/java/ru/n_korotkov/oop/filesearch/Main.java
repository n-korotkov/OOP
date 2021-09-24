package ru.n_korotkov.oop.filesearch;

import java.util.ArrayList;
import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.BufferedReader;

public class Main {

    /**
     * Prints a <code>long[]</code> in the form of an array literal.
     * @param arr the <code>long[]</code> to be printed
     */
    public static void printArray(long[] arr) {
        String[] stringArr = new String[arr.length];
        for (int i = 0; i < arr.length; i++) {
            stringArr[i] = Long.toString(arr[i]);
        }
        System.out.printf("{ %s }%n", String.join(", ", stringArr));
    }

    private static final long P = 771645345, M = 1073741789;

    /**
     * Calculates the polynomial rolling hash of <code>s</code>
     * @param s the string to apply hash function to
     * @return the hash value of <code>s</code>
     */
    public static long rollingHash(String s) {
        long hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash = (hash * P + s.codePointAt(i)) % M;
        }
        return hash;
    }

    /**
     * Searches the file <code>filename</code> for all occurrences of <code>searchString</code>.
     * @param filename the path to the file to search through
     * @param searchString the string to search for
     * @return the array of starting positions of <code>searchString</code> entries in the file.
     *         If <code>searchString</code> is empty, an empty array is returned.
     * @throws IOException
     * @throws FileNotFoundException
     */
    public static long[] searchInFile(String filename, String searchString) throws IOException, FileNotFoundException {
        if (searchString.length() == 0) {
            return new long[0];
        }

        final int[] searchChars = searchString.codePoints().toArray();
        final long searchHash = rollingHash(searchString);
        final int searchSize = searchString.length();
        ArrayList<Long> foundSubstrings = new ArrayList<>();

        BufferedReader inputStream = new BufferedReader(new FileReader(filename));
        long fileCursor = 0, substringHash = 0;
        int[] substringChars = new int[searchSize];

        long frontP = 1;
        for (int i = 0; i < searchSize; i++) {
            frontP = (frontP * P) % M;
        }

        int nextChar = inputStream.read();
        while (nextChar != -1) {
            int frontChar = substringChars[(int) (fileCursor % searchSize)];
            substringHash = Long.remainderUnsigned(substringHash * P - frontChar * frontP + nextChar, M);
            substringChars[(int) (fileCursor % searchSize)] = nextChar;
            nextChar = inputStream.read();
            fileCursor++;
            
            if (fileCursor >= searchSize && substringHash == searchHash) {
                boolean stringsEqual = true;
                for (int i = 0; i < searchSize; i++) {
                    int substringIndex = (int) ((fileCursor + i) % searchSize);
                    if (searchChars[i] != substringChars[substringIndex]) {
                        stringsEqual = false;
                        break;
                    }
                }
                if (stringsEqual) {
                    foundSubstrings.add(fileCursor - searchString.length());
                }
            }
        }
        inputStream.close();

        long[] outputArray = new long[foundSubstrings.size()];
        for (int i = 0; i < foundSubstrings.size(); i++) {
            outputArray[i] = foundSubstrings.get(i);
        }

        return outputArray;
    }

    public static void main(String[] args) {
        System.out.println("Stup");
        if (args.length != 2) {
            System.out.println("Usage: java Main <filename> <search-string>");
            return;
        }

        try {
            printArray(searchInFile(args[0], args[1]));
        } catch (FileNotFoundException e) {
            System.err.printf("Error: file '%s' not found%n", args[0]);
        } catch (IOException e) {
            System.err.printf("Error: %s%n", e.getMessage());
        }
    }

}
