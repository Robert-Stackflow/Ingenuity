package com.cloudchewie.ingenuity.api;

import com.alibaba.fastjson.JSONObject;
import com.cloudchewie.ingenuity.util.http.HttpRequestUtil;
import com.cloudchewie.ingenuity.util.image.ImageUrlUtil;

import org.jetbrains.annotations.Contract;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class ImageUploadRequest {
    public static String upload(String url) {
        final JSONObject[] response = new JSONObject[1];
        Thread thread = new Thread(() -> response[0] = HttpRequestUtil.uploadToServer("/file/upload", new File(url.replace("file://", ""))));
        thread.start();
        try {
            thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        if (response[0] != null && response[0].getString("data") != null) {
            return response[0].getString("data").replace("localhost", HttpRequestUtil.serverHost);
        }
        return ImageUrlUtil.getUrls(2).get(0);
    }

    @Contract("null -> null")
    public static List<String> upload(List<String> urls) {
        if (urls == null) return null;
        if (urls.size() == 0) return new ArrayList<>();
        List<String> strings = new ArrayList<>();
        for (String url : urls)
            strings.add(upload(url));
        return strings;
    }
}
