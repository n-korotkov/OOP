package ru.n_korotkov.oop.gradebook;

import java.util.Map;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

import ru.n_korotkov.oop.gradebook.Gradebook.Grade;

import static org.junit.jupiter.api.Assertions.*;

class GradebookTest {

    static Gradebook authorsGradebook;

    @BeforeAll
    static void initAuthorsGradebook() {
        authorsGradebook = new Gradebook(8);
        Map<String, Grade> firstSemesterGrades = Map.of(
            "Основы культуры речи",                                     Grade.Excellent,
            "Декларативное программирование",                           Grade.Excellent,
            "Цифровые платформы",                                       Grade.Passed,
            "Иностранный язык",                                         Grade.Passed,
            "История",                                                  Grade.Good,
            "Императивное программирование",                            Grade.Excellent,
            "Введение в алгебру и анализ",                              Grade.Excellent,
            "Введение в дискретную математику и математическую логику", Grade.Excellent,
            "Физическая культура и спорт",                              Grade.Passed,
            "Физическая культура и спорт (элективная дисциплина)",      Grade.Passed
        );
        Map<String, Grade> secondSemesterGrades = Map.of(
            "Декларативное программирование",                           Grade.Excellent,
            "Цифровые платформы",                                       Grade.Excellent,
            "Иностранный язык",                                         Grade.Good,
            "Императивное программирование",                            Grade.Excellent,
            "Введение в алгебру и анализ",                              Grade.Excellent,
            "Введение в дискретную математику и математическую логику", Grade.Excellent,
            "Измерительный практикум",                                  Grade.Passed,
            "Физическая культура и спорт",                              Grade.Passed,
            "Физическая культура и спорт (элективная дисциплина)",      Grade.Passed
        );
        authorsGradebook.setGrades(1, firstSemesterGrades);
        authorsGradebook.setGrades(2, secondSemesterGrades);
    }

    @Test
    void authorsGradesTest() {
        for (int i = 1; i < 4; i++) {
            assertFalse(authorsGradebook.increasedScholarship(i));
        }
        assertFalse(authorsGradebook.honorsDegree());
        assertEquals(4.8, authorsGradebook.getAverageGrade(), 0.05);
    }

    @Test
    void honorsDegreeTest() {
        Gradebook gradebook = new Gradebook(1);
        gradebook.setGrade(1, "discipline", Grade.Excellent);
        gradebook.setQualifyingWorkGrade(Grade.Excellent);
        assertTrue(gradebook.honorsDegree());
    }

    @Test
    void lowAverageHonorsDegreeTest() {
        Gradebook gradebook = new Gradebook(1);
        gradebook.setGrade(1, "discipline", Grade.Good);
        gradebook.setQualifyingWorkGrade(Grade.Excellent);
        assertFalse(gradebook.honorsDegree());
    }

    @Test
    void lowQualifyingHonorsDegreeTest() {
        Gradebook gradebook = new Gradebook(1);
        gradebook.setGrade(1, "discipline", Grade.Excellent);
        gradebook.setQualifyingWorkGrade(Grade.Good);
        assertFalse(gradebook.honorsDegree());
    }

    @Test
    void satPresentHonorsDegreeTest() {
        Gradebook gradebook = new Gradebook(1);
        gradebook.setGrade(1, "discipline", Grade.Satisfactory);
        for (int i = 0; i < 100; i++) {
            gradebook.setGrade(1, String.valueOf(i), Grade.Excellent);
        }
        gradebook.setQualifyingWorkGrade(Grade.Excellent);
        assertFalse(gradebook.honorsDegree());
    }

    @Test
    void increasedScholarshipTest() {
        Gradebook gradebook = new Gradebook(4);
        gradebook.setGrade(1, "discipline", Grade.Excellent);
        gradebook.setGrade(2, "discipline", Grade.Excellent);
        gradebook.setGrade(3, "discipline", Grade.Good);
        gradebook.setGrade(4, "discipline", Grade.Excellent);

        assertFalse(gradebook.increasedScholarship(1));
        assertTrue(gradebook.increasedScholarship(2));
        assertTrue(gradebook.increasedScholarship(3));
        assertFalse(gradebook.increasedScholarship(4));
    }

    @Test
    void invalidIndexSetGradeThrows() {
        assertThrows(
            IllegalArgumentException.class,
            () -> authorsGradebook.setGrade(0, "discipline", Grade.Passed)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> authorsGradebook.setGrade(9, "discipline", Grade.Passed)
        );
    }

    @Test
    void invalidIndexSetGradesThrows() {
        assertThrows(
            IllegalArgumentException.class,
            () -> authorsGradebook.setGrades(0, Map.of("discipline", Grade.Passed))
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> authorsGradebook.setGrades(9, Map.of("discipline", Grade.Passed))
        );
    }

    @Test
    void invalidIndexIncreasedScholarshipThrows() {
        assertThrows(
            IllegalArgumentException.class,
            () -> authorsGradebook.increasedScholarship(0)
        );
        assertThrows(
            IllegalArgumentException.class,
            () -> authorsGradebook.increasedScholarship(9)
        );
    }

}
