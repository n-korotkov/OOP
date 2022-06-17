package ru.n_korotkov.oop.dsl.model;

import java.util.List;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@Data
public class Config extends GroovyConfigurable {

    private List<Task> tasks;
    private String groupName;
    private List<Student> students;
    private List<Assignment> assignments;
    private String repositoriesDirectory;

}
