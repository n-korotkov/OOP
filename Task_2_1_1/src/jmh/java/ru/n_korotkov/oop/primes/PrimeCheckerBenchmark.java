package ru.n_korotkov.oop.primes;

import java.math.BigInteger;
import java.util.List;
import java.util.Random;
import java.util.function.Supplier;
import java.util.stream.Stream;

import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@BenchmarkMode(Mode.AverageTime)
@Warmup(iterations = 1, time = 2)
@Measurement(iterations = 3, time = 2)
@Fork(1)
public class PrimeCheckerBenchmark {

    @State(Scope.Benchmark)
    public static class BenchmarkState {

        private final int size = 10000;
        private final int seed = 0xDEADBEEF;
        private final int bitLength = 30;

        List<Integer> numbers;

        @Setup(Level.Trial)
        public void setup() {
            Random rng = new Random(seed);
            Supplier<Integer> primeSupplier = (() ->
                BigInteger.probablePrime(bitLength, rng).intValue()
            );

            this.numbers = Stream.generate(primeSupplier)
                .limit(size)
                .toList();
        }

    }

    @State(Scope.Benchmark)
    public static class MultithreadedState extends BenchmarkState {

        @Param({"1", "2", "3", "4", "5", "6", "7", "8", "9", "10", "11", "12", "13", "14", "15", "16"})
        int cores;

    }

    @Benchmark
    public void measureSerial(BenchmarkState state, Blackhole bh) {
        PrimeChecker checker = new SerialChecker();
        bh.consume(checker.containsNonPrime(state.numbers));
    }

    @Benchmark
    public void measureParallel(BenchmarkState state, Blackhole bh) {
        PrimeChecker checker = new ParallelChecker();
        bh.consume(checker.containsNonPrime(state.numbers));
    }

    @Benchmark
    public void measureMultithreaded(MultithreadedState state, Blackhole bh) {
        PrimeChecker checker = new MultithreadedChecker(state.cores);
        bh.consume(checker.containsNonPrime(state.numbers));
    }

}
