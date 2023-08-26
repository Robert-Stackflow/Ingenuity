package com.cloudchewie.ingenuity.util.webview;


import android.app.Activity;
import android.content.Context;
import android.graphics.Rect;
import android.webkit.WebView;

import androidx.annotation.NonNull;

import com.cloudchewie.ingenuity.util.image.ImageViewInfo;
import com.cloudchewie.ingenuity.util.image.NineGridUtil;
import com.previewlibrary.GPreviewBuilder;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.List;

public class ImageJsInterface {
    private Rect bounds;
    private Context context;
    private List<String> imageUrls;
    private List<ImageViewInfo> mThumbViewInfoList;

    public ImageJsInterface(WebView webView, Context context, @NonNull List<String> imageUrls) {
        this.context = context;
        this.imageUrls = imageUrls;
        bounds = NineGridUtil.getBounds(webView);
        mThumbViewInfoList = new ArrayList<>();
        for (String img : imageUrls) {
            ImageViewInfo imageViewInfo = new ImageViewInfo(img);
            imageViewInfo.setBounds(bounds);
            mThumbViewInfoList.add(imageViewInfo);
        }
    }

    @android.webkit.JavascriptInterface
    public void openImage(@NonNull String img) {
        int index = 0;
        try {
            index = imageUrls.indexOf(URLDecoder.decode(img.replace("file://", ""), "utf-8"));
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (index == -1)
            index = 0;
        if (imageUrls.size() > 9)
            GPreviewBuilder.from((Activity) context).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(index).setSingleFling(true).isDisableDrag(false).setType(GPreviewBuilder.IndicatorType.Number).setFullscreen(true).start();
        else
            GPreviewBuilder.from((Activity) context).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(index).setSingleFling(true).isDisableDrag(false).setType(GPreviewBuilder.IndicatorType.Dot).setFullscreen(true).start();
    }
}
