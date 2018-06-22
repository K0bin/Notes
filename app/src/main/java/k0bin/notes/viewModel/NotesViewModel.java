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
import k0bin.notes.util.AsyncHelper;

public class NotesViewModel extends AndroidViewModel {
	private final NotesDao notesDao;
	private final LiveData<List<Note>> notes;

	public NotesViewModel(@NonNull Application application) {
		super(application);

		final Database db = ((App) application).getDb();
		notesDao = db.notesDao();

		notes = notesDao.getAll();
	}

	public LiveData<List<Note>> getNotes() {
		return notes;
	}

	public void deleteNote(int position) {
		final List<Note> notes = this.notes.getValue();
		if (notes == null) {
			throw new IllegalStateException("No notes loaded");
		}
		if (position < 0 || notes.size() <= position) {
			throw new IndexOutOfBoundsException("position");
		}

		AsyncHelper.runAsync(() -> notesDao.delete(notes.get(position)));
	}
}
