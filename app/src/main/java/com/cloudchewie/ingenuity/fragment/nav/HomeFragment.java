/*
 * Project Name: Worthy
 * Author: Ruida
 * Last Modified: 2022/12/19 14:44:20
 * Copyright(c) 2022 Ruida https://cloudchewie.com
 */

package com.cloudchewie.ingenuity.fragment.nav;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.discover.SearchActivity;
import com.cloudchewie.ingenuity.fragment.global.BaseFragment;
import com.cloudchewie.ui.search.SearchLayout;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class HomeFragment extends BaseFragment implements View.OnClickListener {
    View mainView;
    Context context;
    HomeFragmentAdapter adapter;
    private List<String> titles;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private ViewPager2 viewPager;
    private int followingOption = 1;
    private ImageButton searchButton;
    private SearchLayout searchLayout;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_home, null);
        StatusBarUtil.setStatusBarMarginTop(mainView.findViewById(R.id.home_titlebar), 0, StatusBarUtil.getStatusBarHeight(getActivity()), 0, 0);
        context = getContext();
        tabLayout = mainView.findViewById(R.id.home_tab_layout);
        viewPager = mainView.findViewById(R.id.home_viewpager);
        searchButton = mainView.findViewById(R.id.home_search);
        searchLayout = mainView.findViewById(R.id.home_search_layout);
        searchButton.setVisibility(View.GONE);
        initViewPager();
        initTabLayout();
        return mainView;
    }

    void initViewPager() {
        fragments = new ArrayList<>();
        titles = Arrays.asList(getResources().getStringArray(R.array.fragment_home_titles));
        fragments.add(new RecommendFragment());
        fragments.add(new RecommendFragment());
        adapter = new HomeFragmentAdapter(getChildFragmentManager(), getLifecycle(), fragments);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles.get(position))).attach();
        tabLayout.getParent().requestDisallowInterceptTouchEvent(true);
    }

    @SuppressLint("NotifyDataSetChanged")
    public void setDefaultItem(int position) {
        try {
            Class c = Class.forName("androidx.viewpager2.widget.ViewPager2");
            Field field = c.getDeclaredField("mCurItem");
            field.setAccessible(true);
            field.setInt(viewPager, position);
        } catch (Exception e) {
            e.printStackTrace();
        }
        adapter.notifyDataSetChanged();
        viewPager.setCurrentItem(position);
    }

    void initTabLayout() {
        searchLayout.setOnClickListener(this);
        searchButton.setOnClickListener(this);
        searchLayout.getSearchEdit().setOnClickListener(this);
        searchLayout.getSearchEdit().setKeyListener(null);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.view.setLongClickable(false);
                int finalI = i;
                if (fragments.get(finalI) instanceof BaseFragment)
                    tab.view.setOnClickListener(v -> ((BaseFragment) fragments.get(finalI)).performRefresh());
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                    tab.view.setTooltipText(null);
                }
            }
        }
    }

    @Override
    public void onClick(View view) {
        if (view == searchLayout || view == searchButton || view == searchLayout.getSearchEdit()) {
            Bundle bundle = ActivityOptionsCompat.makeSceneTransitionAnimation(getActivity(), searchLayout, "shareElement").toBundle();
            Intent intent = new Intent(getActivity(), SearchActivity.class);
            if (searchLayout.getVisibility() == View.VISIBLE) startActivity(intent, bundle);
            else startActivity(intent);
        }
    }

    @Override
    public void performRefresh() {
        if (fragments != null && fragments.size() > 0) {
            if (fragments.get(viewPager.getCurrentItem()) instanceof BaseFragment)
                ((BaseFragment) fragments.get(viewPager.getCurrentItem())).performRefresh();
        }
    }

    public class HomeFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public HomeFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
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
