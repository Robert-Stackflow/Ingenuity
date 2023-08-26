package com.cloudchewie.ingenuity.activity.user;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.global.BaseActivity;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.cloudchewie.ui.custom.TitleBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class HomePageSettingActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_home_page_setting);
        ((TitleBar) findViewById(R.id.activity_home_page_setting_titlebar)).setLeftButtonClickListener(v -> finish());
        initView();
        initSwipeRefresh();
    }

    void initView() {
        Intent intent = getIntent();
        if (intent != null) user = (User) intent.getSerializableExtra("user");
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_home_page_setting_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {

    }
}
