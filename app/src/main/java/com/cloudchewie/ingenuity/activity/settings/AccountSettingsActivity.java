/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:19:00
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.activity.settings;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.activity.user.ChangePasswordActivity;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class AccountSettingsActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_account_settings);
        findViewById(R.id.entry_change_password).setOnClickListener(this);
        ((TitleBar) findViewById(R.id.activity_account_settings_titlebar)).setLeftButtonClickListener(v -> finishAfterTransition());
        initSwipeRefresh();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_account_settings_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {
        if (view == findViewById(R.id.entry_change_password)) {
            Intent intent = new Intent(getApplicationContext(), ChangePasswordActivity.class).setAction(Intent.ACTION_DEFAULT);
            startActivity(intent);
        }
    }
}