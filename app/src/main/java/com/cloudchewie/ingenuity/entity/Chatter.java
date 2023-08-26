/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class Chatter implements Serializable {
    //TODO 无效,引用为userId
    User user;
    CHATTER_TYPE type;
    CHATTER_STATE state;
    List<Message> messages;

    public Chatter() {
    }

    public Chatter(User user, CHATTER_STATE state, CHATTER_TYPE type, List<Message> messages) {
        this.user = user;
        this.state = state;
        this.type = type;
        this.messages = messages;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @NonNull
    @Override
    public String toString() {
        return "Messager{" + '\'' + ", state=" + state + ", isStranger=" + type + ", messages=" + messages + '}';
    }

    public String getTypeInfo() {
        switch (type) {
            case SPECIAL:
                return "特别关心";
            case OFFICIAL:
                return "官方";
            case STRANGER:
                return "陌生人";
            case DEFAULT:
            default:
                return null;
        }
    }

    public Message getLastMessage() {
        int index = 0;
        long interval = Long.MAX_VALUE;
        Date date = new Date(System.currentTimeMillis());
        for (Message message : messages) {
            long temp = date.getTime() - message.getDate().getTime();
            if (temp < interval) {
                interval = Math.min(interval, temp);
                index = messages.indexOf(message);
            }
        }
        return messages.get(index);
    }

    public CHATTER_STATE getState() {
        return state;
    }

    public void setState(CHATTER_STATE state) {
        this.state = state;
    }

    public CHATTER_TYPE getType() {
        return type;
    }

    public void setType(CHATTER_TYPE type) {
        this.type = type;
    }

    public List<Message> getMessages() {
        return messages;
    }

    public void setMessages(List<Message> messages) {
        this.messages = messages;
    }

    public enum CHATTER_TYPE {
        DEFAULT, SPECIAL, OFFICIAL, STRANGER
    }

    public enum CHATTER_STATE {
        DEFAULT, PIN, DONTDISTURB, BLOCK
    }
}
