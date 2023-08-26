package com.cloudchewie.ingenuity.request;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.ingenuity.entity.Collection;
import com.cloudchewie.ingenuity.entity.Comment;
import com.cloudchewie.ingenuity.entity.Post;
import com.cloudchewie.ingenuity.util.enumeration.ResponseCode;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

import java.util.ArrayList;
import java.util.List;

public class PostProfileRequest {
    public static Post info(long postId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/profile/info"));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response[0].getJSONObject("data").toJavaObject(Post.class);
    }

    public static List<Comment> getComments(long postId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/profile/comments/" + postId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC200.getCode())
            return response[0].getJSONArray("data").toJavaList(Comment.class);
        return null;
    }

    public static boolean isThumbup(long postId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/profile/isthumbup/" + postId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode())
            return response[0].getBoolean("data");
        return false;
    }

    public static List<Collection> isCollected(long postId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/profile/iscollect/" + postId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getJSONArray("data") != null && response[0].getJSONArray("data").size() > 0 && response[0].getIntValue("code") == ResponseCode.RC200.getCode()) {
            return response[0].getJSONArray("data").toJavaList(Collection.class);
        }
        return new ArrayList<>();
    }
}
