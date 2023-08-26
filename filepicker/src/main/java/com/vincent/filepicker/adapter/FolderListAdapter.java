package com.vincent.filepicker.adapter;

import static com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions.withCrossFade;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.bumptech.glide.Glide;
import com.vincent.filepicker.R;
import com.vincent.filepicker.filter.entity.BaseFile;
import com.vincent.filepicker.filter.entity.Directory;

import java.util.ArrayList;
import java.util.List;


/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class FolderListAdapter<T extends BaseFile> extends BaseAdapter {

    private Activity mActivity;
    private LayoutInflater mInflater;
    private int mVideoSize;
    private List<Directory<T>> directoryList;
    private int lastSelected = 0;
    private boolean isAttachToSystem = false;

    public FolderListAdapter(Activity activity, List<Directory<T>> folders, boolean isAttachToSystem) {
        mActivity = activity;
        this.isAttachToSystem = isAttachToSystem;
        if (folders != null && folders.size() > 0) directoryList = folders;
        else directoryList = new ArrayList<>();
        mVideoSize = getImageItemWidth(mActivity);
        mInflater = (LayoutInflater) activity.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    private static int getImageItemWidth(@NonNull Activity activity) {
        int screenWidth = activity.getResources().getDisplayMetrics().widthPixels;
        int densityDpi = activity.getResources().getDisplayMetrics().densityDpi;
        int cols = screenWidth / densityDpi;
        cols = Math.max(cols, 3);
        int columnSpace = (int) (2 * activity.getResources().getDisplayMetrics().density);
        return (screenWidth - columnSpace * (cols - 1)) / cols;
    }

    public void refreshData(List<Directory<T>> folders) {
        if (folders != null && folders.size() > 0) directoryList = folders;
        else directoryList.clear();
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return directoryList.size();
    }

    @Override
    public Directory getItem(int position) {
        return directoryList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        if (convertView == null) {
            convertView = mInflater.inflate(R.layout.vm_layout_item_video_folder_list, parent, false);
            holder = new ViewHolder(convertView);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        if (isAttachToSystem)
            holder.itemView.setBackgroundTintList(mActivity.getColorStateList(R.color.color_selector_content));
        Directory<T> folder = getItem(position);
        holder.folderName.setText(folder.getName());
        holder.imageCount.setText("共" + folder.getFiles().size() + "个");
        if (folder.getFiles().size() > 0)
            Glide.with(mActivity).load(folder.getFiles().get(0).getPath()).centerCrop().transition(withCrossFade()).into(holder.cover);
        if (lastSelected == position) {
            holder.folderCheck.setVisibility(View.VISIBLE);
        } else {
            holder.folderCheck.setVisibility(View.INVISIBLE);
        }
        return convertView;
    }

    public int getSelectIndex() {
        return lastSelected;
    }

    public void setSelectIndex(int i) {
        if (lastSelected == i) {
            return;
        }
        lastSelected = i;
        notifyDataSetChanged();
    }

    private class ViewHolder {
        View itemView;
        ImageView cover;
        TextView folderName;
        TextView imageCount;
        ImageView folderCheck;

        public ViewHolder(@NonNull View view) {
            itemView = view;
            cover = view.findViewById(R.id.iv_cover);
            folderName = view.findViewById(R.id.tv_folder_name);
            imageCount = view.findViewById(R.id.tv_image_count);
            folderCheck = view.findViewById(R.id.iv_folder_check);
            view.setTag(this);
        }
    }

}
