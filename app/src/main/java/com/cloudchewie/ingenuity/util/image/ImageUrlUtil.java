/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 19:15:26
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.util.image;

import java.util.ArrayList;
import java.util.List;

public final class ImageUrlUtil {
    private static List<String> urls = new ArrayList<>();
    private static List<ImageViewInfo> videos = new ArrayList<>();

    public static List<ImageViewInfo> urlToImageViewInfo(List<String> urls) {
        List<ImageViewInfo> infoList = new ArrayList<>();
        for (String url : urls)
            infoList.add(new ImageViewInfo(url));
        return infoList;
    }

    public static List<ImageViewInfo> urlToImageViewInfo(String url) {
        List<ImageViewInfo> infoList = new ArrayList<>();
        infoList.add(new ImageViewInfo(url));
        return infoList;
    }

    public static List<String> getUrls(int range) {
        urls.clear();
        urls.add("https://hellorfimg.zcool.cn/preview260/129132983.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/1156349101.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/1156349101.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/298757792.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/587590211.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/407797777.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/675219493.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/1045107625.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/551576716.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/1016991457.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/377604361.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/533952916.jpg");
        urls.add("https://hellorfimg.zcool.cn/provider_image/preview260/2234602281.jpg");
        urls.add("https://hellorfimg.zcool.cn/provider_image/preview260/2234602281.jpg");
        urls.add("https://hellorfimg.zcool.cn/provider_image/preview260/2235502018.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/137983838.jpg");
        urls.add("https://hellorfimg.zcool.cn/provider_image/preview260/2234677521.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/321745829.jpg");
        urls.add("https://hellorfimg.zcool.cn/provider_image/preview260/2236891951.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/102396898.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/793540144.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/619468106.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/220323652.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/428018086.jpg");
        urls.add("https://hellorfimg.zcool.cn/provider_image/preview260/2234609274.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/401555077.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/1034426839.jpg");
        urls.add("https://hellorfimg.zcool.cn/provider_image/preview260/2234619039.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/298605944.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/393511423.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/253828555.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/400379983.jpg");
        urls.add("https://hellorfimg.zcool.cn/provider_image/preview260/2237655331.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/606048908.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/181387775.jpg");
        urls.add("https://hellorfimg.zcool.cn/provider_image/preview260/2234602281.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/326490512.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/521163331.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/1499307269.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/459494017.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/102396898.jpg");
        urls.add("https://hellorfimg.zcool.cn/preview260/606363722.jpg");
        urls.add("http://img44.photophoto.cn/20170729/0847085581124963_s.jpg");
        urls.add("http://img44.photophoto.cn/20170729/0847085226124343_s.jpg");
        urls.add("http://img44.photophoto.cn/20170729/0847085226124343_s.jpg");
        urls.add("http://img44.photophoto.cn/20170728/0847085200668628_s.jpg");
        urls.add("http://img44.photophoto.cn/20170728/0847085246003750_s.jpg");
        urls.add("http://img44.photophoto.cn/20170728/0847085012707934_s.jpg");
        urls.add("http://img44.photophoto.cn/20170729/0005018303330857_s.jpg");
        urls.add("http://img44.photophoto.cn/20170729/0847085231427344_s.jpg");
        urls.add("http://img44.photophoto.cn/20170729/0847085236829578_s.jpg");
        urls.add("http://img44.photophoto.cn/20170728/0847085729490157_s.jpg");
        urls.add("http://img44.photophoto.cn/20170727/0847085751995287_s.jpg");
        urls.add("http://img44.photophoto.cn/20170728/0847085729043617_s.jpg");
        urls.add("http://img44.photophoto.cn/20170729/0847085786392651_s.jpg");
        urls.add("http://img44.photophoto.cn/20170728/0847085761440022_s.jpg");
        urls.add("http://img44.photophoto.cn/20170727/0847085275244570_s.jpg");
        urls.add("http://img44.photophoto.cn/20170722/0847085858434984_s.jpg");
        urls.add("http://img44.photophoto.cn/20170721/0847085781987193_s.jpg");
        urls.add("http://img44.photophoto.cn/20170722/0847085707961800_s.jpg");
        urls.add("http://img44.photophoto.cn/20170722/0847085229451104_s.jpg");
        urls.add("http://img44.photophoto.cn/20170720/0847085716198074_s.jpg");
        urls.add("http://img44.photophoto.cn/20170720/0847085769259426_s.jpg");
        urls.add("http://img44.photophoto.cn/20170721/0847085717385169_s.jpg");
        urls.add("http://img44.photophoto.cn/20170721/0847085757949071_s.jpg");
        urls.add("http://img44.photophoto.cn/20170721/0847085789079771_s.jpg");
        urls.add("http://img44.photophoto.cn/20170722/0847085229451104_s.jpg");
        urls.add("http://img44.photophoto.cn/20170721/0847085757949071_s.jpg");
        urls.add("http://img44.photophoto.cn/20170728/0847085265005650_s.jpg");
        urls.add("http://img44.photophoto.cn/20170730/0008118269110532_s.jpg");
        urls.add("http://img44.photophoto.cn/20170731/0008118203762697_s.jpg");
        urls.add("http://img44.photophoto.cn/20170727/0008118269666722_s.jpg");

        List<String> pickedUrls = new ArrayList<>();
        int count = ((int) (Math.random() * 1000)) % range + 1;
        for (int i = 0; i < count; i++)
            pickedUrls.add(urls.get(((int) (Math.random() * 1000)) % 72));
        return pickedUrls;
    }
}
