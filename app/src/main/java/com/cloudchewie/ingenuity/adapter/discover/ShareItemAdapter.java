package com.cloudchewie.ingenuity.adapter.discover;

import static com.cloudchewie.util.ui.SizeUtil.dp2px;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.bean.ShareItem;
import com.cloudchewie.ui.custom.VerticalIconTextItem;

import java.util.List;

public class ShareItemAdapter extends RecyclerView.Adapter<ShareItemAdapter.MyViewHolder> {
    private List<ShareItem> shareItemList;
    private Context context;

    public ShareItemAdapter(List<ShareItem> shareItemList, Context context) {
        this.shareItemList = shareItemList;
        this.context = context;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        VerticalIconTextItem view = new VerticalIconTextItem(context);
        return new ShareItemAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        if (null == shareItemList) {
            return;
        }
        if (position < 0 || position >= shareItemList.size()) {
            return;
        }
        if (null == holder) {
            return;
        }
        final ShareItem shareItem = shareItemList.get(position);
        if (null == shareItem) {
            return;
        }
        holder.mainView.setIcon(shareItem.getIcon());
        holder.mainView.setText(shareItem.getName());
        holder.mainView.setIconBackground(R.drawable.shape_round_dp10);
        holder.mainView.setIconSize(dp2px(context, 50));
        holder.mainView.setSpacing(dp2px(context, 10));
        holder.mainView.setScaleType(0);
    }

    @Override
    public int getItemCount() {
        return shareItemList.size();
    }

    static class MyViewHolder extends RecyclerView.ViewHolder {
        public VerticalIconTextItem mainView;

        public MyViewHolder(View view) {
            super(view);
            mainView = (VerticalIconTextItem) view;
        }
    }
}
