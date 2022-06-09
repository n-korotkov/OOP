package ru.n_korotkov.oop.primes;

import java.util.List;

public class SerialChecker extends PrimeChecker {

    public boolean containsNonPrime(List<Integer> numbers) {
        return !numbers.stream().allMatch(PrimeChecker::checkPrime);
    }

}
