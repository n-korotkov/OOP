package ru.n_korotkov.oop.primes;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultithreadedChecker extends PrimeChecker {

    private class State {
        private int finishedTasks = 0;
        private boolean result = false;
    }

    int threads;

    public MultithreadedChecker(int threads) {
        this.threads = threads;
    }

    public boolean containsNonPrime(List<Integer> numbers) {
        if (numbers.isEmpty())
            return false;

        State state = new State();

        ExecutorService execService = Executors.newFixedThreadPool(threads);
        var taskStream = numbers
            .stream()
            .map(x -> (Runnable)(() -> {
                boolean isPrime = checkPrime(x);
                synchronized (state) {
                    state.result |= !isPrime;
                    state.finishedTasks++;
                    if (!isPrime || state.finishedTasks == numbers.size()) {
                        state.notify();
                    }
                }
            }));

        synchronized (state) {
            try {
                taskStream.forEach(execService::execute);
                state.wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

            execService.shutdownNow();
            return state.result;
        }
    }

}
