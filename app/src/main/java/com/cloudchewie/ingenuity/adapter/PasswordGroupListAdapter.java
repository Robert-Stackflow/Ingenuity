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
import com.cloudchewie.ingenuity.entity.ListBottomSheetBean;
import com.cloudchewie.ingenuity.entity.PasswordGroup;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;

import java.util.Arrays;
import java.util.List;

public class PasswordGroupListAdapter extends RecyclerView.Adapter<PasswordGroupListAdapter.PasswordGroupViewHolder> {
    private final Context context;
    private List<PasswordGroup> passwordGroupList;

    public interface OnGroupOperationListener {
        void onClick(PasswordGroupListAdapter.PasswordGroupViewHolder holder, PasswordGroup passwordGroup);

        void onRename(PasswordGroupListAdapter.PasswordGroupViewHolder holder, PasswordGroup passwordGroup);

        void onEdit(PasswordGroupListAdapter.PasswordGroupViewHolder holder, PasswordGroup passwordGroup);

        void onDelete(PasswordGroupListAdapter.PasswordGroupViewHolder holder, PasswordGroup passwordGroup);

        void onExport(PasswordGroupListAdapter.PasswordGroupViewHolder holder, PasswordGroup passwordGroup);

        void onShare(PasswordGroupListAdapter.PasswordGroupViewHolder holder, PasswordGroup passwordGroup);
    }

    private OnGroupOperationListener onGroupOperationListener;

    public PasswordGroupListAdapter(Context context, List<PasswordGroup> passwordGroupList) {
        this.passwordGroupList = passwordGroupList;
        this.context = context;
    }

    public List<PasswordGroup> getPasswordGroupList() {
        return passwordGroupList;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setPasswordGroupList(List<PasswordGroup> passwordGroupList) {
        this.passwordGroupList = passwordGroupList;
        notifyDataSetChanged();
    }

    public OnGroupOperationListener getOnGroupOperationListener() {
        return onGroupOperationListener;
    }

    public void setOnGroupOperationListener(OnGroupOperationListener onGroupOperationListener) {
        this.onGroupOperationListener = onGroupOperationListener;
    }

    @NonNull
    @Override
    public PasswordGroupListAdapter.PasswordGroupViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_password_group, parent, false);
        return new PasswordGroupListAdapter.PasswordGroupViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PasswordGroupListAdapter.PasswordGroupViewHolder holder, int position) {
        if (null == passwordGroupList) {
            return;
        }
        if (position < 0 || position >= passwordGroupList.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final PasswordGroup passwordGroup = passwordGroupList.get(position);
        if (null == passwordGroup) {
            return;
        }
        holder.nameView.setText(passwordGroup.getName());
        holder.mItemView.setOnClickListener(view -> {
            if (onGroupOperationListener != null)
                onGroupOperationListener.onClick(holder, passwordGroup);
        });
        holder.mItemView.setOnLongClickListener(view -> {
            if (context instanceof Activity)
                KeyboardUtils.hideSoftInput((Activity) context);
            ListBottomSheet bottomSheet = new ListBottomSheet(context, ListBottomSheetBean.strToBean(Arrays.asList(context.getResources().getStringArray(R.array.password_group_operation))));
            bottomSheet.setOnItemClickedListener(pos -> {
                switch (pos) {
                    case 0:
                        if (onGroupOperationListener != null)
                            onGroupOperationListener.onRename(holder, passwordGroup);
                        break;
                    case 1:
                        if (onGroupOperationListener != null)
                            onGroupOperationListener.onEdit(holder, passwordGroup);
                        break;
                    case 2:
                        if (onGroupOperationListener != null)
                            onGroupOperationListener.onDelete(holder, passwordGroup);
                        break;
                    case 3:
                        if (onGroupOperationListener != null)
                            onGroupOperationListener.onExport(holder, passwordGroup);
                        break;
                    case 4:
                        if (onGroupOperationListener != null)
                            onGroupOperationListener.onShare(holder, passwordGroup);
                        break;
                }
                bottomSheet.dismiss();
            });
            bottomSheet.show();
            return false;
        });
    }

    @Override
    public int getItemCount() {
        return passwordGroupList == null ? 0 : passwordGroupList.size();
    }

    public static class PasswordGroupViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public ImageView iconView;
        public TextView nameView;

        public PasswordGroupViewHolder(View view) {
            super(view);
            mItemView = view;
            iconView = mItemView.findViewById(R.id.item_password_group_icon);
            nameView = mItemView.findViewById(R.id.item_password_group_name);
        }
    }
}

