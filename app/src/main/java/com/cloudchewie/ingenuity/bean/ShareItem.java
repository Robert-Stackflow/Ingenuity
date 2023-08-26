package com.cloudchewie.ingenuity.bean;

import android.graphics.drawable.Drawable;

import androidx.annotation.NonNull;

public class ShareItem {
    String name;
    Drawable icon;
    String packageName;

    public ShareItem(CharSequence name, String packageName, Drawable icon) {
        this.name = (String) name;
        this.packageName = packageName;
        this.icon = icon;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Drawable getIcon() {
        return icon;
    }

    public void setIcon(Drawable icon) {
        this.icon = icon;
    }

    public String getPackageName() {
        return packageName;
    }

    public void setPackageName(String packageName) {
        this.packageName = packageName;
    }

    @NonNull
    @Override
    public String toString() {
        return "ShareItem{" +
                "name='" + name + '\'' +
                ", icon=" + icon +
                ", packageName='" + packageName + '\'' +
                '}';
    }
}
