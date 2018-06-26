package k0bin.notes.ui.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import androidx.navigation.Navigation;
import k0bin.notes.R;
import k0bin.notes.model.Note;
import k0bin.notes.ui.fragment.EditFragmentArgs;
import k0bin.notes.viewModel.NotesViewModel;

public class NoteViewHolder extends RecyclerView.ViewHolder {
	private final TextView title;
	private final TextView text;
	private long noteId = 0;

	public NoteViewHolder(@NonNull View itemView) {
		super(itemView);

		this.title = itemView.findViewById(R.id.title);
		this.text = itemView.findViewById(R.id.text);

		itemView.setOnClickListener(v -> {
			EditFragmentArgs args = new EditFragmentArgs.Builder().setNoteId((int)noteId).build();
			Navigation.findNavController(v).navigate(R.id.action_notesFragment_to_editFragment2, args.toBundle());
		});
	}

	public void bind(Note note) {
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
	}
}
