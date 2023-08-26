package com.vincent.filepicker.filter.entity;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

/**
 * Created by Vincent Woo
 * Date: 2016/10/12
 * Time: 14:45
 */

public class NormalFile extends BaseFile implements Parcelable {
    public static final Creator<NormalFile> CREATOR = new Creator<NormalFile>() {
        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public NormalFile[] newArray(int size) {
            return new NormalFile[size];
        }

        @NonNull
        @Override
        public NormalFile createFromParcel(@NonNull Parcel in) {
            NormalFile file = new NormalFile();
            file.setId(in.readLong());
            file.setName(in.readString());
            file.setPath(in.readString());
            file.setSize(in.readLong());
            file.setBucketId(in.readString());
            file.setBucketName(in.readString());
            file.setDate(in.readLong());
            file.setSelected(in.readByte() != 0);
            file.setMimeType(in.readString());
            return file;
        }
    };
    private String mimeType;

    public String getMimeType() {
        return mimeType;
    }

    public void setMimeType(String mimeType) {
        this.mimeType = mimeType;
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
        dest.writeString(getMimeType());
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
