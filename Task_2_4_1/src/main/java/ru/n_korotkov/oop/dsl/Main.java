package ru.n_korotkov.oop.dsl;

import java.io.File;

import ru.n_korotkov.oop.dsl.model.Config;

public class Main {

    public static void main(String[] args) throws Exception {
        Config config = new Config();
        config.configureFromFile(new File("config.groovy"));

        Report report = new Report(config);
        report.printReport();
    }

}
