package k0bin.notes.model;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

public class NoteWithTags {
    private Note note;
    private Set<Tag> tags;

    public NoteWithTags(@NonNull Note note, @Nullable Set<Tag> tags) {
        this.note = note;
        this.tags = tags != null ? tags : new HashSet<>();
    }

    public Note getNote() {
        return note;
    }

    public Set<Tag> getTags() {
        return tags;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NoteWithTags that = (NoteWithTags) o;
        return Objects.equals(note, that.note) &&
                Objects.equals(tags, that.tags);
    }

    @Override
    public String toString() {
        return "NoteWithTags{" +
                "note=" + note +
                ", tags=" + tags +
                '}';
    }

    @Override
    public int hashCode() {
        return Objects.hash(note, tags);
    }
}
