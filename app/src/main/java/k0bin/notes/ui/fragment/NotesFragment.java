package k0bin.notes.ui.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.card.MaterialCardView;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

import androidx.navigation.Navigation;
import k0bin.notes.R;
import k0bin.notes.ui.activity.MainActivity;
import k0bin.notes.ui.adapter.DrawerAdapter;
import k0bin.notes.ui.adapter.NotesAdapter;
import k0bin.notes.viewModel.NotesViewModel;


/**
 * Fragment that lists all notes
 */
public class NotesFragment extends Fragment implements MainActivity.BackFragment {
    private NotesViewModel viewModel;
    private int overlayColor = 0;

    private boolean isDrawerVisible = false;
    private FrameLayout overlay;
    private MaterialCardView drawer;
    private BottomSheetBehavior drawerBehavior;
    private ObjectAnimator overlayAnimator;
    private int animationDuration;

    public NotesFragment() {}

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getContext() != null) {
            overlayColor = ContextCompat.getColor(getContext(), R.color.drawerOverlay);
            animationDuration = getContext().getResources().getInteger(android.R.integer.config_shortAnimTime);
        }

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

        final RecyclerView recycler = view.findViewById(R.id.recycler);
        final NotesAdapter adapter = new NotesAdapter();
        recycler.setHasFixedSize(true);
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


        //Navigation drawer
        final RecyclerView drawerRecycler = view.findViewById(R.id.drawerRecycler);
        final DrawerAdapter drawerAdapter = new DrawerAdapter(viewModel);
        drawerRecycler.setAdapter(drawerAdapter);
        viewModel.getTags().observe(this, drawerAdapter::submitList);

        drawer = view.findViewById(R.id.drawerSheet);
        drawerBehavior = BottomSheetBehavior.from(drawer);
        BottomSheetBehavior.from(drawer).setState(BottomSheetBehavior.STATE_HIDDEN);

        overlay = view.findViewById(R.id.overlay);
        overlay.setOnClickListener(v -> {
            setDrawerVisibility(false);
        });

        overlayAnimator = ObjectAnimator.ofArgb(overlay, "backgroundColor", 0, overlayColor);
        overlayAnimator.setDuration(animationDuration);
        overlayAnimator.addListener(new Animator.AnimatorListener() {
            private boolean isReversed = false;

            @Override
            public void onAnimationStart(Animator animator) {
                isReversed = overlay.getVisibility() == View.VISIBLE;
                if (!isReversed) {
                    overlay.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onAnimationEnd(Animator animator) {
                if (isReversed) {
                    overlay.setVisibility(View.GONE);
                }
            }
            @Override
            public void onAnimationCancel(Animator animator) {}
            @Override
            public void onAnimationRepeat(Animator animator) {}
        });

        final BottomAppBar appBar = view.findViewById(R.id.bottomBar);
        appBar.setNavigationOnClickListener(v -> {
            setDrawerVisibility(true);
        });

    }

    private void setDrawerVisibility(boolean isVisible) {
        if (isDrawerVisible == isVisible) {
            return;
        }
        drawerBehavior.setState(isVisible ? BottomSheetBehavior.STATE_COLLAPSED : BottomSheetBehavior.STATE_HIDDEN);
        if (isVisible) {
            overlayAnimator.start();
        } else {
            overlayAnimator.reverse();
        }
        isDrawerVisible = isVisible;
    }

    @Override
    public boolean goBack() {
        if (isDrawerVisible) {
            setDrawerVisibility(false);
            return true;
        }
        return false;
    }
}
