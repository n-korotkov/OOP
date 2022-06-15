package ru.n_korotkov.oop.dsl;

import java.io.File;

import org.eclipse.jgit.api.Git;
import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.RefNotFoundException;
import org.eclipse.jgit.api.errors.TransportException;

import ru.n_korotkov.oop.dsl.model.Config;
import ru.n_korotkov.oop.dsl.model.Student;

public class Repositories {

    private File baseDirectory;

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
            Git git = Git.cloneRepository()
                .setURI(student.getRepoURL())
                .setDirectory(repoDirectory)
                .setCloneAllBranches(true)
                .call();
            git.checkout()
                .setName("origin/" + student.getBranch())
                .call();
            return repoDirectory;
        } catch (RefNotFoundException e) {
            System.err.printf("ERROR: Branch %s does not exist%n", student.getBranch());
        } catch (TransportException e) {
            System.err.printf("ERROR: Could not clone %s%n", student.getRepoURL());
        }
        return null;
    }

}
