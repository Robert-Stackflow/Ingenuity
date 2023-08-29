/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.entity;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.Ignore;
import androidx.room.PrimaryKey;

import com.cloudchewie.ingenuity.util.image.ImageUrlUtil;

import java.io.Serializable;
import java.util.Date;

import cn.jiguang.imui.commons.models.IUser;

@Entity(tableName = "user")
public class User implements Serializable, IUser {
    @PrimaryKey
    private int userId;
    private String nickname;
    private String password;
    private String mobile;
    private String avatar;
    private String background;
    private String signature;
    private char gender;
    private Date gmtBirth;
    private String token;
    private Date gmtSignup;

    public User() {
    }

    @Ignore
    public User(String mobile, String password) {
        this.mobile = mobile;
        this.password = password;
    }

    @Ignore
    public User(int userId, String nickname, String mobile, String signature, char gender, Date gmtBirth) {
        this.userId = userId;
        this.nickname = nickname;
        this.mobile = mobile;
        this.avatar = ImageUrlUtil.getUrls(1).get(0);
        this.background = ImageUrlUtil.getUrls(1).get(0);
        this.signature = signature;
        this.gender = gender;
        this.gmtBirth = gmtBirth;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public Date getGmtSignup() {
        return gmtSignup;
    }

    public void setGmtSignup(Date gmtSignup) {
        this.gmtSignup = gmtSignup;
    }

    public char getGender() {
        return gender;
    }

    public void setGender(char gender) {
        this.gender = gender;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getBackground() {
        return background;
    }

    public void setBackground(String background) {
        this.background = background;
    }

    public String getSignature() {
        return signature;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }

    public Date getGmtBirth() {
        return gmtBirth;
    }

    public void setGmtBirth(Date gmtBirth) {
        this.gmtBirth = gmtBirth;
    }

    @NonNull
    @Override
    public String toString() {
        return "User{" + "userId=" + userId + ", nickname='" + nickname + '\'' + ", password='" + password + '\'' + ", mobile='" + mobile + '\'' + ", avatar='" + avatar + '\'' + ", background='" + background + '\'' + ", signature='" + signature + '\'' + ", gender=" + gender + ", gmtBirth=" + gmtBirth + ", token='" + token + '\'' + ", gmtSignup=" + gmtSignup + '}';
    }

    @Override
    public String getId() {
        return String.valueOf(userId);
    }

    @Override
    public String getDisplayName() {
        return nickname;
    }

    @Override
    public String getDescribe() {
        return signature;
    }

    @Override
    public String getAvatarFilePath() {
        return avatar;
    }

    public enum FOLLOW_STATUS {
        UNFOLLOW, FOLLOW, SPECIAL, FOLLOW_FRIENDS, SPECIAL_FRIENDS, BLOCK
    }
}
