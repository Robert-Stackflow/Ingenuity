/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 10:30:39
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.adapter.profile;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.user.HomePageActivity;
import com.cloudchewie.ingenuity.bean.ListBottomSheetBean;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.ingenuity.request.UserOperationRequest;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;
import com.cloudchewie.ui.custom.HorizontalIconTextItem;
import com.cloudchewie.ui.custom.MyDialog;
import com.cloudchewie.ui.general.CircleImageView;

import java.util.Arrays;
import java.util.List;

public class UserListAdapter extends RecyclerView.Adapter<UserListAdapter.MyViewHolder> {
    private List<User> users;
    private Context context;

    public UserListAdapter(Context context, List<User> users) {
        this.users = users;
        this.context = context;
    }

    @NonNull
    @Override
    public UserListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_user_item, parent, false);
        return new UserListAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserListAdapter.MyViewHolder holder, int position) {
        if (null == users) {
            return;
        }
        if (position < 0 || position >= users.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final User user = users.get(position);
        if (null == user) {
            return;
        }
        holder.followButton.setOnClickListener(v -> {
            Button button = (Button) v;
            if (v.isActivated()) {
                List<String> list = Arrays.asList(context.getResources().getStringArray(R.array.follow_operations));
                ListBottomSheet listBottomSheet = new ListBottomSheet(context, ListBottomSheetBean.strToBean(list));
                listBottomSheet.setOnItemClickedListener(pos -> {
                    listBottomSheet.dismiss();
                    if (pos == 0) {
                        button.setText("特别关注");
                        UserOperationRequest.special(user.getUserId());
                    } else if (pos == 1) {
                        MyDialog dialog = new MyDialog(context);
                        dialog.setTitle("取消关注");
                        dialog.setPositive("确认");
                        dialog.setNegtive("取消");
                        dialog.setMessage("取消关注用户" + user.getNickname() + "?");
                        dialog.setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
                            @Override
                            public void onPositiveClick() {
                                setButtonChecked(button, false);
                                UserOperationRequest.unFollow(user.getUserId());
                                dialog.dismiss();
                            }

                            @Override
                            public void onNegtiveClick() {
                                dialog.dismiss();
                            }

                            @Override
                            public void onCloseClick() {
                                dialog.dismiss();
                            }
                        });
                        dialog.show();
                    }
                });
                listBottomSheet.show();
            } else {
                setButtonChecked(button, true);
            }
        });
        holder.mItemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, HomePageActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", user);
            intent.putExtras(bundle);
            context.startActivity(intent);
        });
        holder.nameView.setText(user.getNickname());
        if (user.getCurrentLocation() != null)
            holder.locationView.setText(user.getCurrentLocation().replace("市", ""));
        else holder.locationView.setVisibility(View.GONE);
        Glide.with(context).load(user.getAvatar()).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(holder.avatarView);
        holder.postView.setText(String.valueOf(((int) (Math.random() * 100))));
        holder.fansView.setText(String.valueOf(((int) (Math.random() * 10000))));
    }

    void setButtonChecked(Button button, boolean checked) {
        button.setActivated(checked);
        if (checked) {
            button.setText("已关注");
            button.setTextColor(context.getColor(R.color.color_accent));
            button.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_button));
            button.setBackgroundTintList(context.getColorStateList(R.color.tag_background));
        } else {
            button.setText("+ 关注");
            button.setTextColor(context.getColor(R.color.color_prominent));
            button.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_button_frame));
            button.setBackgroundTintList(context.getColorStateList(R.color.color_prominent));
        }
    }

    @Override
    public int getItemCount() {
        return users == null ? 0 : users.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView nameView;
        public HorizontalIconTextItem locationView;
        public HorizontalIconTextItem postView;
        public HorizontalIconTextItem fansView;
        public CircleImageView avatarView;
        public Button followButton;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            nameView = view.findViewById(R.id.user_item_username);
            locationView = view.findViewById(R.id.user_item_location);
            postView = view.findViewById(R.id.user_item_post_count);
            fansView = view.findViewById(R.id.user_item_fans_count);
            avatarView = view.findViewById(R.id.user_item_avatar);
            followButton = view.findViewById(R.id.user_item_follow);
        }
    }
}