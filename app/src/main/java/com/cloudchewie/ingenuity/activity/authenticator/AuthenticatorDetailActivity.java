package com.cloudchewie.ingenuity.activity.authenticator;

import android.app.Activity;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.entity.OtpToken;
import com.cloudchewie.ingenuity.util.authenticator.OtpTokenParser;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.cloudchewie.ingenuity.util.enumeration.OtpTokenType;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.item.InputItem;
import com.cloudchewie.ui.item.RadioItem;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.util.Arrays;
import java.util.Locale;

public class AuthenticatorDetailActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_authenticator_detail);
        ((TitleBar) findViewById(R.id.activity_authenticator_detail_titlebar)).setLeftButtonClickListener(v -> finish());
        ((TitleBar) findViewById(R.id.activity_authenticator_detail_titlebar)).setRightButtonClickListener(v -> confirm());
        issuerItem = findViewById(R.id.activity_authenticator_detail_issuer);
        accountItem = findViewById(R.id.activity_authenticator_detail_account);
        secretItem = findViewById(R.id.activity_authenticator_detail_secret);
        intervalItem = findViewById(R.id.activity_authenticator_detail_interval);
        typeItem = findViewById(R.id.activity_authenticator_detail_type);
        digitsItem = findViewById(R.id.activity_authenticator_detail_digits);
        algorithmItem = findViewById(R.id.activity_authenticator_detail_algorithm);
        counterItem = findViewById(R.id.activity_authenticator_detail_counter);
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
        changeCounterVisibility();
        typeItem.setOnIndexChangedListener((radioButton, index) -> changeCounterVisibility());
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
        String uri = String.format(Locale.US, "otpauth://%sotp/%s:%s?secret=%s&algorithm=%s&digits=%d&period=%d", isHotp ? "h" : "t", issuer, account, secret, algorithm, digits, interval);
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
        LocalStorage.getAppDatabase().otpTokenDao().update(OtpTokenParser.createFromUri(Uri.parse(uri)));
        setResult(Activity.RESULT_OK);
        finish();
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

    }
}