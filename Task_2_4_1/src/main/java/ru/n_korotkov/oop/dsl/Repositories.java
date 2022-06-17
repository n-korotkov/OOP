package ru.n_korotkov.oop.dsl;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.TransportException;

import ru.n_korotkov.oop.dsl.model.Config;
import ru.n_korotkov.oop.dsl.model.Student;

public class Repositories {

    private File baseDirectory;
    private Git git;

    public Repositories(Config config) {
        baseDirectory = new File(config.getRepositoriesDirectory());
    }

    private void deleteRecursive(File file) {
        if (file.isDirectory()) {
            for (File child : file.listFiles()) {
                deleteRecursive(child);
            }
        }
        file.delete();
    }

    public File cloneRepository(Student student) throws GitAPIException {
        File repoDirectory = new File(baseDirectory, student.getId());
        if (repoDirectory.exists()) {
            deleteRecursive(repoDirectory);
        }

        try {
            git = Git.cloneRepository()
                .setURI(student.getRepoURL())
                .setDirectory(repoDirectory)
                .setCloneAllBranches(true)
                .call();
            return repoDirectory;
        } catch (TransportException e) {
            System.out.printf("Could not clone %s%n", student.getRepoURL());
        }
        return null;
    }

    public boolean checkoutBranch(String branch) {
        try {
            git.checkout().setName("origin/" + branch).call();
            return true;
        } catch (GitAPIException e) {
            System.out.printf("Could not checkout branch %s%n", branch);
            return false;
        }
    }

}
