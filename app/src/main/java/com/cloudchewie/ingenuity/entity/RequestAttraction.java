package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

import com.lzy.imagepicker.bean.ImageItem;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class RequestAttraction implements Serializable {
    int authorId;
    Attraction attraction;
    String reason;
    List<String> imageUrls;
    List<ImageItem> imageItems;
    Date gmtCreate;
    Date gmtEdit;

    public RequestAttraction(Attraction attraction, String reason, List<String> imageUrls) {
        this.attraction = attraction;
        this.reason = reason;
        this.imageUrls = imageUrls;
    }

    public RequestAttraction() {
    }

    public List<ImageItem> getImageItems() {
        return imageItems;
    }

    public void setImageItems(List<ImageItem> imageItems) {
        this.imageItems = imageItems;
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

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    @NonNull
    @Override
    public String toString() {
        return "RequestAttraction{" +
                "attraction=" + attraction +
                ", reason='" + reason + '\'' +
                ", imageUrls='" + imageUrls + '\'' +
                '}';
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
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
