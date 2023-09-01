package com.cloudchewie.ingenuity.activity.authenticator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class AuthenticatorSettingsActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_authenticator_settings);
        ((TitleBar) findViewById(R.id.authenticator_settings_titlebar)).setLeftButtonClickListener(v -> finishAfterTransition());
        initSwipeRefresh();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.authenticator_settings_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {

    }
}
