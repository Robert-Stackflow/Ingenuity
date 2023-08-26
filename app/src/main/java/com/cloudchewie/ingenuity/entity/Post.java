/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:39
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

import com.cloudchewie.ingenuity.util.image.ImageUrlUtil;
import com.lzy.imagepicker.bean.ImageItem;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Post implements Serializable, Content {
    long postId;
    long authorId;
    String content;
    List<String> images;
    long attractionId;
    long topicId;
    int commentCount;
    int thumbupCount;
    int collectionCount;
    POST_TYPE type;
    Date gmtCreate;
    Date gmtEdit;
    //冗余属性
    User user;
    Topic topic;
    Attraction attraction;
    List<ImageItem> imageItems;

    public Post() {
    }

    public Post(User user, Date date, String content, int commentCount, int thumbupCount, int collectionCount, Attraction attraction, Topic topic) {
        this.user = user;
        this.gmtCreate = date;
        this.gmtEdit = date;
        this.content = content;
        this.commentCount = commentCount;
        this.thumbupCount = thumbupCount;
        this.collectionCount = collectionCount;
        this.attraction = attraction;
        this.topic = topic;
        this.images = ImageUrlUtil.getUrls(15);
    }

    public long getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public List<ImageItem> getImageItems() {
        return imageItems;
    }

    public void setImageItems(List<ImageItem> imageItems) {
        this.imageItems = imageItems;
    }

    public long getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public long getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(int attractionId) {
        this.attractionId = attractionId;
    }

    public long getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
    }

    public Date getGmtCreate() {
        return gmtCreate;
    }

    public void setGmtCreate(Date gmtCreate) {
        this.gmtCreate = gmtCreate;
    }

    @Override
    public int getContentId() {
        return (int) getPostId();
    }

    @Override
    public String getTitle() {
        return "";
    }

    public Date getGmtEdit() {
        return gmtEdit;
    }

    public void setGmtEdit(Date gmtEdit) {
        this.gmtEdit = gmtEdit;
    }

    public POST_TYPE getType() {
        return type;
    }

    public void setType(POST_TYPE type) {
        this.type = type;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public List<String> getImages() {
        return images;
    }

    public void setImages(List<String> images) {
        this.images = images;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public int getThumbupCount() {
        return thumbupCount;
    }

    public void setThumbupCount(int thumbupCount) {
        this.thumbupCount = thumbupCount;
    }

    public int getCollectionCount() {
        return collectionCount;
    }

    public void setCollectionCount(int collectionCount) {
        this.collectionCount = collectionCount;
    }

    public Attraction getAttraction() {
        return attraction;
    }

    public void setAttraction(Attraction attraction) {
        this.attraction = attraction;
    }

    public Topic getTopic() {
        return topic;
    }

    public void setTopic(Topic topics) {
        this.topic = topics;
    }

    @Override
    public List<String> getImageUrls() {
        return getImages();
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NonNull
    @Override
    public String toString() {
        return "Post{" + "postId=" + postId + ", authorId=" + authorId + ", content='" + content + '\'' + ", imageUrls=" + images + ", attractionId=" + attractionId + ", topicId=" + topicId + ", commentCount=" + commentCount + ", thumbupCount=" + thumbupCount + ", collectionCount=" + collectionCount + ", type=" + type + ", gmtCreate=" + gmtCreate + ", gmtEdit=" + gmtEdit + ", user=" + user + ", topic=" + topic + ", attraction=" + attraction + ", imageItems=" + imageItems + '}';
    }

    enum POST_TYPE {
        TEXT, TEXT_IMAGE, TEXT_WEB, VIDEO
    }
}
