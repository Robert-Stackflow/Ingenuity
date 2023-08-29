/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.api;

import com.alibaba.fastjson.JSONObject;
import com.blankj.utilcode.util.Utils;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.ingenuity.util.database.AppSharedPreferenceUtil;
import com.cloudchewie.ingenuity.util.enumeration.ResponseCode;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

public class UserAuthRequest {

    public static void signUp(User user) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/auth/signup", user)).start();
    }

    public static void login(User user) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/auth/login", user));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode()) {
            User ret = response[0].getJSONObject("data").toJavaObject(User.class);
            AppSharedPreferenceUtil.setToken(Utils.getApp(), ret.getToken());
        }
    }

    public static void logout() {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/auth/logout", "")).start();
    }

    public static void cancel() {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/auth/cancel", "")).start();
    }

    public static User info() {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/auth/info/" + AppSharedPreferenceUtil.getUserId(Utils.getApp())));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        User user = null;
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode())
            user = JSONObject.toJavaObject(response[0].getJSONObject("data"), User.class);
        AppSharedPreferenceUtil.setUserInfo(Utils.getApp(), user);
        return user;
    }

    public static void update(User user) {
        Thread thread = new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/auth/update", user));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        AppSharedPreferenceUtil.setUserInfo(Utils.getApp(), user);
    }
}
