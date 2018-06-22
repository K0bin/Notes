package k0bin.notes.ui.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import k0bin.notes.R;
import k0bin.notes.model.Note;

public class NoteViewHolder extends RecyclerView.ViewHolder {
	private TextView title;
	private TextView text;

	public NoteViewHolder(@NonNull View itemView) {
		super(itemView);

		this.title = itemView.findViewById(R.id.title);
		this.text = itemView.findViewById(R.id.text);
	}

	public void bind(Note note) {
		this.title.setText(note.getTitle());
		this.text.setText(note.getText());
	}
}
