package ru.n_korotkov.oop.dsl;

import java.util.List;
import java.util.stream.Stream;

import ru.n_korotkov.oop.dsl.model.Student;

public class ReportWriter {

    private final String HEADER =
        "┌────────────────────────────────┬────────────────────────────────┬───────┬───────┬────────────────────┬───────┬─────────────┐%n" +
        "│                                │                                │       │       │       Tests        │       │             │%n" +
        "│            Student             │           Task name            │ Found │ Build ├──────┬──────┬──────┤ Score │ Total score │%n" +
        "│                                │                                │       │       │ Pass │ Fail │ Skip │       │             │%n";

    private final String[][] TASK_SEP = {
        {
            "│                                ├────────────────────────────────┼───────┼───────┼────────────────────┼───────┤             │%n",
            "│                                ├────────────────────────────────┼───────┼───────┼──────┬──────┬──────┼───────┤             │%n"
        },
        {
            "│                                ├────────────────────────────────┼───────┼───────┼──────┴──────┴──────┼───────┤             │%n",
            "│                                ├────────────────────────────────┼───────┼───────┼──────┼──────┼──────┼───────┤             │%n"
        }
    };

    private final String[][] STUDENT_SEP = {
        {
            "╞════════════════════════════════╪════════════════════════════════╪═══════╪═══════╪════════════════════╪═══════╪═════════════╡%n",
            "╞════════════════════════════════╪════════════════════════════════╪═══════╪═══════╪══════╤══════╤══════╪═══════╪═════════════╡%n"
        },
        {
            "╞════════════════════════════════╪════════════════════════════════╪═══════╪═══════╪══════╧══════╧══════╪═══════╪═════════════╡%n",
            "╞════════════════════════════════╪════════════════════════════════╪═══════╪═══════╪══════╪══════╪══════╪═══════╪═════════════╡%n"
        }
    };

    private final String FOOTER =
        "└────────────────────────────────┴────────────────────────────────┴───────┴───────┴────────────────────┴───────┴─────────────┘%n";
        
    private final String EMPTY_TASK_ROW =
        "│ %-30s │                                │       │       │                    │       │       %5.1f │%n";
    private final String TASK_ROW =
        "│                                │ %2$-30s │   %3$s   │   %4$s   │ %5$s │ %6$5.1f │             │%n";
    private final String FIRST_TASK_ROW =
        "│ %1$-30s │ %2$-30s │   %3$s   │   %4$s   │ %5$s │ %6$5.1f │       %7$5.1f │%n";
        
    private final String TESTS_DISABLED = "  Tests disabled  ";
    private final String TESTS_NOT_BUILT = " Tests not built  ";
    private final String TESTS_NOT_RUN = "  Tests not run   ";
    private final String TESTS_ROW = "%4d │ %4d │ %4d";

    private List<StudentResult> results;
    private boolean lastTaskTested = true;

    public ReportWriter(List<StudentResult> results) {
        this.results = results;
    }

    public void printReport() {
        System.out.printf(HEADER);
        for (StudentResult studentResult : results) {
            printStudentReport(studentResult);
        }
        System.out.printf(FOOTER);
    }

    private String getSeparator(String[][] separators, boolean taskTested) {
        int x = lastTaskTested ? 1 : 0;
        int y = taskTested ? 1 : 0;
        return separators[x][y];
    }

    private void printStudentReport(StudentResult studentResult) {
        if (studentResult.taskResults().isEmpty()) {
            printEmptyTaskReport(studentResult.student(), studentResult.totalScore());
        } else {
            Stream<TaskResult> taskResultStream = studentResult.taskResults().stream();
            TaskResult firstTaskResult = studentResult.taskResults().get(0);
            printFirstTaskReport(firstTaskResult, studentResult.student(), studentResult.totalScore());
            taskResultStream.skip(1).forEach(this::printNextTaskReport);
        }
    }

    private void printEmptyTaskReport(Student student, float totalScore) {
        System.out.printf(getSeparator(STUDENT_SEP, false));
        System.out.printf(EMPTY_TASK_ROW, student.getName(), totalScore);
        lastTaskTested = false;
    }

    private void printFirstTaskReport(TaskResult taskResult, Student student, float totalScore) {
        printTaskReport(taskResult, student.getName(), totalScore, FIRST_TASK_ROW, STUDENT_SEP);
    }

    private void printNextTaskReport(TaskResult taskResult) {
        printTaskReport(taskResult, "", 0, TASK_ROW, TASK_SEP);
    }

    private void printTaskReport(TaskResult taskResult, String studentName,
            float totalScore, String rowFormatString, String[][] separators) {
        String taskName = taskResult.task().getName();
        String foundStatus = taskResult.found() ? "+" : "-";
        String buildStatus = taskResult.buildSuccessful() ? "+" : "-";
        String testsStatus;

        boolean taskTested = false;
        if (!taskResult.task().isRunTests()) {
            testsStatus = TESTS_DISABLED;
        } else if (taskResult.testResult() == null) {
            testsStatus = TESTS_NOT_RUN;
        } else if (!taskResult.testResult().buildSuccessful()) {
            testsStatus = TESTS_NOT_BUILT;
        } else {
            taskTested = true;
            testsStatus = String.format(
                TESTS_ROW,
                taskResult.testResult().testsPassed(),
                taskResult.testResult().testsFailed(),
                taskResult.testResult().testsSkipped()
            );
        }
        System.out.printf(getSeparator(separators, taskTested));
        System.out.printf(
            rowFormatString,
            studentName,
            taskName,
            foundStatus,
            buildStatus,
            testsStatus,
            taskResult.score(),
            totalScore
        );
        lastTaskTested = taskTested;
    }

}
