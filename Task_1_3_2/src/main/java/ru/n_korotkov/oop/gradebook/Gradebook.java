package ru.n_korotkov.oop.gradebook;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class Gradebook {

    public enum Grade {
        Passed, Satisfactory, Good, Excellent;

        public static int numericalValue(Grade grade) {
            return switch (grade) {
                case Passed -> 1;
                case Satisfactory -> 3;
                case Good -> 4;
                case Excellent -> 5;
            };
        }
    }

    private final List<Map<String, Grade>> semesterGrades;
    private Grade qualifyingWorkGrade;

    public Gradebook(int semesterCount) {
        semesterGrades = new ArrayList<>(semesterCount);
        for (int i = 0; i < semesterCount; i++) {
            semesterGrades.add(new HashMap<>());
        }
    }

    public void setGrade(int semesterIndex, String discipline, Grade grade) {
        if (semesterIndex <= 0 || semesterIndex > semesterGrades.size()) {
            throw new IllegalArgumentException("semesterIndex out of bounds");
        }

        semesterGrades.get(semesterIndex - 1).put(discipline, grade);
    }

    public void setGrades(int semesterIndex, Map<String, Grade> newGrades) {
        if (semesterIndex <= 0 || semesterIndex > semesterGrades.size()) {
            throw new IllegalArgumentException("semesterIndex out of bounds");
        }

        semesterGrades.get(semesterIndex - 1).putAll(newGrades);
    }

    public void setQualifyingWorkGrade(Grade grade) {
        this.qualifyingWorkGrade = grade;
    }

    public double getAverageGrade() {
        return semesterGrades
                .stream()
                .flatMap(semester -> semester.values().stream())
                .filter(grade -> grade != Grade.Passed)
                .collect(Collectors.averagingInt(Grade::numericalValue));
    }

    public boolean honorsDegree() {
        boolean satPresent = semesterGrades
                .stream()
                .flatMap(semester -> semester.values().stream())
                .anyMatch(grade -> grade == Grade.Satisfactory);

        return !satPresent && getAverageGrade() >= 4.75 && qualifyingWorkGrade == Grade.Excellent;
    }

    public boolean increasedScholarship(int semesterIndex) {
        if (semesterIndex <= 0 || semesterIndex > semesterGrades.size()) {
            throw new IllegalArgumentException("semesterIndex out of bounds");
        }

        if (semesterIndex < 3) return false;
        return semesterGrades
                .subList(semesterIndex - 3, semesterIndex - 1)
                .stream()
                .flatMap(semester -> semester.values().stream())
                .filter(grade -> grade != Grade.Passed)
                .allMatch(grade -> grade == Grade.Excellent);
    }

}
