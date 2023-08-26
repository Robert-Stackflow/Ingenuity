package com.cloudchewie.ingenuity.activity.user;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.global.BaseActivity;
import com.cloudchewie.ingenuity.bean.ListBottomSheetBean;
import com.cloudchewie.ingenuity.entity.Setting;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.ingenuity.request.SettingRequest;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;
import com.cloudchewie.ui.custom.EntryItem;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PersonalSpaceSettingActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    User user;
    Setting setting;
    EntryItem presentFollowingEntry;
    EntryItem presentFansEntry;
    EntryItem presentWantEntry;
    EntryItem presentGoneEntry;
    EntryItem presentLocationEntry;
    EntryItem presentFavoritesEntry;
    List<String> typeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_personal_space_setting);
        ((TitleBar) findViewById(R.id.activity_personal_space_setting_titlebar)).setLeftButtonClickListener(v -> finish());
        presentFollowingEntry = findViewById(R.id.entry_present_following);
        presentFansEntry = findViewById(R.id.entry_present_fans);
        presentWantEntry = findViewById(R.id.entry_present_want);
        presentGoneEntry = findViewById(R.id.entry_present_gone);
        presentLocationEntry = findViewById(R.id.entry_present_location);
        presentFavoritesEntry = findViewById(R.id.entry_present_favorites);
        presentFavoritesEntry.setOnClickListener(this);
        presentLocationEntry.setOnClickListener(this);
        presentFollowingEntry.setOnClickListener(this);
        presentFansEntry.setOnClickListener(this);
        presentWantEntry.setOnClickListener(this);
        presentGoneEntry.setOnClickListener(this);
        initView();
        initData();
        initSwipeRefresh();
    }

    void initView() {
        Intent intent = getIntent();
        if (intent != null) user = (User) intent.getSerializableExtra("user");
        typeList = Arrays.asList(getResources().getStringArray(R.array.edit_permission));
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_personal_space_setting_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    void initData() {
        setting = SettingRequest.info(user != null ? user.getUserId() : 0);
        presentLocationEntry.setTipText(typeList.get(setting.getLocationVisible()));
        presentFollowingEntry.setTipText(typeList.get(setting.getFollowVisible()));
        presentFansEntry.setTipText(typeList.get(setting.getFansVisible()));
        presentWantEntry.setTipText(typeList.get(setting.getWantVisible()));
        presentGoneEntry.setTipText(typeList.get(setting.getVisitedVisible()));
        presentFavoritesEntry.setTipText(typeList.get(setting.getFavoritesVisible()));
    }

    @SuppressLint("NonConstantResourceId")
    void updateStatus(EntryItem entryItem, int position) {
        if (setting != null) {
            switch (entryItem.getId()) {
                case R.id.entry_present_fans:
                    setting.setFansVisible((byte) position);
                    SettingRequest.update(setting);
                    break;
                case R.id.entry_present_following:
                    setting.setFollowVisible((byte) position);
                    SettingRequest.update(setting);
                    break;
                case R.id.entry_present_location:
                    setting.setLocationVisible((byte) position);
                    SettingRequest.update(setting);
                    break;
                case R.id.entry_present_want:
                    setting.setWantVisible((byte) position);
                    SettingRequest.update(setting);
                    break;
                case R.id.entry_present_gone:
                    setting.setVisitedVisible((byte) position);
                    SettingRequest.update(setting);
                    break;
                case R.id.entry_present_favorites:
                    setting.setFavoritesVisible((byte) position);
                    SettingRequest.update(setting);
                    break;
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == presentFansEntry || view == presentFollowingEntry || view == presentGoneEntry || view == presentLocationEntry || view == presentWantEntry || view == presentFavoritesEntry) {
            ListBottomSheet bottomSheet = new ListBottomSheet(PersonalSpaceSettingActivity.this, ListBottomSheetBean.strToBean(typeList), true, (!typeList.contains(((EntryItem) view).getTip())) ? 0 : typeList.indexOf(((EntryItem) view).getTip()));
            bottomSheet.setOnItemClickedListener(position -> {
                EntryItem entryItem = (EntryItem) view;
                entryItem.setTipText(typeList.get(position));
                updateStatus((EntryItem) view, position);
                bottomSheet.dismiss();
            });
            bottomSheet.show();
        }
    }
}
