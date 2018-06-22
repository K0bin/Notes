package k0bin.notes.model;

import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

@android.arch.persistence.room.Database(entities = {Note.class}, version = 1)
public abstract class Database extends RoomDatabase {
	private static final String NAME = "notes.db";

	public abstract NotesDao notesDao();

	public static Database build(Context context) {
		return Room.databaseBuilder(context, Database.class, NAME)
				.fallbackToDestructiveMigration()
				.build();
	}
}
