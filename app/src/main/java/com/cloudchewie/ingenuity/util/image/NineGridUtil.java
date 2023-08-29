/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/20 10:47:34
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.util.image;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.webkit.WebView;
import android.widget.ImageView;

import androidx.annotation.NonNull;

import com.cloudchewie.ingenuity.fragment.ImageViewFragment;
import com.cloudchewie.ui.ninegrid.NineGridImageView;
import com.previewlibrary.GPreviewBuilder;
import com.previewlibrary.view.BasePhotoFragment;

import java.util.List;

public class NineGridUtil {
    public static Rect getBounds(ImageView imageView) {
        Rect bounds = new Rect();
        imageView.getGlobalVisibleRect(bounds);
        return bounds;
    }

    public static Rect getBounds(WebView imageView) {
        Rect bounds = new Rect();
        imageView.getGlobalVisibleRect(bounds);
        return bounds;
    }

    public static void computeBoundsBackward(NineGridImageView<ImageViewInfo> nineGridImageView, List<ImageViewInfo> list) {
        for (int i = 0; i < nineGridImageView.getChildCount(); i++) {
            View itemView = nineGridImageView.getChildAt(i);
            Rect bounds = new Rect();
            if (itemView != null)
                itemView.getGlobalVisibleRect(bounds);
            list.get(i).setBounds(bounds);
            list.get(i).setUrl(list.get(i).getUrl());
        }
        for (int i = nineGridImageView.getChildCount(); i < list.size(); i++) {
            View itemView = nineGridImageView.getChildAt(nineGridImageView.getChildCount() - 1);
            Rect bounds = new Rect();
            if (itemView != null)
                itemView.getGlobalVisibleRect(bounds);
            list.get(i).setBounds(bounds);
            list.get(i).setUrl(list.get(i).getUrl());
        }
    }

    public static void setDataSource(NineGridImageView<ImageViewInfo> nineGridImageView, List<ImageViewInfo> imageViewInfos) {
        setDataSource(nineGridImageView, imageViewInfos, true, ImageViewFragment.class);
    }

    public static void setDataSourceWithoutUserFragment(NineGridImageView<ImageViewInfo> nineGridImageView, List<ImageViewInfo> imageViewInfos) {
        setDataSource(nineGridImageView, imageViewInfos, false, ImageViewFragment.class);
    }

    public static void setDataSource(NineGridImageView<ImageViewInfo> nineGridImageView, List<ImageViewInfo> imageViewInfos, boolean isUserFragment, @NonNull Class<? extends BasePhotoFragment> className) {
        GPreviewBuilder.IndicatorType mIndicatorType;
        nineGridImageView.setImagesData(imageViewInfos);
        if (imageViewInfos.size() > 9) mIndicatorType = GPreviewBuilder.IndicatorType.Number;
        else mIndicatorType = GPreviewBuilder.IndicatorType.Dot;
        nineGridImageView.setItemImageClickListener((context, imageView, index, list) -> {
            computeBoundsBackward(nineGridImageView, list);
            if (isUserFragment)
                GPreviewBuilder.from(context).setData(list).setIsScale(true).setCurrentIndex(index).setFullscreen(true).setSingleFling(true).isDisableDrag(false).setType(mIndicatorType).start();
            else
                GPreviewBuilder.from((Activity) context).setData(list).setIsScale(true).setCurrentIndex(index).setFullscreen(true).setSingleFling(true).isDisableDrag(false).setType(mIndicatorType).start();
        });
    }
}
