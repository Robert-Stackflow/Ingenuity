/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:17:11
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.activity.user;

import android.graphics.Typeface;
import android.os.Bundle;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.item.InputLayout;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class ChangePasswordActivity extends BaseActivity {
    RefreshLayout swipeRefreshLayout;

    InputLayout oldPasswordInput;
    InputLayout newPasswordInput;
    InputLayout confirmPasswordInput;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_change_password);
        oldPasswordInput = findViewById(R.id.activity_change_password_oldpassword);
        newPasswordInput = findViewById(R.id.activity_change_password_newpassword);
        confirmPasswordInput = findViewById(R.id.activity_change_password_confirmpassword);
        ((TitleBar) findViewById(R.id.activity_change_password_titlebar)).setLeftButtonClickListener(v -> finishAfterTransition());
        oldPasswordInput.getEditText().setTypeface(Typeface.SANS_SERIF);
        newPasswordInput.getEditText().setTypeface(Typeface.SANS_SERIF);
        confirmPasswordInput.getEditText().setTypeface(Typeface.SANS_SERIF);
        initSwipeRefresh();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_change_password_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }
}