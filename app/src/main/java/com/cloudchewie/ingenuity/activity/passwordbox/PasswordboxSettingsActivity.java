package com.cloudchewie.ingenuity.activity.passwordbox;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.util.ExploreUtil;
import com.cloudchewie.ingenuity.util.enumeration.EventBusCode;
import com.cloudchewie.ingenuity.util.password.ImportPasswordUtil;
import com.cloudchewie.ui.custom.IDialog;
import com.cloudchewie.ui.custom.IToast;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.item.CheckBoxItem;
import com.cloudchewie.ui.item.EntryItem;
import com.cloudchewie.util.system.SharedPreferenceCode;
import com.cloudchewie.util.system.SharedPreferenceUtil;
import com.cloudchewie.util.system.UriUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class PasswordboxSettingsActivity extends BaseActivity implements View.OnClickListener {
    private static final int READ_JSON_REQUEST_CODE = 42;
    private static final int WRITE_JSON_REQUEST_CODE = 43;
    private static final int READ_EXCEL_REQUEST_CODE = 44;
    private static final int WRITE_EXCEL_REQUEST_CODE = 45;
    private String EXPORT_PREFIX = "Password_";
    RefreshLayout swipeRefreshLayout;
    CheckBoxItem longPressItem;
    CheckBoxItem clickItem;
    CheckBoxItem authItem;
    CheckBoxItem screenShotItem;
    EntryItem exportJsonItem;
    EntryItem importJsonItem;
    EntryItem exportExcelItem;
    EntryItem importExcelItem;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_passwordbox_settings);
        ((TitleBar) findViewById(R.id.passwordbox_settings_titlebar)).setLeftButtonClickListener(v -> finishAfterTransition());
        longPressItem = findViewById(R.id.activity_passwordbox_settings_long_press_copy);
        clickItem = findViewById(R.id.activity_passwordbox_settings_click_copy);
        authItem = findViewById(R.id.activity_passwordbox_settings_need_auth);
        screenShotItem = findViewById(R.id.activity_passwordbox_settings_disable_screenshot);
        exportJsonItem = findViewById(R.id.activity_passwordbox_settings_export_json);
        exportExcelItem = findViewById(R.id.activity_passwordbox_settings_export_excel);
        importJsonItem = findViewById(R.id.activity_passwordbox_settings_import_json);
        importExcelItem = findViewById(R.id.activity_passwordbox_settings_import_excel);
        exportExcelItem.setOnClickListener(this);
        exportJsonItem.setOnClickListener(this);
        importJsonItem.setOnClickListener(this);
        importExcelItem.setOnClickListener(this);
        loadSettings();
        bindEvent();
        initSwipeRefresh();
    }

    void loadSettings() {
        longPressItem.setChecked(SharedPreferenceUtil.getBoolean(PasswordboxSettingsActivity.this, SharedPreferenceCode.PASSWORD_LONG_CLICK_COPY.getKey(), true));
        clickItem.setChecked(SharedPreferenceUtil.getBoolean(PasswordboxSettingsActivity.this, SharedPreferenceCode.PASSWORD_CLICK_COPY.getKey(), false));
        authItem.setChecked(SharedPreferenceUtil.getBoolean(PasswordboxSettingsActivity.this, SharedPreferenceCode.PASSWORD_NEED_AUTH.getKey(), true));
        screenShotItem.setChecked(SharedPreferenceUtil.getBoolean(PasswordboxSettingsActivity.this, SharedPreferenceCode.PASSWORD_DISBALE_SCREENSHOT.getKey(), true));
    }

    void bindEvent() {
        longPressItem.setOnCheckedChangedListener((buttonView, isChecked) -> SharedPreferenceUtil.putBoolean(PasswordboxSettingsActivity.this, SharedPreferenceCode.PASSWORD_LONG_CLICK_COPY.getKey(), isChecked));
        clickItem.setOnCheckedChangedListener((buttonView, isChecked) -> SharedPreferenceUtil.putBoolean(PasswordboxSettingsActivity.this, SharedPreferenceCode.PASSWORD_CLICK_COPY.getKey(), isChecked));
        authItem.setOnCheckedChangedListener((buttonView, isChecked) -> {
            SharedPreferenceUtil.putBoolean(PasswordboxSettingsActivity.this, SharedPreferenceCode.PASSWORD_NEED_AUTH.getKey(), isChecked);
            LiveEventBus.get(EventBusCode.CHANGE_PASSWORD_NEED_AUTH.getKey()).post("");
        });
        screenShotItem.setOnCheckedChangedListener((buttonView, isChecked) -> {
            SharedPreferenceUtil.putBoolean(PasswordboxSettingsActivity.this, SharedPreferenceCode.PASSWORD_DISBALE_SCREENSHOT.getKey(), isChecked);
            LiveEventBus.get(EventBusCode.CHANGE_PASSWORD_DISABLE_SCREENSHOT.getKey()).post("");
        });
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.passwordbox_settings_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {
        if (view == exportJsonItem) {
            ExploreUtil.createFile(this, "application/json", EXPORT_PREFIX, "json", WRITE_JSON_REQUEST_CODE, true);
        } else if (view == exportExcelItem) {
            ExploreUtil.createFile(this, "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet", EXPORT_PREFIX, "xlsx", WRITE_EXCEL_REQUEST_CODE, true);
        } else if (view == importJsonItem) {
            ExploreUtil.performFileSearch(this, READ_JSON_REQUEST_CODE);
        } else if (view == importExcelItem) {
            ExploreUtil.performFileSearch(this, READ_EXCEL_REQUEST_CODE);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode != Activity.RESULT_OK)
            return;
        Uri uri = resultData.getData();
        IDialog dialog = new IDialog(this);
        if (uri == null) return;
        switch (requestCode) {
            case WRITE_JSON_REQUEST_CODE:
//                ExportPasswordUtil.exportJsonFile(this, toExportGroup, uri);
                IToast.showBottom(this, getString(R.string.export_success));
                break;
            case READ_JSON_REQUEST_CODE:
                dialog.setTitle(getString(R.string.dialog_title_import_json_password));
                dialog.setMessage(String.format(getString(R.string.dialog_content_import_json_password), UriUtil.getFileAbsolutePath(this, uri)));
                dialog.setOnClickBottomListener(new IDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        try {
                            ImportPasswordUtil.importJsonFile(PasswordboxSettingsActivity.this, uri);
                            IToast.showBottom(PasswordboxSettingsActivity.this, getString(R.string.import_success));
                            LiveEventBus.get(EventBusCode.CHANGE_PASSWORD_GROUP.getKey()).post("");
                        } catch (Exception e) {
                            IToast.showBottom(PasswordboxSettingsActivity.this, getString(R.string.import_fail));
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
            case WRITE_EXCEL_REQUEST_CODE:
                IToast.showBottom(this, getString(R.string.export_success));
                break;
            case READ_EXCEL_REQUEST_CODE:
                dialog.setTitle(getString(R.string.dialog_title_import_excel_password));
                dialog.setMessage(String.format(getString(R.string.dialog_content_import_excel_password), UriUtil.getFileAbsolutePath(this, uri)));
                dialog.setOnClickBottomListener(new IDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        try {
                            IToast.showBottom(PasswordboxSettingsActivity.this, getString(R.string.import_success));
                            LiveEventBus.get(EventBusCode.CHANGE_PASSWORD_GROUP.getKey()).post("");
                        } catch (Exception e) {
                            IToast.showBottom(PasswordboxSettingsActivity.this, getString(R.string.import_fail));
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
