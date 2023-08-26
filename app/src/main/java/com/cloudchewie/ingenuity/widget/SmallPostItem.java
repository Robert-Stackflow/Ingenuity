/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 21:47:29
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.widget;

import static com.cloudchewie.util.basic.DateUtil.beautifyTime;
import static com.cloudchewie.util.basic.StringUtil.handleLineBreaks;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.entity.Post;
import com.cloudchewie.ingenuity.util.image.CornerTransformation;

public class SmallPostItem extends RelativeLayout {
    Post mPost;
    Context context;
    private RelativeLayout mainLayout;
    private TextView username;
    private TextView content;
    private TextView time;
    private ImageView imageView;

    public SmallPostItem(@NonNull Context context) {
        super(context);
        initView(context, null);
    }

    public SmallPostItem(@NonNull Context context, Post post) {
        super(context);
        initView(context, null);
        setPost(post);
    }

    public SmallPostItem(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public SmallPostItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public SmallPostItem(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        this.context = context;
        LayoutInflater.from(context).inflate(com.cloudchewie.ui.R.layout.widget_small_post_item, this, true);
        mainLayout = findViewById(com.cloudchewie.ui.R.id.small_post_item_layout);
        username = findViewById(com.cloudchewie.ui.R.id.small_post_item_username);
        content = findViewById(com.cloudchewie.ui.R.id.small_post_item_content);
        time = findViewById(com.cloudchewie.ui.R.id.small_post_item_time);
        imageView = findViewById(com.cloudchewie.ui.R.id.small_post_item_image);
    }

    public void setPost(Post post) {
        this.mPost = post;
        username.setText(post.getUser().getNickname());
        content.setText(handleLineBreaks(post.getContent()));
        time.setText(beautifyTime(post.getGmtEdit()));
        mainLayout.setOnClickListener(view -> {
        });
        Glide.with(context).load(post.getImages().get(0)).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background).transform(CornerTransformation.getTransform(getContext(), 5, true, true, true, true))).into(imageView);
    }
}
