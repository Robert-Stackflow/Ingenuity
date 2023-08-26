/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 21:06:28
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.entity;

import java.io.Serializable;
import java.util.Date;

public class Notice implements Serializable {
    //TODO 无效,引用为userId
    User user;
    Date date;
    Object obj;
    Object obj2;
    NOTICE_TYPE noticeType;

    public Notice(User user, Date date, NOTICE_TYPE noticeType, Object obj, Object obj2) {
        this.user = user;
        this.date = date;
        this.obj = obj;
        this.obj2 = obj2;
        this.noticeType = noticeType;
    }

    public Notice() {
    }

    public Object getObj2() {
        return obj2;
    }

    public void setObj2(Object obj2) {
        this.obj2 = obj2;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public Object getObj() {
        return obj;
    }

    public void setObj(Object obj) {
        this.obj = obj;
    }

    public NOTICE_TYPE getNoticeType() {
        return noticeType;
    }

    public void setNoticeType(NOTICE_TYPE noticeType) {
        this.noticeType = noticeType;
    }

    public String getDescribe() {
        switch (noticeType) {
            case COMMENT:
                return "有人评论了你的帖子:\n" + ((obj2 == null || !(obj2 instanceof Comment)) ? "" : ((Comment) obj2).getContent());
            case REPLY:
                return "有人回复了你的评论:\n" + ((obj2 == null || !(obj2 instanceof Comment)) ? "" : ((Comment) obj2).getContent());
            case THUMBUP:
                return "有人赞了你的帖子";
            case COLLECT:
                return "有人收藏了你的帖子";
            case FOLLOW:
                return user.getNickname() + "关注了你";
            default:
                return "有人与你进行了互动";
        }
    }

    public enum NOTICE_TYPE {
        COMMENT, REPLY, THUMBUP, COLLECT, FOLLOW
    }
}
