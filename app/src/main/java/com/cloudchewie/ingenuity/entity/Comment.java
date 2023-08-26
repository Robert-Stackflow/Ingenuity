/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:39
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Comment implements Serializable {
    //TODO 无效,引用为userId
    int userId;
    User user;
    Date date;
    String content;
    List<String> imageUrls;
    int thumbupCount;
    COMMENT_TYPE type;
    //TODO 无效引用为commentId;
    List<Comment> replies;
    public Comment() {
    }
    public Comment(User user, Date date, String content, List<String> imageUrls, int thumbupCount, COMMENT_TYPE type, List<Comment> replies) {
        this.user = user;
        this.date = date;
        this.content = content;
        this.imageUrls = imageUrls;
        this.thumbupCount = thumbupCount;
        this.type = type;
        this.replies = replies;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Comment> getReplies() {
        return replies;
    }

    public void setReplies(List<Comment> replies) {
        this.replies = replies;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(List<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public int getReplyCount() {
        return replies.size();
    }

    public int getThumbupCount() {
        return thumbupCount;
    }

    public void setThumbupCount(int thumbupCount) {
        this.thumbupCount = thumbupCount;
    }

    public COMMENT_TYPE getType() {
        return type;
    }

    public void setType(COMMENT_TYPE type) {
        this.type = type;
    }

    public enum COMMENT_TYPE {
        TEXT,
        TEXT_IMAGE
    }
}
