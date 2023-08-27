/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:29:32
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.activity.settings;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.global.BaseActivity;
import com.cloudchewie.ui.custom.EntryItem;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.general.IToast;
import com.cloudchewie.util.basic.DateUtil;
import com.cloudchewie.util.system.AppInfoUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    EntryItem versionItem;
    TextView copyRightView;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_about);
        ((TitleBar) findViewById(R.id.activity_about_titlebar)).setLeftButtonClickListener(v -> finishAfterTransition());
        versionItem = findViewById(R.id.entry_version);
        copyRightView = findViewById(R.id.activity_about_copyright);
        versionItem.setTipText(AppInfoUtil.getAppVersionName());
        versionItem.setOnClickListener(v -> IToast.makeTextBottom(this, "已经是最新版本", Toast.LENGTH_SHORT).show());
        copyRightView.setText("Copyright © 2023-" + DateUtil.getYear() + " Cloudchewie");
        initSwipeRefresh();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_about_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {

    }
}
