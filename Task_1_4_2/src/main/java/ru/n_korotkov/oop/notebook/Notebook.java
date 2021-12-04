package ru.n_korotkov.oop.notebook;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.collect.Multimaps;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.google.gson.reflect.TypeToken;

public class Notebook {

    static class Note {
        String title;
        String contents;
        Note(String title, String contents) {
            this.title = title;
            this.contents = contents;
        }
        @Override
        public boolean equals(Object obj) {
            if (!(obj instanceof Note))
                return false;

            Note objNote = (Note) obj;
            return objNote.title.equals(this.title) && objNote.contents.equals(this.contents);
        }
    }

    static class Serializer implements JsonSerializer<Notebook> {
        @Override
        public JsonElement serialize(Notebook src, Type typeOfSrc, JsonSerializationContext context) {
            return context.serialize(src.getNotes().asMap());
        }
    }

    static class Deserializer implements JsonDeserializer<Notebook> {
        @Override
        public Notebook deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context)
                throws JsonParseException {
            Type noteMapType = new TypeToken<Map<LocalDateTime, Collection<Note>>>() {}.getType();
            Map<LocalDateTime, Collection<Note>> noteMap = context.deserialize(json, noteMapType);
            Notebook notebook = new Notebook();
            for (var entry : noteMap.entrySet()) {
                notebook.notes.putAll(entry.getKey(), entry.getValue());
            }
            return notebook;
        }
    }

    private Multimap<LocalDateTime, Note> notes;

    public Notebook() {
        notes = MultimapBuilder.treeKeys().linkedListValues().build();
    }

    public void addNote(String noteTitle, String noteContents) {
        addNote(noteTitle, noteContents, LocalDateTime.now());
    }

    public void addNote(String noteTitle, String noteContents, LocalDateTime date) {
        notes.put(date, new Note(noteTitle, noteContents));
    }

    public void removeNote(String noteTitle) {
        for (var entry : notes.entries()) {
            LocalDateTime date = entry.getKey();
            Note note = entry.getValue();
            if (noteTitle.equals(note.title)) {
                notes.remove(date, note);
                return;
            }
        }
        throw new IllegalArgumentException(String.format("Could not find note with title '%s'", noteTitle));
    }

    public Multimap<LocalDateTime, Note> getNotes() {
        return MultimapBuilder.treeKeys().linkedListValues().build(notes);
    }

    public Multimap<LocalDateTime, Note> getNotes(LocalDateTime dateFrom, LocalDateTime dateTo) {
        return Multimaps.filterEntries(notes, (entry) -> {
            LocalDateTime date = entry.getKey();
            return !date.isBefore(dateFrom) && date.isBefore(dateTo);
        });
    }

    public Multimap<LocalDateTime, Note> getNotes(
        LocalDateTime dateFrom,
        LocalDateTime dateTo,
        Collection<String> keywords
    ) {
        return Multimaps.filterEntries(getNotes(dateFrom, dateTo), (entry) -> {
            Note note = entry.getValue();
            return keywords.stream().anyMatch(kw -> note.title.contains(kw));
        });
    }

}
