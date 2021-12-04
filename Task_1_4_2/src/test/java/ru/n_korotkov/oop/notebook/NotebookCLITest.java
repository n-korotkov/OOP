package ru.n_korotkov.oop.notebook;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.PrintStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import com.google.common.collect.Streams;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.io.TempDir;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import picocli.CommandLine;

@TestMethodOrder(OrderAnnotation.class)
public class NotebookCLITest {

    @TempDir
    static Path tempDir;
    static Path tempNotebookPath;
    static String fileParam;
    static ByteArrayOutputStream outputStream;

    @BeforeAll
    static void init() {
        tempNotebookPath = tempDir.resolve("notebook.json");
        fileParam = String.format("-file=%s", tempNotebookPath);
        outputStream = new ByteArrayOutputStream();
        System.setOut(new PrintStream(outputStream));
    }

    @BeforeEach
    void clearOutputStream() {
        outputStream.reset();
    }

    static Stream<Arguments> nonEmptyNotebookPathStream() {
        return Stream.of(
            arguments(
                Path.of("sample/notes.json"),
                Path.of("sample/notes_show.txt")
            ),
            arguments(
                Path.of("sample/multinotes.json"),
                Path.of("sample/multinotes_show.txt")
            )
        );
    }

    static Stream<Arguments> notebookPathStream() {
        return Streams.concat(
            nonEmptyNotebookPathStream(),
            Stream.of(
                arguments(
                    Path.of("sample/empty.json"),
                    Path.of("sample/empty_show.txt")
                )
            )
        );
    }

    void copyNotebookToTempDir(Path notebookPath) {
        try {
            Files.copy(notebookPath, tempNotebookPath, StandardCopyOption.REPLACE_EXISTING);
        } catch (Exception e) {
            fail(e);
        }
    }

    @Test
    @Order(0)
    void createTest() {
        String[] cliArgs = new String[]{ fileParam, "-create" };

        assertEquals(CommandLine.ExitCode.OK, new CommandLine(new NotebookCLI()).execute(cliArgs));
        assertThat(tempNotebookPath).exists().isRegularFile();
        assertThat(contentOf(tempNotebookPath.toFile())).isEqualTo(contentOf(new File("sample/empty.json")));

        assertEquals(CommandLine.ExitCode.SOFTWARE, new CommandLine(new NotebookCLI()).execute(cliArgs));
    }

    @Test
    @Order(1)
    void destroyTest() {
        String[] cliArgs = new String[]{ fileParam, "-destroy" };

        assertEquals(CommandLine.ExitCode.OK, new CommandLine(new NotebookCLI()).execute(cliArgs));
        assertThat(tempNotebookPath).doesNotExist();

        assertEquals(CommandLine.ExitCode.SOFTWARE, new CommandLine(new NotebookCLI()).execute(cliArgs));
    }

    @ParameterizedTest
    @MethodSource("notebookPathStream")
    void showTest(Path notebookInitial, Path outputExpected) {
        String[] cliArgs = new String[]{ fileParam, "-show" };
        copyNotebookToTempDir(notebookInitial);

        assertEquals(CommandLine.ExitCode.OK, new CommandLine(new NotebookCLI()).execute(cliArgs));
        assertThat(outputStream.toString()).isEqualTo(contentOf(outputExpected.toFile()));
    }

    @ParameterizedTest
    @MethodSource("notebookPathStream")
    void addTest(Path notebookInitial, Path outputExpected) {
        String[] cliArgs = new String[]{ fileParam, "-add", "new", "note" };
        copyNotebookToTempDir(notebookInitial);

        assertEquals(CommandLine.ExitCode.OK, new CommandLine(new NotebookCLI()).execute(cliArgs));
        assertThat(contentOf(tempNotebookPath.toFile())).contains("\"title\":\"new\"", "\"contents\":\"note\"");
    }

    @ParameterizedTest
    @MethodSource("nonEmptyNotebookPathStream")
    void rmTest(Path notebookInitial, Path outputExpected) {
        String[] cliArgs = new String[]{ fileParam, "-rm", "notes" };
        copyNotebookToTempDir(notebookInitial);

        assertEquals(CommandLine.ExitCode.OK, new CommandLine(new NotebookCLI()).execute(cliArgs));
        assertThat(contentOf(tempNotebookPath.toFile())).doesNotContain("\"title\":\"notes\"", "\"contents\":\"@@@@@@\"");
    }

}
