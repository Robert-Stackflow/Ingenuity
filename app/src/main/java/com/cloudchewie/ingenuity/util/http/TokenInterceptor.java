/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:26:23
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.util.http;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.Utils;
import com.cloudchewie.ingenuity.util.database.AppSharedPreferenceUtil;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class TokenInterceptor implements Interceptor {

    @NonNull
    @Override
    public Response intercept(@NonNull Chain chain) throws IOException {
        Request originRequest = chain.request();
        if (!HttpRequestUtil.isAuthRequest(originRequest.url().toString())) {
            Request newRequest = originRequest.newBuilder()
                    .addHeader("token", AppSharedPreferenceUtil.getToken(Utils.getApp()))
                    .build();
            return chain.proceed(newRequest);
        } else {
            return chain.proceed(originRequest);
        }
    }
}
