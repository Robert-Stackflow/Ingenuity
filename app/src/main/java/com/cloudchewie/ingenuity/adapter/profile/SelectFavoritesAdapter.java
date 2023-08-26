package com.cloudchewie.ingenuity.adapter.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.entity.Favorites;

import java.util.ArrayList;

public class SelectFavoritesAdapter extends RecyclerView.Adapter<SelectFavoritesAdapter.MyViewHolder> {
    private ArrayList<Favorites> favoritesArrayList;
    private Context context;
    private SelectFavoritesAdapter.OnImageRemovedListener onImageRemovedListener;

    public SelectFavoritesAdapter(ArrayList<Favorites> favorites, Context context) {
        this.favoritesArrayList = new ArrayList<>();
        try {
            for (Favorites favorites1 : favorites)
                favoritesArrayList.add(favorites1.clone());
        } catch (CloneNotSupportedException ignored) {
        }
        this.context = context;
    }

    public void insert(Favorites newFavor) {
        favoritesArrayList.add(1, newFavor);
        notifyItemInserted(1);
    }

    public void setOnImageRemovedListener(SelectFavoritesAdapter.OnImageRemovedListener onImageRemovedListener) {
        this.onImageRemovedListener = onImageRemovedListener;
    }

    @NonNull
    @Override
    public SelectFavoritesAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_favorites, parent, false);
        return new SelectFavoritesAdapter.MyViewHolder(view);
    }

    public ArrayList<Favorites> getFavorites() {
        return favoritesArrayList;
    }

    @Override
    public void onBindViewHolder(@NonNull SelectFavoritesAdapter.MyViewHolder holder, int position) {
        if (null == favoritesArrayList) {
            return;
        }
        if (position < 0 || position >= favoritesArrayList.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Favorites favorites = this.favoritesArrayList.get(position);
        if (null == favorites) {
            return;
        }
        if (position == 0) holder.tagView.setVisibility(View.VISIBLE);
        else holder.tagView.setVisibility(View.GONE);
        holder.itemView.setOnClickListener(v -> holder.checkBox.toggle());
        holder.titleView.setText(favorites.getName());
        holder.describeView.setText(favorites.getItemCount() + "个内容 · " + (favorites.isPublic() ? "公开" : "仅自己可见"));
        holder.checkBox.setChecked(favorites.isCollected());
        holder.checkBox.setOnCheckedChangeListener((buttonView, isChecked) -> favorites.setCollected(isChecked));
    }

    @Override
    public int getItemCount() {
        return favoritesArrayList.size();
    }

    public interface OnImageRemovedListener {
        void onImageRemoved(ArrayList<Favorites> favorites);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public TextView titleView;
        public TextView describeView;
        public CheckBox checkBox;
        public TextView tagView;

        public MyViewHolder(View view) {
            super(view);
            this.itemView = view;
            this.titleView = view.findViewById(R.id.item_favorites_title);
            this.describeView = view.findViewById(R.id.item_favorites_describe);
            this.checkBox = view.findViewById(R.id.item_favorites_checkbox);
            tagView = view.findViewById(R.id.item_favorites_tag);
        }
    }
}

