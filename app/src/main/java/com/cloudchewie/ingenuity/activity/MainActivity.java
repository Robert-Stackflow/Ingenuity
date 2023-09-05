/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 13:13:37
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.activity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.fragment.AuthenticatorFragment;
import com.cloudchewie.ingenuity.fragment.HomeFragment;
import com.cloudchewie.ingenuity.fragment.ToolFragment;
import com.cloudchewie.ingenuity.fragment.UserFragment;
import com.cloudchewie.ingenuity.util.database.AppDatabase;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.cloudchewie.ingenuity.util.enumeration.EventBusCode;
import com.cloudchewie.ui.bottombar.ReadableBottomBar;
import com.cloudchewie.ui.general.NoScrollViewPager;
import com.cloudchewie.util.system.SharedPreferenceCode;
import com.cloudchewie.util.system.SharedPreferenceUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private ViewPagerAdapter adapter;
    private List<Fragment> fragments;
    private NoScrollViewPager viewPager;
    private ReadableBottomBar readableBottomBar;
    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalStorage.init(AppDatabase.getInstance(getApplicationContext()));
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        LiveEventBus.get(EventBusCode.CHANGE_THEME.getKey(), String.class).observe(this, s -> recreate());
        LiveEventBus.get(EventBusCode.CHANGE_TOKEN_DISABLE_SCREENSHOT.getKey(), String.class).observe(this, s -> loadEnableScreenShot(SharedPreferenceUtil.getCurrentNavIndex(MainActivity.this)));
    }

    void initView() {
        viewPager = findViewById(R.id.viewpager);
        readableBottomBar = findViewById(R.id.activity_main_readable_bottom_bar);
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new AuthenticatorFragment());
        fragments.add(new ToolFragment());
        fragments.add(new UserFragment());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setNoScroll(false);
        readableBottomBar.setTabInitialSelectedIndex(SharedPreferenceUtil.getCurrentNavIndex(MainActivity.this));
        viewPager.setCurrentItem(SharedPreferenceUtil.getCurrentNavIndex(MainActivity.this));
        readableBottomBar.setOnItemSelectListener(i -> {
            SharedPreferenceUtil.setCurrentNavIndex(MainActivity.this, i);
            viewPager.setCurrentItem(i);
            loadEnableScreenShot(i);
        });
        loadEnableScreenShot(SharedPreferenceUtil.getCurrentNavIndex(MainActivity.this));
    }

    /**
     * @param index 当前所在页面
     */
    public void loadEnableScreenShot(int index) {
        if (SharedPreferenceUtil.getBoolean(this, SharedPreferenceCode.TOKEN_DISBALE_SCREENSHOT.getKey(), true)) {
            if (readableBottomBar.getBottomBarItemList().get(index).getText().equals(getString(R.string.nav_auth))) {
                getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
            } else {
                getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
            }
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    @Override
    public void onClick(View view) {
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    @Override
    public void onPermissionsGranted(int requestCode, @NonNull List<String> perms) {

    }

    @Override
    public void onPermissionsDenied(int requestCode, @NonNull List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private static class ViewPagerAdapter extends FragmentPagerAdapter {
        private final List<Fragment> data;

        public ViewPagerAdapter(FragmentManager fragmentManager, List<Fragment> data) {
            super(fragmentManager);
            this.data = data;
        }

        @Override
        public int getCount() {
            return data.size();
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return data.get(position);
        }
    }
}