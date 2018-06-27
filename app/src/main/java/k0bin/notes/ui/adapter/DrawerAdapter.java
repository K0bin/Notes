package k0bin.notes.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.recyclerview.extensions.ListAdapter;
import android.support.v7.util.DiffUtil;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import k0bin.notes.R;
import k0bin.notes.model.FilterTag;
import k0bin.notes.model.NoteWithTags;
import k0bin.notes.model.Tag;
import k0bin.notes.ui.viewHolder.DrawerViewHolder;
import k0bin.notes.ui.viewHolder.NoteViewHolder;
import k0bin.notes.viewModel.NotesViewModel;

public class DrawerAdapter extends ListAdapter<FilterTag, DrawerViewHolder> {
    @NonNull
    private final NotesViewModel viewModel;

    public DrawerAdapter(@NonNull NotesViewModel viewModel) {
        super(DIFF_CALLBACK);

        this.setHasStableIds(true);
        this.viewModel = viewModel;
    }

    @NonNull
    @Override
    public DrawerViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int position) {
        return new DrawerViewHolder(viewModel, LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.view_drawer_tag, viewGroup, false));
    }

    @Override
    public void onBindViewHolder(@NonNull DrawerViewHolder drawerViewHolder, int position) {
        drawerViewHolder.bind(getItem(position));
    }

    private static final DiffUtil.ItemCallback<FilterTag> DIFF_CALLBACK = new DiffUtil.ItemCallback<FilterTag>() {
        @Override
        public boolean areItemsTheSame(@NonNull FilterTag tag1, @NonNull FilterTag tag2) {
            return tag1.getTag().getName().equals(tag2.getTag().getName());
        }

        @Override
        public boolean areContentsTheSame(@NonNull FilterTag tag1, @NonNull FilterTag tag2) {
            return tag1.equals(tag2);
        }
    };
}
