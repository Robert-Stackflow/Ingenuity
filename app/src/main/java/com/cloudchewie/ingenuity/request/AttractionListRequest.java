package com.cloudchewie.ingenuity.request;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.ingenuity.entity.Attraction;
import com.cloudchewie.ingenuity.util.enumeration.ResponseCode;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

import java.util.ArrayList;
import java.util.List;

public class AttractionListRequest {
    public static List<Attraction> find(String key) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/list/find/" + key));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC200.getCode())
            return response[0].getJSONObject("data").getJSONArray("list").toJavaList(Attraction.class);
        return new ArrayList<>();
    }

    public static List<Attraction> recommend(int pagesize, int pagenum) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/list/recommend/" + pagesize + "/" + pagenum));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode())
            return response[0].getJSONObject("data").getJSONArray("list").toJavaList(Attraction.class);
        return new ArrayList<>();
    }
}
