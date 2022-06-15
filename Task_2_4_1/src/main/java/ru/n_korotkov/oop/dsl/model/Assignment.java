package ru.n_korotkov.oop.dsl.model;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper=true)
@Data
public class Assignment extends GroovyConfigurable {

    private String studentId;
    private String taskId;

}
