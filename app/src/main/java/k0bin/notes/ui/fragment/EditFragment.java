package k0bin.notes.ui.fragment;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import k0bin.notes.R;
import k0bin.notes.model.Note;
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

		final int noteId = EditFragmentArgs.fromBundle(getArguments()).getNoteId();
		viewModel.setNoteId(noteId);
	}

	@Override
	public void onStop() {
		super.onStop();
		viewModel.save();
	}
}
