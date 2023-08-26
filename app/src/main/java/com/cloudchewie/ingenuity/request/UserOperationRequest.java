package com.cloudchewie.ingenuity.request;

import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

public class UserOperationRequest {
    public static void follow(int userId) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/operation/follow/" + userId, "")).start();
    }

    public static void special(int userId) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/operation/special/" + userId, "")).start();
    }

    public static void unFollow(int userId) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/operation/unfollow/" + userId, "")).start();
    }

    public static void block(int userId) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/operation/block/" + userId, "")).start();
    }
}
