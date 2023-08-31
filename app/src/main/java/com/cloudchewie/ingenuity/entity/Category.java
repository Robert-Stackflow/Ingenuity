package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

import java.util.List;

public class Category {
    int id;
    int iconId;
    int order;
    String description;
    List<Tool> tools;

    public Category(int id, int iconId, String description) {
        this.id = id;
        this.iconId = iconId;
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getIconId() {
        return iconId;
    }

    public void setIconId(int iconId) {
        this.iconId = iconId;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @NonNull
    @Override
    public String toString() {
        return "Category{" + "id=" + id + ", iconId=" + iconId + ", description='" + description + '\'' + '}';
    }
}
