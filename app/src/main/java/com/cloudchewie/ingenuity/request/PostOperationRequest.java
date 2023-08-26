package com.cloudchewie.ingenuity.request;

import com.cloudchewie.ingenuity.entity.Collection;
import com.cloudchewie.ingenuity.entity.Comment;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

public class PostOperationRequest {
    public static void thumbup(long postId) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/operation/thumbup/" + postId, "")).start();
    }

    public static void collect(Collection collection) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/operation/collect", collection)).start();
    }

    public static void unthumbup(long postId) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/operation/unthumbup/" + postId, "")).start();
    }

    public static void uncollect(Collection collection) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/operation/uncollect", collection)).start();
    }

    public static void comment(Comment comment) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/operation/comment", comment)).start();
    }
}
