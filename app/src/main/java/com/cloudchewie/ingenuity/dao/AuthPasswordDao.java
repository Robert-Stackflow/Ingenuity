package com.cloudchewie.ingenuity.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cloudchewie.ingenuity.entity.AuthPassword;

import java.util.List;

@Dao
public interface AuthPasswordDao {
    @Query("select * from auth_password order by id")
    List<AuthPassword> getAll();

    @Query("select count(*) from auth_password where groupId=:groupId order by id")
    int count(Integer groupId);

    @Query("select * from auth_password where groupId=:groupId order by id")
    List<AuthPassword> get(Integer groupId);

    @Query("select * from auth_password where id = :id")
    AuthPassword getById(Integer id);

    @Query("delete from auth_password where id = :id")
    void deleteById(Integer id);
    @Query("delete from auth_password where groupId = :groupId")
    void delete(Integer groupId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<AuthPassword> passwordGroups);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(AuthPassword passwordGroup);

    @Update
    void update(AuthPassword passwordGroup);
}

