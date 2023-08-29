/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 19:06:01
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;

import androidx.annotation.NonNull;

import com.cloudchewie.ingenuity.util.image.MyImageLoader;
import com.cloudchewie.util.system.LanguageUtil;
import com.previewlibrary.ZoomMediaLoader;
import com.scwang.smart.refresh.footer.ClassicsFooter;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;

public class App extends Application {
    static {
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> new ClassicsHeader(context).setDrawableSize(10).setDrawableArrowSize(1).setDrawableProgressSize(10));
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context).setDrawableSize(10).setDrawableProgressSize(10).setDrawableArrowSize(10));
    }

    @Override
    public void onCreate() {
        super.onCreate();
        registerActivityLifecycleCallbacks(LanguageUtil.callbacks);
        ZoomMediaLoader.getInstance().init(new MyImageLoader());
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        connectivityManager.requestNetwork(new NetworkRequest.Builder().build(),
                new ConnectivityManager.NetworkCallback() {
                    @Override
                    public void onAvailable(@NonNull Network network) {
                        super.onAvailable(network);
                    }
                });
    }
}