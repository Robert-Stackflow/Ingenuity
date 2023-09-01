package com.cloudchewie.ingenuity.activity.settings;

import android.os.Bundle;
import android.view.View;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.adapter.BaseItemListAdapter;
import com.cloudchewie.ingenuity.entity.BaseItem;
import com.cloudchewie.ingenuity.util.decoration.SpacingItemDecoration;
import com.cloudchewie.ingenuity.util.enumeration.Direction;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.List;

public class HelpActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    BaseItemListAdapter adapter;
    List<BaseItem> baseItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_help);
        ((TitleBar) findViewById(R.id.activity_help_titlebar)).setLeftButtonClickListener(v -> finish());
        initSwipeRefresh();
        bindView();
    }

    void bindView() {
        recyclerView = findViewById(R.id.activity_help_recycler_view);
        baseItemList = new ArrayList<>();
        baseItemList.add(new BaseItem("如何找回密码", "1.如果您的帐号绑定了手机号码，请在登录界面点击”登陆问题“通过绑定的手机号码找回密码。\n2.如果您的帐号绑定了微信帐号，可以使用微信帐号登录并在“我的-设置-账号安全-修改密码”入口修改密码。\n3.如果您绑定的手机号已经失效，请联系管理员进行找回申诉。"));
        baseItemList.add(new BaseItem("如何使用Memos", "登陆帐号即可"));
        adapter = new BaseItemListAdapter(this, baseItemList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacingItemDecoration(this, (int) getResources().getDimension(R.dimen.dp3), Direction.BOTTOM));
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