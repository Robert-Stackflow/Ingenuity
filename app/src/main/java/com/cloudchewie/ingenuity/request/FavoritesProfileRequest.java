package com.cloudchewie.ingenuity.request;

import androidx.annotation.Nullable;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.ingenuity.entity.Favorites;
import com.cloudchewie.ingenuity.entity.Post;
import com.cloudchewie.ingenuity.util.enumeration.ResponseCode;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

import java.util.List;

public class FavoritesProfileRequest {
    public static Favorites info(int favoritesId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/favorites/profile/info/" + favoritesId));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0].getJSONObject("data") != null)
            return response[0].getJSONObject("data").toJavaObject(Favorites.class);
        return null;
    }

    @Nullable
    public static List<Post> getPosts(int favoritesId) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.getFromServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/favorites/profile/items/" + favoritesId));
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
}
