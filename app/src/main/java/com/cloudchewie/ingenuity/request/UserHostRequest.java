package com.cloudchewie.ingenuity.request;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.ingenuity.entity.Attraction;
import com.cloudchewie.ingenuity.entity.Topic;
import com.cloudchewie.ingenuity.util.enumeration.ResponseCode;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

import java.util.ArrayList;
import java.util.List;

public class UserHostRequest {
    public static List<Topic> getHostTopics(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/host/topics/" + userId));
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

    public static List<Attraction> getHostAttractions(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/host/attractions/" + userId));
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
}
