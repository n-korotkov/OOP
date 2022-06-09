package ru.n_korotkov.oop.primes;

import java.util.List;

public abstract class PrimeChecker {

    public static boolean checkPrime(int x) {
        if (x < 2) return false;
        for (int i = 2; i * i <= x; i++) {
            if (Integer.remainderUnsigned(x, i) == 0) return false;
        }
        return true;
    }

    public abstract boolean containsNonPrime(List<Integer> numbers);

}
