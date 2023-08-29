package com.cloudchewie.ingenuity.api;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.ingenuity.util.enumeration.ResponseCode;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

import java.util.ArrayList;
import java.util.List;

public class UserProfileRequest {
    public static User info(long userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/auth/info/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User user = null;
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode())
            user = JSONObject.toJavaObject(response[0].getJSONObject("data"), User.class);
        return user;
    }

    public static List<User> getFans(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/followers/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode() && response[0].getJSONArray("data") != null)
            return response[0].getJSONArray("data").toJavaList(User.class);
        return new ArrayList<>();
    }

    public static List<String> getAlbum(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/album/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode() && response[0].getJSONArray("data") != null)
            return response[0].getJSONArray("data").toJavaList(String.class);
        return new ArrayList<>();
    }

}
