package com.cloudchewie.util.system;

import org.jetbrains.annotations.Contract;

public enum SharedPreferenceCode {
    USER_INFO("user_info", "用户信息"),
    USER_TOKEN("user_token", "用户登陆凭证"),
    APP_FIRST_START("app_first_start", "首次打开APP"),
    START_UP_APP_TIME("start_up_app_time", "打开APP时间"),
    THEME_ID("theme_id", "主题颜色"),
    NAV_INDEX("nav_index", "导航次序"),
    AUTO_DAYNIGHT("auto_daynight", "深色模式跟随系统"),
    IS_NIGHT("is_night", "是否为深色模式"),
    ENABLE_WEB_CACHE("enable_web_cache", "是否允许网站缓存"),
    LONG_PRESS_COPY_CODE("long_press_copy_code", "长按卡片复制密码"),
    LONG_PRESS_COPY_PASSCODE("long_press_copy_passcode", "长按卡片复制密码"),
    CLICK_COPY_CODE("click_copy_code", "点击卡片复制密码"),
    CLICK_COPY_PASSWORD("click_copy_password", "点击卡片复制密码"),
    AUTH_TO_SHOW_CODE("auth_to_show_code", "需要身份验证"),
    DISBALE_SCREEN_SHOT("disable_screen_shot", "禁止屏幕截图");
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
