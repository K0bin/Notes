package k0bin.notes.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Objects;

@Entity(tableName = "notes")
public class Note {
    @PrimaryKey(autoGenerate = true)
    private final long id;
    @NonNull
    private final String title;
    @NonNull
    private final String text;

    @Ignore
    public Note(@NonNull String title, @NonNull String text) {
        this(0L, title, text);
    }

    public Note(long id, @NonNull String title, @NonNull String text) {
        this.id = id;
        this.title = title;
        this.text = text;
    }

    public long getId() {
        return id;
    }

    @NonNull
    public String getTitle() {
        return title;
    }

    @NonNull
    public String getText() {
        return text;
    }

    @NonNull
    @Override
    public String toString() {
        return "Note{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", text='" + text + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Note note = (Note) o;
        return id == note.id &&
                Objects.equals(title, note.title) &&
                Objects.equals(text, note.text);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, text);
    }
}
