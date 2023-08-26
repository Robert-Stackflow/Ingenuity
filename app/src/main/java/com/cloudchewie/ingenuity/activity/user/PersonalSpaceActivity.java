package com.cloudchewie.ingenuity.activity.user;

import android.content.Intent;
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
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.ingenuity.fragment.global.BaseFragment;
import com.cloudchewie.ingenuity.request.UserAuthRequest;
import com.cloudchewie.ingenuity.request.UserFollowingRequest;
import com.cloudchewie.ingenuity.request.UserProfileRequest;
import com.cloudchewie.ingenuity.util.image.ImageViewInfo;
import com.cloudchewie.ingenuity.util.image.NineGridUtil;
import com.cloudchewie.ingenuity.util.system.SPUtil;
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

public class PersonalSpaceActivity extends AppCompatActivity {
    private User mUser;
    private List<String> mTitles;
    private List<Fragment> mFragments;
    private ImageViewInfo mAvatarInfo;
    private ImageViewInfo mBackGroundInfo;
    private ImageViewInfo mSmallAvatarInfo;
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
    private ConstraintLayout mContentBar;
    private Button editInfoButton;
    private Button nameCardButton;
    private VerticalIconTextItem followEntry;
    private VerticalIconTextItem fansEntry;
    private VerticalIconTextItem thumbupEntry;
    private VerticalIconTextItem visitedEntry;
    private VerticalIconTextItem wantEntry;
    //主要控件
    private TabLayout mTabLayout;
    private ViewPager2 mViewPager;
    private HomePageFragmentAdapter mAdapter;
    private AppBarStateChangeListener.State currentState;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_space);
        mAppBar = findViewById(R.id.personal_space_appbar);
        mBackButton = findViewById(R.id.personal_space_back);
        mMoreButton = findViewById(R.id.personal_space_settings);
        mBackGroundView = findViewById(R.id.personal_space_background);
        mAvatarView = findViewById(R.id.personal_space_avatar);
        mUsernameView = findViewById(R.id.personal_space_username);
        mSmallAvatarView = findViewById(R.id.personal_space_small_avatar);
        mSmallUsernameView = findViewById(R.id.personal_space_small_username);
        mTabLayout = findViewById(R.id.personal_space_content_tab_layout);
        mViewPager = findViewById(R.id.personal_space_content_viewpager);
        mLocationView = findViewById(R.id.personal_space_ipaddress);
        mGenderView = findViewById(R.id.personal_space_gender);
        mTitleBar = findViewById(R.id.personal_space_titlebar);
        mTitleBar2 = findViewById(R.id.personal_space_titlebar_2);
        mMainLayout = findViewById(R.id.personal_space_layout);
        mContentBar = findViewById(R.id.personal_space_content_bar);
        editInfoButton = findViewById(R.id.personal_space_editinfo);
        nameCardButton = findViewById(R.id.personal_space_namecard);
        followEntry = findViewById(R.id.personal_space_follow_count);
        fansEntry = findViewById(R.id.personal_space_fans_count);
        thumbupEntry = findViewById(R.id.personal_space_thumbup_count);
        visitedEntry = findViewById(R.id.personal_space_visited_count);
        wantEntry = findViewById(R.id.personal_space_want_count);
        initView();
        initTabLayout();
        initViewPager();
        loadImage();
    }

    void loadImage() {
        Glide.with(PersonalSpaceActivity.this).load(mAvatarInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mAvatarView);
        Glide.with(PersonalSpaceActivity.this).load(mAvatarInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mSmallAvatarView);
        Glide.with(PersonalSpaceActivity.this).load(mBackGroundInfo.getUrl()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(mBackGroundView);
    }

    void initView() {
        Intent intent = this.getIntent();
        mUser = (User) intent.getSerializableExtra("user");
        loadUser();
        mBackButton.setOnClickListener(v -> finish());
        findViewById(R.id.personal_space_back_2).setOnClickListener(v -> finish());
        mMoreButton.setOnClickListener(v -> {
            Intent settingIntent = new Intent(PersonalSpaceActivity.this, PersonalSpaceSettingActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", mUser);
            settingIntent.putExtras(bundle);
            startActivity(settingIntent);
        });
        mBackGroundView.setOnClickListener(v -> {
            List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
            mBackGroundInfo.setBounds(NineGridUtil.getBounds(mBackGroundView));
            mThumbViewInfoList.add(mBackGroundInfo);
            GPreviewBuilder.from(PersonalSpaceActivity.this).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
        mAvatarView.setOnClickListener(v -> {
            List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
            mAvatarInfo.setBounds(NineGridUtil.getBounds(mAvatarView));
            mThumbViewInfoList.add(mAvatarInfo);
            GPreviewBuilder.from(PersonalSpaceActivity.this).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
        mSmallAvatarView.setOnClickListener(v -> {
            List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
            mSmallAvatarInfo.setBounds(NineGridUtil.getBounds(mSmallAvatarView));
            mThumbViewInfoList.add(mSmallAvatarInfo);
            GPreviewBuilder.from(PersonalSpaceActivity.this).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
        });
        mTitleBar2.setAlpha(0f);
        mTitleBar2.setVisibility(View.GONE);
        UltimateBarX.statusBarOnly(this).fitWindow(true).transparent().apply();
        UltimateBarX.addStatusBarTopPadding(mTitleBar);
        UltimateBarX.addStatusBarTopPadding(mTitleBar2);
        UltimateBarX.addStatusBarTopPadding(mContentBar);
        StatusBarUtil.setStatusBarTextColor(this, DarkModeUtil.isDarkMode(this));
        mAppBar.addOnOffsetChangedListener(new AppBarStateChangeListener() {
            @Override
            public void onStateChanged(AppBarLayout appBarLayout, State state, int offset) {
                currentState = state;
                if (state == State.EXPANDED) {
                    mTitleBar2.setVisibility(View.GONE);
                    mTitleBar2.setAlpha(0f);
                } else if (state == State.COLLAPSED) {
                    mTitleBar2.setVisibility(View.VISIBLE);
                    mTitleBar2.setAlpha(1f);
                } else {
                    mTitleBar2.setVisibility(View.VISIBLE);
                    mTitleBar2.setAlpha(1f * Math.abs(offset) / appBarLayout.getTotalScrollRange());
                }
            }
        });
        editInfoButton.setOnClickListener(v -> {
            Intent editInfoIntent = new Intent(PersonalSpaceActivity.this, EditInfoActivity.class);
            Bundle bundle = new Bundle();
            bundle.putSerializable("user", mUser);
            editInfoIntent.putExtras(bundle);
            startActivity(editInfoIntent);
        });
        followEntry.setBigText(String.valueOf(UserFollowingRequest.getFollowingUsers(SPUtil.getUserId(this), UserFollowingRequest.FOLLOW_TYPE.ALL).size()));
        fansEntry.setBigText(String.valueOf(UserProfileRequest.getFans(SPUtil.getUserId(this)).size()));
        thumbupEntry.setBigText(String.valueOf(UserFollowingRequest.getFollowingUsers(SPUtil.getUserId(this), UserFollowingRequest.FOLLOW_TYPE.ALL).size()));
        visitedEntry.setBigText(String.valueOf(UserProfileRequest.getVisitedAttractions(SPUtil.getUserId(this)).size()));
        wantEntry.setBigText(String.valueOf(UserProfileRequest.getWantAttractions(SPUtil.getUserId(this)).size()));
        followEntry.setOnClickListener(v -> {

        });
        fansEntry.setOnClickListener(v -> {

        });
        visitedEntry.setOnClickListener(v -> {

        });
        wantEntry.setOnClickListener(v -> {

        });
        thumbupEntry.setOnClickListener(v -> {
            IToast.showBottom(PersonalSpaceActivity.this, mUser.getNickname() + "共获得" + thumbupEntry.getBigText() + "个赞");
        });
    }

    void loadUser() {
        if (mUser == null) return;
        mAvatarInfo = new ImageViewInfo(mUser.getAvatar());
        mSmallAvatarInfo = new ImageViewInfo(mUser.getAvatar());
        mBackGroundInfo = new ImageViewInfo(mUser.getBackground());
        mUsernameView.setText(mUser.getNickname());
        mSmallUsernameView.setText(mUser.getNickname());
        mGenderView.setText(String.valueOf(mUser.getGender()));
        mLocationView.setText(mUser.getCurrentLocation());
    }

    void initTabLayout() {
        mFragments = new ArrayList<>();
        mTitles = Arrays.asList(getResources().getStringArray(R.array.home_page_tab_titles));
        mFragments.add(new BaseFragment());
        mFragments.add(new BaseFragment());
        mFragments.add(new BaseFragment());
        mAdapter = new PersonalSpaceActivity.HomePageFragmentAdapter(getSupportFragmentManager(), getLifecycle(), mFragments);
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

    @Override
    protected void onResume() {
        super.onResume();
        mUser = UserAuthRequest.info();
        loadUser();
        loadImage();
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

