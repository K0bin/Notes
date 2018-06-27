package k0bin.notes.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.navigation.Navigation;
import k0bin.notes.R;
import k0bin.notes.ui.adapter.NotesAdapter;
import k0bin.notes.viewModel.NotesViewModel;


/**
 * Fragment that lists all notes
 */
public class NotesFragment extends Fragment {
    private NotesViewModel viewModel;

    private DrawerDialogFragment fragment;

    public NotesFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() == null) {
            throw new IllegalStateException("Something went wrong, Activity must not be null here");
        }
        viewModel = ViewModelProviders.of(getActivity()).get(NotesViewModel.class);
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

        fragment = DrawerDialogFragment.newInstance();

        final RecyclerView recycler = view.findViewById(R.id.recycler);
        final NotesAdapter adapter = new NotesAdapter();
        recycler.setAdapter(adapter);

        final ItemTouchHelper helper = new ItemTouchHelper(new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
                return false;
            }
            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
                viewModel.deleteNote(viewHolder.getAdapterPosition());
            }
        });
        helper.attachToRecyclerView(recycler);

        viewModel.getNotes().observe(this, adapter::submitList);

        view.findViewById(R.id.fab).setOnClickListener(it -> {
            EditFragmentArgs args = new EditFragmentArgs.Builder().build();
            Navigation.findNavController(it).navigate(R.id.action_notesFragment_to_editFragment2, args.toBundle());
        });

        final BottomAppBar appBar = view.findViewById(R.id.bottomBar);
        appBar.setNavigationOnClickListener(v -> {
            if (getFragmentManager() != null) {
                fragment.show(getFragmentManager(), DrawerDialogFragment.TAG);
            }
        });
    }
}
