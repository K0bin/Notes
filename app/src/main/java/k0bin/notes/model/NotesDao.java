package k0bin.notes.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface NotesDao {
	@Query("SELECT * FROM notes")
	LiveData<List<Note>> getAll();

	@Query("SELECT * FROM notes WHERE id=:id")
	LiveData<Note> getById(int id);

	@Insert()
	void insert(Note note);

	@Update()
	void update(Note note);

	@Delete()
	void delete(Note note);

	@Query("DELETE FROM notes WHERE id=:id")
	void delete(int id);
}
