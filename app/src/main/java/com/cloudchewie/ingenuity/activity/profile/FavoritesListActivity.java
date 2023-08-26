/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:11:15
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.activity.profile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.RelativeLayout;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.create.CreateFavoritesActivity;
import com.cloudchewie.ingenuity.activity.global.BaseActivity;
import com.cloudchewie.ingenuity.adapter.profile.FavoritesListAdapter;
import com.cloudchewie.ingenuity.entity.Favorites;
import com.cloudchewie.ingenuity.fragment.global.BaseFragment;
import com.cloudchewie.ingenuity.request.SettingRequest;
import com.cloudchewie.ingenuity.request.UserProfileRequest;
import com.cloudchewie.ingenuity.util.decoration.DividerItemDecoration;
import com.cloudchewie.ui.custom.ExpandableList;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.general.ExpandLayout;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

public class FavoritesListActivity extends BaseActivity implements View.OnClickListener, FavoritesListAdapter.OnItemCountChangedListener {
    int userId;
    List<Favorites> mFavoritesList;
    //基本控件
    TitleBar mTitleBar;
    //主要控件
    ExpandableList myExpandableList;
    ExpandableList subscribeExpandableList;
    ExpandLayout myExpandLayout;
    ExpandLayout subscribeExpandLayout;
    RecyclerView myRecyclerView;
    RecyclerView subsrcibeRecyclerView;
    FavoritesListAdapter myAdapter;
    FavoritesListAdapter subsrcibeAdapter;
    RefreshLayout mSwipeRefreshLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_favorites_list);
        mTitleBar = findViewById(R.id.activity_favorites_list_titlebar);
        myExpandableList = findViewById(R.id.activity_favorites_list_my_favorites);
        subscribeExpandableList = findViewById(R.id.activity_favorites_list_subscribe_favorites);
        myExpandLayout = myExpandableList.getExpandLayout();
        subscribeExpandLayout = subscribeExpandableList.getExpandLayout();
        myRecyclerView = myExpandableList.getRecyclerView();
        subsrcibeRecyclerView = subscribeExpandableList.getRecyclerView();
        myExpandableList.initExpand(true);
        subscribeExpandableList.initExpand(true);
        if (getIntent() != null) {
            userId = getIntent().getIntExtra(BaseFragment.EXTRA_USERID, -1);
        }
        initView();
        initSwipeRefresh();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mFavoritesList = getData();
        myExpandableList.setCount(mFavoritesList.size());
        myAdapter.setFavoritesList(mFavoritesList);
    }

    void initView() {
        mTitleBar.setLeftButtonClickListener(v -> finish());
        mTitleBar.setRightButtonClickListener(v -> {
            Intent intent = new Intent(FavoritesListActivity.this, CreateFavoritesActivity.class);
            startActivity(intent);
        });
    }

    List<Favorites> getData() {
        List<Favorites> favorites = UserProfileRequest.getFavorites(userId);
        for (Favorites favor : favorites)
            favor.setUser(UserProfileRequest.info(favor.getUserId()));
        favorites.add(0, getDefaultFavorites());
        return favorites;
    }

    Favorites getDefaultFavorites() {
        Favorites favorites = new Favorites();
        favorites.setName("默认收藏夹");
        favorites.setUserId(userId);
        favorites.setPublic(SettingRequest.isDefaultFavoritesPublic());
        return favorites;
    }

    void initRecyclerView() {
        mFavoritesList = getData();
        myExpandableList.setCount(mFavoritesList.size());
        myAdapter = new FavoritesListAdapter(FavoritesListActivity.this, mFavoritesList).setOnItemCountChangedListener(FavoritesListActivity.this);
        myRecyclerView.setAdapter(myAdapter);
        myRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        myRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    void initSwipeRefresh() {
        mSwipeRefreshLayout = findViewById(R.id.activity_favorites_list_swipe_refresh);
        mSwipeRefreshLayout.setEnableOverScrollDrag(true);
        mSwipeRefreshLayout.setEnableOverScrollBounce(true);
        mSwipeRefreshLayout.setEnableLoadMore(false);
        mSwipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {
    }

    void updateMyExpandList(int count) {
        myExpandableList.setCount(count);
        RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) myExpandLayout.getLayoutParams();
        layoutParams.height = RelativeLayout.LayoutParams.WRAP_CONTENT;
        myExpandLayout.setLayoutParams(layoutParams);
        myExpandLayout.setViewDimensions();
    }

    @Override
    public void onItemCountChanged(int newCount) {
        updateMyExpandList(newCount);
    }
}