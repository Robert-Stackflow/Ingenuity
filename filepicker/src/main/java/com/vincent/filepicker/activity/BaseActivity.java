package com.vincent.filepicker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.cloudchewie.util.system.SharedPreferenceUtil;
import com.cloudchewie.util.ui.DarkModeUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.vincent.filepicker.R;

import java.util.List;

import pub.devrel.easypermissions.AfterPermissionGranted;
import pub.devrel.easypermissions.AppSettingsDialog;
import pub.devrel.easypermissions.EasyPermissions;

/**
 * Created by Vincent Woo
 * Date: 2016/10/12
 * Time: 16:21
 */

public abstract class BaseActivity extends AppCompatActivity implements EasyPermissions.PermissionCallbacks {
    public static final String IS_NEED_FOLDER_LIST = "isNeedFolderList";
    private static final int RC_READ_EXTERNAL_STORAGE = 123;
    private static final String TAG = BaseActivity.class.getName();
    protected boolean isNeedFolderList;

    abstract void permissionGranted();

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(SharedPreferenceUtil.getThemeId(this, R.style.AppTheme));
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarTransparent(this);
        StatusBarUtil.setStatusBarColor(this, getColor(R.color.picker_bar_background));
        StatusBarUtil.setStatusBarTextColor(this, DarkModeUtil.isDarkMode(this));
        isNeedFolderList = getIntent().getBooleanExtra(IS_NEED_FOLDER_LIST, true);
    }

    @Override
    protected void onPostCreate(@Nullable Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        readExternalStorage();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        EasyPermissions.onRequestPermissionsResult(requestCode, permissions, grantResults, this);
    }

    /**
     * Read external storage file
     */
    @AfterPermissionGranted(RC_READ_EXTERNAL_STORAGE)
    private void readExternalStorage() {
        boolean isGranted = EasyPermissions.hasPermissions(this, "android.permission.READ_EXTERNAL_STORAGE");
        if (isGranted) {
            permissionGranted();
        } else {
            EasyPermissions.requestPermissions(this, getString(R.string.vw_rationale_storage), RC_READ_EXTERNAL_STORAGE, "android.permission.READ_EXTERNAL_STORAGE");
        }
    }

    @Override
    public void onPermissionsGranted(int requestCode, List<String> perms) {
        permissionGranted();
    }

    @Override
    public void onPermissionsDenied(int requestCode, List<String> perms) {
        if (EasyPermissions.somePermissionPermanentlyDenied(this, perms)) {
            new AppSettingsDialog.Builder(this).build().show();
        } else {
            finish();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == AppSettingsDialog.DEFAULT_SETTINGS_REQ_CODE) {
            if (EasyPermissions.hasPermissions(this, "android.permission.READ_EXTERNAL_STORAGE")) {
                permissionGranted();
            } else {
                finish();
            }
        }
    }

    public void onBackClick(View view) {
        finish();
    }
}
