package k0bin.notes.ui.fragment;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import k0bin.notes.R;
import k0bin.notes.ui.adapter.DrawerAdapter;
import k0bin.notes.viewModel.NotesViewModel;

public class DrawerDialogFragment extends BottomSheetDialogFragment {
    public static final String TAG = "drawer";

    public static DrawerDialogFragment newInstance() {
        return new DrawerDialogFragment();
    }

    private NotesViewModel viewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getActivity() == null) {
            throw new IllegalStateException("Something went wrong, Activity must not be null here");
        }
        viewModel = ViewModelProviders.of(getActivity()).get(NotesViewModel.class);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_drawer, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        /*
        final RecyclerView recycler = view.findViewById(R.id.drawerRecycler);
        final DrawerAdapter adapter = new DrawerAdapter(viewModel);
        recycler.setAdapter(adapter);

        viewModel.getTags().observe(this, adapter::submitList);*/
    }
}
