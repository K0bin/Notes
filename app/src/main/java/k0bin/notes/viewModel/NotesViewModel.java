package k0bin.notes.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import java.util.List;

import k0bin.notes.App;
import k0bin.notes.model.Database;
import k0bin.notes.model.Note;
import k0bin.notes.model.NotesDao;

public class NotesViewModel extends AndroidViewModel {
	private Database db;
	private NotesDao notesDao;
	private LiveData<List<Note>> notes;

	public NotesViewModel(@NonNull Application application) {
		super(application);

		db = ((App)application).getDb();
		notesDao = db.notesDao();

		notes = notesDao.getNotes();
	}

	public LiveData<List<Note>> getNotes() {
		return notes;
	}
}
