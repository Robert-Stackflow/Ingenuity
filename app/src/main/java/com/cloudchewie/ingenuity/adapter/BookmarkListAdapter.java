package com.cloudchewie.ingenuity.adapter;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.KeyboardUtils;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.entity.Bookmark;
import com.cloudchewie.ingenuity.entity.BookmarkGroup;
import com.cloudchewie.ingenuity.entity.ListBottomSheetBean;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;

import java.util.Arrays;

public class BookmarkListAdapter extends RecyclerView.Adapter<BookmarkListAdapter.BookmarkViewHolder> {
    private final Context context;
    private BookmarkGroup parentGroup;

    public interface OnRootFolderOperationListener {
        void onClick(BookmarkViewHolder holder, BookmarkGroup parent, BookmarkGroup childGroup);

        void onRename(BookmarkViewHolder holder, BookmarkGroup parent, BookmarkGroup childGroup);

        void onDelete(BookmarkViewHolder holder, BookmarkGroup parent, BookmarkGroup childGroup);

        void onExport(BookmarkViewHolder holder, BookmarkGroup parent, BookmarkGroup childGroup);

        void onShare(BookmarkViewHolder holder, BookmarkGroup parent, BookmarkGroup childGroup);
    }

    public interface OnFolderOperationListener {
        void onClick(BookmarkViewHolder holder, BookmarkGroup parent, BookmarkGroup childGroup);

        void onRename(BookmarkViewHolder holder, BookmarkGroup parent, BookmarkGroup childGroup);

        void onDelete(BookmarkViewHolder holder, BookmarkGroup parent, BookmarkGroup childGroup);

        void onMove(BookmarkViewHolder holder, BookmarkGroup parent, BookmarkGroup childGroup);

        void onExport(BookmarkViewHolder holder, BookmarkGroup parent, BookmarkGroup childGroup);

        void onShare(BookmarkViewHolder holder, BookmarkGroup parent, BookmarkGroup childGroup);

    }

    public interface OnBookmarkOperationListener {
        void onOpen(BookmarkViewHolder holder, BookmarkGroup parent, Bookmark childItem);

        void onEdit(BookmarkViewHolder holder, BookmarkGroup parent, Bookmark childItem);

        void onDelete(BookmarkViewHolder holder, BookmarkGroup parent, Bookmark childItem);

        void onMove(BookmarkViewHolder holder, BookmarkGroup parent, Bookmark childItem);

        void onCopyUrl(BookmarkViewHolder holder, BookmarkGroup parent, Bookmark childItem);

        void onShare(BookmarkViewHolder holder, BookmarkGroup parent, Bookmark childItem);
    }

    private OnRootFolderOperationListener onRootFolderOperationListener;
    private OnFolderOperationListener onFolderOperationListener;
    private OnBookmarkOperationListener onBookmarkOperationListener;

    public BookmarkListAdapter(Context context, BookmarkGroup parentGroup) {
        this.parentGroup = parentGroup;
        this.context = context;
    }

    @NonNull
    @Override
    public BookmarkViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_bookmark, parent, false);
        return new BookmarkViewHolder(view);
    }

    public BookmarkGroup getParentGroup() {
        return parentGroup;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(BookmarkGroup parentGroup) {
        this.parentGroup = parentGroup;
        notifyDataSetChanged();
    }

    public void setOnRootFolderOperationListener(OnRootFolderOperationListener onRootFolderOperationListener) {
        this.onRootFolderOperationListener = onRootFolderOperationListener;
    }

    public void setOnFolderOperationListener(OnFolderOperationListener onFolderOperationListener) {
        this.onFolderOperationListener = onFolderOperationListener;
    }

    public void setOnBookmarkOperationListener(OnBookmarkOperationListener onBookmarkOperationListener) {
        this.onBookmarkOperationListener = onBookmarkOperationListener;
    }

    @Override
    public void onBindViewHolder(@NonNull BookmarkViewHolder holder, int position) {
        if (null == parentGroup) {
            return;
        }
        if (position < 0 || position >= parentGroup.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Object o = parentGroup.get(position);
        if (null == o) {
            return;
        }
        if (o instanceof Bookmark) {
            Bookmark childItem = (Bookmark) o;
            holder.nameView.setText(childItem.getName());
            holder.iconView.setImageResource(R.drawable.ic_light_service);
            holder.mItemView.setOnClickListener(view -> {
                if (onBookmarkOperationListener != null)
                    onBookmarkOperationListener.onOpen(holder, parentGroup, childItem);
            });
            holder.mItemView.setOnLongClickListener(view -> {
                if (context instanceof Activity)
                    KeyboardUtils.hideSoftInput((Activity) context);
                ListBottomSheet bottomSheet = new ListBottomSheet(context, ListBottomSheetBean.strToBean(Arrays.asList(context.getResources().getStringArray(R.array.bookmark_operation))));
                bottomSheet.setOnItemClickedListener(pos -> {
                    switch (pos) {
                        case 0:
                            if (onBookmarkOperationListener != null)
                                onBookmarkOperationListener.onOpen(holder, parentGroup, childItem);
                            break;
                        case 1:
                            if (onBookmarkOperationListener != null)
                                onBookmarkOperationListener.onEdit(holder, parentGroup, childItem);
                            break;
                        case 2:
                            if (onBookmarkOperationListener != null)
                                onBookmarkOperationListener.onDelete(holder, parentGroup, childItem);
                            break;
                        case 3:
                            if (onBookmarkOperationListener != null)
                                onBookmarkOperationListener.onCopyUrl(holder, parentGroup, childItem);
                            break;
                        case 4:
                            if (onBookmarkOperationListener != null)
                                onBookmarkOperationListener.onShare(holder, parentGroup, childItem);
                            break;
                        case 5:
                            if (onBookmarkOperationListener != null)
                                onBookmarkOperationListener.onMove(holder, parentGroup, childItem);
                            break;
                        case 6:
                            break;
                    }
                    bottomSheet.dismiss();
                });
                bottomSheet.show();
                return false;
            });
        } else if (o instanceof BookmarkGroup) {
            BookmarkGroup childGroup = (BookmarkGroup) o;
            holder.nameView.setText(childGroup.getName());
            holder.iconView.setImageResource(R.drawable.ic_light_folder);
            if (childGroup.isRoot()) {
                holder.mItemView.setOnClickListener(view -> {
                    if (onRootFolderOperationListener != null)
                        onRootFolderOperationListener.onClick(holder, parentGroup, childGroup);
                });
                holder.mItemView.setOnLongClickListener(view -> {
                    if (context instanceof Activity)
                        KeyboardUtils.hideSoftInput((Activity) context);
                    ListBottomSheet bottomSheet = new ListBottomSheet(context, ListBottomSheetBean.strToBean(Arrays.asList(context.getResources().getStringArray(R.array.root_bookmark_group_operation))));
                    bottomSheet.setOnItemClickedListener(pos -> {
                        switch (pos) {
                            case 0:
                                if (onRootFolderOperationListener != null)
                                    onRootFolderOperationListener.onRename(holder, parentGroup, childGroup);
                                break;
                            case 1:
                                if (onRootFolderOperationListener != null)
                                    onRootFolderOperationListener.onDelete(holder, parentGroup, childGroup);
                                break;
                            case 2:
                                if (onRootFolderOperationListener != null)
                                    onRootFolderOperationListener.onExport(holder, parentGroup, childGroup);
                                break;
                            case 3:
                                if (onRootFolderOperationListener != null)
                                    onRootFolderOperationListener.onShare(holder, parentGroup, childGroup);
                                break;
                        }
                        bottomSheet.dismiss();
                    });
                    bottomSheet.show();
                    return false;
                });
            } else {
                holder.mItemView.setOnClickListener(view -> {
                    if (onFolderOperationListener != null)
                        onFolderOperationListener.onClick(holder, parentGroup, childGroup);
                });
                holder.mItemView.setOnLongClickListener(view -> {
                    if (context instanceof Activity)
                        KeyboardUtils.hideSoftInput((Activity) context);
                    ListBottomSheet bottomSheet = new ListBottomSheet(context, ListBottomSheetBean.strToBean(Arrays.asList(context.getResources().getStringArray(R.array.bookmark_group_operation))));
                    bottomSheet.setOnItemClickedListener(pos -> {
                        switch (pos) {
                            case 0:
                                if (onFolderOperationListener != null)
                                    onFolderOperationListener.onRename(holder, parentGroup, childGroup);
                                break;
                            case 1:
                                if (onFolderOperationListener != null)
                                    onFolderOperationListener.onDelete(holder, parentGroup, childGroup);
                                break;
                            case 2:
                                if (onFolderOperationListener != null)
                                    onFolderOperationListener.onExport(holder, parentGroup, childGroup);
                                break;
                            case 3:
                                if (onFolderOperationListener != null)
                                    onFolderOperationListener.onShare(holder, parentGroup, childGroup);
                                break;
                            case 4:
                                if (onFolderOperationListener != null)
                                    onFolderOperationListener.onMove(holder, parentGroup, childGroup);
                                break;
                        }
                        bottomSheet.dismiss();
                    });
                    bottomSheet.show();
                    return false;
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        return parentGroup == null ? 0 : parentGroup.size();
    }

    public static class BookmarkViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public ImageView iconView;
        public TextView nameView;

        public BookmarkViewHolder(View view) {
            super(view);
            mItemView = view;
            iconView = mItemView.findViewById(R.id.item_bookmark_icon);
            nameView = mItemView.findViewById(R.id.item_bookmark_name);
        }
    }
}
