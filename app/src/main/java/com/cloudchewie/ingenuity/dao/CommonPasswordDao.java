package com.cloudchewie.ingenuity.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cloudchewie.ingenuity.entity.CommonPassword;

import java.util.List;

@Dao
public interface CommonPasswordDao {
    @Query("select * from common_password order by id")
    List<CommonPassword> getAll();
    @Query("select count(*) from common_password where groupId=:groupId order by id")
    int count(Integer groupId);
    @Query("select * from common_password where groupId=:groupId order by id")
    List<CommonPassword> get(Integer groupId);

    @Query("select * from common_password where id = :id")
    CommonPassword getById(Integer id);

    @Query("delete from common_password where id = :id")
    void deleteById(Integer id);
    @Query("delete from common_password where groupId = :groupId")
    void delete(Integer groupId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<CommonPassword> passwordGroups);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(CommonPassword passwordGroup);

    @Update
    void update(CommonPassword passwordGroup);
}
