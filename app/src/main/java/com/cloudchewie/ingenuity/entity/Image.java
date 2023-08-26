package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Image implements Serializable {
    int userId;
    String url;
    Date time;

    @NonNull
    @Override
    public String toString() {
        return "Image{" +
                "userId=" + userId +
                ", url='" + url + '\'' +
                ", time=" + time +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
