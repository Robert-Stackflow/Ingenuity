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
        ClassicsHeader.REFRESH_HEADER_PULLING = "下拉看见更多精彩";
        ClassicsHeader.REFRESH_HEADER_REFRESHING = "新的世界正奔赴而来...";
        ClassicsHeader.REFRESH_HEADER_LOADING = "更多内容正努力赶来...";
        ClassicsHeader.REFRESH_HEADER_RELEASE = "释放立即打开新的世界";
        ClassicsHeader.REFRESH_HEADER_FINISH = "新世界的大门已经向您敞开";
        ClassicsHeader.REFRESH_HEADER_FAILED = "刷新失败";

        ClassicsFooter.REFRESH_FOOTER_FAILED = "加载失败";
        ClassicsFooter.REFRESH_FOOTER_FINISH = "已为您展现更多内容";
        ClassicsFooter.REFRESH_FOOTER_LOADING = "更多内容正努力赶来...";
        ClassicsFooter.REFRESH_FOOTER_NOTHING = "木有更多内容啦";
        ClassicsFooter.REFRESH_FOOTER_PULLING = "上拉加载";
        ClassicsFooter.REFRESH_FOOTER_REFRESHING = "正在加载";
        ClassicsFooter.REFRESH_FOOTER_RELEASE = "释放立即加载";
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