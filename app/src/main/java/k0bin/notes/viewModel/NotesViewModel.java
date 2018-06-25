package k0bin.notes.viewModel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java9.util.stream.Collectors;
import java9.util.stream.StreamSupport;
import k0bin.notes.App;
import k0bin.notes.model.Database;
import k0bin.notes.model.NoteWithTags;
import k0bin.notes.model.NotesDao;
import k0bin.notes.model.Tag;
import k0bin.notes.util.AsyncHelper;

public class NotesViewModel extends AndroidViewModel {
	private final NotesDao notesDao;
	private final MutableLiveData<List<NoteWithTags>> notes = new MutableLiveData<>();
	private final Set<Tag> filter = new HashSet<>();

	@SuppressLint("NewApi")
    public NotesViewModel(@NonNull Application application) {
		super(application);

		final Database db = ((App) application).getDb();
		notesDao = db.notesDao();

		notesDao.getAllWithTags().observeForever(it -> {
		    if (it == null) {
		        return;
            }
            notes.setValue(StreamSupport.stream(it)
                    .filter(note -> note
                                    .getNoteTags()
                                    .containsAll(filter))
                    .collect(Collectors.toList())
            );
        });
	}

	public LiveData<List<NoteWithTags>> getNotes() {
		return notes;
	}

	public void deleteNote(int position) {
		final List<NoteWithTags> notes = this.notes.getValue();
		if (notes == null) {
			throw new IllegalStateException("No notes loaded");
		}
		if (position < 0 || notes.size() <= position) {
			throw new IndexOutOfBoundsException("position");
		}

		AsyncHelper.runAsync(() -> notesDao.delete(notes.get(position).getNote()));
	}

	public void addFilter(Tag tag) {
	    filter.add(tag);
    }

    public void removeFilter(Tag tag) {
	    filter.remove(tag);
    }
}
