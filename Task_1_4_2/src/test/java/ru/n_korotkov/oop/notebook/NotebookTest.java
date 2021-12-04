package ru.n_korotkov.oop.notebook;

import static org.junit.jupiter.api.Assertions.*;

import java.time.LocalDateTime;
import java.util.List;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import ru.n_korotkov.oop.notebook.Notebook.Note;

public class NotebookTest {

    static Notebook notebook;
    static Multimap<LocalDateTime, Note> noteMap;

    @BeforeAll
    static void init() {
        noteMap = ArrayListMultimap.create();
        noteMap.put(LocalDateTime.of(2000, 1,  1,  0,  0),  new Note("1", "1"));
        noteMap.put(LocalDateTime.of(2000, 1,  1,  1,  0),  new Note("2", "2"));
        noteMap.put(LocalDateTime.of(2000, 1,  1,  1,  1),  new Note("3", "3"));
        noteMap.put(LocalDateTime.of(2011, 11, 11, 11, 11), new Note("1234", "4321"));
        noteMap.put(LocalDateTime.of(2011, 11, 11, 11, 11), new Note("note", "!!!!!!!"));
        noteMap.put(LocalDateTime.of(2011, 11, 11, 11, 11), new Note("notes", "@@@@@@"));
        noteMap.put(LocalDateTime.of(2011, 11, 11, 11, 11), new Note("many notes", "#"));
        noteMap.put(LocalDateTime.of(2011, 11, 11, 11, 11), new Note("NOTE", "$$$$$$$"));
        noteMap.put(LocalDateTime.of(2011, 1,  1,  1,  1),  new Note("not", "%%%%%%%%"));
    }

    @BeforeEach
    void initNotebook() {
        notebook = new Notebook();
        for (var e : noteMap.entries()) {
            notebook.addNote(e.getValue().title, e.getValue().contents, e.getKey());
        }
    }

    <K, V> boolean multimapsMatch(Multimap<K, V> map1, Multimap<K, V> map2) {
        return map1.size() == map2.size() &&
            map1.entries().stream().allMatch((e) -> map2.containsEntry(e.getKey(), e.getValue()));
    }

    @Test
    void notebookGetNotesTest() {
        var notes = notebook.getNotes();
        assertTrue(multimapsMatch(notes, noteMap));
    }

    @Test
    void notebookGetNotesWithDatesTest() {
        LocalDateTime dateFrom = LocalDateTime.of(2000, 1, 1, 0,  0);
        LocalDateTime dateTo   = LocalDateTime.of(2000, 1, 1, 12, 0);
        var notes = notebook.getNotes(dateFrom, dateTo);
        var noteMapFiltered = Multimaps.filterEntries(
            noteMap,
            (e) -> !e.getKey().isBefore(dateFrom) && e.getKey().isBefore(dateTo)
        );
        assertTrue(multimapsMatch(notes, noteMapFiltered));
    }

    @Test
    void notebookGetNotesWithDatesAndKeywordsTest() {
        LocalDateTime dateFrom = LocalDateTime.of(2011, 1, 1, 0, 0);
        LocalDateTime dateTo   = LocalDateTime.of(2012, 1, 1, 0, 0);
        List<String>  keywords = List.of("es", "NOT");
        var notes = notebook.getNotes(dateFrom, dateTo, keywords);
        var noteMapFiltered = Multimaps.filterEntries(
            noteMap,
            (e) -> !e.getKey().isBefore(dateFrom) && e.getKey().isBefore(dateTo) &&
                keywords.stream().anyMatch((kw) -> e.getValue().title.contains(kw))
        );
        assertTrue(multimapsMatch(notes, noteMapFiltered));
    }

    @Test
    void notebookAddNoteTest() {
        LocalDateTime timestamp = LocalDateTime.of(2025, 6, 19, 12, 0);
        Note note = new Note("new note", "noteContents");
        notebook.addNote(note.title, note.contents, timestamp);
        var notes = notebook.getNotes();
        var noteMapAdded = ArrayListMultimap.create(noteMap);
        noteMapAdded.put(timestamp, note);
        assertTrue(multimapsMatch(notes, noteMapAdded));
    }

    @Test
    void notebookRemoveNoteTest() {
        var e = noteMap.entries().stream().findAny().get();
        notebook.removeNote(e.getValue().title);
        var notes = notebook.getNotes();
        var noteMapRemoved = ArrayListMultimap.create(noteMap);
        noteMapRemoved.remove(e.getKey(), e.getValue());
        assertTrue(multimapsMatch(notes, noteMapRemoved));
    }

    @Test
    void notebookRemoveNonExistentNoteThrows() {
        assertThrows(IllegalArgumentException.class, () -> notebook.removeNote("not a note"));
    }

}
