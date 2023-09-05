package com.cloudchewie.ingenuity.activity.passwordbox;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.blankj.utilcode.util.ThreadUtils;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.entity.PasswordGroup;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.cloudchewie.ingenuity.util.enumeration.EventBusCode;
import com.cloudchewie.ui.custom.IDialog;
import com.cloudchewie.ui.custom.IToast;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.item.InputItem;
import com.cloudchewie.ui.item.RadioItem;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.Date;

public class PasswordGroupDetailActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    InputItem nameItem;
    InputItem remarkItem;
    RadioItem typeItem;
    String imageUrl;
    PasswordGroup paramPasswordGroup;
    AppCompatButton deleteButton;
    public static String EXTRA_GROUP_ID = "group_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_password_group_detail);
        ((TitleBar) findViewById(R.id.activity_password_group_detail_titlebar)).setLeftButtonClickListener(v -> finish());
        ((TitleBar) findViewById(R.id.activity_password_group_detail_titlebar)).setRightButtonClickListener(v -> confirm());
        nameItem = findViewById(R.id.activity_password_group_detail_name);
        remarkItem = findViewById(R.id.activity_password_group_detail_remark);
        typeItem = findViewById(R.id.activity_password_group_detail_type);
        deleteButton = findViewById(R.id.activity_password_group_detail_delete);
        deleteButton.setOnClickListener(this);
        initSwipeRefresh();
        initData();
    }

    void initData() {
        ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() {
                try {
                    int paramGroupId = getIntent().getIntExtra(EXTRA_GROUP_ID, -1);
                    paramPasswordGroup = LocalStorage.getAppDatabase().passwordGroupDao().getById(paramGroupId);
                } catch (Throwable e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            public void onSuccess(String result) {
                changeState();
            }

            @Override
            public void onFail(Throwable t) {
                super.onFail(t);
                changeState();
            }
        });
    }

    void changeState() {
        if (paramPasswordGroup != null) {
            typeItem.setEnabled(false);
            deleteButton.setVisibility(View.VISIBLE);
            nameItem.getEditText().setText(paramPasswordGroup.getName());
            remarkItem.getEditText().setText(paramPasswordGroup.getRemark());
            typeItem.setSelectedIndex(paramPasswordGroup.getType().ordinal());
            ((TitleBar) findViewById(R.id.activity_password_group_detail_titlebar)).setTitle(getString(R.string.title_detail_password_group));
        } else {
            deleteButton.setVisibility(View.GONE);
            ((TitleBar) findViewById(R.id.activity_password_group_detail_titlebar)).setTitle(getString(R.string.title_add_password_group));
        }
    }

    private void confirm() {
        if (paramPasswordGroup != null) {
            paramPasswordGroup.setName(nameItem.getText());
            paramPasswordGroup.setRemark(remarkItem.getText());
            paramPasswordGroup.setEditDate(new Date());
            LocalStorage.getAppDatabase().passwordGroupDao().update(paramPasswordGroup);
        } else {
            PasswordGroup.PasswordType type = PasswordGroup.PasswordType.values()[typeItem.getSelectedIndex()];
            PasswordGroup passwordGroup = new PasswordGroup();
            passwordGroup.setName(nameItem.getText());
            passwordGroup.setRemark(remarkItem.getText());
            passwordGroup.setType(type);
            passwordGroup.setAddDate(new Date());
            passwordGroup.setEditDate(new Date());
            LocalStorage.getAppDatabase().passwordGroupDao().insert(passwordGroup);
        }
        setResult(Activity.RESULT_OK);
        LiveEventBus.get(EventBusCode.CHANGE_PASSWORD_GROUP.getKey()).post("");
        finish();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_password_group_detail_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {
        if (view == deleteButton) {
            if (paramPasswordGroup != null) {
                IDialog dialog = new IDialog(this);
                dialog.setTitle(getString(R.string.dialog_title_delete_password_group));
                dialog.setMessage(String.format(getString(R.string.dialog_content_delete_password_group), paramPasswordGroup.getName()));
                dialog.setOnClickBottomListener(new IDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        LocalStorage.getAppDatabase().passwordGroupDao().deleteById(paramPasswordGroup.getId());
                        switch (paramPasswordGroup.getType()) {
                            case AUTH:
                                LocalStorage.getAppDatabase().authPasswordDao().delete(paramPasswordGroup.getId());
                                break;
                            case BACKUP:
                                LocalStorage.getAppDatabase().backupPasswordDao().delete(paramPasswordGroup.getId());
                                break;
                            case COMMON:
                                LocalStorage.getAppDatabase().commonPasswordDao().delete(paramPasswordGroup.getId());
                                break;
                        }
                        finish();
                        LiveEventBus.get(EventBusCode.CHANGE_PASSWORD_GROUP.getKey()).post("");
                        IToast.makeTextBottom(PasswordGroupDetailActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
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
        }
    }
}