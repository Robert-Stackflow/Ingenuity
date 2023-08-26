/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:39
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class Topic implements Serializable {
    int topicId;
    String name;
    String description;
    //TODO 无效,引用为userId
    int authorId;
    int visitedCount;
    int followCount;
    FOLLOW_TYPE myType;
    private Date gmtCreate;

    public Topic() {
    }

    public Topic(String name, String description, int visitedCount, int followCount, FOLLOW_TYPE type) {
        this.myType = type;
        this.name = name;
        this.visitedCount = visitedCount;
        this.description = description;
        this.followCount = followCount;
    }

    public int getTopicId() {
        return topicId;
    }

    public void setTopicId(int topicId) {
        this.topicId = topicId;
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

    public FOLLOW_TYPE getMyType() {
        return myType;
    }

    public void setMyType(FOLLOW_TYPE myType) {
        this.myType = myType;
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

    public int getFollowCount() {
        return followCount;
    }

    public void setFollowCount(int followCount) {
        this.followCount = followCount;
    }

    @NonNull
    @Override
    public String toString() {
        return "Topic{" + "name='" + name + '\'' + ", description='" + description + '\'' + ", followerCount=" + followCount + '}';
    }

    public enum FOLLOW_TYPE {
        UNFOLLOWED, FOLLOWED
    }
}
