package com.cloudchewie.ingenuity.entity;

public class Setting {
    private long userId;

    private byte followVisible;

    private byte fansVisible;

    private byte visitedVisible;

    private byte wantVisible;

    private byte locationVisible;

    private byte thumbupVisible;

    private byte favoritesVisible;

    private byte defaultFavoritesPublic;

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public byte getFollowVisible() {
        return followVisible;
    }

    public void setFollowVisible(byte followVisible) {
        this.followVisible = followVisible;
    }

    public byte getFansVisible() {
        return fansVisible;
    }

    public void setFansVisible(byte fansVisible) {
        this.fansVisible = fansVisible;
    }

    public byte getVisitedVisible() {
        return visitedVisible;
    }

    public void setVisitedVisible(byte visitedVisible) {
        this.visitedVisible = visitedVisible;
    }

    public byte getWantVisible() {
        return wantVisible;
    }

    public void setWantVisible(byte wantVisible) {
        this.wantVisible = wantVisible;
    }

    public byte getLocationVisible() {
        return locationVisible;
    }

    public void setLocationVisible(byte locationVisible) {
        this.locationVisible = locationVisible;
    }

    public byte getThumbupVisible() {
        return thumbupVisible;
    }

    public void setThumbupVisible(byte thumbupVisible) {
        this.thumbupVisible = thumbupVisible;
    }

    public byte getFavoritesVisible() {
        return favoritesVisible;
    }

    public void setFavoritesVisible(byte favoritesVisible) {
        this.favoritesVisible = favoritesVisible;
    }

    public byte getDefaultFavoritesPublic() {
        return defaultFavoritesPublic;
    }

    public void setDefaultFavoritesPublic(byte defaultFavoritesPublic) {
        this.defaultFavoritesPublic = defaultFavoritesPublic;
    }
}
