/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 12:09:14
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Favorites implements Serializable, Cloneable {
    String name;
    int userId;
    int folderId;
    User user;
    String description;
    Date gmtCreate;
    Date gmtEdit;
    int isPublic;
    int thumbupCount;
    int itemCount;
    int visitedCount;
    int followCount;
    String cover;
    boolean isCollected = false;

    public Favorites() {
    }

    public Favorites(String name, User user, String description, String cover, Date createTime, boolean isPublic, int thumbupCount, int itemCount, int followCount, int visitedCount) {
        this.name = name;
        this.user = user;
        this.description = description;
        this.gmtCreate = createTime;
        this.isPublic = isPublic ? 1 : 0;
        this.thumbupCount = thumbupCount;
        this.itemCount = itemCount;
        this.followCount = followCount;
        this.visitedCount = visitedCount;
        this.cover = cover;
    }

    public boolean isCollected() {
        return isCollected;
    }

    public void setCollected(boolean collected) {
        isCollected = collected;
    }

    public Date getGmtEdit() {
        return gmtEdit;
    }

    public void setGmtEdit(Date gmtEdit) {
        this.gmtEdit = gmtEdit;
    }

    @NonNull
    @Override
    public String toString() {
        return "Favorites{" +
                "name='" + name + '\'' +
                ", userId=" + userId +
                ", folderId=" + folderId +
                ", description='" + description + '\'' +
                ", gmtCreate=" + gmtCreate +
                ", gmtEdit=" + gmtEdit +
                ", isPublic=" + isPublic +
                ", thumbupCount=" + thumbupCount +
                ", itemCount=" + itemCount +
                ", visitedCount=" + visitedCount +
                ", followCount=" + followCount +
                ", cover='" + cover + '\'' +
                ", isCollected=" + isCollected +
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public int getVisitedCount() {
        return visitedCount;
    }

    public void setVisitedCount(int visitedCount) {
        this.visitedCount = visitedCount;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    public int getIsPublic() {
        return isPublic;
    }

    public void setIsPublic(int isPublic) {
        this.isPublic = isPublic;
    }

    public boolean isPublic() {
        return isPublic == 1;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic ? 1 : 0;
    }

    public int getThumbupCount() {
        return thumbupCount;
    }

    public void setThumbupCount(int thumbupCount) {
        this.thumbupCount = thumbupCount;
    }

    public int getItemCount() {
        return itemCount;
    }

    public void setItemCount(int itemCount) {
        this.itemCount = itemCount;
    }

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    @NonNull
    @Override
    public Favorites clone() throws CloneNotSupportedException {
        return (Favorites) super.clone();
    }
}
