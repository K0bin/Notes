package k0bin.notes.viewModel;

import android.app.Application;
import android.arch.core.util.Function;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Transformations;
import android.arch.persistence.room.Update;
import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;

import k0bin.notes.App;
import k0bin.notes.model.Database;
import k0bin.notes.model.Note;
import k0bin.notes.model.NoteTag;
import k0bin.notes.model.NotesDao;
import k0bin.notes.model.Tag;
import k0bin.notes.model.TagsDao;
import k0bin.notes.util.AsyncHelper;

public class EditViewModel extends AndroidViewModel {
	private final NotesDao notesDao;
	private final TagsDao tagsDao;

    private final MutableLiveData<Integer> noteId = new MutableLiveData<>();

	private final MutableLiveData<String> title = new MutableLiveData<>();
	private boolean isTitleDirty = false;

	private final MutableLiveData<String> text = new MutableLiveData<>();
	private boolean isTextDirty = false;

	private final MutableLiveData<Set<Tag>> tags = new MutableLiveData<>();

	public EditViewModel(@NonNull Application application) {
		super(application);

		Database db = ((App) application).getDb();
		notesDao = db.notesDao();
		tagsDao = db.tagsDao();
		noteId.setValue(-1);

        tags.setValue(new HashSet<>());

		Transformations.switchMap(noteId, notesDao::getByIdWithTags).observeForever(noteWithTags -> {
			Note note = noteWithTags != null ? noteWithTags.getNote() : null;
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

    public MutableLiveData<Set<Tag>> getTags() {
        return tags;
    }

    public void setText(@NonNull String text) {
		if (text.equals(this.text.getValue())) {
			return;
		}
		this.text.setValue(text);
		isTextDirty = true;
	}

	public void addTag(String tagName) {
	    if (tagName == null) {
	        return;
        }

        final String newTagName = tagName.trim();
	    if (newTagName.length() == 0) {
	        return;
        }

	    final Set<Tag> tags = this.tags.getValue();
	    if (tags == null) {
	        return;
        }
        final Set<Tag> newTags = new HashSet<>(tags);

        AsyncHelper.runAsync(() -> {
            Tag tag = tagsDao.getByNameSync(newTagName);
            if (tag == null) {
                tag = new Tag(newTagName);
                tagsDao.insert(tag);
            }
            if (noteId.getValue() != null && noteId.getValue() != 0) {
                tagsDao.insertToNote(new NoteTag(noteId.getValue(), tag.getName()));
            }
            return tag;
        }, tag -> {
            newTags.add(tag);
            EditViewModel.this.tags.setValue(newTags);
        });
    }

    public void removeTag(Tag tag) {
        final Set<Tag> tags = this.tags.getValue();
        if (tags == null) {
            return;
        }
        final Set<Tag> newTags = new HashSet<>(tags);
        newTags.remove(tag);
        this.tags.setValue(newTags);
        AsyncHelper.runAsync(() -> {
            if (noteId.getValue() != null && noteId.getValue() != 0) {
                tagsDao.deleteFromNote(noteId.getValue(), tag.getName());
            }
            int count = tagsDao.countNotesWithTag(tag.getName());
            if (count == 0) {
                tagsDao.delete(tag);
            }
        });
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
