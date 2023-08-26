package com.cloudchewie.ingenuity.request;

import com.cloudchewie.ingenuity.entity.Post;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

public class PostCreationRequest {
    public static void publish(Post post) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/creation/publish", post)).start();
    }

    public static void delete(long postId) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/creation/delete/" + postId, "")).start();
    }

    public static void update(Post post) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/creation/update", post)).start();
    }
}
