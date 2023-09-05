package com.cloudchewie.ingenuity.activity.passwordbox;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ThreadUtils;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.adapter.PasswordGroupListAdapter;
import com.cloudchewie.ingenuity.adapter.PasswordListAdapter;
import com.cloudchewie.ingenuity.entity.PasswordGroup;
import com.cloudchewie.ingenuity.util.ExploreUtil;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.cloudchewie.ingenuity.util.database.MyFileProvider;
import com.cloudchewie.ingenuity.util.decoration.DividerItemDecoration;
import com.cloudchewie.ingenuity.util.decoration.SpacingItemDecoration;
import com.cloudchewie.ingenuity.util.enumeration.Direction;
import com.cloudchewie.ingenuity.util.enumeration.EventBusCode;
import com.cloudchewie.ingenuity.util.password.ExportPasswordUtil;
import com.cloudchewie.ingenuity.widget.InputBottomSheet;
import com.cloudchewie.ui.custom.IDialog;
import com.cloudchewie.ui.custom.IToast;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.fab.FloatingActionButton;
import com.cloudchewie.ui.fab.FloatingActionMenu;
import com.cloudchewie.ui.item.InputLayout;
import com.cloudchewie.util.basic.DateFormatUtil;
import com.cloudchewie.util.system.ShareUtil;
import com.cloudchewie.util.system.SharedPreferenceCode;
import com.cloudchewie.util.system.SharedPreferenceUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class PasswordboxActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener, TextWatcher {
    private static final int EXPORT_JSON_REQUEST_CODE = 43;
    private String EXPORT_PREFIX = "Password_";
    RefreshLayout swipeRefreshLayout;
    FloatingActionButton exportButton;
    FloatingActionButton newGroupButton;
    FloatingActionButton newPasswordButton;
    FloatingActionButton settingsButton;
    FloatingActionMenu floatingActionMenu;
    RecyclerView recyclerView;
    RecyclerView childRecyclerView;
    PasswordGroupListAdapter passwordGroupListAdapter;
    PasswordListAdapter passwordListAdapter;
    PasswordGroup toExportGroup;
    PasswordGroup currentSelectedGroup;
    InputLayout searchEdit;
    TextView blankView;
    List<PasswordGroup> passwordGroups;
    View divider;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_passwordbox);
        ((TitleBar) findViewById(R.id.activity_passwordbox_titlebar)).setLeftButtonClickListener(v -> finishAfterTransition());
        exportButton = findViewById(R.id.activity_passwordbox_export);
        recyclerView = findViewById(R.id.activity_passwordbox_recycler_view);
        childRecyclerView = findViewById(R.id.activity_passwordbox_child_recycler_view);
        newGroupButton = findViewById(R.id.activity_passwordbox_new_group);
        newPasswordButton = findViewById(R.id.activity_passwordbox_new_password);
        settingsButton = findViewById(R.id.activity_passwordbox_settings);
        floatingActionMenu = findViewById(R.id.activity_passwordbox_menu);
        divider = findViewById(R.id.activity_passwordbox_top_divider);
        blankView = findViewById(R.id.activity_passwordbox_blank);
        searchEdit = findViewById(R.id.activity_passwordbox_search);
        exportButton.setOnClickListener(this);
        newPasswordButton.setOnClickListener(this);
        settingsButton.setOnClickListener(this);
        newGroupButton.setOnClickListener(this);
        searchEdit.getEditText().setOnEditorActionListener(this);
        searchEdit.getEditText().addTextChangedListener(this);
        searchEdit.getEditText().setInputType(EditorInfo.IME_ACTION_DONE);
        LiveEventBus.get(EventBusCode.CHANGE_PASSWORD_GROUP.getKey()).observe(this, s -> initData());
        LiveEventBus.get(EventBusCode.CHANGE_PASSWORD.getKey()).observe(this, s -> {
            if (passwordListAdapter != null) passwordListAdapter.refreshData();
            updateNullDataState();
        });
        LiveEventBus.get(EventBusCode.CHANGE_PASSWORD_DISABLE_SCREENSHOT.getKey(), String.class).observe(this, s -> loadEnableScreenshot());
        initSwipeRefresh();
        initRecyclerView();
        loadEnableScreenshot();
    }


    void loadEnableScreenshot() {
        if (SharedPreferenceUtil.getBoolean(PasswordboxActivity.this, SharedPreferenceCode.PASSWORD_DISBALE_SCREENSHOT.getKey(), true)) {
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_SECURE, WindowManager.LayoutParams.FLAG_SECURE);
        } else {
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_SECURE);
        }
    }

    void initData() {
        ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() {
                passwordGroups = LocalStorage.getAppDatabase().passwordGroupDao().getAll();
                return null;
            }

            @Override
            public void onSuccess(String result) {
                runOnUiThread(() -> {
                    passwordGroupListAdapter.setPasswordGroupList(passwordGroups);
                    currentSelectedGroup = null;
                    updateNullDataState();
                });
            }
        });
    }

    void updateNullDataState() {
        if (currentSelectedGroup != null) {
            newGroupButton.setVisibility(View.GONE);
            newPasswordButton.setVisibility(View.VISIBLE);
            childRecyclerView.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
            divider.setVisibility(View.GONE);
            exportButton.setVisibility(View.VISIBLE);
            ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<Integer>() {
                @Override
                public Integer doInBackground() {
                    int count = 0;
                    switch (currentSelectedGroup.getType()) {
                        case AUTH:
                            count = LocalStorage.getAppDatabase().authPasswordDao().count(currentSelectedGroup.getId());
                            break;
                        case BACKUP:
                            count = LocalStorage.getAppDatabase().backupPasswordDao().count(currentSelectedGroup.getId());
                            break;
                        case COMMON:
                            count = LocalStorage.getAppDatabase().commonPasswordDao().count(currentSelectedGroup.getId());
                            break;
                    }
                    return count;
                }

                @Override
                public void onSuccess(Integer result) {
                    runOnUiThread(() -> {
                        if (result > 0) {
                            blankView.setVisibility(View.GONE);
                        } else {
                            blankView.setVisibility(View.VISIBLE);
                        }
                    });
                }
            });
        } else {
            newGroupButton.setVisibility(View.VISIBLE);
            newPasswordButton.setVisibility(View.GONE);
            childRecyclerView.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
            exportButton.setVisibility(View.GONE);
            if (passwordGroups != null && passwordGroups.size() > 0) {
                divider.setVisibility(View.VISIBLE);
                blankView.setVisibility(View.GONE);
            } else {
                blankView.setVisibility(View.VISIBLE);
                divider.setVisibility(View.GONE);
            }
        }
        floatingActionMenu.close(false);
    }

    void initRecyclerView() {
        passwordGroups = new ArrayList<>();
        updateNullDataState();
        initData();
        //绑定适配器
        passwordGroupListAdapter = new PasswordGroupListAdapter(this, passwordGroups);
        recyclerView.setAdapter(passwordGroupListAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        childRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        childRecyclerView.addItemDecoration(new SpacingItemDecoration(this, (int) getResources().getDimension(R.dimen.dp3), Direction.BOTTOM));
        passwordGroupListAdapter.setOnGroupOperationListener(new PasswordGroupListAdapter.OnGroupOperationListener() {
            @Override
            public void onClick(PasswordGroupListAdapter.PasswordGroupViewHolder holder, PasswordGroup passwordGroup) {
                currentSelectedGroup = passwordGroup;
                passwordListAdapter = new PasswordListAdapter(PasswordboxActivity.this, passwordGroup);
                childRecyclerView.setAdapter(passwordListAdapter);
                updateNullDataState();
            }

            @Override
            public void onRename(PasswordGroupListAdapter.PasswordGroupViewHolder holder, PasswordGroup passwordGroup) {
                InputBottomSheet renameBottomSheet = new InputBottomSheet(PasswordboxActivity.this, getString(R.string.rename), passwordGroup.getName(), getString(R.string.password_group_name), 30, true);
                renameBottomSheet.setOnConfirmClickedListener(content -> {
                    passwordGroup.setName(content);
                    holder.nameView.setText(content);
                    LocalStorage.getAppDatabase().passwordGroupDao().update(passwordGroup);
                    IToast.showBottom(PasswordboxActivity.this, getString(R.string.rename_success));
                });
                renameBottomSheet.setOnDismissListener(dialog -> renameBottomSheet.hideKeyBoard());
                renameBottomSheet.show();
            }

            @Override
            public void onEdit(PasswordGroupListAdapter.PasswordGroupViewHolder holder, PasswordGroup passwordGroup) {
                Intent intent = new Intent(PasswordboxActivity.this, PasswordGroupDetailActivity.class).setAction(Intent.ACTION_DEFAULT);
                intent.putExtra(PasswordGroupDetailActivity.EXTRA_GROUP_ID, passwordGroup.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(PasswordGroupListAdapter.PasswordGroupViewHolder holder, PasswordGroup passwordGroup) {
                IDialog dialog = new IDialog(PasswordboxActivity.this);
                dialog.setTitle(getString(R.string.dialog_title_delete_password_group));
                dialog.setMessage(String.format(getString(R.string.dialog_content_delete_password_group), passwordGroup.getName()));
                dialog.setOnClickBottomListener(new IDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        LocalStorage.getAppDatabase().passwordGroupDao().deleteById(passwordGroup.getId());
                        switch (passwordGroup.getType()) {
                            case AUTH:
                                LocalStorage.getAppDatabase().authPasswordDao().delete(passwordGroup.getId());
                                break;
                            case BACKUP:
                                LocalStorage.getAppDatabase().backupPasswordDao().delete(passwordGroup.getId());
                                break;
                            case COMMON:
                                LocalStorage.getAppDatabase().commonPasswordDao().delete(passwordGroup.getId());
                                break;
                        }
                        initData();
                        IToast.showBottom(PasswordboxActivity.this, getString(R.string.delete_success));
                    }

                    @Override
                    public void onNegtiveClick() {

                    }

                    @Override
                    public void onCloseClick() {

                    }
                });
                dialog.show();
            }

            @Override
            public void onExport(PasswordGroupListAdapter.PasswordGroupViewHolder holder, PasswordGroup passwordGroup) {
                toExportGroup = passwordGroup;
                ExploreUtil.createFile(PasswordboxActivity.this, "application/json", EXPORT_PREFIX, "json", EXPORT_JSON_REQUEST_CODE, true);
            }

            @Override
            public void onShare(PasswordGroupListAdapter.PasswordGroupViewHolder holder, PasswordGroup passwordGroup) {
                sharePasswordGroup(passwordGroup);
            }
        });
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_passwordbox_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {
        if (view == newGroupButton) {
            Intent intent = new Intent(this, PasswordGroupDetailActivity.class).setAction(Intent.ACTION_DEFAULT);
            floatingActionMenu.close(true);
            startActivity(intent);
        } else if (view == settingsButton) {
            Intent intent = new Intent(this, PasswordboxSettingsActivity.class).setAction(Intent.ACTION_DEFAULT);
            floatingActionMenu.close(true);
            startActivity(intent);
        } else if (view == exportButton) {
            if (currentSelectedGroup != null) {
                toExportGroup = currentSelectedGroup;
                ExploreUtil.createFile(PasswordboxActivity.this, "application/json", EXPORT_PREFIX, "json", EXPORT_JSON_REQUEST_CODE, true);
            }
        } else if (view == newPasswordButton) {
            if (currentSelectedGroup == null || currentSelectedGroup.getType() == null) return;
            switch (currentSelectedGroup.getType()) {
                case AUTH:
                    Intent authIntent = new Intent(this, AuthPasswordDetailActivity.class).setAction(Intent.ACTION_DEFAULT);
                    authIntent.putExtra(AuthPasswordDetailActivity.EXTRA_GROUP_ID, currentSelectedGroup.getId());
                    startActivity(authIntent);
                    break;
                case BACKUP:
                    Intent backupIntent = new Intent(this, BackupPasswordDetailActivity.class).setAction(Intent.ACTION_DEFAULT);
                    backupIntent.putExtra(BackupPasswordDetailActivity.EXTRA_GROUP_ID, currentSelectedGroup.getId());
                    startActivity(backupIntent);
                    break;
                case COMMON:
                    Intent commonIntent = new Intent(this, CommonPasswordDetailActivity.class).setAction(Intent.ACTION_DEFAULT);
                    commonIntent.putExtra(CommonPasswordDetailActivity.EXTRA_GROUP_ID, currentSelectedGroup.getId());
                    startActivity(commonIntent);
                    break;
            }
            floatingActionMenu.close(true);
        }
    }

    private void sharePasswordGroup(PasswordGroup passwordGroup) {
        String fileName = EXPORT_PREFIX + DateFormatUtil.getSimpleDateFormat(DateFormatUtil.FILE_FORMAT).format(new Date()) + ".json";
        File file = new File(getFilesDir().toString(), fileName);
        Uri uri = MyFileProvider.getUriForFile(this, getApplication().getPackageName() + ".provider", file);
        ExportPasswordUtil.exportJsonFile(this, passwordGroup, uri);
        ShareUtil.shareFile(this, uri, "text/html", getString(R.string.share_password_group));
    }

    @Override
    public void onBackPressed() {
        if (currentSelectedGroup != null) {
            currentSelectedGroup = null;
        } else {
            super.onBackPressed();
        }
        updateNullDataState();
    }


    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
//        if (i == EditorInfo.IME_ACTION_DONE) {
//            if (TextUtils.isEmpty(searchEdit.getText())) {
//            } else {
//            }
//        }
        return false;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode != Activity.RESULT_OK) return;
        Uri uri = resultData.getData();
        if (uri == null) return;
        IDialog dialog = new IDialog(this);
        if (requestCode == EXPORT_JSON_REQUEST_CODE) {
            if (toExportGroup != null) {
                ExportPasswordUtil.exportJsonFile(this, toExportGroup, uri);
                IToast.showBottom(this, getString(R.string.export_success));
            }
        }
    }
}