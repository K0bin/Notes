package k0bin.notes.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.bottomappbar.BottomAppBar;
import android.support.design.chip.Chip;
import android.support.design.chip.ChipGroup;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.navigation.Navigation;
import java9.util.stream.StreamSupport;
import k0bin.notes.R;
import k0bin.notes.model.Tag;
import k0bin.notes.viewModel.EditViewModel;

public class EditFragment extends Fragment {
	private EditViewModel viewModel;

	private EditText titleEdit;
	private EditText textEdit;

	public EditFragment() {	}

	@Override
	public void onCreate(@Nullable Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		viewModel = ViewModelProviders.of(this).get(EditViewModel.class);
	}

	@Nullable
	@Override
	public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
		return inflater.inflate(R.layout.fragment_edit, container, false);
	}

    @Override
	public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);

		Bundle arguments = getArguments();
	    final int noteId = EditFragmentArgs.fromBundle(arguments != null ? arguments : Bundle.EMPTY).getNoteId();
	    viewModel.setNoteId(noteId);

		titleEdit = view.findViewById(R.id.titleEdit);
		viewModel.getTitle().observe(this, text -> {
			if (titleEdit.getText().toString().equals(text)) {
				return;
			}
			titleEdit.setText(text);
		});
		titleEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

			@Override
			public void afterTextChanged(Editable editable) {
				viewModel.setTitle(editable.toString());
			}
		});
		textEdit = view.findViewById(R.id.textEdit);
		viewModel.getText().observe(this,  text -> {
			if (textEdit.getText().toString().equals(text)) {
				return;
			}
			textEdit.setText(text);
		});
		textEdit.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}
			@Override
			public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

			@Override
			public void afterTextChanged(Editable editable) {
				viewModel.setText(editable.toString());
			}
		});

        final EditText labelText = view.findViewById(R.id.tagInput);

		final ImageView addTagButton = view.findViewById(R.id.addTagButton);
		addTagButton.setOnClickListener(v -> {
            viewModel.addTag(labelText.getText().toString());
            labelText.setText("");
        });

		final View.OnClickListener onTagClicked = v -> {
		    final Object viewTag = v.getTag();
		    if (viewTag instanceof Tag) {
                viewModel.removeTag((Tag)viewTag);
            }
        };

		final ChipGroup tagGroup = view.findViewById(R.id.tagChips);
		viewModel.getTags().observe(this, tags -> {
		    tagGroup.removeAllViews();
		    if (tags == null) {
		        return;
            }
            StreamSupport.stream(tags).sorted().forEachOrdered(tag -> {
                final Chip tagView = new Chip(tagGroup.getContext());
                tagView.setLayoutParams(new ChipGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT));
                tagView.setChipText(tag.getName());
                tagView.setTag(tag);
                tagView.setOnClickListener(onTagClicked);
                tagGroup.addView(tagView);
            });
        });

		final BottomAppBar bottomBar = view.findViewById(R.id.bottomBar);
		bottomBar.setNavigationOnClickListener (button -> Navigation.findNavController(view).navigateUp());
	}

	@Override
	public void onStop() {
		super.onStop();
		viewModel.save();
        hideKeyboard();
	}

	private void hideKeyboard() {
        if (getActivity() != null) {
            final View view = getActivity().getCurrentFocus();
            if (view != null) {
                final InputMethodManager imm = ContextCompat.getSystemService(getActivity(), InputMethodManager.class);
                if (imm != null) {
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
    }
}
