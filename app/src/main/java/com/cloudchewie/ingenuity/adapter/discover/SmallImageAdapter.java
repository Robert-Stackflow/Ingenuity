package com.cloudchewie.ingenuity.adapter.discover;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.util.image.ImageViewInfo;
import com.cloudchewie.ui.ninegrid.GridImageView;
import com.lzy.imagepicker.bean.ImageItem;
import com.previewlibrary.GPreviewBuilder;

import java.util.ArrayList;
import java.util.List;

public class SmallImageAdapter extends RecyclerView.Adapter<SmallImageAdapter.MyViewHolder> {
    private ArrayList<ImageItem> imageItems;
    private Context context;
    private OnImageClickListener onImageClickListener;
    private OnImageRemovedListener onImageRemovedListener;

    public SmallImageAdapter(ArrayList<ImageItem> imageItems, Context context) {
        this.imageItems = imageItems;
        this.context = context;
    }

    public void setOnImageRemovedListener(OnImageRemovedListener onImageRemovedListener) {
        this.onImageRemovedListener = onImageRemovedListener;
    }

    public void setOnImageClickListener(OnImageClickListener onImageClickListener) {
        this.onImageClickListener = onImageClickListener;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_small_image, parent, false);
        return new MyViewHolder(view);
    }

    public ArrayList<ImageItem> getImageItems() {
        return imageItems;
    }

    @Override
    public void onBindViewHolder(@NonNull SmallImageAdapter.MyViewHolder holder, int position) {
        if (null == imageItems) {
            return;
        }
        if (position < 0 || position >= imageItems.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final ImageItem imageItem = imageItems.get(position);
        if (null == imageItem) {
            return;
        }
        holder.imageView.setOnClickListener(v -> {
            if (onImageClickListener != null)
                onImageClickListener.onImageClicked((ImageView) v);
            List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
            for (ImageItem item : imageItems) {
                ImageViewInfo viewInfo = new ImageViewInfo(item.path);
                mThumbViewInfoList.add(viewInfo);
            }
            int pos = holder.getAdapterPosition();
            if (pos >= 0 && pos < getItemCount())
                GPreviewBuilder.from(context).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(pos).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
        Glide.with(context).load(imageItem.path).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).centerCrop().into(holder.imageView);
        holder.closeButton.setOnClickListener(v -> {
            holder.imageView.setClickable(false);
            if (holder.getAdapterPosition() >= 0 && holder.getAdapterPosition() < getItemCount()) {
                imageItems.remove(holder.getAdapterPosition());
                notifyItemRemoved(holder.getAdapterPosition());
                if (onImageRemovedListener != null)
                    onImageRemovedListener.onImageRemoved(imageItems);
            }
            holder.imageView.setClickable(true);
        });
    }

    @Override
    public int getItemCount() {
        return imageItems.size();
    }

    public interface OnImageClickListener {
        void onImageClicked(ImageView imageView);
    }

    public interface OnImageRemovedListener {
        void onImageRemoved(ArrayList<ImageItem> imageItems);
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View itemView;
        public GridImageView imageView;
        public ImageView closeButton;

        public MyViewHolder(View view) {
            super(view);
            this.itemView = view;
            this.imageView = view.findViewById(R.id.item_small_image_src);
            this.closeButton = view.findViewById(R.id.item_small_image_delete);
        }
    }
}

