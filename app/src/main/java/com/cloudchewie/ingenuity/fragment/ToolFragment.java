package com.cloudchewie.ingenuity.fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ui.ThemeUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

public class ToolFragment extends Fragment implements View.OnClickListener {
    View mainView;
    RefreshLayout swipeRefreshLayout;
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
        mainView = View.inflate(requireContext(), R.layout.fragment_tool, null);
        StatusBarUtil.setStatusBarMarginTop(mainView.findViewById(R.id.fragment_tool_titlebar), 0, StatusBarUtil.getStatusBarHeight(getActivity()), 0, 0);
        initSwipeRefresh();
        return mainView;
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_tool_swipe_refresh);
        swipeRefreshLayout.setRefreshHeader(new MaterialHeader(requireContext()).setColorSchemeColors(ThemeUtil.getPrimaryColor(requireContext())).setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.card_background)).setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.card_background)));
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setOnRefreshListener(v -> handler.post(getRefreshDatas));
    }

    @Override
    public void onClick(View v) {

    }
}
