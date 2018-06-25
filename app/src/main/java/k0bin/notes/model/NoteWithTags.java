package k0bin.notes.model;

import android.arch.persistence.room.Embedded;
import android.arch.persistence.room.Relation;
import android.support.annotation.NonNull;

import java.util.List;
import java.util.Objects;

public class NoteWithTags {
    @Embedded private Note note;

    @Relation(parentColumn = "id", entityColumn = "noteId")
    private List<NoteTag> noteTags;

    public NoteWithTags(@NonNull Note note) {
        this.note = note;
    }

    public Note getNote() {
        return note;
    }

    public List<NoteTag> getNoteTags() {
        return noteTags;
    }

    public void setNoteTags(List<NoteTag> noteTags) {
        this.noteTags = noteTags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteWithTags that = (NoteWithTags) o;
        return Objects.equals(note, that.note) &&
                Objects.equals(noteTags, that.noteTags);
    }

    @Override
    public String toString() {
        return "NoteWithTags{" +
                "note=" + note +
                ", noteTags=" + noteTags +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(note, noteTags);
    }
}
