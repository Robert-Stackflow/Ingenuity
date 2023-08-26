/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.dao;

import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cloudchewie.ingenuity.entity.Draft;

import java.util.List;

@Dao
public interface DraftDao {
    @Query("select * from draft")
    List<Draft> getAll();

    @Query("select * from draft where draftId= :draftId")
    Draft findById(int draftId);

    @Update
    void update(Draft draft);

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    void insert(Draft draft);

    @Delete
    void delete(Draft draft);

    @Query("delete from draft where 1=1")
    void clear();
}
