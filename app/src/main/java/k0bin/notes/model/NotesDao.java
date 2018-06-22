package k0bin.notes.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface NotesDao {
	@Query("SELECT * FROM notes")
	LiveData<List<Note>> getNotes();
}
