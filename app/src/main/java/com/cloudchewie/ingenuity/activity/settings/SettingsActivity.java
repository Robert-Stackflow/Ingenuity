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
import android.view.View;
import android.widget.Toast;

import com.blankj.utilcode.util.ActivityUtils;
import com.blankj.utilcode.util.SPUtils;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.MainActivity;
import com.cloudchewie.ingenuity.activity.global.BaseActivity;
import com.cloudchewie.ingenuity.bean.ListBottomSheetBean;
import com.cloudchewie.ingenuity.util.system.SPUtil;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;
import com.cloudchewie.ui.custom.EntryItem;
import com.cloudchewie.ui.custom.MyDialog;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.general.IToast;
import com.cloudchewie.util.system.CacheUtil;
import com.cloudchewie.util.system.LanguageUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

public class SettingsActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    EntryItem clearCacheEntry;
    EntryItem languageEntry;


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
        if (!Objects.equals(SPUtils.getInstance().getString(SP_LANGUAGE, ""), ""))
            languageEntry.setTipText(LanguageUtil.getAppLanguage(SettingsActivity.this));
        else languageEntry.setTipText("跟随系统");
        languageEntry.setOnClickListener(this);
        initSwipeRefresh();
        initView();
    }

    void initView() {
        if (!SPUtil.isLogin(this)) {
            findViewById(R.id.entry_logout).setVisibility(View.GONE);
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
        if (view == languageEntry) {
            List<String> strings = Arrays.asList(getResources().getStringArray(R.array.edit_language));
            ListBottomSheet bottomSheet = new ListBottomSheet(SettingsActivity.this, ListBottomSheetBean.strToBean(strings));
            bottomSheet.setOnItemClickedListener(position -> {
                IToast.makeTextBottom(SettingsActivity.this, "切换语言为" + strings.get(position), Toast.LENGTH_SHORT).show();
                if (position == 0) LanguageUtil.attachBaseContext(SettingsActivity.this);
                else if (position == 1)
                    LanguageUtil.changeLanguage(SettingsActivity.this, "zh", "CN");
                else if (position == 2)
                    LanguageUtil.changeLanguage(SettingsActivity.this, "zh", "TW");
                else if (position == 3)
                    LanguageUtil.changeLanguage(SettingsActivity.this, "en", "US");
                languageEntry.setTipText(strings.get(position));
                Intent intent = new Intent(Intent.ACTION_LOCALE_CHANGED);
                intent.putExtra("msg", "EVENT_REFRESH_LANGUAGE");
                sendBroadcast(intent);
                bottomSheet.dismiss();
            });
            bottomSheet.show();
        } else if (view == clearCacheEntry) {
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
            } else {
                IToast.makeTextBottom(this, "不需要再清除缓存啦", Toast.LENGTH_SHORT).show();
            }
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
        }
    }
}
