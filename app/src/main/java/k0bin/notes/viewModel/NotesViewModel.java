package k0bin.notes.viewModel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.os.Handler;
import android.support.annotation.NonNull;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import java9.util.stream.Collectors;
import java9.util.stream.StreamSupport;
import k0bin.notes.App;
import k0bin.notes.model.Database;
import k0bin.notes.model.FilterTag;
import k0bin.notes.model.Note;
import k0bin.notes.model.NoteTag;
import k0bin.notes.model.NoteWithTags;
import k0bin.notes.model.NotesDao;
import k0bin.notes.model.Tag;
import k0bin.notes.model.TagsDao;
import k0bin.notes.util.AsyncHelper;

public class NotesViewModel extends AndroidViewModel {
    private final NotesDao notesDao;
    private final TagsDao tagsDao;
    private final LiveData<List<Note>> notes;
    private final LiveData<List<Tag>> tags;
    private final LiveData<List<NoteTag>> noteTags;

    private final MutableLiveData<List<NoteWithTags>> notesWithTags = new MutableLiveData<>();
    private final MutableLiveData<List<FilterTag>> filterTags = new MutableLiveData<>();
    private final Set<Tag> filter = new HashSet<>();

    private boolean isNoteUpdateQueued = false;
    private final Handler handler; //Used to delay updates coming from the database so we don't do 3 updates if all 3 tables are changed

    @SuppressLint("NewApi")
    public NotesViewModel(@NonNull Application application) {
        super(application);

        handler = new Handler(application.getMainLooper());

        final Database db = ((App) application).getDb();
        notesDao = db.notesDao();
        tagsDao = db.tagsDao();

        notes = notesDao.getAll();
        tags = tagsDao.getAll();
        noteTags = tagsDao.getAllNoteTags();

        notes.observeForever(it -> {
            if (!isNoteUpdateQueued) {
                isNoteUpdateQueued = true;
                handler.post(this::updateNotesWithTags);
            }
        });
        tags.observeForever(it -> {
            if (!isNoteUpdateQueued) {
                isNoteUpdateQueued = true;
                handler.post(this::updateNotesWithTags);
            }
            updateFilterTags();
        });
        noteTags.observeForever(it -> {
            if (!isNoteUpdateQueued) {
                isNoteUpdateQueued = true;
                handler.post(this::updateNotesWithTags);
            }
        });
    }

    private void updateNotesWithTags() {
        if (notes.getValue() == null || tags.getValue() == null || noteTags.getValue() == null) {
            return;
        }

        List<NoteWithTags> newNotesWithTags = StreamSupport.stream(notes.getValue())
                .map(n -> {
                    Set<Tag> tags = StreamSupport.stream(noteTags.getValue())
                            .filter(nt -> nt.getNoteId() == n.getId())
                            .map(
                                    nt -> StreamSupport.stream(this.tags.getValue())
                                            .filter(t -> t.getName().equals(nt.getTagName()))
                                            .findFirst().orElse(null)
                            )
                            .filter(t -> t != null)
                            .collect(Collectors.toSet());

                    return new NoteWithTags(n, tags);
                })
                .filter(n -> n.getTags().containsAll(filter))
                .collect(Collectors.toList());
        notesWithTags.setValue(newNotesWithTags);

        isNoteUpdateQueued = false;
    }

    private void updateFilterTags() {
        if (tags.getValue() == null) {
            return;
        }

        List<FilterTag> newFilterTags = StreamSupport.stream(tags.getValue())
                .map(t -> new FilterTag(t, filter.contains(t)))
                .collect(Collectors.toList());
        filterTags.setValue(newFilterTags);
    }

    public LiveData<List<NoteWithTags>> getNotes() {
        return notesWithTags;
    }

    public LiveData<List<FilterTag>> getTags() {
        return filterTags;
    }

    public void deleteNote(int position) {
        final List<NoteWithTags> notes = this.notesWithTags.getValue();
        if (notes == null) {
            throw new IllegalStateException("No notes loaded");
        }
        if (position < 0 || notes.size() <= position) {
            throw new IndexOutOfBoundsException("position");
        }

        AsyncHelper.runAsync(() -> notesDao.delete(notes.get(position).getNote()));
    }

    public void toggleFilter(Tag tag) {
        if (filter.contains(tag)) {
            filter.remove(tag);
        } else {
            filter.add(tag);
        }

        updateFilterTags();
        handler.post(this::updateNotesWithTags);
    }
}
