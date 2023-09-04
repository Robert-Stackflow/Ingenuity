package com.cloudchewie.ingenuity.adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ThreadUtils;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.entity.Password;
import com.cloudchewie.ingenuity.entity.PasswordGroup;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.cloudchewie.ingenuity.widget.PasswordLayout;
import com.cloudchewie.ui.custom.IToast;
import com.cloudchewie.util.system.ClipBoardUtil;
import com.cloudchewie.util.system.SharedPreferenceCode;
import com.cloudchewie.util.system.SharedPreferenceUtil;

import java.util.List;

public class PasswordListAdapter extends RecyclerView.Adapter<PasswordListAdapter.MyViewHolder> {
    private final Context context;
    private List<? extends Password> passwordList;

    public PasswordGroup passwordGroup;

    public PasswordGroup getPasswordGroup() {
        return passwordGroup;
    }

    public void setPasswordGroup(PasswordGroup passwordGroup) {
        this.passwordGroup = passwordGroup;
    }

    public void refreshData() {
        ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() {
                switch (passwordGroup.getType()) {
                    case AUTH:
                        passwordList = LocalStorage.getAppDatabase().authPasswordDao().get(passwordGroup.getId());
                        break;
                    case BACKUP:
                        passwordList = LocalStorage.getAppDatabase().backupPasswordDao().get(passwordGroup.getId());
                        break;
                    case COMMON:
                        passwordList = LocalStorage.getAppDatabase().commonPasswordDao().get(passwordGroup.getId());
                        break;
                }
                return null;
            }

            @Override
            public void onSuccess(String result) {
                setData(passwordList);
            }
        });
    }

    public PasswordListAdapter(Context context, PasswordGroup passwordGroup) {
        refreshData();
        this.passwordGroup = passwordGroup;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_password, parent, false);
        return new MyViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<? extends Password> contentList) {
        this.passwordList = contentList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (null == passwordList) {
            return;
        }
        if (position < 0 || position >= passwordList.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Password password = passwordList.get(position);
        if (null == password) {
            return;
        }
        holder.passwordLayout.bind(passwordGroup, password);
        holder.mItemView.setOnClickListener(view -> holder.passwordLayout.toggle());
        holder.mItemView.setOnLongClickListener(view -> {
            if (SharedPreferenceUtil.getBoolean(context, SharedPreferenceCode.LONG_PRESS_COPY_PASSCODE.getKey(), true)) {
                ClipBoardUtil.copy(password.getPassword());
                IToast.showBottom(context, context.getString(R.string.copy_success));
            }
            return false;
        });
        holder.mItemView.callOnClick();
    }

    @Override
    public int getItemCount() {
        return passwordList == null ? 0 : passwordList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public ImageView imageView;
        public PasswordLayout passwordLayout;

        public TextView codeView;
        public TextView issuerView;
        public TextView usernameView;


        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            passwordLayout = (PasswordLayout) view;
            imageView = mItemView.findViewById(R.id.item_password_image);
            codeView = mItemView.findViewById(R.id.item_password_code);
            issuerView = mItemView.findViewById(R.id.item_password_issuer);
            usernameView = mItemView.findViewById(R.id.item_password_username);
        }
    }
}
