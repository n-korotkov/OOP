package ru.n_korotkov.oop.dsl;

public record TestResult(boolean buildSuccessful, int testsPassed, int testsFailed, int testsSkipped) {}
