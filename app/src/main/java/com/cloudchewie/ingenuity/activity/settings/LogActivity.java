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

public class LogActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    RecyclerView recyclerView;
    BaseItemListAdapter adapter;
    List<BaseItem> baseItemList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_log);
        ((TitleBar) findViewById(R.id.activity_log_titlebar)).setLeftButtonClickListener(v -> finish());
        initSwipeRefresh();
        bindView();
    }

    void bindView() {
        recyclerView = findViewById(R.id.activity_log_recycler_view);
        baseItemList = new ArrayList<>();
        baseItemList.add(new BaseItem("0.2", "暂无更新日志", "当前版本"));
        baseItemList.add(new BaseItem("0.1", "暂无更新日志"));
        adapter = new BaseItemListAdapter(this, baseItemList);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new SpacingItemDecoration(this, (int) getResources().getDimension(R.dimen.dp3), Direction.BOTTOM));
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_log_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {

    }
}