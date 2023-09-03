package com.cloudchewie.ingenuity.fragment;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.authenticator.AuthenticatorAddActivity;
import com.cloudchewie.ingenuity.activity.authenticator.AuthenticatorScanActivity;
import com.cloudchewie.ingenuity.activity.authenticator.AuthenticatorSettingsActivity;
import com.cloudchewie.ingenuity.adapter.TokenListAdapter;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.cloudchewie.ingenuity.util.decoration.SpacingItemDecoration;
import com.cloudchewie.ingenuity.util.enumeration.Direction;
import com.cloudchewie.ingenuity.util.enumeration.EventBusCode;
import com.cloudchewie.ui.ThemeUtil;
import com.cloudchewie.ui.custom.IToast;
import com.cloudchewie.ui.fab.FloatingActionButton;
import com.cloudchewie.ui.fab.FloatingActionMenu;
import com.cloudchewie.ui.general.BottomSheet;
import com.cloudchewie.util.system.SharedPreferenceCode;
import com.cloudchewie.util.system.SharedPreferenceUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.jeremyliao.liveeventbus.LiveEventBus;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.wei.android.lib.fingerprintidentify.FingerprintIdentify;
import com.wei.android.lib.fingerprintidentify.base.BaseFingerprint;

public class AuthenticatorFragment extends Fragment implements View.OnClickListener, BaseFingerprint.IdentifyListener, BaseFingerprint.ExceptionListener {
    View mainView;
    RefreshLayout swipeRefreshLayout;
    FloatingActionMenu fabMenu;
    FloatingActionButton addButton;
    FloatingActionButton qrcodeButton;
    FloatingActionButton lockButton;
    FloatingActionButton settingButton;
    RecyclerView recyclerView;
    TokenListAdapter adapter;
    RelativeLayout lockLayout;
    boolean isAuthed = false;
    FingerprintIdentify mFingerprintIdentify;
    BottomSheet bottomSheet;
    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
        }
    };
    Runnable getRefreshDatas = () -> {
        Message message = handler.obtainMessage();
        swipeRefreshLayout.finishRefresh();
        adapter = new TokenListAdapter(getContext(), LocalStorage.getAppDatabase().otpTokenDao().getAll());
        recyclerView.setAdapter(adapter);
        isAuthed = true;
        refreshAuthState();
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
        addButton = mainView.findViewById(R.id.fragement_authenticator_add);
        qrcodeButton = mainView.findViewById(R.id.fragement_authenticator_scan);
        fabMenu = mainView.findViewById(R.id.fragement_authenticator_menu);
        lockLayout = mainView.findViewById(R.id.fragment_authenticator_lock_layout);
        lockButton = mainView.findViewById(R.id.fragement_authenticator_lock);
        settingButton = mainView.findViewById(R.id.fragement_authenticator_settings);
        recyclerView = mainView.findViewById(R.id.fragment_authenticator_recyclerview);
        addButton.setOnClickListener(this);
        qrcodeButton.setOnClickListener(this);
        lockButton.setOnClickListener(this);
        settingButton.setOnClickListener(this);
        lockLayout.setOnClickListener(this);
        mainView.findViewById(R.id.fragment_authenticator_lock_icon).setOnClickListener(this);
        mainView.findViewById(R.id.fragment_authenticator_lock_text).setOnClickListener(this);
        initSwipeRefresh();
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        adapter = new TokenListAdapter(getContext(), LocalStorage.getAppDatabase().otpTokenDao().getAll());
        recyclerView.setAdapter(adapter);
        recyclerView.addItemDecoration(new SpacingItemDecoration(getContext(), (int) getResources().getDimension(R.dimen.dp3), Direction.BOTTOM));
        LiveEventBus.get(EventBusCode.CHANGE_TOKEN.getKey()).observe(this, s -> swipeRefreshLayout.autoRefresh());
        LiveEventBus.get(EventBusCode.CHANGE_AUTH_SHOW_CODE.getKey()).observe(this, s -> {
            isAuthed = !SharedPreferenceUtil.getBoolean(getContext(), SharedPreferenceCode.AUTH_TO_SHOW_CODE.getKey(), true);
            lockButton.setVisibility(SharedPreferenceUtil.getBoolean(getContext(), SharedPreferenceCode.AUTH_TO_SHOW_CODE.getKey(), true) ? View.VISIBLE : View.GONE);
            refreshAuthState();
        });
        isAuthed = LocalStorage.getAppDatabase().otpTokenDao().count() <= 0 || !SharedPreferenceUtil.getBoolean(getContext(), SharedPreferenceCode.AUTH_TO_SHOW_CODE.getKey(), true);
        lockButton.setVisibility(SharedPreferenceUtil.getBoolean(getContext(), SharedPreferenceCode.AUTH_TO_SHOW_CODE.getKey(), true) ? View.VISIBLE : View.GONE);
        refreshAuthState();
        initAuth();
        return mainView;
    }

    public void refreshAuthState() {
        if (isAuthed) {
            lockLayout.setVisibility(View.GONE);
            ((View) swipeRefreshLayout).setVisibility(View.VISIBLE);
            fabMenu.setVisibility(View.VISIBLE);
            if (LocalStorage.getAppDatabase().otpTokenDao().count() <= 0) {
                mainView.findViewById(R.id.fragment_authenticator_blank).setVisibility(View.VISIBLE);
            } else {
                mainView.findViewById(R.id.fragment_authenticator_blank).setVisibility(View.GONE);
            }
        } else {
            lockLayout.setVisibility(View.VISIBLE);
            ((View) swipeRefreshLayout).setVisibility(View.GONE);
            fabMenu.setVisibility(View.GONE);
            mainView.findViewById(R.id.fragment_authenticator_blank).setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        fabMenu.close(true);
        mFingerprintIdentify.cancelIdentify();
    }

    @Override
    public void onStop() {
        super.onStop();
        fabMenu.close(true);
        mFingerprintIdentify.cancelIdentify();
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_authenticator_swipe_refresh);
        swipeRefreshLayout.setRefreshHeader(new MaterialHeader(requireContext()).setColorSchemeColors(ThemeUtil.getPrimaryColor(requireContext())).setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.card_background)).setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.card_background)));
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setOnRefreshListener(v -> handler.post(getRefreshDatas));
    }

    public void initAuth() {
        mFingerprintIdentify = new FingerprintIdentify(getContext());
        mFingerprintIdentify.setSupportAndroidL(true);
        mFingerprintIdentify.setExceptionListener(this);
        mFingerprintIdentify.init();
    }

    @Override
    public void onClick(View v) {
        if (v == addButton) {
            Intent intent = new Intent(getActivity(), AuthenticatorAddActivity.class).setAction(Intent.ACTION_DEFAULT);
            startActivity(intent);
            fabMenu.close(true);
        } else if (v == settingButton) {
            Intent intent = new Intent(getActivity(), AuthenticatorSettingsActivity.class).setAction(Intent.ACTION_DEFAULT);
            startActivity(intent);
            fabMenu.close(true);
        } else if (v == qrcodeButton) {
            Intent intent = new Intent(getActivity(), AuthenticatorScanActivity.class).setAction(Intent.ACTION_DEFAULT);
            startActivity(intent);
            fabMenu.close(true);
        } else if (v == lockButton) {
            fabMenu.close(false);
            isAuthed = false;
            refreshAuthState();
        } else if (v == lockLayout || v.getId() == R.id.fragment_authenticator_lock_icon || v.getId() == R.id.fragment_authenticator_lock_text) {
            if (mFingerprintIdentify.isFingerprintEnable()) {
                bottomSheet = new BottomSheet(getContext());
                bottomSheet.setTitle(getString(R.string.verify_finger));
                bottomSheet.setDragBarVisible(false);
                bottomSheet.setLeftButtonVisible(false);
                bottomSheet.setRightButtonVisible(false);
                bottomSheet.setBackgroundColor(getResources().getColor(R.color.card_background));
                bottomSheet.setMainLayout(R.layout.layout_fingerprint);
                bottomSheet.show();
                bottomSheet.setOnCancelListener(dialogInterface -> mFingerprintIdentify.cancelIdentify());
                mFingerprintIdentify.resumeIdentify();
                mFingerprintIdentify.startIdentify(5, this);
            }
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mFingerprintIdentify.cancelIdentify();
    }

    @Override
    public void onCatchException(Throwable exception) {

    }

    @Override
    public void onSucceed() {
        mFingerprintIdentify.cancelIdentify();
        isAuthed = true;
        if (bottomSheet != null)
            bottomSheet.cancel();
        refreshAuthState();
    }

    @Override
    public void onNotMatch(int availableTimes) {
        bottomSheet.setTitle(getString(R.string.verify_finger_fail));
        bottomSheet.setDragBarVisible(false);
        bottomSheet.setTitleColor(getResources().getColor(R.color.text_color_red));
        new Handler().postDelayed(() -> {
            bottomSheet.setTitle(getString(R.string.verify_finger));
            bottomSheet.setDragBarVisible(false);
            bottomSheet.setTitleColor(getResources().getColor(R.color.color_accent));
        }, 500);
    }

    @Override
    public void onFailed(boolean isDeviceLocked) {
        IToast.showBottom(getContext(), getString(R.string.verify_finger_error));
        if (bottomSheet != null)
            bottomSheet.cancel();
    }

    @Override
    public void onStartFailedByDeviceLocked() {

    }
}
