package ru.n_korotkov.oop.filesearch;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.io.BufferedReader;
import java.io.FileReader;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class RabinKarpStringFinderTest {

    static ArrayList<Long> simpleSearch(String filename, String searchQuery) {
        ArrayList<Long> searchResult = new ArrayList<>();

        try {
            String fileContents = Files.readString(Path.of(filename));
            int substringIndex = fileContents.indexOf(searchQuery);
            while (substringIndex != -1) {
                searchResult.add((long) substringIndex);
                substringIndex = fileContents.indexOf(searchQuery, substringIndex + 1);
            }
        } catch (Exception e) {
            fail(e);
        }

        return searchResult;
    }

    static Stream<Arguments> fileSearchArgumentsProvider() {
        return Stream.of(
            arguments("sample/input1.txt", "пирог"),
            arguments("sample/input2.txt", "пирог"),
            arguments("sample/skyrim.txt", "thief"),
            arguments("sample/tasks.txt",  "задания")
        );
    }

    static Stream<Arguments> fileSearchIllegalArgumentsProvider() {
        return Stream.of(
            arguments("sample/input1.txt", ""),
            arguments("sample/input2.txt", null)
        );
    }

    @ParameterizedTest
    @MethodSource("fileSearchArgumentsProvider")
    void rabinKarpSearchTest(String filename, String searchQuery) {
        ArrayList<Long> searchResult = simpleSearch(filename, searchQuery);

        try (BufferedReader inputStream = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            RabinKarpStringFinder stringFinder = new RabinKarpStringFinder();
            ArrayList<Long> rabinKarpSearchResult = stringFinder.search(inputStream, searchQuery);
            assertEquals(rabinKarpSearchResult, searchResult);
        } catch (Exception e) {
            fail(e);
        }
    }

    @ParameterizedTest
    @MethodSource("fileSearchIllegalArgumentsProvider")
    void rabinKarpIllegalSearchQueryTest(String filename, String searchQuery) {
        try (BufferedReader inputStream = new BufferedReader(new FileReader(filename, StandardCharsets.UTF_8))) {
            RabinKarpStringFinder stringFinder = new RabinKarpStringFinder();
            assertThrows(IllegalArgumentException.class, () -> stringFinder.search(inputStream, searchQuery));
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    void rabinKarpIllegalReaderTest() {
        RabinKarpStringFinder stringFinder = new RabinKarpStringFinder();
        assertThrows(IllegalArgumentException.class, () -> stringFinder.search(null, "unused"));
    }

}
