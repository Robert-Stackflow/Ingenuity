package com.cloudchewie.ingenuity.request;

import android.util.Log;

import com.cloudchewie.ingenuity.entity.Topic;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

public class TopicCreationRequest {
    public static void publish(Topic topic) {
        Log.d("xuruida", topic.toString());
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/topic/creation/publish", topic)).start();
    }

    public static void update(Topic topic) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/topic/creation/update", topic)).start();
    }
}
