package ru.n_korotkov.oop.dsl;

import java.util.List;

import ru.n_korotkov.oop.dsl.model.Student;

public record StudentResult(Student student, boolean evaluated, List<TaskResult> taskResults, float totalScore) {}
