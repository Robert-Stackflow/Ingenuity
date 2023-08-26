package com.cloudchewie.ingenuity.request;

import com.cloudchewie.ingenuity.entity.Collection;
import com.cloudchewie.ingenuity.entity.Comment;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

public class ArticleOperationRequest {
    public static void thumbup(int articleId) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/article/operation/thumbup/" + articleId, "")).start();
    }

    public static void collect(Collection collection) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/article/operation/collect", collection)).start();
    }

    public static void unthumbup(int articleId) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/article/operation/unthumbup/" + articleId, "")).start();
    }

    public static void uncollect(Collection collection) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/article/operation/uncollect", collection)).start();
    }

    public static void comment(Comment comment) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/post/operation/comment", comment)).start();
    }
}
