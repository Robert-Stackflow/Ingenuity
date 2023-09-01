package com.cloudchewie.ingenuity.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.authenticator.AuthenticatorAddActivity;
import com.cloudchewie.ingenuity.activity.authenticator.AuthenticatorSettingsActivity;
import com.cloudchewie.ingenuity.adapter.TokenListAdapter;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.cloudchewie.ingenuity.util.decoration.SpacingItemDecoration;
import com.cloudchewie.ingenuity.util.enumeration.Direction;
import com.cloudchewie.ui.ThemeUtil;
import com.cloudchewie.ui.fab.FloatingActionButton;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class AuthenticatorFragment extends Fragment implements View.OnClickListener {
    View mainView;
    RefreshLayout swipeRefreshLayout;
    FloatingActionButton addButton;
    FloatingActionButton qrcodeButton;
    FloatingActionButton lockButton;
    FloatingActionButton settingButton;
    RecyclerView recyclerView;
    TokenListAdapter adapter;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
        }
    };
    Runnable getRefreshDatas = () -> {
        Message message = handler.obtainMessage();
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
        mainView = View.inflate(requireContext(), R.layout.fragment_authenticator, null);
        StatusBarUtil.setStatusBarMarginTop(mainView.findViewById(R.id.fragment_authenticator_titlebar), 0, StatusBarUtil.getStatusBarHeight(getActivity()), 0, 0);
        addButton = mainView.findViewById(R.id.auth_add);
        qrcodeButton = mainView.findViewById(R.id.auth_qrcode);
        lockButton = mainView.findViewById(R.id.auth_lock);
        settingButton = mainView.findViewById(R.id.auth_settings);
        recyclerView = mainView.findViewById(R.id.fragment_authenticator_recyclerview);
        addButton.setOnClickListener(this);
        qrcodeButton.setOnClickListener(this);
        lockButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        initSwipeRefresh();
        adapter = new TokenListAdapter(getContext(), LocalStorage.getAppDatabase().otpTokenDao().getAll());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacingItemDecoration(getContext(), (int) getResources().getDimension(R.dimen.dp3), Direction.BOTTOM));
        return mainView;
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_authenticator_swipe_refresh);
        swipeRefreshLayout.setRefreshHeader(new MaterialHeader(requireContext()).setColorSchemeColors(ThemeUtil.getPrimaryColor(requireContext())).setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.card_background)).setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.card_background)));
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setOnRefreshListener(v -> handler.post(getRefreshDatas));
    }

    @Override
    public void onClick(View v) {
        if (v == addButton) {
            Intent intent = new Intent(getActivity(), AuthenticatorAddActivity.class).setAction(Intent.ACTION_DEFAULT);
            startActivity(intent);
        } else if (v == settingButton) {
            Intent intent = new Intent(getActivity(), AuthenticatorSettingsActivity.class).setAction(Intent.ACTION_DEFAULT);
            startActivity(intent);
        }
    }
}
