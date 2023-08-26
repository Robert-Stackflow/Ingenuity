package com.cloudchewie.ingenuity.entity;

import com.cloudchewie.util.ui.RichEditorUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Article implements Serializable, Content {
    int articleId;
    int authorId;
    int attractionId;
    int topicId;
    Date gmtCreate;
    Date gmtEdit;
    String title;
    String content;
    int commentCount;
    int thumbupCount;
    int collectionCount;
    Post.POST_TYPE type;
    //冗余属性
    User user;
    Attraction attraction;
    Topic topic;

    public Article() {
    }

    public Article(User user, Date date, String title, String content, int commentCount, int thumbupCount, int collectionCount, Attraction attraction, Topic topic) {
        this.user = user;
        this.gmtEdit = date;
        this.gmtCreate = date;
        this.title = title;
        this.content = content;
        this.commentCount = commentCount;
        this.thumbupCount = thumbupCount;
        this.collectionCount = collectionCount;
        this.attraction = attraction;
        this.topic = topic;
    }

    public int getArticleId() {
        return articleId;
    }

    public void setArticleId(int articleId) {
        this.articleId = articleId;
    }

    @Override
    public int getContentId() {
        return getArticleId();
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Post.POST_TYPE getType() {
        return type;
    }

    public void setType(Post.POST_TYPE type) {
        this.type = type;
    }


    public Date getGmtEdit() {
        return gmtEdit;
    }

    public void setGmtEdit(Date date) {
        this.gmtEdit = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
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

    public void setTopic(Topic topic) {
        this.topic = topic;
    }

    public List<String> getImageUrls() {
        return RichEditorUtil.getImageUrls(content);
    }

    @Override
    public String toString() {
        return "Article{" +
                "articleId=" + articleId +
                ", authorId=" + authorId +
                ", attractionId=" + attractionId +
                ", topicId=" + topicId +
                ", gmtCreate=" + gmtCreate +
                ", gmtEdit=" + gmtEdit +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", commentCount=" + commentCount +
                ", thumbupCount=" + thumbupCount +
                ", collectionCount=" + collectionCount +
                ", type=" + type +
                ", user=" + user +
                ", attraction=" + attraction +
                ", topic=" + topic +
                '}';
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public int getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(int attractionId) {
        this.attractionId = attractionId;
    }

    public int getTopicId() {
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

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }
}
