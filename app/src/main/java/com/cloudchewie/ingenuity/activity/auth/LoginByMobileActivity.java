package com.cloudchewie.ingenuity.activity.auth;

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

import com.blankj.utilcode.util.ActivityUtils;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.MainActivity;
import com.cloudchewie.ingenuity.activity.global.BaseActivity;
import com.cloudchewie.ingenuity.bean.ListBottomSheetBean;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.ingenuity.request.UserAuthRequest;
import com.cloudchewie.ingenuity.request.UserProfileRequest;
import com.cloudchewie.ingenuity.util.development.JwtUtil;
import com.cloudchewie.ingenuity.util.system.AppSharedPreferenceUtil;
import com.cloudchewie.ui.ThemeUtil;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;
import com.cloudchewie.ui.custom.InputItem;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.general.IToast;
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
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_login_by_mobile);
        termView = findViewById(R.id.login_by_mobile_term);
        mobileInput = findViewById(R.id.login_by_mobile_phonenumber);
        passwordInput = findViewById(R.id.login_by_mobile_password);
        mobileInput.getEditText().setText("17837353795");
        passwordInput.getEditText().setText("123456");
        ((TitleBar) findViewById(R.id.login_by_mobile_titlebar)).setLeftButtonClickListener(v -> finishAfterTransition());
        findViewById(R.id.login_by_mobile_confirm).setOnClickListener(this);
        findViewById(R.id.login_by_mobile_signup).setOnClickListener(this);
        findViewById(R.id.login_by_mobile_problem).setOnClickListener(this);
        findViewById(R.id.login_by_mobile_toggle).setOnClickListener(this);
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

    void setTermView() {
        SpannableStringBuilder spannableStringBuilder = new SpannableStringBuilder("点击登录即表示您已阅读并同意");
        SpannableString userTermString = new SpannableString("《用户协议》和");
        userTermString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                IToast.makeTextBottom(LoginByMobileActivity.this, "阅读用户协议", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
                ds.setUnderlineText(false);
            }
        }, 0, userTermString.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        userTermString.setSpan(new ForegroundColorSpan(ThemeUtil.getPrimaryColor(this)), 0, userTermString.length() - 1, Spanned.SPAN_INCLUSIVE_EXCLUSIVE);
        spannableStringBuilder.append(userTermString);
        SpannableString privacyTermString = new SpannableString("《隐私政策》");
        privacyTermString.setSpan(new ClickableSpan() {
            @Override
            public void onClick(@NonNull View view) {
                IToast.makeTextBottom(LoginByMobileActivity.this, "阅读隐私政策", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void updateDrawState(TextPaint ds) {
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
        Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(this, findViewById(R.id.login_by_mobile_titlebar), "shareElement").toBundle();
        switch (view.getId()) {
            case R.id.login_by_mobile_toggle:
                IToast.makeTextBottom(this, "系统维护中，暂时无法使用验证码登录", Toast.LENGTH_SHORT).show();
                break;
            case R.id.login_by_mobile_signup:
                Intent signupIntent = new Intent(this, SignupActivity.class);
                startActivity(signupIntent, bundle);
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
                        ActivityUtils.startActivity(new Intent(this, MainActivity.class));
                    } else {
                        IToast.showBottom(this, "登陆失败");
                    }
                } else {
                    IToast.showBottom(this, "登陆失败");
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
