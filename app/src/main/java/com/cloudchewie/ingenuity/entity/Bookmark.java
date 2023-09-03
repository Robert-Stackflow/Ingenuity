package com.cloudchewie.ingenuity.entity;


import androidx.annotation.NonNull;

import java.util.Date;

public class Bookmark implements Cloneable {
    String url;
    String name;
    int order;
    String description;

    public String getShareString() {
        return name + ":" + url;
    }

    public int getOrder() {
        return order;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    String icon;
    Date addTime;

    public String getUrl() {
        return url;
    }

    public Bookmark() {
    }

    public Bookmark(String url, String name, String description, String icon, Date addTime) {
        this.url = url;
        this.name = name;
        this.description = description;
        this.icon = icon;
        this.addTime = addTime;
    }

    public void setUrl(String url) {
        this.url = url;
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

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public Date getAddTime() {
        return addTime;
    }

    public void setAddTime(Date addTime) {
        this.addTime = addTime;
    }

    @NonNull
    @Override
    public String toString() {
//        return "Bookmark{" +
//                "url='" + url + '\'' +
//                ", name='" + name + '\'' +
//                ", description='" + description + '\'' +
//                ", icon='" + '\'' +
//                ", addTime=" + addTime +
//                '}';
        return url.substring(0, 10) + " | " + name;
    }

    public String toHtml() {
        return "<DT><A HREF=\"" + url + "\" DISCRITION=\"" + description + "\" ADD_DATE=\"" + addTime.getTime() + "\" ICON=\"" + icon + "\">" + name + "</A>\n";
    }

    @NonNull
    @Override
    public Bookmark clone() {
        try {
            return (Bookmark) super.clone();
        } catch (CloneNotSupportedException e) {
            throw new AssertionError();
        }
    }
}
