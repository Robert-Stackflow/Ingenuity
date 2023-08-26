package com.vincent.filepicker.filter.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

/**
 * Created by Vincent Woo
 * Date: 2016/10/11
 * Time: 15:23
 */

public class VideoFile extends BaseFile implements Parcelable {
    public static final Creator<VideoFile> CREATOR = new Creator<VideoFile>() {
        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public VideoFile[] newArray(int size) {
            return new VideoFile[size];
        }

        @NonNull
        @Override
        public VideoFile createFromParcel(@NonNull Parcel in) {
            VideoFile file = new VideoFile();
            file.setId(in.readLong());
            file.setName(in.readString());
            file.setPath(in.readString());
            file.setSize(in.readLong());
            file.setBucketId(in.readString());
            file.setBucketName(in.readString());
            file.setDate(in.readLong());
            file.setSelected(in.readByte() != 0);
            file.setDuration(in.readLong());
            file.setThumbnail(in.readString());
            file.setMimeType(in.readString());
            return file;
        }
    };
    private long duration;
    private String thumbnail;
    private String mimeType;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeLong(getId());
        dest.writeString(getName());
        dest.writeString(getPath());
        dest.writeLong(getSize());
        dest.writeString(getBucketId());
        dest.writeString(getBucketName());
        dest.writeLong(getDate());
        dest.writeByte((byte) (isSelected() ? 1 : 0));
        dest.writeLong(getDuration());
        dest.writeString(getThumbnail());
        dest.writeString(getMimeType());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
