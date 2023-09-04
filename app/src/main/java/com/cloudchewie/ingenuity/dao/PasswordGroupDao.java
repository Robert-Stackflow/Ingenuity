package com.cloudchewie.ingenuity.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cloudchewie.ingenuity.entity.PasswordGroup;

import java.util.List;

@Dao
public interface PasswordGroupDao {
    @Query("select * from password_group order by id")
    List<PasswordGroup> getAll();

    @Query("select * from password_group where id = :id")
    PasswordGroup getById(Integer id);

    @Query("select * from password_group where type = :type")
    List<PasswordGroup> getByType(PasswordGroup.PasswordType type);

    @Query("delete from password_group where id = :id")
    void deleteById(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<PasswordGroup> passwordGroups);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    Long insert(PasswordGroup passwordGroup);

    @Update
    void update(PasswordGroup passwordGroup);
}
