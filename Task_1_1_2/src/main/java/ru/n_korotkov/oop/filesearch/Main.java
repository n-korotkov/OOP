package ru.n_korotkov.oop.filesearch;

import java.io.IOException;
import java.io.FileReader;
import java.io.FileNotFoundException;
import java.io.BufferedReader;

public class Main {

    public static void main(String[] args) {
        if (args.length != 2) {
            System.out.println("Usage: java Main <filename> <search-string>");
            return;
        }
        String filename = args[0];
        String searchString = args[1];

        try (BufferedReader inputStream = new BufferedReader(new FileReader(filename))) {
            RabinKarpStringFinder stringFinder = new RabinKarpStringFinder();
            System.out.println(stringFinder.search(inputStream, searchString));
        } catch (FileNotFoundException e) {
            System.err.printf("Error: file '%s' not found%n", args[0]);
        } catch (IllegalArgumentException e) {
            System.err.printf("Error: search-string must not be empty%n");
        } catch (IOException e) {
            System.err.printf("Error: %s%n", e.getMessage());
        }
    }

}
