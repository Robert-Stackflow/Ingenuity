package com.cloudchewie.ingenuity.adapter.discover;

import static com.cloudchewie.util.basic.StringUtil.handleLineBreaks;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.bean.ListBottomSheetBean;
import com.cloudchewie.ingenuity.entity.Content;
import com.cloudchewie.ingenuity.util.image.CornerTransformation;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;
import com.cloudchewie.ui.custom.HorizontalIconTextItem;

import java.util.Arrays;
import java.util.List;

public class CollectionListAdapter extends RecyclerView.Adapter<CollectionListAdapter.MyViewHolder> {
    private List<Content> contentList;
    private Context context;

    public CollectionListAdapter(Context context, List<Content> contentList) {
        this.contentList = contentList;
        this.context = context;
    }

    @NonNull
    @Override
    public CollectionListAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.widget_collection_item, parent, false);
        return new CollectionListAdapter.MyViewHolder(view);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setData(List<Content> contentList) {
        this.contentList = contentList;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(@NonNull CollectionListAdapter.MyViewHolder holder, int position) {
        if (null == contentList) {
            return;
        }
        if (position < 0 || position >= contentList.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final Content content = contentList.get(position);
        if (null == content) {
            return;
        }
        holder.moreView.setOnClickListener(v -> {
            List<String> strings = Arrays.asList(context.getResources().getStringArray(R.array.collection_operations));
            ListBottomSheet bottomSheet = new ListBottomSheet(context, ListBottomSheetBean.strToBean(strings));
            bottomSheet.setOnItemClickedListener(pos -> {
                if (pos == 0) {
                    contentList.remove(holder.getAdapterPosition());
                    notifyItemRemoved(holder.getAdapterPosition());
                }
                bottomSheet.dismiss();
            });
            bottomSheet.show();
        });
        holder.nameView.setText(content.getUser().getNickname());
        holder.thumbupView.setText(String.valueOf(content.getThumbupCount()));
        holder.commentView.setText(String.valueOf(content.getCommentCount()));
        holder.contentView.setText(handleLineBreaks(content.getContent()));
        holder.mainLayout.setBackground(AppCompatResources.getDrawable(context, R.drawable.shape_rect));
        holder.mainLayout.setBackgroundTintList(context.getColorStateList(R.color.color_selector_content));
        if (holder.imageView != null)
            Glide.with(context).load(content.getImageUrls().get(0)).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background).transform(CornerTransformation.getTransform(context, 5))).centerCrop().into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return contentList == null ? 0 : contentList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public View mItemView;
        public HorizontalIconTextItem nameView;
        public HorizontalIconTextItem thumbupView;
        public HorizontalIconTextItem commentView;
        public TextView contentView;
        public RelativeLayout mainLayout;
        public ImageView imageView;
        private ImageView moreView;

        public MyViewHolder(View view) {
            super(view);
            mItemView = view;
            mainLayout = view.findViewById(R.id.collection_item_layout);
            nameView = view.findViewById(R.id.collection_item_username);
            thumbupView = view.findViewById(R.id.collection_item_thumbup);
            commentView = view.findViewById(R.id.collection_item_comment);
            contentView = view.findViewById(R.id.collection_item_content);
            imageView = view.findViewById(R.id.collection_item_image);
            moreView = view.findViewById(R.id.collection_item_more);
        }
    }
}

