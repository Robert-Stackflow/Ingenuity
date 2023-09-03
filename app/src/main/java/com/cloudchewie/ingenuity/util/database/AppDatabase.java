/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:14:24
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.util.database;

import android.content.Context;

import androidx.annotation.NonNull;
import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;
import androidx.room.migration.Migration;
import androidx.sqlite.db.SupportSQLiteDatabase;

import com.cloudchewie.ingenuity.dao.BookmarkDao;
import com.cloudchewie.ingenuity.dao.OtpTokenDao;
import com.cloudchewie.ingenuity.entity.BookmarkGroup;
import com.cloudchewie.ingenuity.entity.OtpToken;

import org.jetbrains.annotations.Contract;

@Database(entities = {OtpToken.class, BookmarkGroup.class}, version = 2)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Contract(pure = true)
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `bookmark` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` TEXT, `order` INTEGER NOT NULL, `addDate` INTEGER, `isRoot` INTEGER NOT NULL, `lastModified` INTEGER, `importTime` INTEGER, `bookmarks` TEXT, `bookmarkGroups` TEXT)");
        }
    };
    private static final String DB_NAME = "cloudchewie.db";
    private static volatile AppDatabase instance;

    public static synchronized AppDatabase getInstance(Context context) {
        if (instance == null) {
            instance = create(context);
        }
        return instance;
    }

    @NonNull
    private static AppDatabase create(final Context context) {
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME).addMigrations(MIGRATION_1_2).allowMainThreadQueries().build();
    }

    public abstract OtpTokenDao otpTokenDao();

    public abstract BookmarkDao bookmarkDao();

}