package com.cloudchewie.ingenuity.activity.authenticator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.widget.AppCompatButton;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.entity.OtpToken;
import com.cloudchewie.ingenuity.util.authenticator.TokenImageUtil;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.cloudchewie.ingenuity.util.enumeration.EventBusCode;
import com.cloudchewie.ingenuity.util.enumeration.OtpTokenType;
import com.cloudchewie.ui.custom.IDialog;
import com.cloudchewie.ui.custom.IToast;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.item.InputItem;
import com.cloudchewie.ui.item.RadioItem;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.Arrays;

public class AuthenticatorDetailActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    RefreshLayout swipeRefreshLayout;
    ImageView logoView;
    InputItem issuerItem;
    InputItem accountItem;
    InputItem secretItem;
    InputItem intervalItem;
    RadioItem typeItem;
    RadioItem digitsItem;
    RadioItem algorithmItem;
    InputItem counterItem;
    String imageUrl;
    OtpToken token;
    AppCompatButton deleteButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_authenticator_detail);
        ((TitleBar) findViewById(R.id.activity_authenticator_detail_titlebar)).setLeftButtonClickListener(v -> finish());
        ((TitleBar) findViewById(R.id.activity_authenticator_detail_titlebar)).setRightButtonClickListener(v -> confirm());
        logoView = findViewById(R.id.activity_authenticator_detail_icon);
        issuerItem = findViewById(R.id.activity_authenticator_detail_issuer);
        accountItem = findViewById(R.id.activity_authenticator_detail_account);
        secretItem = findViewById(R.id.activity_authenticator_detail_secret);
        intervalItem = findViewById(R.id.activity_authenticator_detail_interval);
        typeItem = findViewById(R.id.activity_authenticator_detail_type);
        digitsItem = findViewById(R.id.activity_authenticator_detail_digits);
        algorithmItem = findViewById(R.id.activity_authenticator_detail_algorithm);
        counterItem = findViewById(R.id.activity_authenticator_detail_counter);
        deleteButton = findViewById(R.id.activity_authenticator_detail_delete);
        deleteButton.setOnClickListener(this);
        initSwipeRefresh();
        long tokenId = getIntent().getLongExtra("token_id", 0L);
        token = LocalStorage.getAppDatabase().otpTokenDao().get(tokenId);
        issuerItem.getEditText().setText(token.getIssuer());
        accountItem.getEditText().setText(token.getAccount());
        secretItem.getEditText().setText(token.getSecret());
        intervalItem.getEditText().setText(String.valueOf(token.getPeriod()));
        typeItem.setSelectedIndex(token.getTokenType() == OtpTokenType.TOTP ? 0 : 1);
        digitsItem.setSelectedIndex(token.getDigits() - 5);
        algorithmItem.setSelectedIndex(Arrays.asList(getResources().getStringArray(R.array.auth_algorithms)).indexOf(token.getAlgorithm()));
        typeItem.setEnabled(false);
        digitsItem.setEnabled(false);
        algorithmItem.setEnabled(false);
        intervalItem.getEditText().setEnabled(false);
        TokenImageUtil.setTokenImage(logoView, token);
        changeCounterVisibility();
        typeItem.setOnIndexChangedListener((radioButton, index) -> changeCounterVisibility());
        issuerItem.getEditText().addTextChangedListener(this);
        accountItem.getEditText().addTextChangedListener(this);
    }

    void changeCounterVisibility() {
        if (typeItem.getSelectedIndex() == 0) {
            counterItem.setVisibility(View.GONE);
            intervalItem.setRadiusEnbale(false, true);
        } else {
            counterItem.setVisibility(View.VISIBLE);
            intervalItem.setRadiusEnbale(false, false);
        }
    }

    private void confirm() {
        token.setIssuer(Uri.decode(issuerItem.getText()));
        token.setAccount(Uri.decode(accountItem.getText()));
        token.setSecret(Uri.decode(secretItem.getText()));
        LocalStorage.getAppDatabase().otpTokenDao().update(token);
        setResult(Activity.RESULT_OK);
        finish();
        LiveEventBus.get(EventBusCode.CHANGE_TOKEN.getKey()).post("");
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_authenticator_detail_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {
        if (view == deleteButton) {
            IDialog dialog = new IDialog(this);
            dialog.setTitle(getString(R.string.dialog_title_delete_token));
            dialog.setMessage(getString(R.string.dialog_content_delete_token));
            dialog.setOnClickBottomListener(new IDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    LocalStorage.getAppDatabase().otpTokenDao().deleteById(token.getId());
                    finish();
                    LiveEventBus.get(EventBusCode.CHANGE_TOKEN.getKey()).post("");
                    IToast.makeTextBottom(AuthenticatorDetailActivity.this, getString(R.string.delete_token_success), Toast.LENGTH_SHORT).show();
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

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        OtpToken temp = new OtpToken();
        temp.setAccount(accountItem.getText());
        temp.setIssuer(issuerItem.getText());
        TokenImageUtil.setTokenImage(logoView, temp);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}