package k0bin.notes.ui.fragment;

import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.os.Bundle;

import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import k0bin.notes.R;
import k0bin.notes.model.Note;
import k0bin.notes.ui.adapter.NotesAdapter;
import k0bin.notes.viewModel.NotesViewModel;


/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NotesFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NotesFragment extends Fragment {
	private NotesAdapter adapter;
	private List<Note> notes;
	private NotesViewModel viewModel;

	public NotesFragment() {
		// Required empty public constructor
	}

	@Override
	public void onActivityCreated(@Nullable Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		viewModel = ViewModelProviders.of(this).get(NotesViewModel.class);
		viewModel.getNotes().observe(this, it -> adapter.submitList(it));
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		notes = new ArrayList<>();
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

		view.findViewById(R.id.fab).setOnClickListener(it -> {
			//Navigation.findNavController(it).
			notes.add(new Note(0, "hi", "hiiii"));
			adapter.submitList(notes);
		});
	}

	@Override
	public void onAttach(Context context) {
		super.onAttach(context);
	}

	@Override
	public void onDetach() {
		super.onDetach();
	}

	/**
	 * Use this factory method to create a new instance of
	 * this fragment using the provided parameters.
	 *
	 * @return A new instance of fragment NotesFragment.
	 */
	public static NotesFragment newInstance() {
		return new NotesFragment();
	}
}
