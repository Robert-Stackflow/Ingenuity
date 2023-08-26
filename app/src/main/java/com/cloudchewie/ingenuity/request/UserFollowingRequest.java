package com.cloudchewie.ingenuity.request;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.ingenuity.entity.Attraction;
import com.cloudchewie.ingenuity.entity.Topic;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.ingenuity.util.enumeration.ResponseCode;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

import java.util.ArrayList;
import java.util.List;

public class UserFollowingRequest {
    public static List<Topic> getFollowingTopics(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/following/topics/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode())
            return response[0].getJSONArray("data").toJavaList(Topic.class);
        return new ArrayList<>();
    }

    public static List<Attraction> getFollowingAttractions(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/following/attractions/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode())
            return response[0].getJSONArray("data").toJavaList(Attraction.class);
        return new ArrayList<>();
    }

    public static List<User> getFollowingUsers(int userId, FOLLOW_TYPE type) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/following/users/" + userId + "/" + type.ordinal()));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode())
            return response[0].getJSONArray("data").toJavaList(User.class);
        return new ArrayList<>();
    }

    public enum FOLLOW_TYPE {
        ALL,
        FOLLOW,
        SPECIAL
    }
}
