/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

import com.cloudchewie.util.basic.DateUtil;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;

import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.commons.models.IUser;

public class Message implements Serializable, IMessage {
    int messageId;
    int senderId;
    int receiverId;
    User user;
    Date date;
    String content;
    MessageType type;
    MessageStatus status;
    long duration;
    long size;
    String fileName;
    String fileType;
    String locationName;
    String locationDetail;
    double locationLatitude;
    double locationLongtitude;
    User nameCardUser;
    String locationScreenShot;

    public Message() {
    }

    public Message(int senderId, int receiverId, Date date, String content) {
        this.senderId = senderId;
        this.receiverId = receiverId;
        this.date = date;
        this.content = content;
    }

    @Override
    public String getLocationScreenShot() {
        return locationScreenShot;
    }

    public void setLocationScreenShot(String locationScreenShot) {
        this.locationScreenShot = locationScreenShot;
    }

    @Override
    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    @Override
    public String getLocationDetail() {
        return locationDetail;
    }

    public void setLocationDetail(String locationDetail) {
        this.locationDetail = locationDetail;
    }

    @Override
    public double getLocationLatitude() {
        return locationLatitude;
    }

    public void setLocationLatitude(double locationLatitude) {
        this.locationLatitude = locationLatitude;
    }

    @Override
    public double getLocationLongtitude() {
        return locationLongtitude;
    }

    public void setLocationLongtitude(double locationLongtitude) {
        this.locationLongtitude = locationLongtitude;
    }

    @Override
    public User getNameCardUser() {
        return nameCardUser;
    }

    public void setNameCardUser(User nameCardUser) {
        this.nameCardUser = nameCardUser;
    }

    @Override
    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    @Override
    public String getMsgId() {
        return String.valueOf(messageId);
    }

    @Override
    public IUser getFromUser() {
        return user;
    }

    @Override
    public Date getTime() {
        return date;
    }

    @Override
    public String getTimeString() {
        return DateUtil.beautifyTime(date);
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public MessageStatus getStatus() {
        return status;
    }

    public void setStatus(MessageStatus status) {
        this.status = status;
    }

    public MessageType getType() {
        return type;
    }

    public void setType(MessageType type) {
        this.type = type;
    }

    @Override
    public MessageStatus getMessageStatus() {
        return status;
    }

    @Override
    public String getText() {
        return content;
    }

    @Override
    public String getFilePath() {
        return content;
    }

    @Override
    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    @Override
    public long getFileSize() {
        return size;
    }

    public void setFileSize(long size) {
        this.size = size;
    }

    @Override
    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    @Override
    public String getProgress() {
        return null;
    }

    @Override
    public HashMap<String, String> getExtras() {
        return null;
    }

    @NonNull
    @Override
    public String toString() {
        return "Message{" + "aId=" + senderId + ", bId=" + receiverId + ", type=" + type + ", date=" + date + ", content='" + content + '\'' + '}';
    }

    public int getSenderId() {
        return senderId;
    }

    public void setSenderId(int senderId) {
        this.senderId = senderId;
    }

    public int getReceiverId() {
        return receiverId;
    }

    public void setReceiverId(int receiverId) {
        this.receiverId = receiverId;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

}
