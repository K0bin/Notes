package k0bin.notes.ui.viewHolder;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.CheckBox;
import android.widget.TextView;

import javax.annotation.Nullable;

import k0bin.notes.R;
import k0bin.notes.model.FilterTag;
import k0bin.notes.viewModel.NotesViewModel;

public class DrawerViewHolder extends RecyclerView.ViewHolder {
    private final TextView tagName;
    private final CheckBox tagCheckbox;

    private final NotesViewModel viewModel;

    private @Nullable FilterTag tag;

    public DrawerViewHolder(@NonNull NotesViewModel viewModel, @NonNull View itemView) {
        super(itemView);
        tagName = itemView.findViewById(R.id.tagName);
        tagCheckbox = itemView.findViewById(R.id.tagCheckbox);

        this.viewModel = viewModel;
        itemView.setOnClickListener(v -> {
            if (this.tag != null) {
                viewModel.toggleFilter(this.tag.getTag());
            }
        });
    }

    public void bind(@NonNull FilterTag tag) {
        this.tag = tag;
        tagName.setText(tag.getTag().getName());
        tagCheckbox.setChecked(tag.isActive());
    }
}
