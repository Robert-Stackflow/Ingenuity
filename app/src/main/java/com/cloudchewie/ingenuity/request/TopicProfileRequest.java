package com.cloudchewie.ingenuity.request;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.ingenuity.entity.Article;
import com.cloudchewie.ingenuity.entity.Post;
import com.cloudchewie.ingenuity.entity.Topic;
import com.cloudchewie.ingenuity.util.enumeration.ResponseCode;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

import java.util.List;

public class TopicProfileRequest {
    public static Topic info(long topicId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/topic/profile/info/" + topicId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getJSONObject("data") != null)
            return response[0].getJSONObject("data").toJavaObject(Topic.class);
        return null;
    }

    public static List<Post> getPosts(int topicId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/topic/profile/posts/" + topicId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC200.getCode())
            return response[0].getJSONArray("data").toJavaList(Post.class);
        return null;
    }

    public static List<Article> getArticles(int topicId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/topic/profile/articles/" + topicId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getIntValue("code") == ResponseCode.RC200.getCode())
            return response[0].getJSONArray("data").toJavaList(Article.class);
        return null;
    }

}
