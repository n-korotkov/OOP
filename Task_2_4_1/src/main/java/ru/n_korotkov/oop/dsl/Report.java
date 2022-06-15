package ru.n_korotkov.oop.dsl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;

import ru.n_korotkov.oop.dsl.model.Config;
import ru.n_korotkov.oop.dsl.model.Student;

public class Report {

    private List<StudentResult> results;
    private String groupName;

    public Report(Config config) throws GitAPIException {
        groupName = config.getGroupName();
        Repositories repos = new Repositories(config);
        TaskRunner runner = new TaskRunner(config);
        results = new ArrayList<>();
        for (Student student : config.getStudents()) {
            StudentResult studentResult;
            File repoDirectory = repos.cloneRepository(student);
            if (repoDirectory != null) {
                studentResult = runner.evaluateStudent(student, repoDirectory);
            } else {
                studentResult = new StudentResult(student, false, List.of(), 0);
            }
            results.add(studentResult);
        }
    }

    public void printReport() {
        System.out.printf("=======================%n");
        System.out.printf("Results for group %s%n", groupName);
        System.out.printf("=======================%n");
        for (StudentResult studentResult : results) {
            System.out.printf("%s:%n", studentResult.student().getName());
            for (TaskResult taskResult : studentResult.taskResults()) {
                System.out.printf("%-20s %.1f%n", taskResult.task().getName(), taskResult.score());
            }
            System.out.printf("%-20s %.1f%n%n", "Total", studentResult.totalScore());
        }
    }

}
