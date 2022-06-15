package ru.n_korotkov.oop.dsl;

import ru.n_korotkov.oop.dsl.model.Task;

public record TaskResult(Task task, boolean evaluated, boolean buildSuccessful, boolean testSuccessful, float score) {}
