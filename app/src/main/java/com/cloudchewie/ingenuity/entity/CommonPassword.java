package com.cloudchewie.ingenuity.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.Date;

@Entity(tableName = "common_password")
public class CommonPassword implements Password {
    @PrimaryKey(autoGenerate = true)
    Integer id;
    Integer groupId;
    String issuer;
    String username;
    String nickname;
    String email;
    String mobile;
    String password;
    String website;
    String remark;
    Date addDate;
    Date editDate;

    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public String getUsername() {
        return username;
    }

    @Override
    public String getRemark() {
        return remark;
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    public Date getAddDate() {
        return addDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getGroupId() {
        return groupId;
    }

    public void setGroupId(Integer groupId) {
        this.groupId = groupId;
    }

    public void setIssuer(String issuer) {
        this.issuer = issuer;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    @Override
    public Date getEditDate() {
        return editDate;
    }
}
