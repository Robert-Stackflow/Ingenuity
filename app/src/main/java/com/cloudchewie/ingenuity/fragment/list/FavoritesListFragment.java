package com.cloudchewie.ingenuity.fragment.list;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.adapter.profile.FavoritesListAdapter;
import com.cloudchewie.ingenuity.entity.Favorites;
import com.cloudchewie.ingenuity.fragment.global.BaseFragment;
import com.cloudchewie.ingenuity.request.SettingRequest;
import com.cloudchewie.ingenuity.request.UserProfileRequest;
import com.cloudchewie.ingenuity.util.decoration.DividerItemDecoration;
import com.cloudchewie.ui.ThemeUtil;
import com.scwang.smart.refresh.header.ClassicsHeader;
import com.scwang.smart.refresh.header.MaterialHeader;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.util.List;

public class FavoritesListFragment extends BaseFragment implements View.OnClickListener {
    View mainView;
    List<Favorites> favoritesList;
    FavoritesListAdapter favoritesListAdapter;
    RecyclerView recyclerView;
    RefreshLayout swipeRefreshLayout;
    ClassicsHeader header;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mainView = View.inflate(getContext(), R.layout.fragment_list_favorites, null);
        recyclerView = mainView.findViewById(R.id.fragment_favorites_list_recyclerview);
        getParams();
        initRecyclerView();
        initSwipeRefresh();
        return mainView;
    }

    List<Favorites> getData() {
        List<Favorites> favorites = UserProfileRequest.getFavorites(userId);
        favorites.add(0, getDefaultFavorites());
        return favorites;
    }

    @Override
    public void onResume() {
        super.onResume();
        refreshData();
    }

    Favorites getDefaultFavorites() {
        Favorites favorites = new Favorites();
        favorites.setName("默认收藏夹");
        favorites.setUserId(userId);
        favorites.setPublic(SettingRequest.isDefaultFavoritesPublic());
        return favorites;
    }

    private void getParams() {
        if (getArguments() != null) {
            userId = getArguments().getInt(EXTRA_USERID, -1);
            enabledRefresh = getArguments().getBoolean(EXTRA_ENABLE_REFRESH, true);
            enabledLoadMore = getArguments().getBoolean(EXTRA_ENABLE_LOADMORE, true);
            enabledOverscrollDrag = getArguments().getBoolean(EXTRA_ENABLE_OVERSCROLLDRAG, true);
        }
    }

    void initRecyclerView() {
        recyclerView.post(() -> {
            favoritesList = getData();
            favoritesListAdapter = new FavoritesListAdapter(getActivity(), favoritesList);
            recyclerView.setAdapter(favoritesListAdapter);
        });
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
    }

    @Override
    public void refreshData() {
        if (recyclerView != null) {
            recyclerView.post(() -> {
                favoritesList = getData();
                favoritesListAdapter = new FavoritesListAdapter(getActivity(), favoritesList);
                recyclerView.setAdapter(favoritesListAdapter);
            });
        }
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = mainView.findViewById(R.id.fragment_favorites_list_swipe_refresh);
        header = mainView.findViewById(R.id.fragment_favorites_list_swipe_refresh_header);
        swipeRefreshLayout.setRefreshHeader(new MaterialHeader(requireContext()).setColorSchemeColors(ThemeUtil.getPrimaryColor(requireContext())).setProgressBackgroundColorSchemeColor(getResources().getColor(R.color.card_background)));
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnableRefresh(false);
        swipeRefreshLayout.setEnableOverScrollDrag(enabledOverscrollDrag);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setDisableContentWhenRefresh(true);
        swipeRefreshLayout.setDisableContentWhenLoading(true);
        swipeRefreshLayout.setEnableRefresh(enabledRefresh);
        swipeRefreshLayout.setEnableLoadMore(enabledLoadMore);
        header.setEnableLastTime(false);
        header.setTextSizeTitle(14);
    }

    @Override
    public void performRefresh() {
        if (swipeRefreshLayout != null) swipeRefreshLayout.autoRefresh();
    }

    @Override
    public void onClick(View view) {
    }
}
