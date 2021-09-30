package ru.n_korotkov.oop.filesearch;

import java.io.IOException;
import java.io.Reader;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Random;

/**
 * A class implementing the Rabin-Karp string search algorithm.
 */
public class RabinKarpStringFinder {

    private long hashMultiplier, hashModulus;

    /**
     * Constructs a <code>RabinKarpStringFinder</code> with randomly generated polynomial rolling
     * hash parameters.
     */
    public RabinKarpStringFinder() {
        Random rng = new Random();
        this.hashMultiplier = BigInteger.probablePrime(30, rng).longValue();
        this.hashModulus    = BigInteger.probablePrime(31, rng).longValue();
    }

    /**
     * Calculates <code>hashMultiplier</code> raised to the power of <code>exponent</code> modulo
     * <code>hashModulus</code>.
     * @param exponent the power to raise to. Must not be negative.
     * @return <i>(hashMultiplier ^ exponent) mod hashModulus</i>
     * @throws IllegalArgumentException if <code>exponent < 0</code>
     */
    private long hashMultiplierPow(int exponent) throws IllegalArgumentException {
        if (exponent < 0)
            throw new IllegalArgumentException("The exponent must not be negative");

        long multPow = 1;
        for (int i = 0; i < exponent; i++) {
            multPow = (multPow * hashMultiplier) % hashModulus;
        }
        return multPow;
    }

    /**
     * Calculates the polynomial hash of <code>s</code>.
     * @param s the <code>String</code> to hash
     * @return the hash value
     */
    private long polynomialHash(String s) {
        long hash = 0;
        for (int i = 0; i < s.length(); i++) {
            hash = (hash * hashMultiplier + s.codePointAt(i)) % hashModulus;
        }
        return hash;
    }

    /**
     * Advances the hash value by removing <code>frontChar</code> from the hashing window and
     * inserting <code>nextChar</code>.
     * @param oldHash the hash to advance
     * @param frontCharMultiplier the multiplier applied to <code>frontChar</code> --
     *                            should be <i>hashMultiplier ^ windowLength</i>
     * @param frontChar the first character in the window
     * @param nextChar the next character after the window
     * @return the new hash value
     */
    private long advancePolynomialHash(long oldHash, long frontCharMultiplier, int frontChar, int nextChar) {
        return (oldHash * hashMultiplier + (hashModulus - frontChar) * frontCharMultiplier + nextChar) % hashModulus;
    }

    /**
     * Searches the contents of <code>inputReader</code> using the Rabin-Karp algorithm.
     * @param inputReader the <code>Reader</code> to search through. Must not be <code>null</code>.
     * @param searchString the <code>String</code> to search for. Must not be <code>null</code> or empty.
     * @return the <code>ArrayList&lt;Long&gt;</code> containing all occurrences of <code>searchString</code>
     * @throws IOException if an I/O error occurs
     * @throws IllegalArgumentException if <code>inputReader == null || searchString == null ||
     *                                  searchString.isEmpty()</code>
     */
    public ArrayList<Long> search(Reader inputReader, String searchString) throws IOException, IllegalArgumentException {
        if (inputReader == null)
            throw new IllegalArgumentException("inputReader must not be null");

        if (searchString == null)
            throw new IllegalArgumentException("searchString must not be null");

        if (searchString.isEmpty())
            throw new IllegalArgumentException("searchString must not be empty");

        final int[] searchChars = searchString.codePoints().toArray();
        final long searchHash = polynomialHash(searchString);
        final int searchSize = searchString.length();
        ArrayList<Long> foundSubstrings = new ArrayList<>();

        long fileCursor = 0, substringHash = 0;
        int[] substringChars = new int[searchSize];
        long frontCharMultiplier = hashMultiplierPow(searchSize);

        int nextChar = inputReader.read();
        while (nextChar != -1) {
            int frontCharIndex = (int) (fileCursor % searchSize);
            int frontChar = substringChars[frontCharIndex];
            substringHash = advancePolynomialHash(substringHash, frontCharMultiplier, frontChar, nextChar);
            substringChars[frontCharIndex] = nextChar;
            nextChar = inputReader.read();
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

        return foundSubstrings;
    }

}
