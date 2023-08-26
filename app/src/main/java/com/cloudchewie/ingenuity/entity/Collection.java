package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Collection implements Serializable {
    private int userId;
    private int folderId;
    private int postArticleId;
    private int type;
    private Date gmtCollect;

    public Collection() {

    }

    public Collection(int userId, int folderId, int postArticleId, Date gmtCollect, int type) {
        this.userId = userId;
        this.folderId = folderId;
        this.postArticleId = postArticleId;
        this.gmtCollect = gmtCollect;
        this.type = type;
    }

    public int typeInt() {
        return Integer.parseInt(String.valueOf(type));
    }

    public void setTypeInt(int type) {
        this.type = type;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return "Collection{" +
                "userId=" + userId +
                ", folderId=" + folderId +
                ", postArticleId=" + postArticleId +
                ", gmtCollect=" + gmtCollect +
                ", type='" + type + '\'' +
                '}';
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getFolderId() {
        return folderId;
    }

    public void setFolderId(int folderId) {
        this.folderId = folderId;
    }

    public int getPostArticleId() {
        return postArticleId;
    }

    public void setPostArticleId(int postArticleId) {
        this.postArticleId = postArticleId;
    }

    public Date getGmtCollect() {
        return gmtCollect;
    }

    public void setGmtCollect(Date gmtCollect) {
        this.gmtCollect = gmtCollect;
    }
}
