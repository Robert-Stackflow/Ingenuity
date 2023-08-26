/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

import com.cloudchewie.ingenuity.util.image.ImageUrlUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Attraction implements Serializable {
    int attractionId;
    String name;
    String location;
    String description;
    //TODO 无效,引用为userId
    double score;
    int authorId;
    double latitude;
    double longtitude;
    int visitedCount;
    int recommendCount;
    int wantCount;
    Date gmtCreate;
    String cover;
    FOLLOW_TYPE myType;

    public Attraction() {
    }

    public Attraction(String name, String location, String description, int authorId, double longtitude, double latitude, int wantCount, int recommendCount, int visitedCount, List<String> tags, FOLLOW_TYPE type) {
        this.name = name;
        this.myType = type;
        this.location = location;
        this.description = description;
        this.authorId = authorId;
        this.longtitude = longtitude;
        this.latitude = latitude;
        this.wantCount = wantCount;
        this.recommendCount = recommendCount;
        this.visitedCount = visitedCount;
        cover = ImageUrlUtil.getUrls(1).get(0);
    }

    public int getAttractionId() {
        return attractionId;
    }

    public void setAttractionId(int attractionId) {
        this.attractionId = attractionId;
    }

    public double getScore() {
        return score;
    }

    public void setScore(double score) {
        this.score = score;
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


    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStatistics() {
        return recommendCount + "人推荐 · " + visitedCount + "人看过 · " + wantCount + "人想去";
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getAuthorId() {
        return authorId;
    }

    public void setAuthorId(int authorId) {
        this.authorId = authorId;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public int getWantCount() {
        return wantCount;
    }

    public void setWantCount(int wantCount) {
        this.wantCount = wantCount;
    }

    public int getRecommendCount() {
        return recommendCount;
    }

    public void setRecommendCount(int recommendCount) {
        this.recommendCount = recommendCount;
    }

    public int getVisitedCount() {
        return visitedCount;
    }

    public void setVisitedCount(int visitedCount) {
        this.visitedCount = visitedCount;
    }

    @NonNull
    @Override
    public String toString() {
        return "Attraction{" + "name='" + name + '\'' + ", location='" + location + '\'' + ", detail='" + description + '\'' + ", authorId=" + authorId + ", longtitude=" + longtitude + ", latitude=" + latitude + ", followerCount=" + wantCount + ", visitorCount=" + recommendCount + ", postCount=" + visitedCount + '}';
    }

    public enum FOLLOW_TYPE {
        NONE, WANT, GONE
    }
}
