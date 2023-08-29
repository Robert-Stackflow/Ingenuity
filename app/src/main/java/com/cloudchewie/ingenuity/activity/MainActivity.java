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

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.fragment.BaseFragment;
import com.cloudchewie.ingenuity.fragment.HomeFragment;
import com.cloudchewie.ingenuity.fragment.UserFragment;
import com.cloudchewie.ingenuity.util.database.AppDatabase;
import com.cloudchewie.ingenuity.util.enumeration.EventBusCode;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.cloudchewie.ui.bottombar.ReadableBottomBar;
import com.cloudchewie.ui.general.NoScrollViewPager;
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
        LiveEventBus.get(EventBusCode.CHANGE_THEME.getKey(), String.class).observe(this, s -> recreate());
        initView();
    }

    void initView() {
        viewPager = findViewById(R.id.viewpager);
        readableBottomBar = findViewById(R.id.activity_main_readable_bottom_bar);
        fragments = new ArrayList<>();
        fragments.add(new HomeFragment());
        fragments.add(new BaseFragment());
        fragments.add(new UserFragment());
        adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager.setAdapter(adapter);
        viewPager.setNoScroll(false);
        readableBottomBar.setTabInitialSelectedIndex(SharedPreferenceUtil.getCurrentNavIndex(MainActivity.this));
        viewPager.setCurrentItem(SharedPreferenceUtil.getCurrentNavIndex(MainActivity.this));
        readableBottomBar.setOnItemSelectListener(i -> {
            SharedPreferenceUtil.setCurrentNavIndex(MainActivity.this, i);
            viewPager.setCurrentItem(i);
        });
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