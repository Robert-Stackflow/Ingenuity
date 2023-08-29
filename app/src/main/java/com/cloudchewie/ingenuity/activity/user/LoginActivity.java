/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:29:32
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.activity.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextPaint;
import android.text.method.LinkMovementMethod;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityOptionsCompat;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.activity.WebViewActivity;
import com.cloudchewie.ingenuity.bean.ListBottomSheetBean;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;
import com.cloudchewie.ui.ThemeUtil;
import com.cloudchewie.ui.custom.IToast;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.util.ui.StatusBarUtil;

import java.util.Arrays;
import java.util.List;

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    TextView termView;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_bottom_in, R.anim.anim_none);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_login);
        termView = findViewById(R.id.login_term);
        ((TitleBar) findViewById(R.id.login_titlebar)).setLeftButtonClickListener(v -> {
            finishAfterTransition();
            overridePendingTransition(R.anim.anim_none, R.anim.anim_bottom_out);
        });
        findViewById(R.id.login_by_mobile).setOnClickListener(this);
        findViewById(R.id.login_by_wechat).setOnClickListener(this);
        findViewById(R.id.login_signup).setOnClickListener(this);
        findViewById(R.id.login_problem).setOnClickListener(this);
        setTermView();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(R.anim.anim_none, R.anim.anim_bottom_out);
    }

    void setTermView() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getString(R.string.tap_login_to_agree_1));
        SpannableString userTermString = new SpannableString(getString(R.string.tap_login_to_agree_2));
        userTermString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class).setAction(Intent.ACTION_DEFAULT);
                intent.putExtra("url", getString(R.string.url_term));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, 0, userTermString.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        userTermString.setSpan(new ForegroundColorSpan(ThemeUtil.getPrimaryColor(this)), 0, userTermString.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(userTermString);
        SpannableString privacyTermString = new SpannableString(getString(R.string.tap_login_to_agree_3));
        privacyTermString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(LoginActivity.this, WebViewActivity.class).setAction(Intent.ACTION_DEFAULT);
                intent.putExtra("url", getString(R.string.url_term));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, 0, privacyTermString.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        privacyTermString.setSpan(new ForegroundColorSpan(ThemeUtil.getPrimaryColor(this)), 0, userTermString.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.append(privacyTermString);
        termView.setMovementMethod(LinkMovementMethod.getInstance());
        termView.setText(spannableStringBuilder);
        termView.setHighlightColor(Color.TRANSPARENT);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, findViewById(R.id.login_titlebar), "shareElement").toBundle();
        switch (view.getId()) {
            case R.id.login_by_mobile:
                Intent loginByMobileIntent = new Intent(this, LoginByMobileActivity.class).setAction(Intent.ACTION_DEFAULT);
                startActivity(loginByMobileIntent, bundle);
                break;
            case R.id.login_by_wechat:
                IToast.makeTextBottom(this, getString(R.string.fail_to_login_by_wechat), Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_signup:
                Intent signupIntent = new Intent(this, SignupActivity.class).setAction(Intent.ACTION_DEFAULT);
                startActivity(signupIntent, bundle);
                break;
            case R.id.login_problem:
                showLoginProblemDialog();
                break;
        }
    }

    public void showLoginProblemDialog() {
        List<String> operations = Arrays.asList(getResources().getStringArray(R.array.login_problems));
        ListBottomSheet popupWindow = new ListBottomSheet(this, ListBottomSheetBean.strToBean(operations));
        popupWindow.setOnItemClickedListener(position -> popupWindow.dismiss());
        popupWindow.show();
    }
}