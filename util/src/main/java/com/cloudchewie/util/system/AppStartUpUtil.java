package com.cloudchewie.util.system;

import static com.cloudchewie.util.basic.DateFormatUtil.MD_FORMAT_WITH_BAR;

import android.content.Context;

import com.cloudchewie.util.basic.Constant;
import com.cloudchewie.util.basic.DateFormatUtil;

import java.util.Date;

/**
 * APP启动判断工具类
 */
public class AppStartUpUtil {
    private static String DEFAULT_STARTUP_DAY = "2023-01-01";

    /**
     * 判断是否是首次启动
     *
     * @param context
     * @return
     */
    public static boolean isFirstStartApp(Context context) {
        boolean isFirst = SharedPreferenceUtil.getBoolean(context, Constant.APP_FIRST_START, true);
        if (isFirst) {
            SharedPreferenceUtil.putBoolean(context, Constant.APP_FIRST_START, false);
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断是否是今日首次启动APP
     *
     * @param context
     * @return
     */
    public static boolean isTodayFirstStartApp(Context context) {
        String defaultDay = SharedPreferenceUtil.getString(context, Constant.START_UP_APP_TIME, DEFAULT_STARTUP_DAY);
        String today = DateFormatUtil.getSimpleDateFormat(MD_FORMAT_WITH_BAR).format(new Date());
        if (!defaultDay.equals(today)) {
            SharedPreferenceUtil.putString(context, Constant.START_UP_APP_TIME, today);
            return true;
        } else {
            return false;
        }
    }
}
