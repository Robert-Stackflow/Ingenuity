package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

import com.cloudchewie.ingenuity.util.image.ImageUrlUtil;
import com.cloudchewie.util.basic.DateUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Activity implements Serializable {
    int activityId;
    int creatorId;
    String cover;
    String name;
    ACTIVITY_TYPE type;
    String diyTag;
    int personCount;
    List<TrackPoint> trackPoints;
    List<Image> images;

    public Activity(int activityId, String name, ACTIVITY_TYPE type, String diyTag, int personCount, List<TrackPoint> trackPoints) {
        this.activityId = activityId;
        this.name = name;
        this.type = type;
        this.diyTag = diyTag;
        this.personCount = personCount;
        this.trackPoints = trackPoints;
        this.cover = ImageUrlUtil.getUrls(1).get(0);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return "Activity{" +
                "activityId=" + activityId +
                ", creatorId=" + creatorId +
                ", cover='" + cover + '\'' +
                ", type=" + type +
                ", diyTag='" + diyTag + '\'' +
                ", personCount=" + personCount +
                ", trackPoints=" + trackPoints +
                ", images=" + images +
                '}';
    }

    public int getActivityId() {
        return activityId;
    }

    public void setActivityId(int activityId) {
        this.activityId = activityId;
    }

    public int getCreatorId() {
        return creatorId;
    }

    public void setCreatorId(int creatorId) {
        this.creatorId = creatorId;
    }

    public String getCover() {
        return cover;
    }

    public void setCover(String cover) {
        this.cover = cover;
    }

    public ACTIVITY_TYPE getType() {
        return type;
    }

    public void setType(ACTIVITY_TYPE type) {
        this.type = type;
    }

    public String getTag() {
        switch (type) {
            case DIY:
                return diyTag;
            case DINE:
                return "聚餐";
            case PARTY:
                return "聚会";
            case OUTING:
                return "郊游";
            case TRAVEL:
                return "打卡";
            case TEAM_BUILD:
                return "团建";
        }
        return diyTag;
    }

    public String getDiyTag() {
        return diyTag;
    }

    public void setDiyTag(String diyTag) {
        this.diyTag = diyTag;
    }

    public int getPersonCount() {
        return personCount;
    }

    public void setPersonCount(int personCount) {
        this.personCount = personCount;
    }

    public List<TrackPoint> getTrackPoints() {
        return trackPoints;
    }

    public void setTrackPoints(List<TrackPoint> trackPoints) {
        this.trackPoints = trackPoints;
    }

    public String getLocation() {
        if (trackPoints != null && trackPoints.size() > 0) {
            StringBuilder location = new StringBuilder();
            for (TrackPoint trackPoint : trackPoints) {
                location.append(trackPoint.getName());
                if (trackPoints.indexOf(trackPoint) < trackPoints.size() - 1)
                    location.append("->");
            }
            return location.toString();
        }
        return "";
    }

    public int getDuration() {
        if (trackPoints != null && trackPoints.size() > 1) {
            return DateUtil.getIntervalDayOfDate(trackPoints.get(trackPoints.size() - 1).getTime(), trackPoints.get(0).getTime());
        }
        return 0;
    }

    public Date getTime() {
        if (trackPoints != null && trackPoints.size() > 0) {
            return trackPoints.get(0).getTime();
        }
        return new Date();
    }

    public List<Image> getImages() {
        return images;
    }

    public void setImages(List<Image> images) {
        this.images = images;
    }

    public enum ACTIVITY_TYPE {
        DINE, TRAVEL, PARTY, OUTING, TEAM_BUILD, DIY
    }
}
