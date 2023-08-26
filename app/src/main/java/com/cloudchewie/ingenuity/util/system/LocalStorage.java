/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:14:24
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.util.system;

import androidx.annotation.NonNull;

import com.cloudchewie.ingenuity.util.database.AppDatabase;

import org.jetbrains.annotations.Contract;

public class LocalStorage {
    private static AppDatabase appDatabase;

    public static void init(@NonNull AppDatabase appDatabase) {
        LocalStorage.appDatabase = appDatabase;
    }

    @Contract(pure = true)
    public static AppDatabase getAppDatabase() {
        return appDatabase;
    }

    public static void setAppDatabase(AppDatabase appDatabase) {
        LocalStorage.appDatabase = appDatabase;
    }
}
