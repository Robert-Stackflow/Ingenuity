package com.cloudchewie.ingenuity.activity.authenticator;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.util.authenticator.ImportExportUtil;
import com.cloudchewie.ingenuity.util.enumeration.EventBusCode;
import com.cloudchewie.ui.custom.IDialog;
import com.cloudchewie.ui.custom.IToast;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.item.CheckBoxItem;
import com.cloudchewie.ui.item.EntryItem;
import com.cloudchewie.util.basic.DateFormatUtil;
import com.cloudchewie.util.system.SharedPreferenceCode;
import com.cloudchewie.util.system.SharedPreferenceUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.Date;

public class AuthenticatorSettingsActivity extends BaseActivity implements View.OnClickListener {
    private static final int READ_JSON_REQUEST_CODE = 42;
    private static final int WRITE_JSON_REQUEST_CODE = 43;
    private static final int READ_KEY_URI_REQUEST_CODE = 44;
    private static final int WRITE_KEY_URI_REQUEST_CODE = 45;
    private static final String SCREENSHOT_MODE_EXTRA = "screenshot_mode";
    private static final String SHARE_FROM_PACKAGE_NAME_INTENT_EXTRA = "shareFromPackageName";
    RefreshLayout swipeRefreshLayout;
    CheckBoxItem longPressItem;
    CheckBoxItem clickItem;
    CheckBoxItem authItem;
    CheckBoxItem screenShotItem;
    EntryItem exportJsonItem;
    EntryItem importJsonItem;
    EntryItem exportUriItem;
    EntryItem importUriItem;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_authenticator_settings);
        ((TitleBar) findViewById(R.id.authenticator_settings_titlebar)).setLeftButtonClickListener(v -> finishAfterTransition());
        longPressItem = findViewById(R.id.activity_authenticator_settings_long_press_copy);
        clickItem = findViewById(R.id.activity_authenticator_settings_click_copy);
        authItem = findViewById(R.id.activity_authenticator_settings_need_auth);
        screenShotItem = findViewById(R.id.activity_authenticator_settings_disable_screenshot);
        exportJsonItem = findViewById(R.id.activity_authenticator_settings_export_json);
        exportUriItem = findViewById(R.id.activity_authenticator_settings_export_uri);
        importJsonItem = findViewById(R.id.activity_authenticator_settings_import_json);
        importUriItem = findViewById(R.id.activity_authenticator_settings_import_uri);
        exportUriItem.setOnClickListener(this);
        exportJsonItem.setOnClickListener(this);
        importUriItem.setOnClickListener(this);
        importJsonItem.setOnClickListener(this);
        loadSettings();
        bindEvent();
        initSwipeRefresh();
    }

    void loadSettings() {
        longPressItem.setChecked(SharedPreferenceUtil.getBoolean(AuthenticatorSettingsActivity.this, SharedPreferenceCode.LONG_PRESS_COPY_CODE.getKey(), true));
        clickItem.setChecked(SharedPreferenceUtil.getBoolean(AuthenticatorSettingsActivity.this, SharedPreferenceCode.CLICK_COPY_CODE.getKey(), false));
        authItem.setChecked(SharedPreferenceUtil.getBoolean(AuthenticatorSettingsActivity.this, SharedPreferenceCode.AUTH_TO_SHOW_CODE.getKey(), true));
        screenShotItem.setChecked(SharedPreferenceUtil.getBoolean(AuthenticatorSettingsActivity.this, SharedPreferenceCode.DISBALE_SCREEN_SHOT.getKey(), true));
    }

    void bindEvent() {
        longPressItem.setOnCheckedChangedListener((buttonView, isChecked) -> SharedPreferenceUtil.putBoolean(AuthenticatorSettingsActivity.this, SharedPreferenceCode.LONG_PRESS_COPY_CODE.getKey(), isChecked));
        clickItem.setOnCheckedChangedListener((buttonView, isChecked) -> SharedPreferenceUtil.putBoolean(AuthenticatorSettingsActivity.this, SharedPreferenceCode.CLICK_COPY_CODE.getKey(), isChecked));
        authItem.setOnCheckedChangedListener((buttonView, isChecked) -> {
            SharedPreferenceUtil.putBoolean(AuthenticatorSettingsActivity.this, SharedPreferenceCode.AUTH_TO_SHOW_CODE.getKey(), isChecked);
            LiveEventBus.get(EventBusCode.CHANGE_AUTH_SHOW_CODE.getKey()).post("");
        });
        screenShotItem.setOnCheckedChangedListener((buttonView, isChecked) -> {
            SharedPreferenceUtil.putBoolean(AuthenticatorSettingsActivity.this, SharedPreferenceCode.DISBALE_SCREEN_SHOT.getKey(), isChecked);
            LiveEventBus.get(EventBusCode.CHANGE_SCREEN_SHOT.getKey()).post("");
        });
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.authenticator_settings_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {
        if (view == exportJsonItem) {
            createFile("application/json", "OTPBackup", "json", WRITE_JSON_REQUEST_CODE, true);
        } else if (view == exportUriItem) {
            createFile("text/plain", "OTPBackup", "txt", WRITE_KEY_URI_REQUEST_CODE, true);
        } else if (view == importJsonItem) {
            performFileSearch(READ_JSON_REQUEST_CODE);
        } else if (view == importUriItem) {
            performFileSearch(READ_KEY_URI_REQUEST_CODE);
        }
    }

    private void performFileSearch(int requestCode) {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        try {
            startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            IToast.showBottom(this, getString(R.string.permission_fail_explorer));
        }
    }


    private void createFile(String mimeType, String fileName, String fileExtension, int requestCode, boolean appendTimestamp) {
        Intent intent = new Intent(Intent.ACTION_CREATE_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        String fullName = fileName;
        if (appendTimestamp) {
            fullName += "_";
            fullName += DateFormatUtil.getSimpleDateFormat(DateFormatUtil.FULL_FORMAT).format(new Date());
        }
        fullName += ".";
        fullName += fileExtension;

        intent.setType(mimeType);
        intent.putExtra(Intent.EXTRA_TITLE, fullName);
        try {
            startActivityForResult(intent, requestCode);
        } catch (ActivityNotFoundException e) {
            IToast.showBottom(this, getString(R.string.permission_fail_explorer));
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode != Activity.RESULT_OK) {
            return;
        }
        Uri uri = resultData.getData();
        IDialog dialog = new IDialog(this);
        if (uri == null) return;
        switch (requestCode) {
            case WRITE_JSON_REQUEST_CODE:
                ImportExportUtil.exportJsonFile(AuthenticatorSettingsActivity.this, uri);
                IToast.showBottom(this, getString(R.string.export_success));
                break;
            case READ_JSON_REQUEST_CODE:
                dialog.setTitle(getString(R.string.dialog_title_import_json_token));
                dialog.setMessage(String.format(getString(R.string.dialog_content_import_json_token), uri.getPath()));
                dialog.setOnClickBottomListener(new IDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        try {
                            ImportExportUtil.importJsonFile(AuthenticatorSettingsActivity.this, uri);
                            IToast.showBottom(AuthenticatorSettingsActivity.this, getString(R.string.import_success));
                        } catch (Exception e) {
                            IToast.showBottom(AuthenticatorSettingsActivity.this, getString(R.string.import_fail));
                        }
                    }

                    @Override
                    public void onNegtiveClick() {

                    }

                    @Override
                    public void onCloseClick() {

                    }
                });
                dialog.show();
                break;
            case WRITE_KEY_URI_REQUEST_CODE:
                ImportExportUtil.exportKeyUriFile(AuthenticatorSettingsActivity.this, uri);
                IToast.showBottom(this, getString(R.string.export_success));
                break;
            case READ_KEY_URI_REQUEST_CODE:
                dialog.setTitle(getString(R.string.dialog_title_import_uri_token));
                dialog.setMessage(String.format(getString(R.string.dialog_content_import_uri_token), uri.getPath()));
                dialog.setOnClickBottomListener(new IDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        try {
                            ImportExportUtil.importKeyUriFile(AuthenticatorSettingsActivity.this, uri);
                            IToast.showBottom(AuthenticatorSettingsActivity.this, getString(R.string.import_success));
                        } catch (Exception e) {
                            IToast.showBottom(AuthenticatorSettingsActivity.this, getString(R.string.import_fail));
                        }
                    }

                    @Override
                    public void onNegtiveClick() {

                    }

                    @Override
                    public void onCloseClick() {

                    }
                });
                dialog.show();
                break;
        }

    }

}
