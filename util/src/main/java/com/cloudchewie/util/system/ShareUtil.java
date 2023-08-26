package com.cloudchewie.util.system;

import androidx.annotation.NonNull;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.Map;

public class ShareUtil {
    private static String BAIDUMAP = "com.baidu.BaiduMap";

    /**
     * 获取受支持的分享应用包名列表
     *
     * @return 应用包名列表
     */
    @NonNull
    public static Map<String, String> getSupportedPackages() {
        Map<String, String> appPackageNameMap = new HashMap<>();
        appPackageNameMap.put("微信", "com.tencent.mm");
        appPackageNameMap.put("QQ", "com.tencent.mobileqq");
        appPackageNameMap.put("QQ空间", "com.qzone");
        appPackageNameMap.put("微博", "com.sina.weibo");
        appPackageNameMap.put("TIM", "com.tencent.tim");
        return appPackageNameMap;
    }

    @Contract(pure = true)
    public static String getBaiduMapPackageName() {
        return BAIDUMAP;
    }
}
