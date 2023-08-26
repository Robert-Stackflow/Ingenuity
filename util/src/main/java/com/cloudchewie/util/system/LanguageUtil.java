package com.cloudchewie.util.system;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.os.Build;
import android.os.Bundle;
import android.os.LocaleList;
import android.text.TextUtils;
import android.util.DisplayMetrics;

import androidx.annotation.NonNull;
import androidx.core.os.ConfigurationCompat;
import androidx.core.os.LocaleListCompat;

import com.blankj.utilcode.util.SPUtils;

import org.jetbrains.annotations.Contract;

import java.util.Locale;

public class LanguageUtil {
    public static String SP_LANGUAGE = "language";
    public static String SP_COUNTRY = "country";

    public static Application.ActivityLifecycleCallbacks callbacks = new Application.ActivityLifecycleCallbacks() {
        @Override
        public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
            String language = SPUtils.getInstance().getString(SP_LANGUAGE, "");
            String country = SPUtils.getInstance().getString(SP_COUNTRY, "");
            if (!TextUtils.isEmpty(language) && !TextUtils.isEmpty(country)) {
                if (!isSameWithSetting(activity)) {
                    Locale locale = new Locale(language, country);
                    setAppLanguage(activity, locale);
                }
            }
        }

        @Contract(pure = true)
        @Override
        public void onActivityStarted(Activity activity) {

        }

        @Contract(pure = true)
        @Override
        public void onActivityResumed(Activity activity) {

        }

        @Contract(pure = true)
        @Override
        public void onActivityPaused(Activity activity) {

        }

        @Contract(pure = true)
        @Override
        public void onActivityStopped(Activity activity) {

        }

        @Contract(pure = true)
        @Override
        public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

        }

        @Contract(pure = true)
        @Override
        public void onActivityDestroyed(Activity activity) {

        }
    };

    /**
     * 修改应用内语言设置
     *
     * @param language 语言
     * @param area     地区
     */
    public static void changeLanguage(Context context, String language, String area) {
        if (TextUtils.isEmpty(language) && TextUtils.isEmpty(area)) {
            SPUtils.getInstance().put(SP_LANGUAGE, "");
            SPUtils.getInstance().put(SP_LANGUAGE, "");
        } else {
            Locale newLocale = new Locale(language, area);
            setAppLanguage(context, newLocale);
            saveLanguageSetting(context, newLocale);
        }
    }

    /**
     * 更新应用语言（核心）
     *
     * @param context
     * @param locale
     */
    private static void setAppLanguage(@NonNull Context context, Locale locale) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        Configuration configuration = resources.getConfiguration();
        if (Build.VERSION.SDK_INT >= 24) {
            configuration.setLocale(locale);
            configuration.setLocales(new LocaleList(locale));
            context.createConfigurationContext(configuration);
            resources.updateConfiguration(configuration, metrics);
        } else {
            configuration.setLocale(locale);
            resources.updateConfiguration(configuration, metrics);
        }
    }

    /**
     * 跟随系统语言
     */
    @Contract("_ -> param1")
    public static Context attachBaseContext(Context context) {
        String spLanguage = SPUtils.getInstance().getString(SP_LANGUAGE, "");
        String spCountry = SPUtils.getInstance().getString(SP_COUNTRY, "");
        if (!TextUtils.isEmpty(spLanguage) && !TextUtils.isEmpty(spCountry)) {
            Locale locale = new Locale(spLanguage, spCountry);
            setAppLanguage(context, locale);
        }
        return context;
    }

    /**
     * 判断SharedPrefences中存储和app中的多语言信息是否相同
     */
    public static boolean isSameWithSetting(Context context) {
        Locale locale = getAppLocale(context);
        String language = locale.getLanguage();
        String country = locale.getCountry();
        String sp_language = SPUtils.getInstance().getString(SP_LANGUAGE, "");
        String sp_country = SPUtils.getInstance().getString(SP_COUNTRY, "");
        return language.equals(sp_language) && country.equals(sp_country);
    }

    /**
     * 保存多语言信息到sp中
     */
    public static void saveLanguageSetting(Context context, @NonNull Locale locale) {
        SPUtils.getInstance().put(SP_LANGUAGE, locale.getLanguage());
        SPUtils.getInstance().put(SP_COUNTRY, locale.getCountry());
    }

    /**
     * 获取应用语言
     */
    public static Locale getAppLocale(Context context) {
        Locale local;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            local = context.getResources().getConfiguration().getLocales().get(0);
        } else {
            local = context.getResources().getConfiguration().locale;
        }
        return local;
    }

    /**
     * 获取系统语言
     */
    @NonNull
    public static LocaleListCompat getSystemLanguage() {
        Configuration configuration = Resources.getSystem().getConfiguration();
        return ConfigurationCompat.getLocales(configuration);
    }

    /**
     * 获取系统语言
     */
    @NonNull
    public static String getAppLanguage(Context context) {
        Locale locale = getAppLocale(context);
        if (locale.toLanguageTag().equals("zh-CN"))
            return "简体中文";
        else if (locale.toLanguageTag().equals("en-US"))
            return "English";
        return "跟随系统";
    }

    /**
     * 设置语言信息
     * <p>
     * 说明：
     * 该方法建议在attachBaseContext和onConfigurationChange中调用，attachBaseContext可以保证页面加载时修改语言信息，
     * 而onConfigurationChange则是为了对应横竖屏切换时系统更新Resource的问题
     *
     * @param context application context
     */
    public static void setConfiguration(Context context) {
        if (context == null) {
            return;
        }
        Context appContext = context.getApplicationContext();
        Locale preferredLocale = getSysPreferredLocale();
        Configuration configuration = appContext.getResources().getConfiguration();
        configuration.setLocale(preferredLocale);
        Resources resources = appContext.getResources();
        DisplayMetrics dm = resources.getDisplayMetrics();
        resources.updateConfiguration(configuration, dm);
    }

    /**
     * 获取系统首选语言
     * <p>
     * 注意：该方法获取的是用户实际设置的不经API调整的系统首选语言
     *
     * @return
     */
    public static Locale getSysPreferredLocale() {
        Locale locale;
        if (Build.VERSION.SDK_INT < 24) {
            locale = Locale.getDefault();
        } else {
            locale = LocaleList.getDefault().get(0);
        }
        return locale;
    }

}
