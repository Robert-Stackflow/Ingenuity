/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 12:08:19
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.adapter.profile;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.create.CreateFavoritesActivity;
import com.cloudchewie.ingenuity.activity.profile.FavoritesDetailActivity;
import com.cloudchewie.ingenuity.bean.ListBottomSheetBean;
import com.cloudchewie.ingenuity.entity.Favorites;
import com.cloudchewie.ingenuity.request.FavoritesCreationRequest;
import com.cloudchewie.ingenuity.request.UserProfileRequest;
import com.cloudchewie.ingenuity.util.image.CornerTransformation;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;
import com.cloudchewie.ui.custom.HorizontalIconTextItem;
import com.cloudchewie.ui.custom.MyDialog;

import java.util.Arrays;
import java.util.List;

public class FavoritesListAdapter extends RecyclerView.Adapter<FavoritesListAdapter.MyViewHolder> {
    private List<Favorites> favoritesList;
    private Context context;
    private OnItemCountChangedListener onItemCountChangedListener;

    public FavoritesListAdapter(Context context, List<Favorites> favoritesList) {
        this.favoritesList = favoritesList;
        this.context = context;
    }

    public FavoritesListAdapter setOnItemCountChangedListener(OnItemCountChangedListener onItemCountChangedListener) {
        this.onItemCountChangedListener = onItemCountChangedListener;
        return this;
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setFavoritesList(List<Favorites> favoritesList) {
        this.favoritesList = favoritesList;
        notifyDataSetChanged();
        if (onItemCountChangedListener != null) {
            onItemCountChangedListener.onItemCountChanged(getItemCount());
        }
    }

    @NonNull
    @Override
    public FavoritesListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_favorites_item, parent, false);
        return new FavoritesListAdapter.MyViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull FavoritesListAdapter.MyViewHolder holder, int position) {
        if (null == favoritesList) {
            return;
        }
        if (position < 0 || position >= favoritesList.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Favorites favorites = favoritesList.get(position);
        if (null == favorites) {
            return;
        }
        if (favorites.getUser() == null) {
            favorites.setUser(UserProfileRequest.info(favorites.getUserId()));
        }
        ViewGroup.LayoutParams layoutParams = holder.itemView.getLayoutParams();
        layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT;
        holder.itemView.setLayoutParams(layoutParams);
        holder.nameView.setText(favorites.getName());
        if (position == 0) {
            holder.mItemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, FavoritesDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(CreateFavoritesActivity.EXTRA_FAVORITES, favorites);
                bundle.putBoolean(CreateFavoritesActivity.EXTRA_DEFAULT, true);
                intent.putExtras(bundle);
                context.startActivity(intent);
            });
            holder.moreView.setOnClickListener(v -> {
                List<String> strings = Arrays.asList(context.getResources().getStringArray(R.array.favorites_operations_without_delete));
                ListBottomSheet bottomSheet = new ListBottomSheet(context, ListBottomSheetBean.strToBean(strings));
                bottomSheet.setOnItemClickedListener(pos -> {
                    if (pos == 0) {
                        Intent intent = new Intent(context, CreateFavoritesActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(CreateFavoritesActivity.EXTRA_FAVORITES, favorites);
                        bundle.putBoolean(CreateFavoritesActivity.EXTRA_DEFAULT, true);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    }
                    bottomSheet.dismiss();
                });
                bottomSheet.show();
            });
        } else {
            holder.mItemView.setOnClickListener(v -> {
                Intent intent = new Intent(context, FavoritesDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable(CreateFavoritesActivity.EXTRA_FAVORITES, favorites);
                intent.putExtras(bundle);
                context.startActivity(intent);
            });
            holder.moreView.setOnClickListener(v -> {
                List<String> strings = Arrays.asList(context.getResources().getStringArray(R.array.favorites_operations));
                ListBottomSheet bottomSheet = new ListBottomSheet(context, ListBottomSheetBean.strToBean(strings));
                bottomSheet.setOnItemClickedListener(pos -> {
                    if (pos == 0) {
                        Intent intent = new Intent(context, CreateFavoritesActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable(CreateFavoritesActivity.EXTRA_FAVORITES, favorites);
                        intent.putExtras(bundle);
                        context.startActivity(intent);
                    } else if (pos == 1) {
                        MyDialog dialog = new MyDialog(context);
                        dialog.setTitle("删除收藏夹");
                        dialog.setMessage("确定要删除收藏夹《" + favorites.getName() + "》吗?");
                        dialog.setPositive("确认");
                        dialog.setNegtive("取消");
                        dialog.setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
                            @SuppressLint("NotifyDataSetChanged")
                            @Override
                            public void onPositiveClick() {
                                FavoritesCreationRequest.delete(favorites.getFolderId());
                                favoritesList.remove(holder.getAdapterPosition());
                                notifyItemRemoved(holder.getAdapterPosition());
                                if (onItemCountChangedListener != null)
                                    onItemCountChangedListener.onItemCountChanged(getItemCount());
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
                    bottomSheet.dismiss();
                });
                bottomSheet.show();
            });
        }
        if (favorites.getUser() != null)
            holder.authorView.setText("创建者: " + favorites.getUser().getNickname());
        else holder.authorView.setVisibility(View.GONE);
        holder.isPublicView.setText(favorites.isPublic() ? "公开" : "私密");
        holder.itemCount.setText(String.valueOf(favorites.getItemCount()));
        Glide.with(context).load(favorites.getCover()).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background).transform(CornerTransformation.getTransform(context, 5))).into(holder.coverView);
    }

    @Override
    public int getItemCount() {
        return favoritesList == null ? 0 : favoritesList.size();
    }

    public interface OnItemCountChangedListener {
        void onItemCountChanged(int newCount);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView nameView;
        public TextView authorView;
        public TextView isPublicView;
        public ImageView moreView;
        public HorizontalIconTextItem itemCount;
        public ImageView coverView;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            nameView = view.findViewById(R.id.favorites_item_name);
            authorView = view.findViewById(R.id.favorites_item_author);
            itemCount = view.findViewById(R.id.favorites_item_count);
            moreView = view.findViewById(R.id.favorites_item_more);
            isPublicView = view.findViewById(R.id.favorites_item_ispublic);
            coverView = view.findViewById(R.id.favorites_item_cover);
        }
    }
}
