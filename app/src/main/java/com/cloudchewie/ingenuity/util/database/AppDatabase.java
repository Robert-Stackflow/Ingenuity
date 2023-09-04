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

import com.cloudchewie.ingenuity.dao.AuthPasswordDao;
import com.cloudchewie.ingenuity.dao.BackupPasswordDao;
import com.cloudchewie.ingenuity.dao.BookmarkDao;
import com.cloudchewie.ingenuity.dao.CommonPasswordDao;
import com.cloudchewie.ingenuity.dao.OtpTokenDao;
import com.cloudchewie.ingenuity.dao.PasswordGroupDao;
import com.cloudchewie.ingenuity.entity.AuthPassword;
import com.cloudchewie.ingenuity.entity.BackupPassword;
import com.cloudchewie.ingenuity.entity.BookmarkGroup;
import com.cloudchewie.ingenuity.entity.CommonPassword;
import com.cloudchewie.ingenuity.entity.OtpToken;
import com.cloudchewie.ingenuity.entity.PasswordGroup;

import org.jetbrains.annotations.Contract;

@Database(entities = {OtpToken.class, BookmarkGroup.class, PasswordGroup.class, AuthPassword.class, BackupPassword.class, CommonPassword.class}, version = 3)
@TypeConverters(Converters.class)
public abstract class AppDatabase extends RoomDatabase {
    static final Migration MIGRATION_1_2 = new Migration(1, 2) {
        @Contract(pure = true)
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `bookmark` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` TEXT, `order` INTEGER NOT NULL, `addDate` INTEGER, `isRoot` INTEGER NOT NULL, `lastModified` INTEGER, `importTime` INTEGER, `bookmarks` TEXT, `bookmarkGroups` TEXT)");
        }
    };

    static final Migration MIGRATION_2_3 = new Migration(2, 3) {
        @Contract(pure = true)
        @Override
        public void migrate(@NonNull SupportSQLiteDatabase database) {
            database.execSQL("CREATE TABLE IF NOT EXISTS `password_group` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `name` TEXT, `remark` TEXT, `type` INTEGER, `addDate` INTEGER, `editDate` INTEGER)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `auth_password` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `groupId` INTEGER, `issuer` TEXT, `email` TEXT, `password` TEXT, `website` TEXT, `remark` TEXT, `addDate` INTEGER, `editDate` INTEGER)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `backup_password` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `groupId` INTEGER, `issuer` TEXT, `username` TEXT, `website` TEXT, `passwords` TEXT, `states` TEXT, `remark` TEXT, `addDate` INTEGER, `editDate` INTEGER)");
            database.execSQL("CREATE TABLE IF NOT EXISTS `common_password` (`id` INTEGER PRIMARY KEY AUTOINCREMENT, `groupId` INTEGER, `issuer` TEXT, `username` TEXT, `nickname` TEXT, `email` TEXT, `mobile` TEXT, `password` TEXT, `website` TEXT, `remark` TEXT, `addDate` INTEGER, `editDate` INTEGER)");
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
        return Room.databaseBuilder(context, AppDatabase.class, DB_NAME).addMigrations(MIGRATION_1_2, MIGRATION_2_3).allowMainThreadQueries().build();
    }

    public abstract OtpTokenDao otpTokenDao();

    public abstract BookmarkDao bookmarkDao();

    public abstract PasswordGroupDao passwordGroupDao();

    public abstract AuthPasswordDao authPasswordDao();

    public abstract CommonPasswordDao commonPasswordDao();

    public abstract BackupPasswordDao backupPasswordDao();
}