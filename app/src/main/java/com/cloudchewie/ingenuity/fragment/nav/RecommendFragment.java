package com.cloudchewie.ingenuity.fragment.nav;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.lifecycle.Lifecycle;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.fragment.global.BaseFragment;
import com.cloudchewie.ui.general.IToast;
import com.cloudchewie.ui.general.ViewPager2Container;
import com.google.android.material.tabs.TabLayout;
import com.google.android.material.tabs.TabLayoutMediator;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class RecommendFragment extends BaseFragment implements View.OnClickListener {
    View mainView;
    Context context;
    RecommendFragmentAdapter adapter;
    RecyclerView.OnScrollChangeListener onScrollChangeListener;
    RecyclerView.OnScrollListener onScrollListener;
    private List<String> titles;
    private TabLayout tabLayout;
    private List<Fragment> fragments;
    private ViewPager2 viewPager;
    private int recommendingOption = 1;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_recommend, null);
        context = getContext();
        tabLayout = mainView.findViewById(R.id.recommend_tab_layout);
        viewPager = mainView.findViewById(R.id.recommend_viewpager);
        initViewPager();
        initTabLayout();
        viewPager.getParent().requestDisallowInterceptTouchEvent(true);
        ((ViewPager2Container) mainView.findViewById(R.id.recommend_viewpager_container)).disallowParentInterceptDownEvent(true);
        return mainView;
    }

    public RecommendFragment setRecyclerViewScrollListener(RecyclerView.OnScrollListener onScrollListener, RecyclerView.OnScrollChangeListener onScrollChangeListener) {
        this.onScrollListener = onScrollListener;
        this.onScrollChangeListener = onScrollChangeListener;
        return this;
    }

    void initViewPager() {
        fragments = new ArrayList<>();
        titles = Arrays.asList(getResources().getStringArray(R.array.fragment_recommend_titles));
        for (int i = 0; i < titles.size(); i++)
            fragments.add(new BaseFragment());
        adapter = new RecommendFragmentAdapter(getChildFragmentManager(), getLifecycle(), fragments);
        viewPager.setAdapter(adapter);
        new TabLayoutMediator(tabLayout, viewPager, (tab, position) -> tab.setText(titles.get(position))).attach();
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
        mainView.findViewById(R.id.recommend_sort).setOnClickListener(this);
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
    public void performRefresh() {
        if (fragments != null && fragments.size() > 0) {
            if (fragments.get(viewPager.getCurrentItem()) instanceof BaseFragment)
                ((BaseFragment) fragments.get(viewPager.getCurrentItem())).performRefresh();
        }
    }

    @Override
    public void onClick(View view) {
        if (view == mainView.findViewById(R.id.recommend_sort)) {
            IToast.showBottom(getContext(), "编辑兴趣领域");
        }
    }

    public class RecommendFragmentAdapter extends FragmentStateAdapter {
        private final List<Fragment> fragmentList;

        public RecommendFragmentAdapter(FragmentManager fragmentManager, Lifecycle lifecycle, List<Fragment> fragments) {
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
