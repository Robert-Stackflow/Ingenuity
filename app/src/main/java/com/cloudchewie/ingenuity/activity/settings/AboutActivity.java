/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:29:32
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.activity.settings;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.global.BaseActivity;
import com.cloudchewie.ingenuity.activity.global.WebViewActivity;
import com.cloudchewie.ui.custom.EntryItem;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.general.IToast;
import com.cloudchewie.util.basic.DateUtil;
import com.cloudchewie.util.system.AppInfoUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class AboutActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    EntryItem versionEntry;
    TextView copyRightView;
    EntryItem logEntry;
    EntryItem privacyEntry;
    EntryItem termEntry;
    EntryItem opensourceEntry;
    EntryItem contactEntry;
    EntryItem storyEntry;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_about);
        ((TitleBar) findViewById(R.id.activity_about_titlebar)).setLeftButtonClickListener(v -> finishAfterTransition());
        versionEntry = findViewById(R.id.entry_version);
        logEntry = findViewById(R.id.entry_log);
        privacyEntry = findViewById(R.id.entry_privacy_policy);
        termEntry = findViewById(R.id.entry_service_terms);
        opensourceEntry = findViewById(R.id.entry_opensource);
        contactEntry = findViewById(R.id.entry_contact);
        storyEntry = findViewById(R.id.entry_story);
        copyRightView = findViewById(R.id.activity_about_copyright);
        versionEntry.setTipText(AppInfoUtil.getAppVersionName());
        copyRightView.setText("Copyright © 2023-" + DateUtil.getYear() + " Cloudchewie");
        versionEntry.setOnClickListener(this);
        logEntry.setOnClickListener(this);
        privacyEntry.setOnClickListener(this);
        termEntry.setOnClickListener(this);
        opensourceEntry.setOnClickListener(this);
        contactEntry.setOnClickListener(this);
        storyEntry.setOnClickListener(this);
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
        if (view == versionEntry) {
            IToast.makeTextBottom(this, "已经是最新版本", Toast.LENGTH_SHORT).show();
        } else if (view == logEntry) {
            Intent intent = new Intent(this, LogActivity.class);
            startActivity(intent);
        } else if (view == privacyEntry) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("url", "https://blog.cloudchewie.com/term");
            startActivity(intent);
        } else if (view == termEntry) {
            Intent intent = new Intent(this, WebViewActivity.class);
            intent.putExtra("url", "https://blog.cloudchewie.com/term");
            startActivity(intent);
        } else if (view == opensourceEntry) {
            IToast.showBottom(this, "还木有开源啦");
        } else if (view == contactEntry) {
            IToast.showBottom(this, "你休想！");
        } else if (view == storyEntry) {
            IToast.showBottom(this, "故事的种子尚未发芽");
        }
    }
}
