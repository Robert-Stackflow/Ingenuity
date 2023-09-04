package com.cloudchewie.ingenuity.entity;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity(tableName = "backup_password")
public class BackupPassword implements Password {
    @PrimaryKey(autoGenerate = true)
    Integer id;
    Integer groupId;
    String issuer;
    String username;
    String website;
    List<String> passwords;
    List<Boolean> states;
    String remark;
    Date addDate;
    Date editDate;

    public void setAddDate(Date addDate) {
        this.addDate = addDate;
    }

    public void setEditDate(Date editDate) {
        this.editDate = editDate;
    }

    @Override
    public String getIssuer() {
        return issuer;
    }

    @Override
    public String getUsername() {
        return username;
    }

    public String getWebsite() {
        return website;
    }

    public void setWebsite(String website) {
        this.website = website;
    }

    @Override
    public String getRemark() {
        return remark;
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

    public List<String> getPasswords() {
        return passwords;
    }

    public void setPasswords(List<String> passwords) {
        if (passwords == null)
            passwords = new ArrayList<>();
        this.passwords = passwords;
        this.states = new ArrayList<>();
        for (int i = 0; i < this.passwords.size(); i++) {
            states.add(false);
        }
    }

    public List<Boolean> getStates() {
        return states;
    }

    public void setStates(List<Boolean> states) {
        this.states = states;
    }

    public void use(String passowrd) {
        use(passwords.indexOf(passowrd));
    }

    public void use(int index) {
        if (index >= 0 && index < states.size())
            states.set(index, true);
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    @Override
    public String getPassword() {
        return null;
    }

    public String getFirstUnusedPassowrd() {
        for (Boolean state : states) {
            if (!state) {
                int index = states.indexOf(state);
                if (index >= 0 && index < passwords.size())
                    return passwords.get(index);
                return null;
            }
        }
        return null;
    }

    @Override
    public Date getAddDate() {
        return addDate;
    }

    @Override
    public Date getEditDate() {
        return editDate;
    }
}
