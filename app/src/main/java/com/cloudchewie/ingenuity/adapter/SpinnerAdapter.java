package com.cloudchewie.ingenuity.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.ingenuity.R;

import java.util.List;

public class SpinnerAdapter extends RecyclerView.Adapter<SpinnerAdapter.MyViewHolder> {
    List<String> stringList;
    Context context;
    onItemClickedListener listener;
    int selection = -1;

    public SpinnerAdapter(Context context, List<String> stringList) {
        this.stringList = stringList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_view_item, parent, false);
        return new MyViewHolder(view);
    }

    public SpinnerAdapter setOnItemClickListener(onItemClickedListener listener) {
        this.listener = listener;
        return this;
    }

    public SpinnerAdapter setSelection(int position) {
        this.selection = position;
        notifyItemChanged(selection);
        return this;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (null == stringList) {
            return;
        }
        if (position < 0 || position >= stringList.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final String string = stringList.get(position);
        if (null == string) {
            return;
        }
        holder.textView.setText(string);
        holder.textView.setActivated(selection == position);
        holder.mItemView.setOnClickListener(v -> {
            if (listener != null)
                listener.onItemClicked(position);
            holder.textView.setActivated(true);
            notifyItemChanged(position);
        });
    }

    @Override
    public int getItemCount() {
        return stringList == null ? 0 : stringList.size();
    }

    public interface onItemClickedListener {
        void onItemClicked(int position);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public TextView textView;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            textView = view.findViewById(R.id.list_view_item_text);
        }
    }
}
