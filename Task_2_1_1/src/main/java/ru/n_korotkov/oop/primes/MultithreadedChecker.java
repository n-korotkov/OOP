package ru.n_korotkov.oop.primes;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

public class MultithreadedChecker extends PrimeChecker {

    int threads;

    public MultithreadedChecker(int threads) {
        this.threads = threads;
    }

    public boolean containsNonPrime(List<Integer> numbers) {
        if (numbers.isEmpty())
            return false;

        AtomicBoolean nonPrimeFound = new AtomicBoolean(false);

        ExecutorService execService = Executors.newFixedThreadPool(threads);
        for (int i = 0; i < threads; i++) {
            int taskN = i;
            execService.execute(() -> {
                for (int index = taskN; index < numbers.size() && !nonPrimeFound.get(); index += threads) {
                    boolean isPrime = checkPrime(numbers.get(index));
                    if (!isPrime) {
                        nonPrimeFound.set(true);
                    }
                }
            });
        }

        try {
            execService.shutdown();
            execService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        return nonPrimeFound.get();
    }

}
