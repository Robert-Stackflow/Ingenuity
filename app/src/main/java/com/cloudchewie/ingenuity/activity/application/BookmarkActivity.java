package com.cloudchewie.ingenuity.activity.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.blankj.utilcode.util.ThreadUtils;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.adapter.BookmarkListAdapter;
import com.cloudchewie.ingenuity.entity.Bookmark;
import com.cloudchewie.ingenuity.entity.BookmarkGroup;
import com.cloudchewie.ingenuity.util.ExploreUtil;
import com.cloudchewie.ingenuity.util.bookmark.ExportBookmarkUtil;
import com.cloudchewie.ingenuity.util.bookmark.ImportBookmarkUtil;
import com.cloudchewie.ingenuity.util.database.LocalStorage;
import com.cloudchewie.ingenuity.util.database.MyFileProvider;
import com.cloudchewie.ingenuity.util.decoration.DividerItemDecoration;
import com.cloudchewie.ingenuity.widget.EditBookmarkBottomSheet;
import com.cloudchewie.ingenuity.widget.InputBottomSheet;
import com.cloudchewie.ui.custom.IDialog;
import com.cloudchewie.ui.custom.IToast;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.fab.FloatingActionButton;
import com.cloudchewie.ui.fab.FloatingActionMenu;
import com.cloudchewie.ui.item.InputLayout;
import com.cloudchewie.util.basic.DateFormatUtil;
import com.cloudchewie.util.system.ClipBoardUtil;
import com.cloudchewie.util.system.ShareUtil;
import com.cloudchewie.util.system.UriUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.scwang.smart.refresh.layout.api.RefreshLayout;

import java.io.File;
import java.util.Date;
import java.util.Stack;

public class BookmarkActivity extends BaseActivity implements View.OnClickListener, TextView.OnEditorActionListener, TextWatcher {
    RefreshLayout swipeRefreshLayout;
    private static final int IMPORT_HTML_REQUEST_CODE = 42;
    private static final int EXPORT_HTML_REQUEST_CODE = 43;
    private String EXPORT_PREFIX = "bookmark_";
    FloatingActionButton importButton;
    FloatingActionButton newFolderButton;
    FloatingActionButton newBookmarkButton;
    FloatingActionMenu floatingActionMenu;
    RecyclerView recyclerView;
    BookmarkListAdapter adapter;
    BookmarkGroup toExportGroup;
    BookmarkGroup currentSelectedGroup;
    Stack<BookmarkGroup> stack;
    InputLayout searchEdit;
    BookmarkGroup rootBookmarkGroup;
    TextView blankView;
    View divider;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_bookmark);
        ((TitleBar) findViewById(R.id.activity_bookmark_titlebar)).setLeftButtonClickListener(v -> finishAfterTransition());
        importButton = findViewById(R.id.activity_bookmark_import);
        recyclerView = findViewById(R.id.activity_bookmark_recycler_view);
        newFolderButton = findViewById(R.id.activity_bookmark_new_folder);
        newBookmarkButton = findViewById(R.id.activity_bookmark_new_bookmark);
        floatingActionMenu = findViewById(R.id.activity_bookmark_menu);
        divider = findViewById(R.id.activity_bookmark_top_divider);
        blankView = findViewById(R.id.activity_bookmark_blank);
        searchEdit = findViewById(R.id.activity_bookmark_search);
        importButton.setOnClickListener(this);
        newFolderButton.setOnClickListener(this);
        newBookmarkButton.setOnClickListener(this);
        searchEdit.getEditText().setOnEditorActionListener(this);
        searchEdit.getEditText().addTextChangedListener(this);
        searchEdit.getEditText().setInputType(EditorInfo.IME_ACTION_DONE);
        initSwipeRefresh();
        initRecyclerView();
    }

    void initData() {
        ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() {
                rootBookmarkGroup.setBookmarkGroups(LocalStorage.getAppDatabase().bookmarkDao().getAll());
                return null;
            }

            @Override
            public void onSuccess(String result) {
                runOnUiThread(() -> {
                    adapter.setData(rootBookmarkGroup);
                    updateNullDataState();
                });
            }
        });
    }

    void initRecyclerView() {
        stack = new Stack<>();
        //获取所有根书签夹
        rootBookmarkGroup = new BookmarkGroup();
        rootBookmarkGroup.setAll(true);
        currentSelectedGroup = rootBookmarkGroup;
        updateNullDataState();
        initData();
        //绑定适配器
        adapter = new BookmarkListAdapter(this, rootBookmarkGroup);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        recyclerView.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));
        adapter.setOnRootFolderOperationListener(new BookmarkListAdapter.OnRootFolderOperationListener() {
            @Override
            public void onClick(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, BookmarkGroup childGroup) {
                onClickBookmarkGroup(childGroup);
            }

            @Override
            public void onRename(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, BookmarkGroup childGroup) {
                InputBottomSheet renameBottomSheet = new InputBottomSheet(BookmarkActivity.this, getString(R.string.rename), childGroup.getName(), getString(R.string.bookmark_group_name), 30, true);
                renameBottomSheet.setOnConfirmClickedListener(content -> {
                    childGroup.setName(content);
                    LocalStorage.getAppDatabase().bookmarkDao().update(childGroup);
                    holder.nameView.setText(content);
                    IToast.showBottom(BookmarkActivity.this, getString(R.string.rename_success));
                });
                renameBottomSheet.setOnDismissListener(dialog -> renameBottomSheet.hideKeyBoard());
                renameBottomSheet.show();
            }

            @Override
            public void onDelete(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, BookmarkGroup childGroup) {
                IDialog dialog = new IDialog(BookmarkActivity.this);
                dialog.setTitle(getString(R.string.dialog_title_delete_root_bookmark_group));
                dialog.setMessage(String.format(getString(R.string.dialog_content_delete_root_bookmark_group), childGroup.getName()));
                dialog.setOnClickBottomListener(new IDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        LocalStorage.getAppDatabase().bookmarkDao().deleteById(childGroup.getId());
                        initData();
                        IToast.showBottom(BookmarkActivity.this, getString(R.string.delete_success));
                    }

                    @Override
                    public void onNegtiveClick() {

                    }

                    @Override
                    public void onCloseClick() {

                    }
                });
                dialog.show();
            }

            @Override
            public void onExport(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, BookmarkGroup childGroup) {
                toExportGroup = childGroup;
                ExploreUtil.createFile(BookmarkActivity.this, "text/html", EXPORT_PREFIX, "html", EXPORT_HTML_REQUEST_CODE, true);
            }

            @Override
            public void onShare(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, BookmarkGroup childGroup) {
                shareBookmarkGroup(childGroup);
            }
        });
        adapter.setOnFolderOperationListener(new BookmarkListAdapter.OnFolderOperationListener() {
            @Override
            public void onClick(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, BookmarkGroup childGroup) {
                onClickBookmarkGroup(childGroup);
            }

            @Override
            public void onRename(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, BookmarkGroup childGroup) {
                InputBottomSheet renameBottomSheet = new InputBottomSheet(BookmarkActivity.this, getString(R.string.rename), childGroup.getName(), getString(R.string.bookmark_group_name), 30, true);
                renameBottomSheet.setOnConfirmClickedListener(content -> {
                    childGroup.setName(content);
                    holder.nameView.setText(content);
                    updateDatabase();
                    IToast.showBottom(BookmarkActivity.this, getString(R.string.rename_success));
                });
                renameBottomSheet.setOnDismissListener(dialog -> renameBottomSheet.hideKeyBoard());
                renameBottomSheet.show();
            }

            @Override
            public void onDelete(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, BookmarkGroup childGroup) {
                IDialog dialog = new IDialog(BookmarkActivity.this);
                dialog.setTitle(getString(R.string.dialog_title_delete_bookmark_group));
                dialog.setMessage(String.format(getString(R.string.dialog_content_delete_bookmark_group), parentGroup.getName(), childGroup.getName()));
                dialog.setOnClickBottomListener(new IDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        parentGroup.deleteGroup(childGroup);
                        adapter.setData(parentGroup);
                        updateDatabase();
                        IToast.showBottom(BookmarkActivity.this, getString(R.string.delete_success));
                    }

                    @Override
                    public void onNegtiveClick() {

                    }

                    @Override
                    public void onCloseClick() {

                    }
                });
                dialog.show();
            }

            @Override
            public void onMove(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, BookmarkGroup childGroup) {

            }

            @Override
            public void onExport(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, BookmarkGroup childGroup) {
                toExportGroup = childGroup.clone();
                toExportGroup.setRoot(true);
                ExploreUtil.createFile(BookmarkActivity.this, "text/html", EXPORT_PREFIX, "html", EXPORT_HTML_REQUEST_CODE, true);
            }

            @Override
            public void onShare(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, BookmarkGroup childGroup) {
                shareBookmarkGroup(childGroup);
            }
        });
        adapter.setOnBookmarkOperationListener(new BookmarkListAdapter.OnBookmarkOperationListener() {
            @Override
            public void onOpen(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, Bookmark bookmark) {
                Intent intent = new Intent(BookmarkActivity.this, WebViewActivity.class).setAction(Intent.ACTION_DEFAULT);
                intent.putExtra("url", bookmark.getUrl());
                startActivity(intent);
            }

            @Override
            public void onEdit(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, Bookmark bookmark) {
                EditBookmarkBottomSheet editBookmarkBottomSheet = new EditBookmarkBottomSheet(BookmarkActivity.this, getString(R.string.edit_bookmark), bookmark.getName(), bookmark.getUrl());
                editBookmarkBottomSheet.setOnConfirmClickedListener((name, url) -> {
                    bookmark.setName(name);
                    bookmark.setUrl(url);
                    updateDatabase();
                    holder.nameView.setText(name);
                    IToast.showBottom(BookmarkActivity.this, getString(R.string.edit_success));
                });
                editBookmarkBottomSheet.setOnDismissListener(dialog -> editBookmarkBottomSheet.hideKeyBoard());
                editBookmarkBottomSheet.show();
            }

            @Override
            public void onDelete(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, Bookmark bookmark) {
                parentGroup.deleteItem(bookmark);
                adapter.setData(parentGroup);
                updateDatabase();
                IToast.showBottom(BookmarkActivity.this, getString(R.string.delete_success));
            }

            @Override
            public void onMove(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, Bookmark bookmark) {

            }

            @Override
            public void onCopyUrl(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, Bookmark bookmark) {
                ClipBoardUtil.copy(bookmark.getUrl());
                IToast.showBottom(BookmarkActivity.this, getString(R.string.copy_success));
            }

            @Override
            public void onShare(BookmarkListAdapter.BookmarkViewHolder holder, BookmarkGroup parentGroup, Bookmark bookmark) {
                ShareUtil.shareText(BookmarkActivity.this, bookmark.getShareString(), getString(R.string.share_bookmark));
            }
        });
    }

    private void updateDatabase() {
        ThreadUtils.executeBySingle(new ThreadUtils.SimpleTask<String>() {
            @Override
            public String doInBackground() {
                if (rootBookmarkGroup != null) {
                    for (BookmarkGroup bookmarkGroup : rootBookmarkGroup.getBookmarkGroups()) {
                        LocalStorage.getAppDatabase().bookmarkDao().update(bookmarkGroup);
                    }
                }
                return null;
            }

            @Override
            public void onSuccess(String result) {

            }
        });
    }

    private void shareBookmarkGroup(BookmarkGroup bookmarkGroup) {
        String fileName = EXPORT_PREFIX + DateFormatUtil.getSimpleDateFormat(DateFormatUtil.FILE_FORMAT).format(new Date()) + ".html";
        File file = new File(getFilesDir().toString(), fileName);
        Uri uri = MyFileProvider.getUriForFile(BookmarkActivity.this, getApplication().getPackageName() + ".provider", file);
        ExportBookmarkUtil.exportBookmarks(BookmarkActivity.this, bookmarkGroup, uri);
        ShareUtil.shareFile(BookmarkActivity.this, uri, "text/html", getString(R.string.share_bookmark_group));
    }

    @Override
    public void onBackPressed() {
        if (currentSelectedGroup != adapter.getParentGroup()) {
            //如果正在搜索，则返回
            adapter.setData(currentSelectedGroup);
        } else {
            //如果栈为空，则退出activity
            if (stack.empty()) {
                super.onBackPressed();
            } else {
                //返回上级书签夹
                if (adapter != null) {
                    BookmarkGroup lastGroup = stack.pop();
                    adapter.setData(lastGroup);
                    currentSelectedGroup = lastGroup;
                }
            }
        }
        updateNullDataState();
    }

    void onClickBookmarkGroup(BookmarkGroup bookmarkGroup) {
        stack.push(adapter.getParentGroup());
        currentSelectedGroup = bookmarkGroup;
        adapter.setData(bookmarkGroup);
        updateNullDataState();
    }

    void updateNullDataState() {
        if (currentSelectedGroup.size() <= 0) {
            blankView.setVisibility(View.VISIBLE);
            divider.setVisibility(View.GONE);
        } else {
            divider.setVisibility(View.VISIBLE);
            blankView.setVisibility(View.GONE);
        }
        if (currentSelectedGroup.isAll()) {
            newFolderButton.setVisibility(View.GONE);
            newBookmarkButton.setVisibility(View.GONE);
        } else {
            newFolderButton.setVisibility(View.VISIBLE);
            newBookmarkButton.setVisibility(View.VISIBLE);
        }
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_bookmark_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    @Override
    public void onClick(View view) {
        if (view == importButton) {
            floatingActionMenu.close(true);
            ExploreUtil.performFileSearch(this, IMPORT_HTML_REQUEST_CODE);
        } else if (view == newFolderButton) {
            floatingActionMenu.close(true);
            if (currentSelectedGroup.isAll())
                return;
            BookmarkGroup childGroup = new BookmarkGroup();
            InputBottomSheet newGroupBottomSheet = new InputBottomSheet(BookmarkActivity.this, getString(R.string.new_bookmark_group), childGroup.getName(), getString(R.string.bookmark_group_name), 30, true);
            newGroupBottomSheet.setOnConfirmClickedListener(content -> {
                childGroup.setName(content);
                currentSelectedGroup.addBookmarkGroup(childGroup);
                updateDatabase();
                adapter.setData(currentSelectedGroup);
                IToast.showBottom(BookmarkActivity.this, getString(R.string.new_success));
            });
            newGroupBottomSheet.setOnDismissListener(dialog -> newGroupBottomSheet.hideKeyBoard());
            newGroupBottomSheet.show();
        } else if (view == newBookmarkButton) {
            floatingActionMenu.close(true);
            if (currentSelectedGroup.isAll())
                return;
            Bookmark bookmark = new Bookmark();
            EditBookmarkBottomSheet newBookmarkBottomSheet = new EditBookmarkBottomSheet(BookmarkActivity.this, getString(R.string.new_bookmark), bookmark.getName(), bookmark.getUrl());
            newBookmarkBottomSheet.setOnConfirmClickedListener((name, url) -> {
                bookmark.setName(name);
                bookmark.setUrl(url);
                currentSelectedGroup.addBookmark(bookmark);
                updateDatabase();
                adapter.setData(currentSelectedGroup);
                IToast.showBottom(BookmarkActivity.this, getString(R.string.new_success));
            });
            newBookmarkBottomSheet.setOnDismissListener(dialog -> newBookmarkBottomSheet.hideKeyBoard());
            newBookmarkBottomSheet.show();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent resultData) {
        super.onActivityResult(requestCode, resultCode, resultData);
        if (resultCode != Activity.RESULT_OK)
            return;
        Uri uri = resultData.getData();
        if (uri == null) return;
        IDialog dialog = new IDialog(this);
        switch (requestCode) {
            case EXPORT_HTML_REQUEST_CODE:
                if (toExportGroup != null) {
                    ExportBookmarkUtil.exportBookmarks(this, toExportGroup, uri);
                    IToast.showBottom(this, getString(R.string.export_success));
                }
                break;
            case IMPORT_HTML_REQUEST_CODE:
                dialog.setTitle(getString(R.string.dialog_title_import_bookmark));
                dialog.setMessage(String.format(getString(R.string.dialog_content_import_bookmark), UriUtil.getFileAbsolutePath(this, uri)));
                dialog.setOnClickBottomListener(new IDialog.OnClickBottomListener() {
                    @Override
                    public void onPositiveClick() {
                        try {
                            LocalStorage.getAppDatabase().bookmarkDao().insert(ImportBookmarkUtil.importBookmarks(BookmarkActivity.this, uri));
                            initRecyclerView();
                            IToast.showBottom(BookmarkActivity.this, getString(R.string.import_success));
                        } catch (Exception e) {
                            IToast.showBottom(BookmarkActivity.this, getString(R.string.import_fail));
                        }
                    }

                    @Override
                    public void onNegtiveClick() {

                    }

                    @Override
                    public void onCloseClick() {

                    }
                });
                dialog.show();
                break;
        }

    }

    @Override
    public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
        if (i == EditorInfo.IME_ACTION_DONE) {
            if (TextUtils.isEmpty(searchEdit.getText())) {
                adapter.setData(currentSelectedGroup);
            } else {
                BookmarkGroup bookmarkGroup = new BookmarkGroup();
                bookmarkGroup.setBookmarks(currentSelectedGroup.find(searchEdit.getText()));
                adapter.setData(bookmarkGroup);
            }
        }
        return false;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        //如果搜索栏为空，则恢复书签夹
        if (TextUtils.isEmpty(charSequence)) {
            if (adapter != null) adapter.setData(currentSelectedGroup);
        }
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }
}