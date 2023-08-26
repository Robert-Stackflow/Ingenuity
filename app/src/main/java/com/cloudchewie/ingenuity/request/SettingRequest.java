package com.cloudchewie.ingenuity.request;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.Utils;
import com.cloudchewie.ingenuity.entity.Setting;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;
import com.cloudchewie.ingenuity.util.system.SPUtil;

import org.jetbrains.annotations.Contract;

public class SettingRequest {
    public static boolean isDefaultFavoritesPublic() {
        if (info(SPUtil.getUserId(Utils.getApp())) != null)
            return info(SPUtil.getUserId(Utils.getApp())).getDefaultFavoritesPublic() != 0;
        return false;
    }

    @Contract(pure = true)
    public static void setDefaultFavoritesPublic(boolean defaultFavoritesPublic) {
        Setting setting = info(SPUtil.getUserId(Utils.getApp()));
        setting.setDefaultFavoritesPublic((byte) (defaultFavoritesPublic ? 1 : 0));
    }

    public static boolean isDefaultFavoritesPublic(int userId) {
        return Math.random() > 0.5;
    }

    public static Setting info(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/setting/info/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getJSONObject("data") != null)
            return response[0].getJSONObject("data").toJavaObject(Setting.class);
        return new Setting();
    }

    public static void update(Setting setting) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/setting/update", setting)).start();
    }
}
