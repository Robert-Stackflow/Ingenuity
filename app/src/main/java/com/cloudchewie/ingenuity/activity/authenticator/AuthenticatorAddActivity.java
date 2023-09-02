package com.cloudchewie.ingenuity.activity.authenticator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ImageView;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.entity.OtpToken;
import com.cloudchewie.ingenuity.util.authenticator.OtpTokenParser;
import com.cloudchewie.ingenuity.util.authenticator.TokenImageUtil;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.cloudchewie.ingenuity.util.enumeration.EventBusCode;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.item.InputItem;
import com.cloudchewie.ui.item.RadioItem;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Locale;

public class AuthenticatorAddActivity extends BaseActivity implements View.OnClickListener, TextWatcher {
    RefreshLayout swipeRefreshLayout;
    InputItem issuerItem;
    InputItem accountItem;
    InputItem secretItem;
    InputItem intervalItem;
    RadioItem typeItem;
    RadioItem digitsItem;
    RadioItem algorithmItem;
    InputItem counterItem;
    ImageView logoView;
    String imageUrl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_authenticator_add);
        ((TitleBar) findViewById(R.id.activity_authenticator_add_titlebar)).setLeftButtonClickListener(v -> finish());
        ((TitleBar) findViewById(R.id.activity_authenticator_add_titlebar)).setRightButtonClickListener(v -> confirm());
        issuerItem = findViewById(R.id.activity_authenticator_add_issuer);
        logoView = findViewById(R.id.activity_authenticator_add_icon);
        accountItem = findViewById(R.id.activity_authenticator_add_account);
        secretItem = findViewById(R.id.activity_authenticator_add_secret);
        intervalItem = findViewById(R.id.activity_authenticator_add_interval);
        typeItem = findViewById(R.id.activity_authenticator_add_type);
        digitsItem = findViewById(R.id.activity_authenticator_add_digits);
        algorithmItem = findViewById(R.id.activity_authenticator_add_algorithm);
        counterItem = findViewById(R.id.activity_authenticator_add_counter);
        initSwipeRefresh();
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
        String issuer = Uri.decode(issuerItem.getText());
        String account = Uri.decode(accountItem.getText());
        String secret = Uri.decode(secretItem.getText());
        Integer interval = Integer.parseInt(Uri.decode(intervalItem.getText()));
        String algorithm = (String) getResources().getTextArray(R.array.auth_algorithms)[algorithmItem.getSelectedIndex()];
        Integer digits = Integer.parseInt((String) getResources().getTextArray(R.array.auth_digits)[digitsItem.getSelectedIndex()]);
        boolean isHotp = Boolean.parseBoolean((String) getResources().getTextArray(R.array.auth_type)[typeItem.getSelectedIndex()]);
        String uri = String.format(Locale.US,
                "otpauth://%sotp/%s:%s?secret=%s&algorithm=%s&digits=%d&period=%d",
                isHotp ? "h" : "t", issuer, account,
                secret, algorithm, digits, interval);
        if (isHotp) {
            Integer counter = Integer.parseInt(counterItem.getText());
            uri += String.format(Locale.US, "&counter=%d", counter);
        }
        if (imageUrl != null) {
            try {
                String enc = URLEncoder.encode(imageUrl, "utf-8");
                uri += String.format(Locale.US, "&image=%s", enc);
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
        }
        LocalStorage.getAppDatabase().otpTokenDao().insert(OtpTokenParser.createFromUri(Uri.parse(uri)));
        setResult(Activity.RESULT_OK);
        LiveEventBus.get(EventBusCode.CHANGE_TOKEN.getKey()).post("");
        finish();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_authenticator_add_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {

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