package com.cloudchewie.ingenuity.util.enumeration;

import org.jetbrains.annotations.Contract;

public enum EventBusCode {
    CHANGE_THEME("change_theme", "更改主题颜色"),
    CHANGE_AUTO_DAYNIGHT("change_auto_daynight", "更改深色模式是否跟随系统"),
    CHANGE_TOKEN("change_token", "令牌更新"),
    CHANGE_BOOKMARK("change_bookmark", "书签更新"),
    CHANGE_AUTH_SHOW_CODE("auth_show_code", "身份验证"),
    CHANGE_SCREEN_SHOT("change_screen_shot", "是否允许截图更新");
    private final String key;
    private final String describe;

    EventBusCode(String key, String describe) {
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
