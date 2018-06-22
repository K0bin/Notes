package k0bin.notes.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import java.util.List;

import androidx.navigation.Navigation;
import k0bin.notes.R;
import k0bin.notes.model.Note;
import k0bin.notes.ui.adapter.NotesAdapter;
import k0bin.notes.viewModel.NotesViewModel;


/**
 * Fragment that lists all notes
 */
public class NotesFragment extends Fragment {
	private NotesAdapter adapter;
	private NotesViewModel viewModel;

	public NotesFragment() {}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		viewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
	                         Bundle savedInstanceState) {
		// Inflate the layout for this fragment
		return inflater.inflate(R.layout.fragment_notes, container, false);
	}

	@Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		RecyclerView recycler = view.findViewById(R.id.recycler);
		adapter = new NotesAdapter();
		recycler.setAdapter(adapter);

		viewModel.getNotes().observe(this, adapter::submitList);

		view.findViewById(R.id.fab).setOnClickListener(it -> {
			EditFragmentArgs args = new EditFragmentArgs.Builder().build();
			Navigation.findNavController(it).navigate(R.id.action_notesFragment_to_editFragment2, args.toBundle());
		});
	}
}
