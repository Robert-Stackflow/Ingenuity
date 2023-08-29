package com.cloudchewie.util.system;

import org.jetbrains.annotations.Contract;

public enum SharedPreferenceCode {
    USER_INFO("user_info", "用户信息"),
    USER_TOKEN("user_token", "用户登陆凭证"),
    THEME_ID("theme_id", "主题颜色"),
    NAV_INDEX("nav_index", "导航次序"),
    AUTO_DAYNIGHT("auto_daynight", "深色模式跟随系统"),
    IS_NIGHT("is_night", "是否为深色模式"),
    ENABLE_WEB_CACHE("enable_web_cache", "是否允许网站缓存");
    private final String key;
    private final String describe;

    SharedPreferenceCode(String key, String describe) {
        this.key = key;
        this.describe = describe;
    }

    @Contract(pure = true)
    public String getKey() {
        return key;
    }

    @Contract(pure = true)
    public String getDescribe() {
        return describe;
    }
}
