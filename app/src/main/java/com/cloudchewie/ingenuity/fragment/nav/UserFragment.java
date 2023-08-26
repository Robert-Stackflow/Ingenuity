/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:42:52
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.fragment.nav;

import static com.cloudchewie.ingenuity.util.basic.DomainUtil.getCity;
import static com.cloudchewie.ingenuity.util.basic.DomainUtil.getDate;
import static com.cloudchewie.ingenuity.util.basic.DomainUtil.getPhone;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.auth.LoginActivity;
import com.cloudchewie.ingenuity.activity.entry.FeedbackActivity;
import com.cloudchewie.ingenuity.activity.entry.HelpActivity;
import com.cloudchewie.ingenuity.activity.global.WebViewActivity;
import com.cloudchewie.ingenuity.activity.settings.SettingsActivity;
import com.cloudchewie.ingenuity.activity.user.PersonalSpaceActivity;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.ingenuity.request.UserAuthRequest;
import com.cloudchewie.ingenuity.util.system.SPUtil;
import com.cloudchewie.ui.custom.VerticalIconTextItem;
import com.cloudchewie.ui.general.CircleImageView;
import com.cloudchewie.util.ui.DarkModeUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class UserFragment extends Fragment implements View.OnClickListener {
    User user;
    View mainView;
    TextView usernameView;
    ImageView homePageEntry;
    CircleImageView avatarView;
    RefreshLayout swipeRefreshLayout;
    ConstraintLayout avatarLayout;
    //Profile
    VerticalIconTextItem relationEntry;
    VerticalIconTextItem followingEntry;
    VerticalIconTextItem collectionEntry;
    VerticalIconTextItem footprintEntry;
    //Creation
    RelativeLayout creationCenterEntry;
    VerticalIconTextItem worksManageEntry;
    VerticalIconTextItem draftEntry;
    VerticalIconTextItem hostEntry;
    VerticalIconTextItem dataPreviewEntry;
    //Application
    VerticalIconTextItem feedbackEntry;
    VerticalIconTextItem helpEntry;
    VerticalIconTextItem activityCenterEntry;
    VerticalIconTextItem githubEntry;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
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
        mainView = View.inflate(getContext(), R.layout.fragment_user, null);
        StatusBarUtil.setStatusBarMarginTop(mainView.findViewById(R.id.user_titlebar), 0, StatusBarUtil.getStatusBarHeight(getActivity()), 0, 0);
        mainView.findViewById(R.id.user_settings).setOnClickListener(this);
        mainView.findViewById(R.id.switch_daynight).setOnClickListener(this);
        homePageEntry = mainView.findViewById(R.id.entry_home_page);
        avatarView = mainView.findViewById(R.id.fragment_user_avatar);
        usernameView = mainView.findViewById(R.id.fragment_user_username);
        avatarLayout = mainView.findViewById(R.id.fragment_user_avatar_layout);
        avatarLayout.setOnClickListener(this);
        homePageEntry.setOnClickListener(this);
        avatarView.setOnClickListener(this);
        usernameView.setOnClickListener(this);
        relationEntry = mainView.findViewById(R.id.fragment_user_entry_profile_relation);
        followingEntry = mainView.findViewById(R.id.fragment_user_entry_profile_following);
        collectionEntry = mainView.findViewById(R.id.fragment_user_entry_profile_collection);
        footprintEntry = mainView.findViewById(R.id.fragment_user_entry_profile_footprint);
        draftEntry = mainView.findViewById(R.id.fragment_user_entry_creation_draft);
        hostEntry = mainView.findViewById(R.id.fragment_user_entry_creation_host);
        worksManageEntry = mainView.findViewById(R.id.fragment_user_entry_creation_manager);
        activityCenterEntry = mainView.findViewById(R.id.fragment_user_entry_application_activity_center);
        dataPreviewEntry = mainView.findViewById(R.id.fragment_user_entry_creation_data);
        helpEntry = mainView.findViewById(R.id.fragment_user_entry_application_help_center);
        feedbackEntry = mainView.findViewById(R.id.fragment_user_entry_application_feedback);
        githubEntry = mainView.findViewById(R.id.fragment_user_entry_application_github);
        creationCenterEntry = mainView.findViewById(R.id.fragment_user_entry_creation_top_layout);
        footprintEntry.setOnClickListener(this);
        followingEntry.setOnClickListener(this);
        relationEntry.setOnClickListener(this);
        collectionEntry.setOnClickListener(this);
        creationCenterEntry.setOnClickListener(this);
        draftEntry.setOnClickListener(this);
        hostEntry.setOnClickListener(this);
        worksManageEntry.setOnClickListener(this);
        activityCenterEntry.setOnClickListener(this);
        helpEntry.setOnClickListener(this);
        feedbackEntry.setOnClickListener(this);
        dataPreviewEntry.setOnClickListener(this);
        githubEntry.setOnClickListener(this);
        mainView.findViewById(R.id.fragment_user_username).setOnClickListener(this);
        mainView.findViewById(R.id.fragment_user_avatar).setOnClickListener(this);
        initSwipeRefresh();
        checkLogin();
        return mainView;
    }

    void checkLogin() {
        if (SPUtil.isLogin(getContext())) {
            user = UserAuthRequest.info();
            if (user != null) {
                usernameView.setText(user.getNickname());
                Glide.with(getContext()).load(user.getAvatar()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(avatarView);
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
        swipeRefreshLayout.setRefreshHeader(new MaterialHeader(getContext()).setColorSchemeColors(getResources().getColor(R.color.color_prominent)).setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.card_background)).setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.card_background)));
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setOnRefreshListener(v -> handler.post(getRefreshDatas));
    }

    @Override
    public void onClick(View view) {
        if (view == mainView.findViewById(R.id.user_settings)) {
            Intent intent = new Intent(getActivity(), SettingsActivity.class);
            startActivity(intent);
            return;
        } else if (view == mainView.findViewById(R.id.switch_daynight)) {
            ImageButton switchDaynight = (ImageButton) view;
            if (switchDaynight != null) DarkModeUtil.toggle(getActivity());
            return;
        } else if (view == feedbackEntry) {
            Intent intent = new Intent(getActivity(), FeedbackActivity.class);
            startActivity(intent);
            return;
        } else if (view == helpEntry) {
            Intent intent = new Intent(getActivity(), HelpActivity.class);
            startActivity(intent);
            return;
        } else if (view == activityCenterEntry) {

        } else if (view == githubEntry) {
            Intent intent = new Intent(getActivity(), WebViewActivity.class);
            intent.putExtra("url", "https://github.com/Robert-Stackflow");
            startActivity(intent);
            return;
        }
        if (!SPUtil.isLogin(getContext()) || user == null) {
            Intent intent = new Intent(getActivity(), LoginActivity.class);
            startActivity(intent);
        } else {
            if (view == collectionEntry) {

            }  else if (view == followingEntry) {

            } else if (view == relationEntry) {

            } else if (view == footprintEntry) {

            } else if (view == avatarView || view == usernameView || view == avatarLayout || view == homePageEntry) {
                Intent intent = new Intent(getActivity(), PersonalSpaceActivity.class);
                Bundle bundle = new Bundle();
                if (user != null) bundle.putSerializable("user", user);
                else
                    bundle.putSerializable("user", new User(((int) (Math.random() * 1000)), "Ruida", getPhone(), "", '男', getDate(), getCity()));
                intent.putExtras(bundle);
                startActivity(intent);
            } else if (view == worksManageEntry) {

            } else if (view == hostEntry) {

            } else if (view == dataPreviewEntry) {

            }
        }
    }
}
