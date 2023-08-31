package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

public class Tool {
    int id;
    int categoryId;
    int order;
    String uri;
    int iconId;
    int times;
    String description;

    @NonNull
    @Override
    public String toString() {
        return "Tool{" + "id=" + id + ", categoryId=" + categoryId + ", uri='" + uri + '\'' + ", iconId=" + iconId + ", times=" + times + ", description='" + description + '\'' + '}';
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public int getTimes() {
        return times;
    }

    public void setTimes(int times) {
        this.times = times;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Tool(int id, int categoryId, String uri, int iconId, int times, String description) {
        this.id = id;
        this.categoryId = categoryId;
        this.uri = uri;
        this.iconId = iconId;
        this.times = times;
        this.description = description;
    }

}
