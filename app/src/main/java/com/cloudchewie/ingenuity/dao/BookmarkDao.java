package com.cloudchewie.ingenuity.dao;

import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;
import androidx.room.Update;

import com.cloudchewie.ingenuity.entity.BookmarkGroup;

import java.util.List;

@Dao
public interface BookmarkDao {
    @Query("select * from bookmark order by id")
    List<BookmarkGroup> getAll();

    @Query("select * from bookmark where id = :id")
    BookmarkGroup getById(Integer id);

    @Query("delete from bookmark where id = :id")
    void deleteById(Integer id);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insertAll(List<BookmarkGroup> bookmarkGroups);

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    void insert(BookmarkGroup bookmarkGroup);

    @Update
    void update(BookmarkGroup bookmarkGroup);
}
