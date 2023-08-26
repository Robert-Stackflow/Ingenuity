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
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;

import com.blankj.utilcode.util.ActivityUtils;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.MainActivity;
import com.cloudchewie.ingenuity.activity.global.BaseActivity;
import com.cloudchewie.ingenuity.util.system.SPUtil;
import com.cloudchewie.ui.custom.EntryItem;
import com.cloudchewie.ui.custom.MyDialog;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.general.IToast;
import com.cloudchewie.util.system.AppInfoUtil;
import com.cloudchewie.util.system.CacheUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    EntryItem checkUpdateEntry;
    EntryItem clearCacheEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_settings);
        ((TitleBar) findViewById(R.id.settings_titlebar)).setLeftButtonClickListener(v -> finish());
        findViewById(R.id.entry_account).setOnClickListener(this);
        findViewById(R.id.entry_notification).setOnClickListener(this);
        findViewById(R.id.entry_privacy).setOnClickListener(this);
        findViewById(R.id.entry_general).setOnClickListener(this);
        findViewById(R.id.entry_about).setOnClickListener(this);
        findViewById(R.id.entry_logout).setOnClickListener(this);
        checkUpdateEntry = findViewById(R.id.entry_check_update);
        clearCacheEntry = findViewById(R.id.entry_clear_cache);
        clearCacheEntry.setTipText(CacheUtil.getTotalCacheSize(this));
        clearCacheEntry.setOnClickListener(v -> {
            if (!CacheUtil.getTotalCacheSize(this).equals("0.00MB")) {
                MyDialog dialog = new MyDialog(this);
                dialog.setTitle("清除缓存");
                dialog.setMessage("是否清除缓存（缓存包括下载的图片、数据等，清除后需要重新下载）");
                dialog.setPositive("确定");
                dialog.setNegtive("取消");
                dialog.setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        CacheUtil.clearAllCache(SettingsActivity.this);
                        clearCacheEntry.setTipText(CacheUtil.getTotalCacheSize(SettingsActivity.this));
                        IToast.makeTextBottom(SettingsActivity.this, "缓存清除成功", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNegtiveClick() {
                    }

                    @Override
                    public void onCloseClick() {
                    }
                });
                dialog.show();
            }
        });
        checkUpdateEntry.setTipText(AppInfoUtil.getAppVersionName());
        checkUpdateEntry.setOnClickListener(v -> IToast.makeTextBottom(this, "已经是最新版本", Toast.LENGTH_SHORT).show());
        initSwipeRefresh();
        initView();
    }

    void initView() {
        if (!SPUtil.isLogin(this)) {
            findViewById(R.id.entry_logout).setVisibility(View.GONE);
            findViewById(R.id.entry_account).setVisibility(View.GONE);
            ((EntryItem) findViewById(R.id.entry_notification)).setRadiusEnbale(true, false);
        }
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.settings_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, findViewById(R.id.settings_titlebar), "shareElement").toBundle();
        if (view == findViewById(R.id.entry_notification)) {
            Intent intent = new Intent(getApplicationContext(), NotificationSettingsActivity.class);
            startActivity(intent, bundle);
        } else if (view == findViewById(R.id.entry_account)) {
            Intent intent = new Intent(getApplicationContext(), AccountSettingsActivity.class);
            startActivity(intent, bundle);
        } else if (view == findViewById(R.id.entry_general)) {
            Intent intent = new Intent(getApplicationContext(), GeneralSettingsActivity.class);
            startActivity(intent, bundle);
        } else if (view == findViewById(R.id.entry_privacy)) {
            Intent intent = new Intent(getApplicationContext(), PrivacySettingsActivity.class);
            startActivity(intent, bundle);
        } else if (view == findViewById(R.id.entry_logout)) {
            MyDialog dialog = new MyDialog(SettingsActivity.this);
            dialog.setTitle("退出登录");
            dialog.setMessage("是否退出登录？");
            dialog.setNegtive("取消");
            dialog.setPositive("确定");
            dialog.setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    SPUtil.logout(SettingsActivity.this);
                    ActivityUtils.finishAllActivities();
                    ActivityUtils.startActivity(new Intent(SettingsActivity.this, MainActivity.class));
                    dialog.dismiss();
                }

                @Override
                public void onNegtiveClick() {
                    dialog.dismiss();
                }

                @Override
                public void onCloseClick() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        } else if (view == findViewById(R.id.entry_about)) {
            Intent intent = new Intent(getApplicationContext(), AboutActivity.class);
            startActivity(intent, bundle);
        }
    }
}