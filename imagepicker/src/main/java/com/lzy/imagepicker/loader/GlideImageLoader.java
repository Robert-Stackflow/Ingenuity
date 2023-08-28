package com.lzy.imagepicker.loader;

import android.app.Activity;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.lzy.imagepicker.R;

import java.io.File;

/**
 * ================================================
 * 作    者：jeasonlzy（廖子尧 Github地址：https://github.com/jeasonlzy0216
 * 版    本：1.0
 * 创建日期：2016/5/19
 * 描    述：
 * 修订历史：
 * ================================================
 */
public class GlideImageLoader implements ImageLoader {

    @Override
    public void displayImage(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity).load(new File(path)).error(R.mipmap.default_error).placeholder(R.mipmap.default_error).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }


    @Override
    public void displayImagePreview(Activity activity, String path, ImageView imageView, int width, int height) {
        Glide.with(activity).load(new File(path)).error(R.mipmap.default_error).placeholder(R.mipmap.default_error).diskCacheStrategy(DiskCacheStrategy.ALL).into(imageView);
    }

    @Override
    public void clearMemoryCache() {
    }

}
