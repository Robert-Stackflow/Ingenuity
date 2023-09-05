package com.cloudchewie.ingenuity.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cloudchewie.ingenuity.entity.BackupPassword;

import java.util.List;

@Dao
public interface BackupPasswordDao {
    @Query("select * from backup_password order by id")
    List<BackupPassword> getAll();

    @Query("select count(*) from backup_password where groupId=:groupId order by id")
    int count(Integer groupId);

    @Query("select * from backup_password where groupId=:groupId order by id")
    List<BackupPassword> get(Integer groupId);

    @Query("select * from backup_password where id = :id")
    BackupPassword getById(Integer id);

    @Query("delete from backup_password where id = :id")
    void deleteById(Integer id);
    @Query("delete from backup_password where groupId = :groupId")
    void delete(Integer groupId);
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BackupPassword> passwordGroups);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BackupPassword passwordGroup);

    @Update
    void update(BackupPassword passwordGroup);
}
