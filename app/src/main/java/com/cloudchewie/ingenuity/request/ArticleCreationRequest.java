package com.cloudchewie.ingenuity.request;

import com.cloudchewie.ingenuity.entity.Article;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

public class ArticleCreationRequest {
    public static void publish(Article article) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/article/creation/publish", article)).start();
    }

    public static void delete(int articleId) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/article/creation/delete/" + articleId, "")).start();
    }

    public static void update(Article article) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/article/creation/update", article)).start();
    }
}
