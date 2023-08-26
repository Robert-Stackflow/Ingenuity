package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

import com.lzy.imagepicker.bean.ImageItem;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RequestTopic implements Serializable {
    int authorId;
    Date gmtCreate;
    Date gmtEdit;
    Topic topic;
    String reason;
    List<String> imageUrls;
    List<ImageItem> imageItems;

    public RequestTopic() {
    }

    public RequestTopic(Topic topic, String reason, List<String> imageUrls) {
        this.topic = topic;
        this.reason = reason;
        this.imageUrls = imageUrls;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public Date getGmtEdit() {
        return gmtEdit;
    }

    public void setGmtEdit(Date gmtEdit) {
        this.gmtEdit = gmtEdit;
    }

    public List<ImageItem> getImageItems() {
        return imageItems;
    }

    public void setImageItems(List<ImageItem> imageItems) {
        this.imageItems = imageItems;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    @NonNull
    @Override
    public String toString() {
        return "RequestTopic{" +
                "topic=" + topic +
                ", reason='" + reason + '\'' +
                ", imageUrls='" + imageUrls + '\'' +
                '}';
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }
}
