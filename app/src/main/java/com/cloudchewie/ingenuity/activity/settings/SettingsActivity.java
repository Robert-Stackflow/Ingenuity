/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:19:00
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.activity.settings;

import static com.cloudchewie.util.system.LanguageUtil.SP_LANGUAGE;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.activity.MainActivity;
import com.cloudchewie.ingenuity.bean.ListBottomSheetBean;
import com.cloudchewie.ingenuity.util.database.AppSharedPreferenceUtil;
import com.cloudchewie.ingenuity.util.enumeration.EventBusCode;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;
import com.cloudchewie.ui.custom.IDialog;
import com.cloudchewie.ui.custom.IToast;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.item.CheckBoxItem;
import com.cloudchewie.ui.item.EntryItem;
import com.cloudchewie.util.system.CacheUtil;
import com.cloudchewie.util.system.LanguageUtil;
import com.cloudchewie.util.system.SharedPreferenceCode;
import com.cloudchewie.util.system.SharedPreferenceUtil;
import com.cloudchewie.util.ui.DarkModeUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    EntryItem clearCacheEntry;
    EntryItem languageEntry;
    CheckBoxItem autoDaynightEntry;
    CheckBoxItem switchDaynightEntry;
    CheckBoxItem enableWebCacheEntry;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_settings);
        ((TitleBar) findViewById(R.id.settings_titlebar)).setLeftButtonClickListener(v -> finish());
        findViewById(R.id.entry_logout).setOnClickListener(this);
        clearCacheEntry = findViewById(R.id.entry_clear_cache);
        clearCacheEntry.setTipText(CacheUtil.getTotalCacheSize(this));
        clearCacheEntry.setOnClickListener(this);
        languageEntry = findViewById(R.id.entry_language);
        languageEntry.setOnClickListener(this);
        autoDaynightEntry = findViewById(R.id.switch_auto_daynight);
        switchDaynightEntry = findViewById(R.id.switch_daynight);
        enableWebCacheEntry = findViewById(R.id.entry_enable_web_cache);
        initSwipeRefresh();
        initView();
        initEvent();
    }

    void initView() {
        if (!AppSharedPreferenceUtil.isLogin(this)) {
            findViewById(R.id.entry_logout).setVisibility(View.GONE);
        }
        if (!Objects.equals(SPUtils.getInstance().getString(SP_LANGUAGE, ""), ""))
            languageEntry.setTipText(LanguageUtil.getAppLanguage(SettingsActivity.this));
        else languageEntry.setTipText(getString(R.string.language_default));
        //加载是否自动跟随或深色模式
        if (AppSharedPreferenceUtil.isAutoDaynight(this)) {
            AppSharedPreferenceUtil.setNight(SettingsActivity.this, DarkModeUtil.isDarkMode(SettingsActivity.this));
        }
        autoDaynightEntry.setRadiusEnbale(true, AppSharedPreferenceUtil.isAutoDaynight(this));
        switchDaynightEntry.setVisibility(AppSharedPreferenceUtil.isAutoDaynight(SettingsActivity.this) ? View.GONE : View.VISIBLE);
        new Handler().postDelayed(() -> {
            switchDaynightEntry.setChecked(AppSharedPreferenceUtil.isNight(SettingsActivity.this));
            autoDaynightEntry.setChecked(AppSharedPreferenceUtil.isAutoDaynight(this));
        }, 100);
        enableWebCacheEntry.setChecked(SharedPreferenceUtil.getBoolean(this, SharedPreferenceCode.ENABLE_WEB_CACHE.getKey(), true));
    }

    void initEvent() {
        autoDaynightEntry.setOnCheckedChangedListener((buttonView, isChecked) -> {
            AppSharedPreferenceUtil.setAutoDaynight(SettingsActivity.this, isChecked);
            LiveEventBus.get(EventBusCode.CHANGE_AUTO_DAYNIGHT.getKey()).post("change");
            if (isChecked) {
                DarkModeUtil.switchToAlwaysSystemMode();
                autoDaynightEntry.setRadiusEnbale(true, true);
            } else {
                autoDaynightEntry.setRadiusEnbale(true, false);
                if (AppSharedPreferenceUtil.isNight(this)) {
                    DarkModeUtil.switchToAlwaysDarkMode();
                } else {
                    DarkModeUtil.switchToAlwaysLightMode();
                }
            }
            switchDaynightEntry.setVisibility(AppSharedPreferenceUtil.isAutoDaynight(SettingsActivity.this) ? View.GONE : View.VISIBLE);
        });
        switchDaynightEntry.setOnCheckedChangedListener((buttonView, isChecked) -> {
            AppSharedPreferenceUtil.setNight(SettingsActivity.this, isChecked);
            if (isChecked) {
                DarkModeUtil.switchToAlwaysDarkMode();
            } else {
                DarkModeUtil.switchToAlwaysLightMode();
            }
        });
        enableWebCacheEntry.setOnCheckedChangedListener((buttonView, isChecked) -> SharedPreferenceUtil.putBoolean(SettingsActivity.this, SharedPreferenceCode.ENABLE_WEB_CACHE.getKey(), isChecked));
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
        if (view == languageEntry) {
            List<String> strings = Arrays.asList(getResources().getStringArray(R.array.edit_language));
            ListBottomSheet bottomSheet = new ListBottomSheet(SettingsActivity.this, ListBottomSheetBean.strToBean(strings));
            bottomSheet.setOnItemClickedListener(position -> {
//                IToast.showBottom(SettingsActivity.this, getString(R.string.reboot_to_apply));
                if (position == 0)
                    LanguageUtil.changeLanguage(SettingsActivity.this, "zh", "CN");
                else if (position == 1)
                    LanguageUtil.changeLanguage(SettingsActivity.this, "zh", "TW");
                else if (position == 2)
                    LanguageUtil.changeLanguage(SettingsActivity.this, "en", "US");
                else if (position == 3)
                    LanguageUtil.changeLanguage(SettingsActivity.this, "ja", "JP");
                if (languageEntry.getTip().equals(strings.get(position))) {
                    bottomSheet.dismiss();
                } else {
                    languageEntry.setTipText(strings.get(position));
                    ActivityUtils.finishAllActivities();
                    ActivityUtils.startActivity(new Intent(this, MainActivity.class).setAction(Intent.ACTION_DEFAULT));
                }

            });
            bottomSheet.show();
        } else if (view == clearCacheEntry) {
            if (!CacheUtil.getTotalCacheSize(this).equals(getString(R.string.zero_cache))) {
                IDialog dialog = new IDialog(this);
                dialog.setTitle(getString(R.string.dialog_title_clear_cache));
                dialog.setMessage(getString(R.string.dialog_content_clear_cache));
                dialog.setOnClickBottomListener(new IDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        CacheUtil.clearAllCache(SettingsActivity.this);
                        clearCacheEntry.setTipText(CacheUtil.getTotalCacheSize(SettingsActivity.this));
                        IToast.makeTextBottom(SettingsActivity.this, getString(R.string.clear_cache_success), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void onNegtiveClick() {
                    }

                    @Override
                    public void onCloseClick() {
                    }
                });
                dialog.show();
            } else {
                IToast.makeTextBottom(this, getString(R.string.no_need_to_clear_cache), Toast.LENGTH_SHORT).show();
            }
        } else if (view == findViewById(R.id.entry_logout)) {
            IDialog dialog = new IDialog(SettingsActivity.this);
            dialog.setTitle(getString(R.string.dialog_title_logout));
            dialog.setMessage(getString(R.string.dialog_content_logout));
            dialog.setOnClickBottomListener(new IDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    AppSharedPreferenceUtil.logout(SettingsActivity.this);
                    ActivityUtils.finishAllActivities();
                    ActivityUtils.startActivity(new Intent(SettingsActivity.this, MainActivity.class).setAction(Intent.ACTION_DEFAULT));
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
        }
    }
}
