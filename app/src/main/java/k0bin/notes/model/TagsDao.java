package k0bin.notes.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.OnConflictStrategy;
import android.arch.persistence.room.Query;

import java.util.List;

@Dao
public interface TagsDao {
    @Query("SELECT * FROM tags")
    LiveData<List<Tag>> getAll();

    @Query("SELECT * FROM tags WHERE name=:name")
    Tag getByNameSync(String name);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    long insert(Tag note);

    @Delete()
    void delete(Tag note);

    @Query("SELECT t.* FROM tags t LEFT JOIN noteTags nt ON nt.tagName = t.name WHERE nt.tagName IS NULL")
    List<Tag> getUnusedTags();

    @Query("DELETE FROM noteTags WHERE noteId=:noteId")
    void deleteAllFromNote(long noteId);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insertToNote(NoteTag noteTag);

    @Query("SELECT t.* FROM tags t LEFT JOIN noteTags nt ON nt.tagName = t.name WHERE nt.noteId=:noteId")
    LiveData<List<Tag>> getForNote(long noteId);

    @Query("SELECT * FROM noteTags")
    LiveData<List<NoteTag>> getAllNoteTags();
}
