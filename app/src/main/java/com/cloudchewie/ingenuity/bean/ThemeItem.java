package com.cloudchewie.ingenuity.bean;

import androidx.annotation.NonNull;

public class ThemeItem {
    public String title;
    public String description;
    public int colorId;
    public int id;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public ThemeItem(String title, String description, int colorId, int id) {
        this.title = title;
        this.description = description;
        this.colorId = colorId;
        this.id = id;
    }

    @NonNull
    @Override
    public String toString() {
        return "ThemeItem{" + "title='" + title + '\'' + ", description='" + description + '\'' + ", colorId=" + colorId + ", id=" + id + '}';
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getColorId() {
        return colorId;
    }

    public void setColorId(int colorId) {
        this.colorId = colorId;
    }
}
