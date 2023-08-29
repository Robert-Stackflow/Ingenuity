package com.cloudchewie.ingenuity.util.enumeration;

import org.jetbrains.annotations.Contract;

public enum EventBusCode {
    CHANGE_THEME("change_theme", "更改主题颜色"),
    CHANGE_AUTO_DAYNIGHT("change_auto_daynight", "更改深色模式是否跟随系统");
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
