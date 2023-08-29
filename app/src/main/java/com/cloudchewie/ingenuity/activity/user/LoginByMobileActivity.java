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

import com.blankj.utilcode.util.ActivityUtils;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.activity.MainActivity;
import com.cloudchewie.ingenuity.activity.WebViewActivity;
import com.cloudchewie.ingenuity.api.UserAuthRequest;
import com.cloudchewie.ingenuity.api.UserProfileRequest;
import com.cloudchewie.ingenuity.bean.ListBottomSheetBean;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.ingenuity.util.database.AppSharedPreferenceUtil;
import com.cloudchewie.ingenuity.util.http.JwtUtil;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;
import com.cloudchewie.ui.ThemeUtil;
import com.cloudchewie.ui.custom.IToast;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.item.InputItem;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.Arrays;
import java.util.List;

public class LoginByMobileActivity extends BaseActivity implements View.OnClickListener {
    TextView termView;
    RefreshLayout swipeRefreshLayout;
    InputItem mobileInput;
    InputItem passwordInput;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        overridePendingTransition(R.anim.anim_bottom_in, R.anim.anim_none);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_login_by_mobile);
        termView = findViewById(R.id.login_by_mobile_term);
        mobileInput = findViewById(R.id.login_by_mobile_phonenumber);
        passwordInput = findViewById(R.id.login_by_mobile_password);
        ((TitleBar) findViewById(R.id.login_by_mobile_titlebar)).setLeftButtonClickListener(v -> {
            finishAfterTransition();
            overridePendingTransition(R.anim.anim_none, R.anim.anim_bottom_out);
        });
        findViewById(R.id.login_by_mobile_confirm).setOnClickListener(this);
        findViewById(R.id.login_by_mobile_signup).setOnClickListener(this);
        findViewById(R.id.login_by_mobile_problem).setOnClickListener(this);
        findViewById(R.id.login_by_mobile_code).setOnClickListener(this);
        setTermView();
        initSwipeRefresh();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.login_by_mobile_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
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
                Intent intent = new Intent(LoginByMobileActivity.this, WebViewActivity.class).setAction(Intent.ACTION_DEFAULT);
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
                Intent intent = new Intent(LoginByMobileActivity.this, WebViewActivity.class).setAction(Intent.ACTION_DEFAULT);
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
        switch (view.getId()) {
            case R.id.login_by_mobile_code:
                IToast.makeTextBottom(this, getString(R.string.fail_to_login_by_code), Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_by_mobile_signup:
                Intent signupIntent = new Intent(this, SignupActivity.class).setAction(Intent.ACTION_DEFAULT);
                startActivity(signupIntent);
                break;
            case R.id.login_by_mobile_problem:
                showLoginProblemDialog();
                break;
            case R.id.login_by_mobile_confirm:
                User user = new User();
                user.setPassword(passwordInput.getText());
                user.setMobile(mobileInput.getText());
                UserAuthRequest.login(user);
                if (AppSharedPreferenceUtil.getToken(this) != null) {
                    User ret = UserProfileRequest.info(JwtUtil.getAud(AppSharedPreferenceUtil.getToken(this)));
                    if (ret != null) {
                        AppSharedPreferenceUtil.setUserInfo(this, ret);
                        ActivityUtils.finishAllActivities();
                        ActivityUtils.startActivity(new Intent(this, MainActivity.class).setAction(Intent.ACTION_DEFAULT));
                    } else {
                        IToast.showBottom(this, getString(R.string.fail_to_login));
                    }
                } else {
                    IToast.showBottom(this, getString(R.string.fail_to_login));
                }
        }
    }

    public void showLoginProblemDialog() {
        List<String> operations = Arrays.asList(getResources().getStringArray(R.array.login_problems));
        ListBottomSheet popupWindow = new ListBottomSheet(this, ListBottomSheetBean.strToBean(operations));
        popupWindow.setOnItemClickedListener(position -> popupWindow.dismiss());
        popupWindow.show();
    }
}
