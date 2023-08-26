/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:25:09
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.activity.global;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.net.ConnectivityManager;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudchewie.ingenuity.broadcast.NetWorkStateReceiver;
import com.cloudchewie.util.ui.DarkModeUtil;
import com.cloudchewie.util.ui.FontUtil;
import com.cloudchewie.util.ui.StatusBarUtil;

import java.util.Objects;

public class BaseActivity extends AppCompatActivity {
    static float fontScale = 1f;
    BroadcastReceiver broadcastReceiver;
    NetWorkStateReceiver netWorkStateReceiver;
    private Configuration mConfiguration;

    @Override
    public Resources getResources() {
        Resources resources = super.getResources();
        return FontUtil.getResources(this, resources, fontScale);
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(FontUtil.attachBaseContext(newBase, fontScale));
    }

    public void setFontScale(float fontScale) {
        BaseActivity.fontScale = fontScale;
        FontUtil.recreate(this);
    }

    @Override
    public void onConfigurationChanged(@NonNull Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        if ((mConfiguration.diff(newConfig) & ActivityInfo.CONFIG_UI_MODE) != 0) {
            if (DarkModeUtil.isDarkMode(this)) DarkModeUtil.switchToAlwaysLightMode();
            else DarkModeUtil.switchToAlwaysDarkMode();
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mConfiguration = new Configuration(getResources().getConfiguration());
        StatusBarUtil.setStatusBarTransparent(this);
        StatusBarUtil.setStatusBarTextColor(this, DarkModeUtil.isDarkMode(getApplicationContext()));
        new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, @NonNull Intent intent) {
                if (Objects.equals(intent.getStringExtra("msg"), "EVENT_REFRESH_LANGUAGE")) {
                    recreate();
                }
            }
        };
    }

    @Override
    protected void onResume() {
        if (netWorkStateReceiver == null) netWorkStateReceiver = new NetWorkStateReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkStateReceiver, filter);
        super.onResume();
    }

    @Override
    protected void onPause() {
        unregisterReceiver(netWorkStateReceiver);
        super.onPause();
    }
}
