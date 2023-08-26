package com.cloudchewie.ingenuity.request;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.ingenuity.entity.Article;
import com.cloudchewie.ingenuity.entity.Attraction;
import com.cloudchewie.ingenuity.entity.Favorites;
import com.cloudchewie.ingenuity.entity.Post;
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

    public static List<Attraction> getVisitedAttractions(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/visited/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode() && response[0].getJSONArray("data") != null)
            return response[0].getJSONArray("data").toJavaList(Attraction.class);
        return new ArrayList<>();
    }

    public static List<Attraction> getWantAttractions(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/want/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode() && response[0].getJSONArray("data") != null)
            return response[0].getJSONArray("data").toJavaList(Attraction.class);
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

    public static List<Post> getPosts(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/posts/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode() && response[0].getJSONArray("data") != null)
            return response[0].getJSONArray("data").toJavaList(Post.class);
        return new ArrayList<>();
    }

    public static List<Article> getArticles(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/articles/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode() && response[0].getJSONArray("data") != null)
            return response[0].getJSONArray("data").toJavaList(Article.class);
        return new ArrayList<>();
    }

    public static List<Favorites> getFavorites(int userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/favorites/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode() && response[0].getJSONArray("data") != null)
            return response[0].getJSONArray("data").toJavaList(Favorites.class);
        return new ArrayList<>();
    }

    public static int getRelation(long userId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/user/profile/relation/" + userId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getIntValue("code") == ResponseCode.RC200.getCode())
            return response[0].getIntValue("data");
        return -1;
    }

}
