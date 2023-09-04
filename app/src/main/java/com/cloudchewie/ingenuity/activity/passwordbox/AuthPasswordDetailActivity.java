package com.cloudchewie.ingenuity.activity.passwordbox;

import android.app.Activity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.blankj.utilcode.util.ThreadUtils;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.entity.AuthPassword;
import com.cloudchewie.ingenuity.entity.CommonPassword;
import com.cloudchewie.ingenuity.util.authenticator.TokenImageUtil;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.cloudchewie.ingenuity.util.enumeration.EventBusCode;
import com.cloudchewie.ui.custom.IDialog;
import com.cloudchewie.ui.custom.IToast;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.item.InputItem;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.Date;

public class AuthPasswordDetailActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    RefreshLayout swipeRefreshLayout;
    InputItem issuerItem;
    InputItem passwordItem;
    InputItem emailItem;
    InputItem websiteItem;
    InputItem remarkItem;
    AuthPassword paramPassword;
    AppCompatButton deleteButton;
    ImageView logoView;
    int paramGroupId;
    public static String EXTRA_PASSWORD_ID = "password_id";
    public static String EXTRA_GROUP_ID = "group_id";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_password_detail_auth);
        ((TitleBar) findViewById(R.id.activity_auth_password_detail_titlebar)).setLeftButtonClickListener(v -> finish());
        ((TitleBar) findViewById(R.id.activity_auth_password_detail_titlebar)).setRightButtonClickListener(v -> confirm());
        issuerItem = findViewById(R.id.activity_auth_password_detail_issuer);
        logoView = findViewById(R.id.activity_auth_password_detail_icon);
        passwordItem = findViewById(R.id.activity_auth_password_detail_password);
        emailItem = findViewById(R.id.activity_auth_password_detail_email);
        websiteItem = findViewById(R.id.activity_auth_password_detail_website);
        remarkItem = findViewById(R.id.activity_auth_password_detail_remark);
        deleteButton = findViewById(R.id.activity_auth_password_detail_delete);
        issuerItem.getEditText().addTextChangedListener(this);
        deleteButton.setOnClickListener(this);
        initSwipeRefresh();
        initData();
    }

    void initData() {
        ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() {
                try {
                    int paramPasswordId = getIntent().getIntExtra(EXTRA_PASSWORD_ID, -1);
                    paramGroupId = getIntent().getIntExtra(EXTRA_GROUP_ID, -1);
                    paramPassword = LocalStorage.getAppDatabase().authPasswordDao().getById(paramPasswordId);
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
        if (paramPassword != null) {
            deleteButton.setVisibility(View.VISIBLE);
            issuerItem.getEditText().setText(paramPassword.getIssuer());
            passwordItem.getEditText().setText(paramPassword.getPassword());
            websiteItem.getEditText().setText(paramPassword.getWebsite());
            emailItem.getEditText().setText(paramPassword.getEmail());
            remarkItem.getEditText().setText(paramPassword.getRemark());
            ((TitleBar) findViewById(R.id.activity_auth_password_detail_titlebar)).setTitle(getString(R.string.title_detail_password));
        } else {
            deleteButton.setVisibility(View.GONE);
            ((TitleBar) findViewById(R.id.activity_auth_password_detail_titlebar)).setTitle(getString(R.string.title_add_password));
        }
    }

    private void confirm() {
        if (paramPassword != null) {
            paramPassword.setIssuer(issuerItem.getText());
            paramPassword.setPassword(passwordItem.getText());
            paramPassword.setEmail(emailItem.getText());
            paramPassword.setWebsite(websiteItem.getText());
            paramPassword.setRemark(remarkItem.getText());
            paramPassword.setEditDate(new Date());
            LocalStorage.getAppDatabase().authPasswordDao().update(paramPassword);
            IToast.showBottom(this, getString(R.string.edit_success));
        } else {
            AuthPassword authPassword = new AuthPassword();
            authPassword.setGroupId(paramGroupId);
            authPassword.setIssuer(issuerItem.getText());
            authPassword.setPassword(passwordItem.getText());
            authPassword.setEmail(emailItem.getText());
            authPassword.setWebsite(websiteItem.getText());
            authPassword.setRemark(remarkItem.getText());
            authPassword.setAddDate(new Date());
            authPassword.setEditDate(new Date());
            LocalStorage.getAppDatabase().authPasswordDao().insert(authPassword);
            IToast.showBottom(this, getString(R.string.new_success));
        }
        setResult(Activity.RESULT_OK);
        LiveEventBus.get(EventBusCode.CHANGE_PASSWORD.getKey()).post("");
        finish();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_auth_password_detail_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {
        if (view == deleteButton) {
            if (paramPassword != null) {
                IDialog dialog = new IDialog(this);
                dialog.setTitle(getString(R.string.dialog_title_delete_password));
                dialog.setMessage(String.format(getString(R.string.dialog_content_delete_password), paramPassword.getIssuer()));
                dialog.setOnClickBottomListener(new IDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        LocalStorage.getAppDatabase().authPasswordDao().deleteById(paramPassword.getId());
                        finish();
                        LiveEventBus.get(EventBusCode.CHANGE_PASSWORD.getKey()).post("");
                        IToast.makeTextBottom(AuthPasswordDetailActivity.this, getString(R.string.delete_success), Toast.LENGTH_SHORT).show();
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        CommonPassword temp = new CommonPassword();
        temp.setIssuer(issuerItem.getText());
        TokenImageUtil.setPasswordImage(logoView, temp);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}
