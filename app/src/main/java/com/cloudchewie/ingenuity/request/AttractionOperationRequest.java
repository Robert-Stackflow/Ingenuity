package com.cloudchewie.ingenuity.request;

import com.cloudchewie.ingenuity.entity.Attraction;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;

public class AttractionOperationRequest {
    public void follow(int attractionId, Attraction.FOLLOW_TYPE type) {
        new Thread(() -> HttpRequestUtil.sendToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/operation/follow/" + attractionId + "/" + type)).start();
    }

    public void unfollow(int attractionId) {
        new Thread(() -> HttpRequestUtil.postToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/operation/unfollow/" + attractionId, "")).start();
    }

    public void evaluate(int attractionId, double score) {
        new Thread(() -> HttpRequestUtil.sendToServer(HttpRequestUtil.MEDIA_TYPE_JSON, "/attraction/operation/evaluate/" + score)).start();
    }
}
