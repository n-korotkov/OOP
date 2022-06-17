package ru.n_korotkov.oop.dsl;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Multimap;

import org.gradle.tooling.BuildException;
import org.gradle.tooling.BuildLauncher;
import org.gradle.tooling.GradleConnector;
import org.gradle.tooling.ProjectConnection;
import org.gradle.tooling.TestExecutionException;
import org.gradle.tooling.events.OperationDescriptor;
import org.gradle.tooling.events.OperationType;
import org.gradle.tooling.events.ProgressEvent;
import org.gradle.tooling.events.ProgressListener;
import org.gradle.tooling.events.test.JvmTestKind;
import org.gradle.tooling.events.test.JvmTestOperationDescriptor;
import org.gradle.tooling.events.test.TestFailureResult;
import org.gradle.tooling.events.test.TestFinishEvent;
import org.gradle.tooling.events.test.TestOperationResult;
import org.gradle.tooling.events.test.TestSkippedResult;
import org.gradle.tooling.events.test.TestSuccessResult;

import lombok.Getter;
import ru.n_korotkov.oop.dsl.model.Assignment;
import ru.n_korotkov.oop.dsl.model.Config;
import ru.n_korotkov.oop.dsl.model.Student;
import ru.n_korotkov.oop.dsl.model.Task;

public class TaskRunner {

    private class TestProgressListener implements ProgressListener {

        private @Getter int testsPassed = 0;
        private @Getter int testsFailed = 0;
        private @Getter int testsSkipped = 0;

        public void statusChanged(ProgressEvent event) {
            OperationDescriptor descriptor = event.getDescriptor();
            if (event instanceof TestFinishEvent && descriptor instanceof JvmTestOperationDescriptor) {
                if (((JvmTestOperationDescriptor) descriptor).getJvmTestKind() == JvmTestKind.ATOMIC) {
                    TestOperationResult result = ((TestFinishEvent) event).getResult();
                    if (result instanceof TestSuccessResult) {
                        testsPassed++;
                    }
                    if (result instanceof TestFailureResult) {
                        testsFailed++;
                    }
                    if (result instanceof TestSkippedResult) {
                        testsSkipped++;
                    }
                }
            }
        }

        private TestResult getResult() {
            return new TestResult(true, testsPassed, testsFailed, testsSkipped);
        }
    }

    private Map<String, Task> tasks;
    private Multimap<String, Assignment> assignments;
    private Repositories repos;

    public TaskRunner(Config config, Repositories repos) {
        this.repos = repos;
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
                TaskResult result = evaluateTask(task, taskDirectory, assignment.getBranch());
                taskResults.add(result);
                totalScore += result.score();
            }
        }
        return new StudentResult(student, true, taskResults, totalScore);
    }

    private TaskResult evaluateTask(Task task, File taskDirectory, String branch) {
        boolean couldCheckout = repos.checkoutBranch(branch);
        if (!couldCheckout || !taskDirectory.exists()) {
            System.out.printf("Could not find %s%n", task.getName());
            return new TaskResult(task, false, false, null, 0);
        }

        ProjectConnection connection = GradleConnector
            .newConnector()
            .useGradleVersion("7.2")
            .forProjectDirectory(taskDirectory)
            .connect();

        float achievedScore = 0;
        boolean buildSuccessful = false;
        TestResult testResult = null;

        System.out.printf("Building %s: ", task.getName());
        buildSuccessful = buildTask(connection);
        if (buildSuccessful) {
            System.out.printf("success%n");
            achievedScore += task.getScore();
            if (task.isRunTests()) {
                System.out.printf("Testing %s: ", task.getName());
                testResult = testTask(connection, task.getId());
                if (testResult.buildSuccessful() && testResult.testsFailed() == 0) {
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

        return new TaskResult(task, true, buildSuccessful, testResult, achievedScore);
    }

    private boolean buildTask(ProjectConnection connection) {
        BuildLauncher build = connection.newBuild().forTasks("assemble");
        try {
            build.run();
            return true;
        } catch (BuildException e) {
            printThrowableCause(e);
            return false;
        }
    }

    private void printThrowableCause(Throwable e) {
        System.out.println(e.getCause().getMessage());
    }

    private TestResult testTask(ProjectConnection connection, String taskId) {
        TestProgressListener listener = new TestProgressListener();
        BuildLauncher test = connection
            .newBuild()
            .forTasks("test")
            .withArguments("--rerun-tasks")
            .addProgressListener(listener, OperationType.TEST);

        try {
            test.run();
            return listener.getResult();
        } catch (TestExecutionException e) {
            printThrowableCause(e);
            return listener.getResult();
        } catch (BuildException e) {
            printThrowableCause(e);
            return new TestResult(false, 0, 0, 0);
        }
    }

}
