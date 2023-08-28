/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:05:46
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.activity.profile;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.create.CreateFavoritesActivity;
import com.cloudchewie.ingenuity.activity.global.BaseActivity;
import com.cloudchewie.ingenuity.adapter.discover.CollectionListAdapter;
import com.cloudchewie.ingenuity.bean.ListBottomSheetBean;
import com.cloudchewie.ingenuity.entity.Content;
import com.cloudchewie.ingenuity.entity.Favorites;
import com.cloudchewie.ingenuity.request.FavoritesCreationRequest;
import com.cloudchewie.ingenuity.request.FavoritesProfileRequest;
import com.cloudchewie.ingenuity.request.SettingRequest;
import com.cloudchewie.ingenuity.request.UserProfileRequest;
import com.cloudchewie.ingenuity.util.basic.DomainUtil;
import com.cloudchewie.ingenuity.util.decoration.DividerItemDecoration;
import com.cloudchewie.ingenuity.util.image.CornerTransformation;
import com.cloudchewie.ingenuity.util.image.ImageViewInfo;
import com.cloudchewie.ingenuity.util.image.NineGridUtil;
import com.cloudchewie.ingenuity.util.system.AppSharedPreferenceUtil;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;
import com.cloudchewie.ui.custom.HorizontalIconTextItem;
import com.cloudchewie.ui.custom.MyDialog;
import com.cloudchewie.ui.custom.VerticalIconTextItem;
import com.cloudchewie.ui.general.RoundImageView;
import com.cloudchewie.util.listener.AppBarStateChangeListener;
import com.google.android.material.appbar.AppBarLayout;
import com.previewlibrary.GPreviewBuilder;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class FavoritesDetailActivity extends BaseActivity implements View.OnClickListener {
    boolean isFirstEnter = true;
    boolean isDefault = false;
    Favorites favorites;
    List<Content> contentList;
    CollectionListAdapter postAdapter;
    RecyclerView collectionRecyclerView;
    RefreshLayout swipeRefreshLayout;
    RoundImageView coverView;
    TextView nameView;
    TextView authorView;
    TextView describeView;
    ImageView moreView;
    ImageView searchView;
    AppBarLayout appBarLayout;
    VerticalIconTextItem collectView;
    VerticalIconTextItem thumbupView;
    VerticalIconTextItem visitedView;
    HorizontalIconTextItem itemCountView;
    ConstraintLayout statisticsLayout;
    TextView titleView;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            contentList.addAll((List<Content>) msg.obj);
            postAdapter.notifyItemInserted(contentList.size());
        }
    };
    Runnable getMoreData = () -> {
        Message message = handler.obtainMessage();
        message.obj = DomainUtil.getPostList(this);
        handler.sendMessage(message);
        swipeRefreshLayout.finishLoadMore();
    };

    void search() {

    }

    List<Content> getData() {
        return new ArrayList<>();
    }

    void setData(Favorites favor) {
        if (favorites == null)
            return;
        nameView.setText(favorites.getName());
        describeView.setText("简介:" + favorites.getDescription());
        Glide.with(this).load(favorites.getCover()).apply(RequestOptions.errorOf(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background).transform(CornerTransformation.getTransform(this, 5, true, true, true, true))).into((ImageView) findViewById(R.id.favorites_detail_cover));
        if (favorites.getUser() != null)
            authorView.setText("创建者:" + favorites.getUser().getNickname());
        else {
            authorView.setVisibility(View.GONE);
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) authorView.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.favorites_detail_cover);
            authorView.setLayoutParams(layoutParams);
        }
        if (isDefault) {
            RelativeLayout.LayoutParams layoutParams = (RelativeLayout.LayoutParams) authorView.getLayoutParams();
            layoutParams.addRule(RelativeLayout.ALIGN_BOTTOM, R.id.favorites_detail_cover);
            authorView.setLayoutParams(layoutParams);
            describeView.setVisibility(View.GONE);
            statisticsLayout.setVisibility(View.GONE);
        }
        itemCountView.setText(String.valueOf(favorites.getItemCount()));
        collectView.setText(String.valueOf(favorites.getFollowCount()));
        thumbupView.setText(String.valueOf(favorites.getThumbupCount()));
        visitedView.setText(String.valueOf(favorites.getVisitedCount()));
        if (favorites.getUserId() == AppSharedPreferenceUtil.getUserId(this)) {
            collectView.setIconColor(getColor(R.color.color_selector_icon));
            collectView.setEnabled(false);
        }
    }

    void showOperationBottomSheet() {
        if (isDefault) {
            List<String> strings = Arrays.asList(getResources().getStringArray(R.array.favorites_operations_without_delete));
            ListBottomSheet bottomSheet = new ListBottomSheet(this, ListBottomSheetBean.strToBean(strings));
            bottomSheet.setOnItemClickedListener(pos -> {
                if (pos == 0) {
                    Intent intent = new Intent(this, CreateFavoritesActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable(CreateFavoritesActivity.EXTRA_FAVORITES, favorites);
                    bundle.putBoolean(CreateFavoritesActivity.EXTRA_DEFAULT, true);
                    intent.putExtras(bundle);
                    startActivity(intent);
                }
                bottomSheet.dismiss();
            });
            bottomSheet.show();
        } else {
            List<String> strings = Arrays.asList(getResources().getStringArray(R.array.favorites_operations));
            ListBottomSheet bottomSheet = new ListBottomSheet(FavoritesDetailActivity.this, ListBottomSheetBean.strToBean(strings));
            bottomSheet.setOnItemClickedListener(pos -> {
                if (pos == 0) {
                    Intent createIntent = new Intent(FavoritesDetailActivity.this, CreateFavoritesActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("favorites", favorites);
                    createIntent.putExtras(bundle);
                    startActivity(createIntent);
                } else if (pos == 1) {
                    MyDialog dialog = new MyDialog(FavoritesDetailActivity.this);
                    dialog.setTitle("删除收藏夹");
                    dialog.setMessage("确定要删除收藏夹《" + favorites.getName() + "》吗?");
                    dialog.setPositive("确认");
                    dialog.setNegtive("取消");
                    dialog.setOnClickBottomListener(new MyDialog.OnClickBottomListener() {
                        @Override
                        public void onPositiveClick() {
                            FavoritesCreationRequest.delete(favorites.getFolderId());
                            finish();
                            dialog.dismiss();
                        }

                        @Override
                        public void onNegtiveClick() {
                            dialog.dismiss();
                        }

                        @Override
                        public void onCloseClick() {
                            dialog.dismiss();
                        }
                    });
                    dialog.show();
                }
                bottomSheet.dismiss();
            });
            bottomSheet.show();
        }
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_favorites_detail);
        nameView = findViewById(R.id.favorites_detail_name);
        titleView = findViewById(R.id.favorites_detail_title);
        authorView = findViewById(R.id.favorites_detail_author);
        describeView = findViewById(R.id.favorites_detail_describe);
        statisticsLayout = findViewById(R.id.favorites_detail_statistics_layout);
        itemCountView = findViewById(R.id.favorites_detail_count);
        thumbupView = findViewById(R.id.favorites_detail_thumbup_count);
        visitedView = findViewById(R.id.favorites_detail_visitor_count);
        collectView = findViewById(R.id.favorites_detail_collection_count);
        swipeRefreshLayout = findViewById(R.id.favorites_detail_swipe_refresh);
        appBarLayout = findViewById(R.id.favorites_detail_appbar);
        Intent intent = this.getIntent();
        if (intent != null) {
            favorites = (Favorites) intent.getSerializableExtra(CreateFavoritesActivity.EXTRA_FAVORITES);
            isDefault = intent.getBooleanExtra(CreateFavoritesActivity.EXTRA_DEFAULT, false);
        }
        setData(favorites);
        findViewById(R.id.favorites_detail_back).setOnClickListener(v -> finish());
        findViewById(R.id.favorites_detail_search).setOnClickListener(v -> search());
        findViewById(R.id.favorites_detail_more).setOnClickListener(v -> showOperationBottomSheet());
        findViewById(R.id.favorites_detail_cover).setOnClickListener(v -> {
            List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
            ImageViewInfo mCoverInfo = new ImageViewInfo(favorites.getCover());
            mCoverInfo.setBounds(NineGridUtil.getBounds((ImageView) findViewById(R.id.favorites_detail_cover)));
            mThumbViewInfoList.add(mCoverInfo);
            GPreviewBuilder.from(FavoritesDetailActivity.this).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
        appBarLayout.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int offset) {
                if (state == State.EXPANDED) {
                    titleView.setText("收藏夹详情");
                } else if (state == State.COLLAPSED) {
                    titleView.setText(favorites.getName());
                }
            }
        });
        initSwipeRefresh();
        initRecyclerView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!isFirstEnter) {
            if (isDefault) {
                favorites.setPublic(SettingRequest.isDefaultFavoritesPublic());
            } else {
                favorites = FavoritesProfileRequest.info(favorites.getFolderId());
                if (favorites != null)
                    favorites.setUser(UserProfileRequest.info(favorites.getUserId()));
            }
            setData(favorites);
        }
        if (isFirstEnter) isFirstEnter = false;
//        collectionRecyclerView.post(() -> postAdapter.setData(DomainUtil.getPostList(FavoritesDetailActivity.this)));
    }

    void initSwipeRefresh() {
        swipeRefreshLayout.setEnableOverScrollDrag(false);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);
        swipeRefreshLayout.setDisableContentWhenLoading(true);
        swipeRefreshLayout.setEnableRefresh(false);
        swipeRefreshLayout.setOnLoadMoreListener(v -> handler.post(getMoreData));
    }

    void initRecyclerView() {
        contentList = new ArrayList<>();
        collectionRecyclerView = findViewById(R.id.favorites_detail_recyclerview);
        postAdapter = new CollectionListAdapter(this, contentList);
        collectionRecyclerView.setAdapter(postAdapter);
        collectionRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        collectionRecyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
    }

    @Override
    public void onClick(View view) {
    }
}