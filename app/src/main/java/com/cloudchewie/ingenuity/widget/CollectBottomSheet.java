package com.cloudchewie.ingenuity.widget;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.activity.result.ActivityResultLauncher;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.adapter.profile.SelectFavoritesAdapter;
import com.cloudchewie.ingenuity.entity.Article;
import com.cloudchewie.ingenuity.entity.Collection;
import com.cloudchewie.ingenuity.entity.Favorites;
import com.cloudchewie.ingenuity.entity.Post;
import com.cloudchewie.ingenuity.request.ArticleOperationRequest;
import com.cloudchewie.ingenuity.request.PostOperationRequest;
import com.cloudchewie.ingenuity.request.PostProfileRequest;
import com.cloudchewie.ingenuity.request.SettingRequest;
import com.cloudchewie.ingenuity.request.UserProfileRequest;
import com.cloudchewie.ingenuity.util.decoration.DividerItemDecoration;
import com.cloudchewie.ingenuity.util.system.SPUtil;
import com.cloudchewie.ui.general.BottomSheet;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class CollectBottomSheet extends BottomSheet implements View.OnClickListener {
    Context context;
    TextView newFavorites;
    TextView confirmButton;
    RecyclerView recyclerView;
    SelectFavoritesAdapter adapter;
    List<Favorites> favoritesList;
    ActivityResultLauncher createFavoritesLaucher;
    Object object;

    public CollectBottomSheet(@NonNull Context context, Object o) {
        super(context);
        this.context = context;
        this.object = o;
        initView();
    }

    public void setCreateFavoritesLaucher(ActivityResultLauncher createFavoritesLaucher) {
        this.createFavoritesLaucher = createFavoritesLaucher;
    }

    public void addNewFavorites(Favorites newFavor) {
        if (newFavor == null) return;
        favoritesList.add(1, newFavor);
        adapter.insert(newFavor);
        recyclerView.scrollToPosition(0);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    void initView() {
        getBehavior().setDraggable(false);
        setDragBarVisible(false);
        leftButton.setVisibility(View.GONE);
        rightButton.setVisibility(View.GONE);
        setMainLayout(R.layout.layout_collect_bottom_sheet);
        confirmButton = mainView.findViewById(R.id.layout_collect_bottom_sheet_confirm);
        newFavorites = mainView.findViewById(R.id.layout_collect_bottom_sheet_new);
        recyclerView = mainView.findViewById(R.id.layout_collect_bottom_sheet_recyclerview);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        confirmButton.setOnClickListener(this);
        newFavorites.setOnClickListener(this);
        recyclerView.post(() -> {
            favoritesList = getData();
            if (object instanceof Post) {
                Post post = (Post) object;
                List<Collection> collectionObjects = PostProfileRequest.isCollected(post.getPostId());
                if (collectionObjects != null && collectionObjects.size() != 0) {
                    for (Collection collection : collectionObjects)
                        for (Favorites favorites : favoritesList)
                            if (favorites.getFolderId() == collection.getFolderId())
                                favorites.setCollected(true);
                }
            } else if (object instanceof Article) {
                Article article = (Article) object;
                List<Collection> collectionObjects = PostProfileRequest.isCollected(article.getArticleId());
                if (collectionObjects != null && collectionObjects.size() != 0) {
                    for (Collection collection : collectionObjects)
                        for (Favorites favorites : favoritesList)
                            if (favorites.getFolderId() == collection.getFolderId())
                                favorites.setCollected(true);
                }
            }
            adapter = new SelectFavoritesAdapter((ArrayList<Favorites>) favoritesList, getContext());
            recyclerView.setAdapter(adapter);
        });
    }

    Favorites getDefaultFavorites() {
        Favorites favorites = new Favorites();
        favorites.setName("默认收藏夹");
        favorites.setPublic(SettingRequest.isDefaultFavoritesPublic());
        return favorites;
    }

    List<Favorites> getData() {
        List<Favorites> favorites = UserProfileRequest.getFavorites(SPUtil.getUserId(getContext()));
        for (Favorites favor : favorites)
            favor.setUser(UserProfileRequest.info(favor.getUserId()));
        favorites.add(0, getDefaultFavorites());
        return favorites;
    }

    @Override
    public void onClick(View v) {
        if (v == confirmButton) {
            if (adapter != null) {
                List<Favorites> changedFavorites = adapter.getFavorites();
                for (Favorites change : changedFavorites) {
                    for (Favorites favorites : favoritesList) {
                        if (favorites.getFolderId() == change.getFolderId() && favorites.isCollected() != change.isCollected()) {
                            if (object instanceof Post) {
                                Post post = (Post) object;
                                if (change.isCollected())
                                    PostOperationRequest.collect(new Collection(SPUtil.getUserId(context), change.getFolderId(), (int) post.getPostId(), new Date(), 1));
                                else
                                    PostOperationRequest.uncollect(new Collection(SPUtil.getUserId(context), change.getFolderId(), (int) post.getPostId(), new Date(), 1));
                            } else if (object instanceof Article) {
                                Article article = (Article) object;
                                if (change.isCollected())
                                    ArticleOperationRequest.collect(new Collection(SPUtil.getUserId(context), change.getFolderId(), article.getArticleId(), new Date(), 0));
                                else
                                    ArticleOperationRequest.uncollect(new Collection(SPUtil.getUserId(context), change.getFolderId(), article.getArticleId(), new Date(), 0));
                            }
                        }
                    }
                }
            }
            dismiss();
        } else if (v == newFavorites) {
            if (createFavoritesLaucher != null) createFavoritesLaucher.launch(null);
        }
    }
}
