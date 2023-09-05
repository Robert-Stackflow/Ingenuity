package com.cloudchewie.util.system;

import org.jetbrains.annotations.Contract;

public enum SharedPreferenceCode {
    USER_INFO("user_info", "用户信息"),
    USER_TOKEN("user_token", "用户登陆凭证"),
    APP_FIRST_START("app_first_start", "首次打开APP"),
    START_UP_APP_TIME("start_up_app_time", "打开APP时间"),
    //系统设置
    THEME_ID("theme_id", "主题ID"),
    NAV_INDEX("nav_index", "上次导航栏次序"),
    AUTO_DAYNIGHT("auto_daynight", "深色模式跟随系统"),
    IS_NIGHT("is_night", "是否为深色模式"),
    ENABLE_WEB_CACHE("enable_web_cache", "是否允许网站缓存"),
    TOKEN_CLICK_COPY("token_click_copy", "点击卡片复制令牌"),
    TOKEN_LONG_CLICK_COPY("token_long_click_copy", "长按卡片复制令牌"),
    TOKEN_NEED_AUTH("token_need_auth", "需要身份验证"),
    TOKEN_DISBALE_SCREENSHOT("token_disable_screenshot", "禁止屏幕截图"),
    PASSWORD_CLICK_COPY("password_click_copy", "点击卡片复制密码"),
    PASSWORD_LONG_CLICK_COPY("password_long_click_copy", "长按卡片复制密码"),
    PASSWORD_NEED_AUTH("password_need_auth", "需要身份验证"),
    PASSWORD_DISBALE_SCREENSHOT("password_disable_screenshot", "禁止屏幕截图");
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
