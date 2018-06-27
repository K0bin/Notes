package k0bin.notes.model;

import android.support.annotation.NonNull;

import java.util.Objects;

public class FilterTag {
    @NonNull
    private Tag tag;
    private boolean isActive;

    public FilterTag(@NonNull Tag tag, boolean isActive) {
        this.tag = tag;
        this.isActive = isActive;
    }

    @NonNull
    public Tag getTag() {
        return tag;
    }

    public boolean isActive() {
        return isActive;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        FilterTag filterTag = (FilterTag) o;
        return isActive == filterTag.isActive &&
                Objects.equals(tag, filterTag.tag);
    }

    @Override
    public int hashCode() {
        return Objects.hash(tag, isActive);
    }

    @Override
    public String toString() {
        return "FilterTag{" +
                "tag=" + tag +
                ", isActive=" + isActive +
                '}';
    }
}
