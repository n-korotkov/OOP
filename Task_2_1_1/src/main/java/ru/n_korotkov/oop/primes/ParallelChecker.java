package ru.n_korotkov.oop.primes;

import java.util.List;

public class ParallelChecker extends PrimeChecker {

    public boolean containsNonPrime(List<Integer> numbers) {
        return !numbers.parallelStream().allMatch(PrimeChecker::checkPrime);
    }

}
