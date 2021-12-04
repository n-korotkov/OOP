package ru.n_korotkov.oop.notebook;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Reader;
import java.io.Writer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;

import com.google.common.collect.Multimap;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonSerializer;
import com.google.gson.JsonSyntaxException;

import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;
import picocli.CommandLine.Parameters;
import ru.n_korotkov.oop.notebook.Notebook.Note;

@Command(name = "notebook", synopsisSubcommandLabel = "<command>")
public class NotebookCLI {

    static final DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");
    static final Gson notebookGson = new GsonBuilder()
        .registerTypeAdapter(Notebook.class, new Notebook.Serializer())
        .registerTypeAdapter(Notebook.class, new Notebook.Deserializer())
        .registerTypeAdapter(
            LocalDateTime.class,
            (JsonSerializer<LocalDateTime>) (src,  type, ctx) ->
                ctx.serialize(src.format(DateTimeFormatter.ISO_DATE_TIME))
        )
        .registerTypeAdapter(
            LocalDateTime.class,
            (JsonDeserializer<LocalDateTime>) (json, type, ctx) ->
                LocalDateTime.parse(json.getAsString())
        )
        .enableComplexMapKeySerialization()
        .create();


    @Option(
        names = "-help",
        usageHelp = true,
        description = "Show usage help and exit.",
        scope = CommandLine.ScopeType.INHERIT
    )
    boolean help;

    @Option(
        names = "-file",
        defaultValue = "notes.json",
        description = "Specify the notebook file to use (default is '${DEFAULT-VALUE}').",
        paramLabel = "<filename>",
        scope = CommandLine.ScopeType.INHERIT
    )
    String notesFilename;
    Notebook notebook;


    public static void main(String[] args) {
        System.exit(
            new CommandLine(new NotebookCLI())
                .registerConverter(LocalDateTime.class, (value) -> LocalDateTime.parse(value, dateFormatter))
                .execute(args)
        );
    }


    @Command(name = "-add", description = "Add a new note to the notebook. If the notebook does not exist, it is created.")
    int addCommand(
        @Parameters(paramLabel = "<title>") String title,
        @Parameters(paramLabel = "<contents>") String contents
    ) {
        if (!readNotes(true))
            return CommandLine.ExitCode.SOFTWARE;

        notebook.addNote(title, contents);
        return writeNotes() ? CommandLine.ExitCode.OK : CommandLine.ExitCode.SOFTWARE;
    }


    @Command(name = "-rm", description = "Remove a note from the notebook.")
    int rmCommand(
        @Parameters(paramLabel = "<title>") String title
    ) {
        if (!readNotes(false))
            return CommandLine.ExitCode.SOFTWARE;

        try {
            notebook.removeNote(title);
        } catch (IllegalArgumentException e) {
            System.err.format("Error: note '%s' does not exist%n", title);
            return CommandLine.ExitCode.SOFTWARE;
        }
        return writeNotes() ? CommandLine.ExitCode.OK : CommandLine.ExitCode.SOFTWARE;
    }


    @Command(name = "-show", description = "Show stored notes.")
    int showCommand(
        @Parameters(arity = "0..1", paramLabel = "<dateFrom>") LocalDateTime dateFrom,
        @Parameters(arity = "0..1", paramLabel = "<dateTo>") LocalDateTime dateTo,
        @Parameters(paramLabel = "<keyword>") String[] keywords
    ) {
        if (!readNotes(false))
            return CommandLine.ExitCode.SOFTWARE;

        if (dateFrom == null) dateFrom = LocalDateTime.MIN;
        if (dateTo   == null) dateTo   = LocalDateTime.MAX;

        Multimap<LocalDateTime, Note> notes;
        if (keywords == null) {
            notes = notebook.getNotes(dateFrom, dateTo);
        } else {
            notes = notebook.getNotes(dateFrom, dateTo, Arrays.asList(keywords));
        }

        notes.forEach((date, note) -> {
            System.out.format("%s | %s: %s%n", date.format(dateFormatter), note.title, note.contents);
        });
        System.out.println();
        return CommandLine.ExitCode.OK;
    }


    @Command(name = "-create", description = "Create an empty notebook file.")
    int createCommand() {
        try {
            Files.createFile(Path.of(notesFilename));
        } catch (IOException e) {
            System.err.println("Error while creating a notebook file: " + e);
            return CommandLine.ExitCode.SOFTWARE;
        }
        notebook = new Notebook();
        return writeNotes() ? CommandLine.ExitCode.OK : CommandLine.ExitCode.SOFTWARE;
    }


    @Command(name = "-destroy", description = "Delete the notebook file.")
    int destroyCommand() {
        if (!readNotes(false))
            return CommandLine.ExitCode.SOFTWARE;

        try {
            Files.delete(Path.of(notesFilename));
        } catch (IOException e) {
            System.err.println("Error while deleting the notebook file: " + e);
            return CommandLine.ExitCode.SOFTWARE;
        }
        return CommandLine.ExitCode.OK;
    }


    boolean readNotes(boolean createNotebookIfFileNotFound) {
        try (Reader inputReader = new BufferedReader(new FileReader(notesFilename))) {
            notebook = notebookGson.fromJson(inputReader, Notebook.class);
        } catch (FileNotFoundException e) {
            if (createNotebookIfFileNotFound) {
                notebook = new Notebook();
            } else {
                System.err.format("Error: '%s' not found%n", notesFilename);
                return false;
            }
        } catch (JsonSyntaxException e) {
            System.err.format("Error: '%s' is not a valid notebook file: %n", notesFilename);
            return false;
        } catch (IOException e) {
            System.err.println("Error while reading json: " + e);
            return false;
        }
        return true;
    }

    boolean writeNotes() {
        try (Writer outputWriter = new BufferedWriter(new FileWriter(notesFilename, false))) {
            notebookGson.toJson(notebook, outputWriter);
            outputWriter.flush();
        } catch (IOException e) {
            System.err.println("Error while writing json: " + e);
            return false;
        }
        return true;
    }

}
