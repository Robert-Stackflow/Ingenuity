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
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.auth.LoginActivity;
import com.cloudchewie.ingenuity.activity.global.BaseActivity;
import com.cloudchewie.ingenuity.fragment.global.BaseFragment;
import com.cloudchewie.ingenuity.fragment.nav.HomeFragment;
import com.cloudchewie.ingenuity.fragment.nav.UserFragment;
import com.cloudchewie.ingenuity.util.database.AppDatabase;
import com.cloudchewie.ingenuity.util.system.LocalStorage;
import com.cloudchewie.ingenuity.util.system.SPUtil;
import com.cloudchewie.ui.custom.NoScrollViewPager;
import com.cloudchewie.util.ui.SizeUtil;
import com.yh.bottomnavigation_base.IMenuListener;
import com.yh.bottomnavigationex.BottomNavigationViewEx;

import java.util.ArrayList;
import java.util.List;

import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

public class MainActivity extends BaseActivity implements View.OnClickListener, EasyPermissions.PermissionCallbacks {
    private ViewPagerAdapter adapter;
    private List<Fragment> fragments;
    private ImageButton userButton;
    private NoScrollViewPager viewPager;
    private BottomNavigationViewEx bottomNavigation;

    @Override
    @SuppressLint("SourceLockedOrientationActivity")
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        LocalStorage.init(AppDatabase.getInstance(getApplicationContext()));
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        initView();
        initEvent();
    }

    void initView() {
        viewPager = findViewById(R.id.viewpager);
        View view = findViewById(R.id.activity_main_bottom_navigation);
        if (view instanceof BottomNavigationViewEx) {
            bottomNavigation = (BottomNavigationViewEx) view;
            bottomNavigation.enableLabelVisibility(false);
            bottomNavigation.enableItemHorizontalTranslation(false);
            bottomNavigation.setSmallTextSize(11);
            bottomNavigation.setLargeTextSize(11);
            bottomNavigation.enableAnimation(false);
            bottomNavigation.setIconSize(24);
            bottomNavigation.setBNMenuViewHeight(SizeUtil.dp2px(this, 60));
            {
                fragments = new ArrayList<>();
                fragments.add(new HomeFragment());
                fragments.add(new BaseFragment());
                fragments.add(new UserFragment());
                adapter = new ViewPagerAdapter(getSupportFragmentManager(), fragments);
                viewPager.setAdapter(adapter);
                viewPager.setNoScroll(false);
                bottomNavigation.setupWithViewPager(viewPager);
            }
            for (int i = 0; i < bottomNavigation.getBNItemViewCount(); i++) {
                bottomNavigation.getBNMenuView().getChildAt(i).setOnLongClickListener(v -> true);
            }
        }
    }

    @SuppressLint("NonConstantResourceId")
    private void initEvent() {
        if (bottomNavigation == null)
            return;
        bottomNavigation.setMenuListener(new IMenuListener() {
            private int previousPosition = 0;
            private boolean unselected = false;

            @SuppressLint("NonConstantResourceId")
            @Override
            public boolean onNavigationItemSelected(int i, @NonNull MenuItem item, boolean b) {
                for (MenuItem menuItem : bottomNavigation.getMenuItems()) {
                    switch (menuItem.getItemId()) {
                        case R.id.menu_home:
                            menuItem.setIcon(R.drawable.ic_nav_home);
                            break;
                        case R.id.menu_memos:
                            menuItem.setIcon(R.drawable.ic_nav_discover);
                            break;
                        case R.id.menu_user:
                            menuItem.setIcon(R.drawable.ic_nav_user);
                            break;
                    }
                }
                switch (item.getItemId()) {
                    case R.id.menu_home:
                        item.setIcon(R.drawable.ic_nav_home_fill);
                        break;
                    case R.id.menu_memos:
                        item.setIcon(R.drawable.ic_nav_discover_fill);
                        break;
                    case R.id.menu_user:
                        item.setIcon(R.drawable.ic_nav_user_fill);
                        break;
                }
                if (unselected) {
                    unselected = false;
                    MenuItem previousItem = bottomNavigation.getMenuItems().get(previousPosition);
                    switch (previousItem.getItemId()) {
                        case R.id.menu_home:
                            previousItem.setIcon(R.drawable.ic_nav_home_fill);
                            break;
                        case R.id.menu_user:
                            previousItem.setIcon(R.drawable.ic_nav_user_fill);
                            break;
                    }
                    return false;
                } else {
                    previousPosition = i;
                    return true;
                }
            }
        });
        bottomNavigation.setMenuDoubleClickListener((i, item) -> {
            switch (item.getItemId()) {
                case R.id.menu_home:
                case R.id.menu_user:
                    if (fragments != null && fragments.size() > 0)
                        ((BaseFragment) fragments.get(i)).performRefresh();
                    break;
                case R.id.menu_memos:
                    if (!SPUtil.isLogin(MainActivity.this)) {
                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        if (fragments != null && fragments.size() > 0)
                            ((BaseFragment) fragments.get(i)).performRefresh();
                    }
                    break;
            }
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