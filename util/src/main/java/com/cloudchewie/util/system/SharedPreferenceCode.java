package com.cloudchewie.util.system;

import org.jetbrains.annotations.Contract;

public enum SharedPreferenceCode {
    USER_INFO("user_info", "用户信息"),
    USER_TOKEN("user_token", "用户登陆凭证"),
    THEME_ID("theme_id", "主题颜色"),
    NAV_INDEX("nav_index", "导航次序");
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
