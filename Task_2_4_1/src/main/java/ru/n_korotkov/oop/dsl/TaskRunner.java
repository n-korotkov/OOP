package ru.n_korotkov.oop.dsl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import org.bouncycastle.util.test.TestFailedException;
import org.gradle.tooling.BuildException;
import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.TestExecutionException;
import org.gradle.tooling.TestLauncher;

import ru.n_korotkov.oop.dsl.model.Assignment;
import ru.n_korotkov.oop.dsl.model.Config;
import ru.n_korotkov.oop.dsl.model.Student;
import ru.n_korotkov.oop.dsl.model.Task;

public class TaskRunner {

    private Map<String, Task> tasks;
    private Multimap<String, Assignment> assignments;

    public TaskRunner(Config config) {
        tasks = new HashMap<>();
        for (Task task : config.getTasks()) {
            tasks.put(task.getId(), task);
        }
        assignments = LinkedHashMultimap.create();
        for (Assignment assignment : config.getAssignments()) {
            assignments.put(assignment.getStudentId(), assignment);
        }
    }

    public StudentResult evaluateStudent(Student student, File repoDirectory) {
        System.out.printf("%nEvaluating %s%n", student.getName());
        List<TaskResult> taskResults = new ArrayList<>();
        float totalScore = 0;
        for (Assignment assignment : assignments.get(student.getId())) {
            if (assignment.getStudentId() == student.getId()) {
                Task task = tasks.get(assignment.getTaskId());
                File taskDirectory = new File(repoDirectory, task.getId());
                TaskResult result = evaluateTask(task, taskDirectory);
                taskResults.add(result);
                totalScore += result.score();
            }
        }
        return new StudentResult(student, true, taskResults, totalScore);
    }

    private TaskResult evaluateTask(Task task, File taskDirectory) {
        if (!taskDirectory.exists()) {
            System.out.printf("Could not find %s%n", task.getName());
            return new TaskResult(task, false, false, false, 0);
        }

        ProjectConnection connection = GradleConnector
            .newConnector()
            .forProjectDirectory(taskDirectory)
            .connect();
        
        float achievedScore = 0;
        boolean buildSuccessful = false;
        boolean testSuccessful = false;

        System.out.printf("Building %s: ", task.getName());
        buildSuccessful = buildTask(connection);
        if (buildSuccessful) {
            System.out.printf("success%n");
            achievedScore += task.getScore();
            if (task.isRunTests()) {
                System.out.printf("Testing %s: ", task.getName());
                testSuccessful = testTask(connection);
                if (testSuccessful) {
                    System.out.printf("success%n");
                } else {
                    System.out.printf("failure%n");
                    achievedScore /= 2;
                }
            }
        } else {
            System.out.printf("failure%n");
        }
        connection.close();

        return new TaskResult(task, true, buildSuccessful, testSuccessful, achievedScore);
    }

    private boolean buildTask(ProjectConnection connection) {
        BuildLauncher build = connection.newBuild().forTasks("assemble");
        try {
            build.run();
            return true;
        } catch (BuildException e) {
            return false;
        }
    }

    private boolean testTask(ProjectConnection connection) {
        TestLauncher test = connection.newTestLauncher().withJvmTestClasses("*");
        try {
            test.run();
            return true;
        } catch (TestFailedException | TestExecutionException | BuildException e) {
            return false;
        }
    }

}
