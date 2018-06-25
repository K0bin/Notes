package k0bin.notes.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import k0bin.notes.R;
import k0bin.notes.model.Note;
import k0bin.notes.model.NoteWithTags;
import k0bin.notes.ui.viewHolder.NoteViewHolder;
import k0bin.notes.viewModel.NotesViewModel;

public class NotesAdapter extends ListAdapter<NoteWithTags, NoteViewHolder> {
	public NotesAdapter() {
		super(DIFF_CALLBACK);

		this.setHasStableIds(true);
	}

	@NonNull
	@Override
	public NoteViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
		return new NoteViewHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_note, viewGroup, false));
	}

	@Override
	public void onBindViewHolder(@NonNull NoteViewHolder noteViewHolder, int position) {
		noteViewHolder.bind(getItem(position).getNote());
	}

	@Override
	public long getItemId(int position) {
		return getItem(position).getNote().getId();
	}

	private static final DiffUtil.ItemCallback<NoteWithTags> DIFF_CALLBACK = new DiffUtil.ItemCallback<NoteWithTags>() {
		@Override
		public boolean areItemsTheSame(@NonNull NoteWithTags note1, @NonNull NoteWithTags note2) {
			return note1.getNote().getId() == note2.getNote().getId();
		}

		@Override
		public boolean areContentsTheSame(@NonNull NoteWithTags note1, @NonNull NoteWithTags note2) {
			return note1.equals(note2);
		}
	};
}
