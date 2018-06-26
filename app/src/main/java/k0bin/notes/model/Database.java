package k0bin.notes.model;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@android.arch.persistence.room.Database(entities = {Note.class, Tag.class, NoteTag.class}, version = 3)
public abstract class Database extends RoomDatabase {
	private static final String NAME = "notes.db";

	public abstract NotesDao notesDao();
	public abstract TagsDao tagsDao();

	public static Database build(Context context) {
		return Room.databaseBuilder(context, Database.class, NAME)
				.fallbackToDestructiveMigration()
				.build();
	}
}
