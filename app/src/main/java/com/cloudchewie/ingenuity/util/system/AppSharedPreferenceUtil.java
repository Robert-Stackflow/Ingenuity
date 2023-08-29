package com.cloudchewie.ingenuity.util.system;

import static com.cloudchewie.util.system.SharedPreferenceUtil.getObject;
import static com.cloudchewie.util.system.SharedPreferenceUtil.getString;
import static com.cloudchewie.util.system.SharedPreferenceUtil.putObject;
import static com.cloudchewie.util.system.SharedPreferenceUtil.putString;

import android.content.Context;
import android.util.Log;

import androidx.annotation.NonNull;

import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.util.system.SharedPreferenceCode;
import com.cloudchewie.util.system.SharedPreferenceUtil;
import com.cloudchewie.util.ui.DarkModeUtil;

/**
 * SharedPreferences工具类
 */
public class AppSharedPreferenceUtil {
    private static final String NAME = "config";

    public static boolean isAutoDaynight(@NonNull Context context) {
        return SharedPreferenceUtil.getBoolean(context, SharedPreferenceCode.AUTO_DAYNIGHT.getKey(), true);
    }

    public static boolean isNight(@NonNull Context context) {
        return SharedPreferenceUtil.getBoolean(context, SharedPreferenceCode.IS_NIGHT.getKey(), DarkModeUtil.isDarkMode(context));
    }

    public static void setAutoDaynight(@NonNull Context context, boolean isAutoDaynight) {
        SharedPreferenceUtil.putBoolean(context, SharedPreferenceCode.AUTO_DAYNIGHT.getKey(), isAutoDaynight);
        Log.d("xuruida", isAutoDaynight ? "设置为深色跟随系统" : "设置为深色不跟随系统");
    }

    public static void setNight(@NonNull Context context, boolean isNight) {
        SharedPreferenceUtil.putBoolean(context, SharedPreferenceCode.IS_NIGHT.getKey(), isNight);
        Log.d("xuruida", isNight ? "设置为深色" : "设置为浅色");
    }


    public static boolean isLogin(@NonNull Context context) {
        return (getToken(context) != null) && (getUserInfo(context) != null);
    }

    public static User getUserInfo(@NonNull Context context) {
        return (User) getObject(context, SharedPreferenceCode.USER_INFO.getKey(), User.class);
    }

    public static void setUserInfo(@NonNull Context context, User user) {
        putObject(context, SharedPreferenceCode.USER_INFO.getKey(), user);
    }

    public static int getUserId(@NonNull Context context) {
        return getUserInfo(context) == null ? -1 : getUserInfo(context).getUserId();
    }

    public static String getToken(@NonNull Context context) {
        return getString(context, SharedPreferenceCode.USER_TOKEN.getKey(), "");
    }

    public static void setToken(@NonNull Context context, String token) {
        putString(context, SharedPreferenceCode.USER_TOKEN.getKey(), token);
    }

    public static void logout(@NonNull Context context) {
        SharedPreferenceUtil.remove(context, SharedPreferenceCode.USER_TOKEN.getKey());
        SharedPreferenceUtil.remove(context, SharedPreferenceCode.USER_INFO.getKey());
    }
}
