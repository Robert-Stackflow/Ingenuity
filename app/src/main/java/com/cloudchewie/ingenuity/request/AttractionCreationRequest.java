package com.cloudchewie.ingenuity.request;

import com.cloudchewie.ingenuity.entity.Attraction;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

public class AttractionCreationRequest {
    public static void publish(Attraction attraction) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/creation/publish", attraction)).start();
    }

    public static void update(Attraction attraction) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/creation/update", attraction)).start();
    }
}
