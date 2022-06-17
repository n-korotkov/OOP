package ru.n_korotkov.oop.dsl;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import org.eclipse.jgit.api.errors.GitAPIException;

import ru.n_korotkov.oop.dsl.model.Config;
import ru.n_korotkov.oop.dsl.model.Student;

public class Report {

    private List<StudentResult> results;

    public Report(Config config) throws GitAPIException {
        Repositories repos = new Repositories(config);
        TaskRunner runner = new TaskRunner(config, repos);
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
        ReportWriter writer = new ReportWriter(results);
        writer.printReport();
    }

}
