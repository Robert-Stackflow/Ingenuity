package com.cloudchewie.ingenuity.entity;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.alibaba.fastjson.JSON;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "draft")
public class Draft implements Serializable {
    @PrimaryKey(autoGenerate = true)
    public int draftId;
    public Date createTime;
    public String str;
    public Date lastSaveTime;
    public DRAFT_TYPE type;

    public Draft() {
    }

    @Ignore
    public Draft(Object object, Date createTime, Date lastSaveTime, DRAFT_TYPE type) {
        this.str = JSON.toJSONString(object);
        this.createTime = createTime;
        this.lastSaveTime = lastSaveTime;
        this.type = type;
        Log.d("xuruida", toString());
    }

    @NonNull
    @Override
    public String toString() {
        return "Draft{" +
                "draftId=" + draftId +
                ", createTime=" + createTime +
                ", str='" + str + '\'' +
                ", lastSaveTime=" + lastSaveTime +
                ", type=" + type +
                '}';
    }

    public int getDraftId() {
        return draftId;
    }

    public void setDraftId(int draftId) {
        this.draftId = draftId;
    }

    public String getStr() {
        return str;
    }

    public void setStr(String str) {
        this.str = str;
    }

    public Object getObject() {
        switch (type) {
            case ARTICLE:
                return JSON.parseObject(str, Article.class);
            case POST:
                return JSON.parseObject(str, Post.class);
            case ATTRACTION:
                return JSON.parseObject(str, RequestAttraction.class);
            case TOPIC:
                return JSON.parseObject(str, RequestTopic.class);
        }
        return null;
    }

    public void setObject(Object object) {
        this.str = JSON.toJSONString(object);
    }

    public Date getCreateTime() {
        return createTime;
    }

    public Draft setCreateTime(Date createTime) {
        this.createTime = createTime;
        return this;
    }

    public Date getLastSaveTime() {
        return lastSaveTime;
    }

    public Draft setLastSaveTime(Date lastSaveTime) {
        this.lastSaveTime = lastSaveTime;
        return this;
    }

    public DRAFT_TYPE getType() {
        return type;
    }

    public Draft setType(DRAFT_TYPE type) {
        this.type = type;
        return this;
    }

    public enum DRAFT_TYPE {
        POST,
        ARTICLE,
        ATTRACTION,
        TOPIC
    }
}
