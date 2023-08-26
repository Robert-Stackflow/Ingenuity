/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/18 15:51:03
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.fragment.global;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.cloudchewie.ingenuity.R;

import java.io.Serializable;

public class BaseFragment extends Fragment {
    public static String EXTRA_TYPE = "type";
    public static String EXTRA_OBJECT = "obj";
    public static String EXTRA_USERID = "userId";
    public static String EXTRA_ENABLE_OVERSCROLLDRAG = "enable_overscrolldrag";
    public static String EXTRA_ENABLE_REFRESH = "enable_refresh";
    public static String EXTRA_ENABLE_LOADMORE = "enable_loadmore";
    protected Object obj = null;
    protected int userId = -1;
    protected boolean enabledRefresh = true;
    protected boolean enabledLoadMore = true;
    protected boolean enabledOverscrollDrag = true;
    protected String title;
    protected int PAGE_SIZE = 10;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) title = getArguments().getString("title", "base");
        else title = "base";
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_base, null);
        if (view.findViewById(R.id.tv_title) != null)
            ((TextView) view.findViewById(R.id.tv_title)).setText(title);
        return view;
    }

    public BaseFragment putParams(Bundle bundle) {
        setArguments(bundle);
        return this;
    }

    public void performRefresh() {
    }

    public void refreshData() {
    }

    public interface TYPE extends Serializable {

    }
}
