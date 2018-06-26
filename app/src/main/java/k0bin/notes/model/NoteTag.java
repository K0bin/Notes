package k0bin.notes.model;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.ForeignKey;
import android.arch.persistence.room.Index;
import android.support.annotation.NonNull;

import java.util.Objects;

@Entity(tableName = "noteTags",
        primaryKeys = {"noteId", "tagName"},
        foreignKeys = {
            @ForeignKey(entity = Note.class, parentColumns = "id", childColumns = "noteId", onDelete = ForeignKey.CASCADE),
            @ForeignKey(entity = Tag.class, parentColumns = "name", childColumns = "tagName", onDelete = ForeignKey.CASCADE),
        },
        indices = { @Index("noteId"), @Index("tagName")})
public class NoteTag {
    private long noteId;
    @NonNull
    private String tagName;

    public NoteTag(long noteId, @NonNull String tagName) {
        this.noteId = noteId;
        this.tagName = tagName;
    }

    public long getNoteId() {
        return noteId;
    }

    @NonNull
    public String getTagName() {
        return tagName;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteTag noteTag = (NoteTag) o;
        return noteId == noteTag.noteId &&
                Objects.equals(tagName, noteTag.tagName);
    }

    @NonNull
    @Override
    public String toString() {
        return "NoteTag{" +
                "noteId=" + noteId +
                ", tagName=" + tagName +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(noteId, tagName);
    }
}
