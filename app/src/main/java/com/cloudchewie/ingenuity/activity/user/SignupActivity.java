package com.cloudchewie.ingenuity.activity.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
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

import androidx.annotation.NonNull;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.activity.application.WebViewActivity;
import com.cloudchewie.ui.ThemeUtil;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.item.InputLayout;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class SignupActivity extends BaseActivity implements View.OnClickListener {
    TextView termView;
    RefreshLayout swipeRefreshLayout;
    InputLayout passwordInput;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_signup);
        termView = findViewById(R.id.activity_signup_term);
        passwordInput = findViewById(R.id.activity_signup_password);
        ((TitleBar) findViewById(R.id.activity_signup_titlebar)).setLeftButtonClickListener(v -> finishAfterTransition());
        findViewById(R.id.activity_signup_confirm).setOnClickListener(this);
        findViewById(R.id.signup_login).setOnClickListener(this);
        passwordInput.getEditText().setTypeface(Typeface.SANS_SERIF);
        setTermView();
        initSwipeRefresh();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_signup_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    void setTermView() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder(getString(R.string.tap_login_to_agree_1));
        SpannableString userTermString = new SpannableString(getString(R.string.tap_login_to_agree_2));
        userTermString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(SignupActivity.this, WebViewActivity.class).setAction(Intent.ACTION_DEFAULT);
                intent.putExtra("url", getString(R.string.url_term));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, 0, userTermString.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        userTermString.setSpan(new ForegroundColorSpan(ThemeUtil.getPrimaryColor(this)), 0, userTermString.length(), Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(userTermString);
        spannableStringBuilder.append(new SpannableString(getString(R.string.tap_login_to_agree_and)));
        SpannableString privacyTermString = new SpannableString(getString(R.string.tap_login_to_agree_3));
        privacyTermString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                Intent intent = new Intent(SignupActivity.this, WebViewActivity.class).setAction(Intent.ACTION_DEFAULT);
                intent.putExtra("url", getString(R.string.url_term));
                startActivity(intent);
            }

            @Override
            public void updateDrawState(@NonNull TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, 0, privacyTermString.length() - 1, Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        privacyTermString.setSpan(new ForegroundColorSpan(ThemeUtil.getPrimaryColor(this)), 0, privacyTermString.length(), Spanned.SPAN_INCLUSIVE_INCLUSIVE);
        spannableStringBuilder.append(privacyTermString);
        termView.setMovementMethod(LinkMovementMethod.getInstance());
        termView.setText(spannableStringBuilder);
        termView.setHighlightColor(Color.TRANSPARENT);
    }

    @SuppressLint("NonConstantResourceId")
    @Override
    public void onClick(View view) {
        if (view.getId() == R.id.signup_login) {
            Intent loginIntent = new Intent(this, LoginActivity.class).setAction(Intent.ACTION_DEFAULT);
            startActivity(loginIntent);
        }
    }
}
