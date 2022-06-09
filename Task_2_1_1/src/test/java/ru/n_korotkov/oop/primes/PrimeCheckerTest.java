package ru.n_korotkov.oop.primes;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.ValueSource;

public class PrimeCheckerTest {

    static Stream<List<Integer>> listOfPrimesProvider() {
        return Stream.of(
            List.of(),
            List.of(2),
            List.of(2, 3, 5),
            List.of(2, 1_000_000_007, 1_000_000_009, 3, 97, 5),
            List.of(6997901, 6997927, 6997937, 6997967, 6998009, 6998029, 6998039, 6998051, 6998053)
        );
    }

    @ParameterizedTest
    @ValueSource(ints = { 2, 3, 5, 97, 1_000_000_007, 1_000_000_009 })
    void checkPrimeTest(int x) {
        assertTrue(PrimeChecker.checkPrime(x));
    }

    @ParameterizedTest
    @ValueSource(ints = { 0, 1, 4, 6, 9, 200, 841, 111_111_111 })
    void checkNonPrimeTest(int x) {
        assertFalse(PrimeChecker.checkPrime(x));
    }

    void primeCheckerTest(PrimeChecker checker, List<Integer> primes) {
        List<Integer> primesWithCompound = new ArrayList<>(primes);
        primesWithCompound.add(100);
        assertFalse(checker.containsNonPrime(primes));
        assertTrue(checker.containsNonPrime(primesWithCompound));
    }

    @ParameterizedTest
    @MethodSource("listOfPrimesProvider")
    void serialCheckerTest(List<Integer> primes) {
        primeCheckerTest(new SerialChecker(), primes);
    }

    @ParameterizedTest
    @MethodSource("listOfPrimesProvider")
    void parallelCheckerTest(List<Integer> primes) {
        primeCheckerTest(new ParallelChecker(), primes);
    }

    @ParameterizedTest
    @MethodSource("listOfPrimesProvider")
    void multithreadedCheckerTest(List<Integer> primes) {
        primeCheckerTest(new MultithreadedChecker(8), primes);
    }

}
