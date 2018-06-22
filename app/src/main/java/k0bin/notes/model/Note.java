package k0bin.notes.model;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.arch.persistence.room.PrimaryKey;
import android.support.annotation.NonNull;

import java.util.Objects;

@Entity(tableName = "notes")
public class Note {
	@PrimaryKey(autoGenerate = true)
	private final int id;
	private final String title;
	private final String text;

	@Ignore
	public Note(@NonNull String title, @NonNull String text) {
		this(0, title, text);
	}

	public Note(int id, @NonNull String title, @NonNull String text) {
		this.id = id;
		this.title = title;
		this.text = text;
	}

	public int getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public String getText() {
		return text;
	}

	@Override
	public boolean equals(Object obj) {
		return obj == this || obj instanceof Note && this.id == ((Note)obj).id && this.title.equals(((Note)obj).title) && this.text.equals(((Note)obj).text);
	}

	@Override
	public int hashCode() {
		return Objects.hash(id, title, text);
	}
}
