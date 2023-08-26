package com.cloudchewie.ingenuity.util.image;

import android.content.Context;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.ingenuity.R;

import cn.jiguang.imui.commons.ImageLoader;

public class MyMessageImageLoader implements ImageLoader {
    Context context;

    public MyMessageImageLoader(Context context) {
        this.context = context;
    }

    @Override
    public void loadAvatarImage(ImageView avatarImageView, String string) {
        Glide.with(context).load(string).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(avatarImageView);
    }

    @Override
    public void loadImage(ImageView imageView, String string) {
        Glide.with(context).load(string).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).centerCrop().into(imageView);
    }

    @Override
    public void loadImage(ImageView imageView, String string, int radius, boolean leftTop, boolean rightTop, boolean leftBottom, boolean rightBottom) {
        Glide.with(context).load(string).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background).transform(CornerTransformation.getTransform(context, radius, leftTop, rightTop, leftBottom, rightBottom))).centerCrop().into(imageView);
    }

    @Override
    public void loadVideo(ImageView imageCover, String uri) {
        Glide.with(context).load(uri).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).centerCrop().into(imageCover);
    }
}
