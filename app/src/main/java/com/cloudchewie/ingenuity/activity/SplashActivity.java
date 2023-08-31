/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:29:32
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.Nullable;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.util.database.AppSharedPreferenceUtil;
import com.cloudchewie.util.system.LanguageUtil;
import com.cloudchewie.util.system.SharedPreferenceUtil;
import com.cloudchewie.util.ui.DarkModeUtil;

public class SplashActivity extends BaseActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        initApp();
        jumpToMainActivity();
    }

    void initApp() {
        if (SharedPreferenceUtil.getThemeId(this, 0) == 0)
            SharedPreferenceUtil.setThemeId(this, R.style.AppTheme_Color1);
        boolean isAutoDaynight = AppSharedPreferenceUtil.isAutoDaynight(this);
        boolean isNight = AppSharedPreferenceUtil.isNight(this);
        if (!isAutoDaynight && isNight) {
            DarkModeUtil.switchToAlwaysDarkMode();
        } else if (!isAutoDaynight && !isNight) {
            DarkModeUtil.switchToAlwaysLightMode();
        } else if (isAutoDaynight) {
            DarkModeUtil.switchToAlwaysSystemMode();
        }
        if (LanguageUtil.getAppLanguage(SplashActivity.this).equals(getString(R.string.language_english))) {
            findViewById(R.id.appsloganr).setVisibility(View.GONE);
            findViewById(R.id.appsloganl).setVisibility(View.GONE);
        }
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
    }

    void jumpToMainActivity() {
        Thread thread = new Thread() {
            @Override
            public void run() {
                try {
                    sleep(1000);
                    @SuppressLint("IntentWithNullActionLaunch") Intent it = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(it);
                    finish();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        thread.start();
    }
}
