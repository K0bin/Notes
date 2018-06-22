package k0bin.notes.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import k0bin.notes.App;
import k0bin.notes.model.Database;
import k0bin.notes.model.Note;
import k0bin.notes.model.NotesDao;
import k0bin.notes.util.AsyncHelper;

public class EditViewModel extends AndroidViewModel {
	private final NotesDao notesDao;
	private final MutableLiveData<Integer> noteId = new MutableLiveData<>();

	private final MutableLiveData<String> title = new MutableLiveData<>();
	private boolean isTitleDirty = false;

	private final MutableLiveData<String> text = new MutableLiveData<>();
	private boolean isTextDirty = false;

	public EditViewModel(@NonNull Application application) {
		super(application);

		Database db = ((App) application).getDb();
		notesDao = db.notesDao();
		noteId.setValue(-1);

		Transformations.switchMap(noteId, id -> notesDao.getById(id)).observeForever(note -> {
			if (note == null) {
				text.setValue("");
				title.setValue("");
				return;
			}
			if (!isTitleDirty) {
				title.setValue(note.getTitle());
			}
			if (!isTextDirty) {
				text.setValue(note.getText());
			}
		});
	}

	public void setNoteId(int noteId) {
		this.noteId.setValue(noteId);
	}

	public MutableLiveData<String> getTitle() {
		return title;
	}

	public void setTitle(@NonNull String title) {
		if (title.equals(this.title.getValue())) {
			return;
		}
		this.title.setValue(title);
		isTitleDirty = true;
	}

	public MutableLiveData<String> getText() {
		return text;
	}

	public void setText(@NonNull String text) {
		if (text.equals(this.text.getValue())) {
			return;
		}
		this.text.setValue(text);
		isTextDirty = true;
	}

	public void save() {
		if (!isTitleDirty && !isTextDirty) {
			return;
		}
		final int noteId = this.noteId.getValue() != null ? this.noteId.getValue() : 0;
		final String title = this.title.getValue() != null ? this.title.getValue().trim() : "";
		final String text = this.text.getValue() != null ? this.text.getValue().trim() : "";

		AsyncHelper.runAsync(() -> {
			if (noteId != 0) {
				if (title.length() > 0 || text.length() > 0) {
					notesDao.update(new Note(noteId, title, text));
				} else {
					notesDao.delete(noteId);
				}
			} else {
				if (title.length() > 0 || text.length() > 0) {
					notesDao.insert(new Note(0, title, text));
				}
			}
		});
		isTitleDirty = false;
		isTextDirty = false;
	}
}