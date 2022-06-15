package ru.n_korotkov.oop.dsl.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@Data
public class Student extends GroovyConfigurable {

    private String id;
    private String name;
    private String repoURL;
    private String branch;

}
