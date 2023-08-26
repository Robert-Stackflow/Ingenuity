/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/17 22:05:40
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ui.custom;

import android.content.Context;
import android.content.res.ColorStateList;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.appcompat.content.res.AppCompatResources;

import com.cloudchewie.ui.R;
import com.cloudchewie.util.ui.SizeUtil;

public class TagItem extends RelativeLayout {
    private TextView textView;
    private RelativeLayout mainLayout;

    public TagItem(Context context) {
        super(context);
        initView(context, null);
    }

    public TagItem(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context, attrs);
    }

    public TagItem(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context, attrs);
    }

    public TagItem(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initView(context, attrs);
    }

    private void initView(Context context, AttributeSet attrs) {
        LayoutInflater.from(context).inflate(R.layout.widget_tag_item, this, true);
        mainLayout = findViewById(R.id.tag_item_layout);
        textView = findViewById(R.id.tag_item_text);
        TypedArray attr = context.obtainStyledAttributes(attrs, R.styleable.TagItem);
        String text;
        if (attr != null) {
            int backgroundId = attr.getResourceId(R.styleable.TagItem_tag_item_background, R.drawable.shape_round_dp10);
            text = attr.getString(R.styleable.TagItem_tag_item_text);
            int textColor = attr.getColor(R.styleable.TagItem_tag_item_text_color, getResources().getColor(R.color.text_color_entry, getResources().newTheme()));
            int textSize = (int) attr.getDimension(R.styleable.TagItem_tag_item_text_size, 14);
            setText(text);
            setTextSize(textSize);
            setTextColor(textColor);
            setBackground(backgroundId);
            attr.recycle();
        }
    }

    public void setPadding(int left, int top, int right, int bottom) {
        textView.setPadding(SizeUtil.dp2px(getContext(), left), SizeUtil.dp2px(getContext(), top), SizeUtil.dp2px(getContext(), right), SizeUtil.dp2px(getContext(), bottom));
    }

    public void setTextSize(int size) {
        textView.setTextSize(size);
    }

    public void setTextColor(int color) {
        textView.setTextColor(color);
    }

    public void setBackground(int resId) {
        textView.setBackground(AppCompatResources.getDrawable(getContext(), resId));
    }

    @Override
    public void setBackgroundTintList(ColorStateList tintList) {
        textView.setBackgroundTintList(tintList);
    }

    public void setMinEms(int ems) {
        textView.setMinEms(ems);
    }

    public void setMaxEms(int ems) {
        textView.setMaxEms(ems);
        textView.setEllipsize(TextUtils.TruncateAt.END);
    }

    public void setMaxLines(int lines) {
        textView.setMaxLines(lines);
        textView.setEllipsize(TextUtils.TruncateAt.END);
    }

    public void setSingleLine(boolean singleLine) {
        textView.setSingleLine(true);
        textView.setEllipsize(TextUtils.TruncateAt.END);
    }

    @Override
    public void setBackground(Drawable drawable) {
        textView.setBackground(drawable);
    }

    public TextView getTextView() {
        return textView;
    }

    public String getText() {
        return (String) textView.getText();
    }

    public void setText(String text) {
        textView.setText(text);
    }
}
