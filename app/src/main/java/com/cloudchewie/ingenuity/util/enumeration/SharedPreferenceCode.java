package com.cloudchewie.ingenuity.util.enumeration;

import org.jetbrains.annotations.Contract;

public enum SharedPreferenceCode {
    USER_INFO("user_info", "用户信息"),
    USER_TOKEN("user_token", "用户登陆凭证"),
    CITY_HISTORY("city_history", "搜索城市列表"),
    ATTRACTION_HISTORY("attraction_history", "搜索地点列表");
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
