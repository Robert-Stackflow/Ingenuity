package com.cloudchewie.ingenuity.request;

import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.cloudchewie.ingenuity.entity.Favorites;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

public class FavoritesCreationRequest {
    public static void create(Favorites favorites) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/favorites/creation/create", favorites)).start();
    }

    public static void update(Favorites favorites) {
        Log.d("xuruida", JSON.toJSONString(favorites));
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/favorites/creation/update", favorites)).start();
    }

    public static void delete(int favoritesId) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/favorites/creation/delete/" + favoritesId, "")).start();
    }
}
