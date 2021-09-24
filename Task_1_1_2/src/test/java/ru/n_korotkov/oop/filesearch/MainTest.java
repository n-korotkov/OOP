package ru.n_korotkov.oop.filesearch;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

class MainTest {

    static Stream<Arguments> fileSearchArgumentsProvider() {
        return Stream.of(
            arguments("sample/input1.txt", "пирог",   new long[] { 7 }),
            arguments("sample/input2.txt", "пирог",   new long[0]),
            arguments("sample/skyrim.txt", "thief",   new long[] { 145, 497, 989 }),
            arguments("sample/tasks.txt",  "задания", new long[] { 372, 408, 534 })
        );
    }

    @ParameterizedTest
    @MethodSource("fileSearchArgumentsProvider")
    void searchInFileTest(String filename, String searchQuery, long[] expectedSearchResult) {
        try {
            long[] searchResult = Main.searchInFile(filename, searchQuery);
            assertArrayEquals(searchResult, expectedSearchResult);
        } catch (Exception e) {
            fail(e);
        }
    }

}
