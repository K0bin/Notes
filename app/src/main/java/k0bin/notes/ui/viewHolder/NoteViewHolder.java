package k0bin.notes.ui.viewHolder;

import android.content.res.ColorStateList;
import android.support.annotation.NonNull;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Set;

import androidx.navigation.Navigation;
import k0bin.notes.R;
import k0bin.notes.model.Note;
import k0bin.notes.model.NoteWithTags;
import k0bin.notes.model.Tag;
import k0bin.notes.ui.fragment.EditFragmentArgs;

public class NoteViewHolder extends RecyclerView.ViewHolder {
    private final TextView title;
    private final TextView text;
    private final ChipGroup tagGroup;
    private long noteId = 0;
    private final int accentColor;

    public NoteViewHolder(@NonNull View itemView, int accentColor) {
        super(itemView);

        this.title = itemView.findViewById(R.id.title);
        this.text = itemView.findViewById(R.id.text);
        this.tagGroup = itemView.findViewById(R.id.tagChips);
        this.accentColor = accentColor;

        itemView.setOnClickListener(v -> {
            EditFragmentArgs args = new EditFragmentArgs.Builder().setNoteId((int)noteId).build();
            Navigation.findNavController(v).navigate(R.id.action_notesFragment_to_editFragment2, args.toBundle());
        });
    }

    public void bind(@NonNull NoteWithTags noteWithTags) {
        final Note note = noteWithTags.getNote();
        final Set<Tag> tags = noteWithTags.getTags();
        this.noteId = note.getId();

        if (note.getTitle().length() == 0) {
            this.title.setText(note.getText());
            this.text.setVisibility(View.GONE);
        } else if (note.getText().length() == 0) {
            this.title.setText(note.getTitle());
            this.text.setVisibility(View.GONE);
        } else {
            this.title.setText(note.getTitle());
            this.text.setText(note.getText());
            this.text.setVisibility(View.VISIBLE);
        }

        tagGroup.removeAllViews();
        tagGroup.setVisibility(tags.isEmpty() ? View.GONE : View.VISIBLE);

        for (Tag tag : tags) {
            final Chip tagView = new Chip(tagGroup.getContext());
            tagView.setLayoutParams(new ChipGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
            tagView.setChipText(tag.getName());
            tagView.setTag(tag);
            tagView.setChipBackgroundColor(ColorStateList.valueOf(accentColor));
            tagGroup.addView(tagView);
        }
    }
}
