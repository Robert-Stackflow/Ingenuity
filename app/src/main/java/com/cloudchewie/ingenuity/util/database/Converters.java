/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:26:23
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.util.database;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import com.cloudchewie.ingenuity.entity.Bookmark;
import com.cloudchewie.ingenuity.entity.BookmarkGroup;
import com.cloudchewie.util.basic.CalendarUtil;
import com.google.gson.Gson;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class Converters {
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @TypeConverter
    public static Long dateToTimestamp(Date date) {
        return date == null ? null : date.getTime();
    }

    @TypeConverter
    public static Calendar fromCalendar(String value) {
        return CalendarUtil.parseToJavaCalendar(value);
    }

    @NonNull
    @TypeConverter
    public static String calendarTo(Calendar date) {
        return CalendarUtil.calendarToString(date);
    }

    @NonNull
    @TypeConverter
    public static String bookmarkListTo(List<Bookmark> bookmarks) {
        return new Gson().toJson(bookmarks.toArray());
    }

    @NonNull
    @TypeConverter
    public static List<Bookmark> toBookmarkList(String json) {
        return new ArrayList<>(Arrays.asList(new Gson().fromJson(json, Bookmark[].class)));
    }

    @NonNull
    @TypeConverter
    public static String bookmarkGroupListTo(List<BookmarkGroup> bookmarkGroups) {
        return new Gson().toJson(bookmarkGroups.toArray());
    }

    @NonNull
    @TypeConverter
    public static List<BookmarkGroup> toBookmarkGroupList(String json) {
        return new ArrayList<>(Arrays.asList(new Gson().fromJson(json, BookmarkGroup[].class)));
    }
}
