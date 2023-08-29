/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:42:52
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.fragment.nav;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.global.WebViewActivity;
import com.cloudchewie.ingenuity.activity.settings.AboutActivity;
import com.cloudchewie.ingenuity.activity.settings.FeedbackActivity;
import com.cloudchewie.ingenuity.activity.settings.HelpActivity;
import com.cloudchewie.ingenuity.activity.settings.SettingsActivity;
import com.cloudchewie.ingenuity.activity.settings.ThemeActivity;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.ingenuity.request.UserAuthRequest;
import com.cloudchewie.ingenuity.util.enumeration.EventBusCode;
import com.cloudchewie.ingenuity.util.system.AppSharedPreferenceUtil;
import com.cloudchewie.ui.ThemeUtil;
import com.cloudchewie.ui.custom.VerticalIconTextItem;
import com.cloudchewie.ui.general.CircleImageView;
import com.cloudchewie.util.ui.DarkModeUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class UserFragment extends Fragment implements View.OnClickListener {
    User user;
    View mainView;
    TextView usernameView;
    CircleImageView avatarView;
    RefreshLayout swipeRefreshLayout;
    ConstraintLayout avatarLayout;
    //Application
    VerticalIconTextItem dayNightEntry;
    VerticalIconTextItem themeEntry;
    VerticalIconTextItem settingEntry;
    VerticalIconTextItem aboutEntry;
    VerticalIconTextItem helpEntry;
    VerticalIconTextItem feedbackEntry;
    VerticalIconTextItem githubEntry;
    VerticalIconTextItem blogEntry;
    VerticalIconTextItem homeEntry;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
        }
    };
    Runnable getRefreshDatas = () -> {
        Message message = handler.obtainMessage();
        checkLogin();
        swipeRefreshLayout.finishRefresh();
        handler.sendMessage(message);
    };

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(requireContext(), R.layout.fragment_user, null);
        StatusBarUtil.setStatusBarMarginTop(mainView.findViewById(R.id.fragment_user_titlebar), 0, StatusBarUtil.getStatusBarHeight(getActivity()), 0, 0);
        avatarView = mainView.findViewById(R.id.fragment_user_avatar);
        usernameView = mainView.findViewById(R.id.fragment_user_username);
        avatarLayout = mainView.findViewById(R.id.fragment_user_avatar_layout);
        avatarLayout.setOnClickListener(this);
        avatarView.setOnClickListener(this);
        usernameView.setOnClickListener(this);
        dayNightEntry = mainView.findViewById(R.id.fragment_user_entry_switch_daynight);
        themeEntry = mainView.findViewById(R.id.fragment_user_entry_theme);
        settingEntry = mainView.findViewById(R.id.fragment_user_entry_setting);
        aboutEntry = mainView.findViewById(R.id.fragment_user_entry_about);
        helpEntry = mainView.findViewById(R.id.fragment_user_entry_help);
        feedbackEntry = mainView.findViewById(R.id.fragment_user_entry_feedback);
        githubEntry = mainView.findViewById(R.id.fragment_user_entry_github);
        blogEntry = mainView.findViewById(R.id.fragment_user_entry_blog);
        homeEntry = mainView.findViewById(R.id.fragment_user_entry_home);
        dayNightEntry.setOnClickListener(this);
        themeEntry.setOnClickListener(this);
        settingEntry.setOnClickListener(this);
        aboutEntry.setOnClickListener(this);
        helpEntry.setOnClickListener(this);
        feedbackEntry.setOnClickListener(this);
        githubEntry.setOnClickListener(this);
        blogEntry.setOnClickListener(this);
        homeEntry.setOnClickListener(this);
        mainView.findViewById(R.id.fragment_user_username).setOnClickListener(this);
        mainView.findViewById(R.id.fragment_user_avatar).setOnClickListener(this);
        initSwipeRefresh();
        checkLogin();
        initEvent();
        return mainView;
    }

    void initEvent() {
        LiveEventBus.get(EventBusCode.CHANGE_AUTO_DAYNIGHT.getKey(), String.class).observe(this, s -> {
            if (AppSharedPreferenceUtil.isAutoDaynight(requireContext())) {
                dayNightEntry.setVisibility(View.GONE);
            } else {
                dayNightEntry.setVisibility(View.VISIBLE);
            }
            if (DarkModeUtil.isDarkMode(getContext())) {
                dayNightEntry.setText("浅色模式");
            } else {
                dayNightEntry.setText("深色模式");
            }
        });
        if (AppSharedPreferenceUtil.isAutoDaynight(requireContext())) {
            dayNightEntry.setVisibility(View.GONE);
        } else {
            dayNightEntry.setVisibility(View.VISIBLE);
        }
        if (DarkModeUtil.isDarkMode(getContext())) {
            dayNightEntry.setText("浅色模式");
        } else {
            dayNightEntry.setText("深色模式");
        }
    }

    void checkLogin() {
        if (AppSharedPreferenceUtil.isLogin(requireContext())) {
            user = UserAuthRequest.info();
            if (user != null) {
                usernameView.setText(user.getNickname());
                Glide.with(requireContext()).load(user.getAvatar()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(avatarView);
            } else {
                usernameView.setText("点击登录");
            }
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        checkLogin();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_user_swipe_refresh);
        swipeRefreshLayout.setRefreshHeader(new MaterialHeader(requireContext()).setColorSchemeColors(ThemeUtil.getPrimaryColor(requireContext())).setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.card_background)).setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.card_background)));
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setOnRefreshListener(v -> handler.post(getRefreshDatas));
    }

    @Override
    public void onClick(View view) {
        if (view == dayNightEntry) {
            if (DarkModeUtil.isDarkMode(getContext())) {
                DarkModeUtil.switchToAlwaysLightMode();
                AppSharedPreferenceUtil.setNight(requireContext(), false);
                dayNightEntry.setText("深色模式");
            } else {
                DarkModeUtil.switchToAlwaysDarkMode();
                AppSharedPreferenceUtil.setNight(requireContext(), true);
                dayNightEntry.setText("浅色模式");
            }
        } else if (view == settingEntry) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
        } else if (view == aboutEntry) {
            Intent intent = new Intent(getActivity(), AboutActivity.class);
            startActivity(intent);
        } else if (view == feedbackEntry) {
            Intent intent = new Intent(getActivity(), FeedbackActivity.class);
            startActivity(intent);
        } else if (view == helpEntry) {
            Intent intent = new Intent(getActivity(), HelpActivity.class);
            startActivity(intent);
        } else if (view == themeEntry) {
            Intent intent = new Intent(getActivity(), ThemeActivity.class);
            startActivity(intent);
        } else if (view == githubEntry) {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("url", "https://github.com/Robert-Stackflow");
            startActivity(intent);
        } else if (view == blogEntry) {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("url", "https://blog.cloudchewie.com");
            startActivity(intent);
        } else if (view == homeEntry) {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("url", "https://www.cloudchewie.com");
            startActivity(intent);
        }
//        if (!AppSharedPreferenceUtil.isLogin(requireContext()) || user == null) {
//            Intent intent = new Intent(getActivity(), LoginActivity.class);
//            startActivity(intent);
//        }
    }
}
