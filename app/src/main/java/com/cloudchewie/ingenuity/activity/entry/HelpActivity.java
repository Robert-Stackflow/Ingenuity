package com.cloudchewie.ingenuity.activity.entry;

import android.os.Bundle;
import android.view.View;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.global.BaseActivity;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.cloudchewie.ui.custom.ExpandableItem;
import com.cloudchewie.ui.custom.TitleBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class HelpActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    ExpandableItem retrievePasswordItem;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_help);
        ((TitleBar) findViewById(R.id.activity_help_titlebar)).setLeftButtonClickListener(v -> finish());
        initSwipeRefresh();
        bindView();
        initData();
    }

    void bindView() {
        retrievePasswordItem = findViewById(R.id.activity_help_expand_retrieve_password);
        retrievePasswordItem.setTitle("如何找回密码");
        retrievePasswordItem.setContent("1.如果您的帐号绑定了手机号码，请在登录界面点击”登陆问题“通过绑定的手机号码找回密码。2.如果您的帐号绑定了微信帐号，可以使用微信帐号登录并在“我的-设置-账号安全-修改密码”入口修改密码。3.如果您绑定的手机号已经失效，请联系管理员进行找回申诉。");
    }

    void initData() {

    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_help_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {

    }
}
