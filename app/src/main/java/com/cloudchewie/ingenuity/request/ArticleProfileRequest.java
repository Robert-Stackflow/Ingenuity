package com.cloudchewie.ingenuity.request;

import android.util.Log;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.ingenuity.entity.Article;
import com.cloudchewie.ingenuity.entity.Collection;
import com.cloudchewie.ingenuity.entity.Comment;
import com.cloudchewie.ingenuity.util.enumeration.ResponseCode;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

import java.util.ArrayList;
import java.util.List;

public class ArticleProfileRequest {
    public Article info(int articleId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/article/profile/info"));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return response[0].getJSONObject("data").toJavaObject(Article.class);
    }

    public List<Comment> getComments(int articleId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/article/profile/comments/" + articleId));
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

    public boolean isThumbup(int articleId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/article/profile/isthumbup/" + articleId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("xuruida", String.valueOf(response[0]));
        if (response[0].getIntValue("code") == ResponseCode.RC200.getCode())
            return response[0].getBoolean("data");
        return false;
    }

    public List<Collection> isCollected(int articleId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/article/profile/iscollect/" + articleId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        Log.d("xuruida", String.valueOf(response[0]));
        if (response[0].getIntValue("code") == ResponseCode.RC200.getCode())
            return response[0].getJSONArray("data").toJavaList(Collection.class);
        return new ArrayList<>();
    }
}
