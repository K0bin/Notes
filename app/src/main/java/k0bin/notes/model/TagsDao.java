package k0bin.notes.model;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Delete;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;
import android.arch.persistence.room.Update;

import java.util.List;

@Dao
public interface TagsDao {
    @Query("SELECT * FROM tags")
    LiveData<List<Tag>> getAll();

    @Query("SELECT * FROM tags WHERE name=:name")
    LiveData<Tag> getByName(String name);

    @Query("SELECT * FROM tags WHERE name=:name")
    Tag getByNameSync(String name);

    @Insert()
    void insert(Tag note);

    @Update()
    void update(Tag note);

    @Delete()
    void delete(Tag note);

    @Query("DELETE FROM tags WHERE name=:name")
    void delete(String name);

    @Query("SELECT COUNT(*) FROM noteTags WHERE tagName=:tagName")
    int countNotesWithTag(String tagName);

    @Query("DELETE FROM noteTags WHERE noteId=:noteId AND tagName=:tagName")
    void deleteFromNote(int noteId, String tagName);

    @Insert()
    void insertToNote(NoteTag noteTag);

    @Query("SELECT t.* FROM tags t LEFT JOIN noteTags nt ON nt.tagName = t.name WHERE nt.noteId=:noteId")
    LiveData<List<Tag>> getForNote(int noteId);

    @Query("SELECT * FROM noteTags")
    LiveData<List<NoteTag>> getAllNoteTags();
}
