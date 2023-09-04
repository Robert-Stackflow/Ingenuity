package com.cloudchewie.ingenuity.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "password_group")
public class PasswordGroup implements Serializable {
    @PrimaryKey(autoGenerate = true)
    Integer id;
    String name;
    String remark;
    PasswordType type = PasswordType.COMMON;
    Date addDate;
    Date editDate;
    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public PasswordType getType() {
        return type;
    }

    public void setType(PasswordType type) {
        this.type = type;
    }

    public Date getAddDate() {
        return addDate;
    }

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public Date getEditDate() {
        return editDate;
    }
    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }
    public enum PasswordType {
        COMMON, AUTH, BACKUP
    }
}
