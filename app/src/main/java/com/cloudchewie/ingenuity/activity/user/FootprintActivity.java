package com.cloudchewie.ingenuity.activity.user;

import android.os.Bundle;
import android.view.View;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.global.BaseActivity;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.cloudchewie.ui.custom.TitleBar;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class FootprintActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_footprint);
        ((TitleBar) findViewById(R.id.activity_footprint_titlebar)).setLeftButtonClickListener(v -> finish());
        initSwipeRefresh();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_footprint_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {

    }
}
