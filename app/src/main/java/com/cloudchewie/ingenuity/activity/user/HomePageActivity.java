/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 15:14:10
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.activity.user;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.content.res.AppCompatResources;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.bean.ListBottomSheetBean;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.ingenuity.entity.User.FOLLOW_STATUS;
import com.cloudchewie.ingenuity.fragment.global.BaseFragment;
import com.cloudchewie.ingenuity.request.UserFollowingRequest;
import com.cloudchewie.ingenuity.request.UserOperationRequest;
import com.cloudchewie.ingenuity.request.UserProfileRequest;
import com.cloudchewie.ingenuity.util.image.ImageViewInfo;
import com.cloudchewie.ingenuity.util.image.NineGridUtil;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;
import com.cloudchewie.ui.custom.HorizontalIconTextItem;
import com.cloudchewie.ui.custom.VerticalIconTextItem;
import com.cloudchewie.ui.general.IToast;
import com.cloudchewie.util.listener.AppBarStateChangeListener;
import com.cloudchewie.util.ui.DarkModeUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;
import com.previewlibrary.GPreviewBuilder;
import com.zackratos.ultimatebarx.ultimatebarx.java.UltimateBarX;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomePageActivity extends AppCompatActivity {
    private User mUser;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    private ImageViewInfo mAvatarInfo;
    private ImageViewInfo mBackGroundInfo;
    private ImageViewInfo mSmallAvatarInfo;
    private FOLLOW_STATUS currentStatus;
    //基本控件
    private AppBarLayout mAppBar;
    private ImageView mBackButton;
    private ImageView mMoreButton;
    private ImageView mBackGroundView;
    private ImageView mAvatarView;
    private TextView mUsernameView;
    private ImageView mSmallAvatarView;
    private ConstraintLayout mTitleBar;
    private ConstraintLayout mTitleBar2;
    private RelativeLayout mMainLayout;
    private TextView mSmallUsernameView;
    private HorizontalIconTextItem mLocationView;
    private HorizontalIconTextItem mGenderView;
    private Button sendButton;
    private Button followButton;
    private VerticalIconTextItem followEntry;
    private VerticalIconTextItem fansEntry;
    private VerticalIconTextItem thumbupEntry;
    private VerticalIconTextItem visitedEntry;
    private VerticalIconTextItem wantEntry;
    private ConstraintLayout mContentBar;
    //主要控件
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private HomePageFragmentAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_page);
        mAppBar = findViewById(R.id.home_page_appbar);
        mBackButton = findViewById(R.id.home_page_back);
        mMoreButton = findViewById(R.id.home_page_more);
        mBackGroundView = findViewById(R.id.home_page_background);
        mAvatarView = findViewById(R.id.home_page_avatar);
        mUsernameView = findViewById(R.id.home_page_username);
        mSmallAvatarView = findViewById(R.id.home_page_small_avatar);
        mSmallUsernameView = findViewById(R.id.home_page_small_username);
        mTabLayout = findViewById(R.id.home_page_content_tab_layout);
        mViewPager = findViewById(R.id.home_page_content_viewpager);
        mLocationView = findViewById(R.id.home_page_ipaddress);
        mGenderView = findViewById(R.id.home_page_gender);
        mTitleBar = findViewById(R.id.home_page_titlebar);
        mTitleBar2 = findViewById(R.id.home_page_titlebar_2);
        mMainLayout = findViewById(R.id.home_page_layout);
        mContentBar = findViewById(R.id.home_page_content_bar);
        sendButton = findViewById(R.id.home_page_sendmessage);
        followButton = findViewById(R.id.home_page_follow);
        followEntry = findViewById(R.id.home_page_follow_count);
        fansEntry = findViewById(R.id.home_page_fans_count);
        thumbupEntry = findViewById(R.id.home_page_thumbup_count);
        visitedEntry = findViewById(R.id.home_page_visited_count);
        wantEntry = findViewById(R.id.home_page_want_count);
        initView();
        initTabLayout();
        initViewPager();
        initGlide();
        loadStatus();
    }

    void initGlide() {
        Glide.with(HomePageActivity.this).load(mAvatarInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mAvatarView);
        Glide.with(HomePageActivity.this).load(mAvatarInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mSmallAvatarView);
        Glide.with(HomePageActivity.this).load(mBackGroundInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mBackGroundView);
    }

    void initView() {
        Intent intent = this.getIntent();
        mUser = (User) intent.getSerializableExtra("user");
        mAvatarInfo = new ImageViewInfo(mUser.getAvatar());
        mSmallAvatarInfo = new ImageViewInfo(mUser.getAvatar());
        mBackGroundInfo = new ImageViewInfo(mUser.getBackground());
        mBackButton.setOnClickListener(v -> finish());
        findViewById(R.id.home_page_back_2).setOnClickListener(v -> finish());
        mMoreButton.setOnClickListener(v -> {
            Intent settingIntent = new Intent(HomePageActivity.this, HomePageSettingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", mUser);
            settingIntent.putExtras(bundle);
            startActivity(settingIntent);
        });
        mBackGroundView.setOnClickListener(v -> {
            List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
            mBackGroundInfo.setBounds(NineGridUtil.getBounds(mBackGroundView));
            mThumbViewInfoList.add(mBackGroundInfo);
            GPreviewBuilder.from(HomePageActivity.this).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
        mAvatarView.setOnClickListener(v -> {
            List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
            mAvatarInfo.setBounds(NineGridUtil.getBounds(mAvatarView));
            mThumbViewInfoList.add(mAvatarInfo);
            GPreviewBuilder.from(HomePageActivity.this).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
        mSmallAvatarView.setOnClickListener(v -> {
            List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
            mSmallAvatarInfo.setBounds(NineGridUtil.getBounds(mSmallAvatarView));
            mThumbViewInfoList.add(mSmallAvatarInfo);
            GPreviewBuilder.from(HomePageActivity.this).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
        mUsernameView.setText(mUser.getNickname());
        mSmallUsernameView.setText(mUser.getNickname());
        mGenderView.setText(String.valueOf(mUser.getGender()));
        mLocationView.setText(mUser.getCurrentLocation());
        UltimateBarX.statusBarOnly(this).fitWindow(true).transparent().apply();
        UltimateBarX.addStatusBarTopPadding(mTitleBar);
        UltimateBarX.addStatusBarTopPadding(mTitleBar2);
        UltimateBarX.addStatusBarTopPadding(mContentBar);
        StatusBarUtil.setStatusBarTextColor(this, DarkModeUtil.isDarkMode(this));
        mAppBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int offset) {
                if (state == State.EXPANDED) {
                    mTitleBar2.setAlpha(1f);
                    mTitleBar2.setVisibility(View.GONE);
                } else if (state == State.COLLAPSED) {
                    mTitleBar2.setAlpha(1f);
                    mTitleBar2.setVisibility(View.VISIBLE);
                } else {
                    mTitleBar2.setVisibility(View.VISIBLE);
                    mTitleBar2.setAlpha(1f * Math.abs(offset) / appBarLayout.getTotalScrollRange());
                }
            }
        });
        followEntry.setBigText(String.valueOf(UserFollowingRequest.getFollowingUsers(mUser.getUserId(), UserFollowingRequest.FOLLOW_TYPE.ALL).size()));
        fansEntry.setBigText(String.valueOf(UserProfileRequest.getFans(mUser.getUserId()).size()));
        thumbupEntry.setBigText(String.valueOf(UserFollowingRequest.getFollowingUsers(mUser.getUserId(), UserFollowingRequest.FOLLOW_TYPE.ALL).size()));
        visitedEntry.setBigText(String.valueOf(UserProfileRequest.getVisitedAttractions(mUser.getUserId()).size()));
        wantEntry.setBigText(String.valueOf(UserProfileRequest.getWantAttractions(mUser.getUserId()).size()));
        followEntry.setOnClickListener(v -> {

        });
        fansEntry.setOnClickListener(v -> {

        });
        visitedEntry.setOnClickListener(v -> {

        });
        wantEntry.setOnClickListener(v -> {

        });
        thumbupEntry.setOnClickListener(v -> IToast.showBottom(HomePageActivity.this, mUser.getNickname() + "共获得" + thumbupEntry.getBigText() + "个赞"));
        followButton.setOnClickListener(v -> {
            List<String> strings;
            ListBottomSheet bottomSheet;
            switch (currentStatus) {
                case UNFOLLOW:
                    setFollowStatus(FOLLOW_STATUS.FOLLOW);
                    UserOperationRequest.follow(mUser.getUserId());
                    IToast.showBottom(HomePageActivity.this, "关注成功");
                    break;
                case FOLLOW:
                    strings = Arrays.asList(getResources().getStringArray(R.array.follow_operations));
                    bottomSheet = new ListBottomSheet(HomePageActivity.this, ListBottomSheetBean.strToBean(strings));
                    bottomSheet.setOnItemClickedListener(position -> {
                        if (position == 0) {
                            setFollowStatus(FOLLOW_STATUS.SPECIAL);
                            UserOperationRequest.special(mUser.getUserId());
                        } else if (position == 1) {
                            setFollowStatus(FOLLOW_STATUS.UNFOLLOW);
                            UserOperationRequest.unFollow(mUser.getUserId());
                        }
                        bottomSheet.dismiss();
                    });
                    bottomSheet.show();
                    break;
                case FOLLOW_FRIENDS:
                    strings = Arrays.asList(getResources().getStringArray(R.array.follow_operations));
                    bottomSheet = new ListBottomSheet(HomePageActivity.this, ListBottomSheetBean.strToBean(strings));
                    bottomSheet.setOnItemClickedListener(position -> {
                        if (position == 0) {
                            setFollowStatus(FOLLOW_STATUS.SPECIAL_FRIENDS);
                            UserOperationRequest.special(mUser.getUserId());
                        } else if (position == 1) {
                            setFollowStatus(FOLLOW_STATUS.UNFOLLOW);
                            UserOperationRequest.unFollow(mUser.getUserId());
                        }
                        bottomSheet.dismiss();
                    });
                    bottomSheet.show();
                    break;
                case SPECIAL:
                    strings = Arrays.asList(getResources().getStringArray(R.array.special_operations));
                    bottomSheet = new ListBottomSheet(HomePageActivity.this, ListBottomSheetBean.strToBean(strings));
                    bottomSheet.setOnItemClickedListener(position -> {
                        if (position == 0) {
                            setFollowStatus(FOLLOW_STATUS.FOLLOW);
                            UserOperationRequest.follow(mUser.getUserId());
                            IToast.showBottom(HomePageActivity.this, "取消特别关注");
                        } else if (position == 1) {
                            setFollowStatus(FOLLOW_STATUS.UNFOLLOW);
                            UserOperationRequest.unFollow(mUser.getUserId());
                        }
                        bottomSheet.dismiss();
                    });
                    bottomSheet.show();
                    break;
                case SPECIAL_FRIENDS:
                    strings = Arrays.asList(getResources().getStringArray(R.array.special_operations));
                    bottomSheet = new ListBottomSheet(HomePageActivity.this, ListBottomSheetBean.strToBean(strings));
                    bottomSheet.setOnItemClickedListener(position -> {
                        if (position == 0) {
                            setFollowStatus(FOLLOW_STATUS.FOLLOW_FRIENDS);
                            UserOperationRequest.follow(mUser.getUserId());
                            IToast.showBottom(HomePageActivity.this, "取消特别关注");
                        } else if (position == 1) {
                            setFollowStatus(FOLLOW_STATUS.UNFOLLOW);
                            UserOperationRequest.unFollow(mUser.getUserId());
                        }
                        bottomSheet.dismiss();
                    });
                    bottomSheet.show();
                    break;
                case BLOCK:
                    break;
            }
            v.postDelayed(this::loadStatus, 100);
        });
    }

    void initTabLayout() {
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.home_page_tab_titles));
        mFragments.add(new BaseFragment());
        mFragments.add(new BaseFragment());
        mFragments.add(new BaseFragment());
        mAdapter = new HomePageFragmentAdapter(getSupportFragmentManager(), getLifecycle(), mFragments);
        mViewPager.setAdapter(mAdapter);
        new TabLayoutMediator(mTabLayout, mViewPager, (tab, position) -> tab.setText(mTitles.get(position))).attach();
        for (int i = 0; i < mTabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = mTabLayout.getTabAt(i);
            if (tab != null) {
                tab.view.setLongClickable(false);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    tab.view.setTooltipText(null);
                }
            }
        }
    }

    void setFollowStatus(FOLLOW_STATUS newStatus) {
        currentStatus = newStatus;
        updateFollowButton();
    }

    void loadStatus() {
        int status = UserProfileRequest.getRelation(mUser.getUserId());
        if (status >= 0 && status < FOLLOW_STATUS.values().length)
            setFollowStatus(FOLLOW_STATUS.values()[status]);
    }

    void updateFollowButton() {
        switch (currentStatus) {
            case UNFOLLOW:
                followButton.setText("关注");
                followButton.setBackgroundTintList(getColorStateList(R.color.color_selector_prominent));
                break;
            case FOLLOW:
                followButton.setText("已关注");
                followButton.setBackgroundTintList(getColorStateList(R.color.color_selector_gray));
                break;
            case SPECIAL:
                followButton.setText("特别关注");
                followButton.setBackgroundTintList(getColorStateList(R.color.color_selector_gray));
                break;
            case FOLLOW_FRIENDS:
            case SPECIAL_FRIENDS:
                followButton.setText("互相关注");
                followButton.setBackgroundTintList(getColorStateList(R.color.color_selector_gray));
                break;
            case BLOCK:
                followButton.setText("取消黑名单");
                followButton.setTextColor(getColor(R.color.color_accent));
                followButton.setBackground(AppCompatResources.getDrawable(this, R.drawable.shape_button_round_frame));
                followButton.setBackgroundTintList(ColorStateList.valueOf(getColor(R.color.color_red)));
                break;
        }
    }

    void initViewPager() {
        mViewPager.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                View view = mFragments.get(position).getView();
                updatePagerHeightForChild(view, mViewPager);
            }

            private void updatePagerHeightForChild(View view, ViewPager2 pager) {
                if (view == null) return;
                view.post(() -> {
                    int wMeasureSpec = View.MeasureSpec.makeMeasureSpec(view.getWidth(), View.MeasureSpec.EXACTLY);
                    int hMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
                    view.measure(wMeasureSpec, hMeasureSpec);
                    if (pager.getLayoutParams().height != view.getMeasuredHeight()) {
                        ViewGroup.LayoutParams layoutParams = pager.getLayoutParams();
                        layoutParams.height = view.getMeasuredHeight();
                        pager.setLayoutParams(layoutParams);
                    }
                });
            }
        });
    }

    public class HomePageFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public HomePageFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
            super(fragmentManager, lifecycle);
            fragmentList = fragments;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}
