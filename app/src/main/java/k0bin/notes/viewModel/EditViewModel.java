package k0bin.notes.viewModel;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.Transformations;
import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

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

    private final MutableLiveData<Long> noteId = new MutableLiveData<>();

	private final MutableLiveData<String> title = new MutableLiveData<>();
	private boolean isTitleDirty = false;

	private final MutableLiveData<String> text = new MutableLiveData<>();
	private boolean isTextDirty = false;

	private final MutableLiveData<Set<Tag>> tags = new MutableLiveData<>();
    private boolean areTagsDirty = false;
    private LiveData<List<Tag>> dbTags;
    private final Observer<List<Tag>> dbTagsObserver;

	public EditViewModel(@NonNull Application application) {
		super(application);

		Database db = ((App) application).getDb();
		notesDao = db.notesDao();
		tagsDao = db.tagsDao();
		noteId.setValue(-1L);

		dbTagsObserver = tags -> {
		    if (!areTagsDirty) {
                this.tags.setValue(tags != null ? new HashSet<>(tags) : new HashSet<>());
            }
        };

		Transformations.switchMap(noteId, notesDao::getById).observeForever(note -> {
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

	public void setNoteId(long noteId) {
	    if (this.noteId.getValue() == null || noteId != this.noteId.getValue()) {
	        save();

            this.noteId.setValue(noteId);
            if (this.dbTags != null) {
                this.dbTags.removeObserver(dbTagsObserver);
            }
            this.dbTags = tagsDao.getForNote(noteId);
		    this.dbTags.observeForever(dbTagsObserver);
        }
	}

	public LiveData<String> getTitle() {
		return title;
	}

	public void setTitle(@NonNull String title) {
		if (title.equals(this.title.getValue())) {
			return;
		}
		this.title.setValue(title);
		isTitleDirty = true;
	}

	public LiveData<String> getText() {
		return text;
	}

    public LiveData<Set<Tag>> getTags() {
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
            }
            return tag;
        }, tag -> {
            areTagsDirty = true;
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
        areTagsDirty = true;
        newTags.remove(tag);
        this.tags.setValue(newTags);
    }

	public void save() {
		if (!isTitleDirty && !isTextDirty && !areTagsDirty) {
			return;
		}
		final long noteId = this.noteId.getValue() != null ? this.noteId.getValue() : 0L;
		final String title = this.title.getValue() != null ? this.title.getValue().trim() : "";
		final String text = this.text.getValue() != null ? this.text.getValue().trim() : "";

		AsyncHelper.runAsync(() -> {
            final Set<Tag> tags = this.tags.getValue() != null ? this.tags.getValue() : new HashSet<>();
			if (noteId != 0) {
				if (title.length() > 0 || text.length() > 0) {
					notesDao.update(new Note(noteId, title, text));
                    for (Tag tag : tags) {
                        tagsDao.insert(tag);
                        tagsDao.insertToNote(new NoteTag(noteId, tag.getName()));
                    }
				} else {
					notesDao.delete(noteId);
					for (Tag tag : tags) {
						tagsDao.deleteFromNote(noteId, tag.getName());
						int count = tagsDao.countNotesWithTag(tag.getName());
						if (count == 0) {
							tagsDao.delete(tag);
						}
					}
				}
			} else {
				if (title.length() > 0 || text.length() > 0) {
					long newId = notesDao.insert(new Note(0L, title, text));
                    for (Tag tag : tags) {
                        tagsDao.insertToNote(new NoteTag(newId, tag.getName()));
                    }
				}
			}
		});
		isTitleDirty = false;
		isTextDirty = false;
		areTagsDirty = false;
	}
}
