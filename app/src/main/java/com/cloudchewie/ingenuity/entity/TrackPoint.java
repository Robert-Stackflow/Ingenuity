package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;

public class TrackPoint implements Serializable {
    double latitude;
    double longtitude;
    String name;
    Date time;
    String transport;

    public TrackPoint(double latitude, double longtitude, String name, Date time) {
        this.latitude = latitude;
        this.longtitude = longtitude;
        this.name = name;
        this.time = time;
    }

    @NonNull
    @Override
    public String toString() {
        return "TrackPoint{" +
                "latitude=" + latitude +
                ", longtitude=" + longtitude +
                ", name='" + name + '\'' +
                ", time=" + time +
                '}';
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongtitude() {
        return longtitude;
    }

    public void setLongtitude(double longtitude) {
        this.longtitude = longtitude;
    }

    public String getTransport() {
        return transport;
    }

    public void setTransport(String transport) {
        this.transport = transport;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
