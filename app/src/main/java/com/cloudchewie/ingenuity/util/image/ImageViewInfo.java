/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 19:00:16
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.util.image;

import android.graphics.Rect;
import android.os.Parcel;

import androidx.annotation.Nullable;

import com.previewlibrary.enitity.IThumbViewInfo;

import java.io.Serializable;

public class ImageViewInfo implements IThumbViewInfo, Serializable {
    public static final Creator<ImageViewInfo> CREATOR = new Creator<ImageViewInfo>() {
        @Override
        public ImageViewInfo createFromParcel(Parcel source) {
            return new ImageViewInfo(source);
        }

        @Override
        public ImageViewInfo[] newArray(int size) {
            return new ImageViewInfo[size];
        }
    };
    private String url;
    private Rect mBounds = null;
    private String tag = "用户字段";
    private String videoUrl;

    public ImageViewInfo(String url) {
        this.url = url;
    }

    public ImageViewInfo(String videoUrl, String url) {
        this.url = url;
        this.videoUrl = videoUrl;
    }

    protected ImageViewInfo(Parcel in) {
        this.url = in.readString();
        this.mBounds = in.readParcelable(Rect.class.getClassLoader());
        this.tag = in.readString();
        this.videoUrl = in.readString();
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    @Override
    public String getUrl() {//将你的图片地址字段返回
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    @Override
    public Rect getBounds() {//将你的图片显示坐标字段返回
        return mBounds;
    }

    public void setBounds(Rect bounds) {
        mBounds = bounds;
    }

    @Nullable
    @Override
    public String getVideoUrl() {
        return videoUrl;
    }

    public void setVideoUrl(String videoUrl) {
        this.videoUrl = videoUrl;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.url);
        dest.writeParcelable(this.mBounds, flags);
        dest.writeString(this.tag);
        dest.writeString(this.videoUrl);
    }
}