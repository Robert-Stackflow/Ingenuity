/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:26:23
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.util.database;

import androidx.annotation.NonNull;
import androidx.room.TypeConverter;

import com.cloudchewie.util.basic.CalendarUtil;

import org.jetbrains.annotations.Contract;

import java.util.Calendar;
import java.util.Date;

public class Converters {
    @Contract("null -> null; !null -> new")
    @TypeConverter
    public static Date fromTimestamp(Long value) {
        return value == null ? null : new Date(value);
    }

    @Contract("null -> null")
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
    
}
