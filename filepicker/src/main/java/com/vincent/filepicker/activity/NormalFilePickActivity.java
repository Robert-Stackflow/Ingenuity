package com.vincent.filepicker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vincent.filepicker.Constant;
import com.vincent.filepicker.R;
import com.vincent.filepicker.adapter.FolderListAdapter;
import com.vincent.filepicker.adapter.NormalFilePickAdapter;
import com.vincent.filepicker.filter.FileFilter;
import com.vincent.filepicker.filter.entity.Directory;
import com.vincent.filepicker.filter.entity.NormalFile;
import com.vincent.filepicker.view.DividerListItemDecoration;
import com.vincent.filepicker.view.FolderPopUpWindow;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent Woo
 * Date: 2016/10/26
 * Time: 10:14
 */

public class NormalFilePickActivity extends BaseActivity {
    public static final int DEFAULT_MAX_NUMBER = 9;
    public static final String SUFFIX = "Suffix";
    private int mMaxNumber;
    private int mCurrentNumber = 0;
    private RecyclerView mRecyclerView;
    private NormalFilePickAdapter mAdapter;
    private final ArrayList<NormalFile> mSelectedList = new ArrayList<>();
    private List<Directory<NormalFile>> mAll;
    private ProgressBar mProgressBar;
    private String[] mSuffix;

    private TextView tv_title;
    private TextView tv_folder;
    private Button tv_done;
    private RelativeLayout ll_folder;
    private RelativeLayout rl_done;
    private RelativeLayout tb_pick;

    private FolderListAdapter<NormalFile> mDirectoryAdapter;
    private FolderPopUpWindow mFolderPopupWindow;

    @Override
    void permissionGranted() {
        loadData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vw_activity_file_pick);

        mMaxNumber = getIntent().getIntExtra(Constant.MAX_NUMBER, DEFAULT_MAX_NUMBER);
        mSuffix = getIntent().getStringArrayExtra(SUFFIX);
        initView();
    }

    private void initView() {
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText("选择文件");
        tv_done = findViewById(R.id.tv_done);
        tv_done.setText("完成");
        tv_done.setEnabled(false);
        mRecyclerView = (RecyclerView) findViewById(R.id.rv_file_pick);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerListItemDecoration(this,
                LinearLayoutManager.VERTICAL, R.drawable.shape_divider));
        mAdapter = new NormalFilePickAdapter(this, mMaxNumber);
        mRecyclerView.setAdapter(mAdapter);

        mAdapter.setOnSelectStateListener((state, file) -> {
            if (state) {
                mSelectedList.add(file);
                mCurrentNumber++;
            } else {
                mSelectedList.remove(file);
                mCurrentNumber--;
            }
            if (mCurrentNumber <= 0) {
                tv_done.setEnabled(false);
                tv_done.setText("完成");
            } else {
                tv_done.setEnabled(true);
                tv_done.setText("完成(" + mCurrentNumber + "/" + mMaxNumber + ")");
            }
        });

        mProgressBar = (ProgressBar) findViewById(R.id.pb_file_pick);
        mDirectoryAdapter = new FolderListAdapter(this, mAll, true);

        tv_done.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(Constant.RESULT_PICK_FILE, mSelectedList);
            setResult(RESULT_OK, intent);
            finish();
        });

        tb_pick = (RelativeLayout) findViewById(R.id.tb_pick);
        ll_folder = (RelativeLayout) findViewById(R.id.ll_folder);
        if (isNeedFolderList) {
            ll_folder.setVisibility(View.VISIBLE);
            ll_folder.setOnClickListener(v -> {
                createPopupFolderList();
                mDirectoryAdapter.refreshData(mAll);
                if (mFolderPopupWindow.isShowing()) {
                    mFolderPopupWindow.dismiss();
                } else {
                    mFolderPopupWindow.showAtLocation(ll_folder, Gravity.NO_GRAVITY, 0, 0);
                    int index = mDirectoryAdapter.getSelectIndex();
                    index = index == 0 ? index : index - 1;
                    mFolderPopupWindow.setSelection(index);
                }
            });
            tv_folder = (TextView) findViewById(R.id.tv_folder);
            tv_folder.setText(getResources().getString(R.string.vw_all));
        }
    }

    private void createPopupFolderList() {
        mFolderPopupWindow = new FolderPopUpWindow(this, mDirectoryAdapter);
        mFolderPopupWindow.setOnItemClickListener((adapterView, view, position, l) -> {
            mDirectoryAdapter.setSelectIndex(position);
            mFolderPopupWindow.dismiss();
            Directory<NormalFile> videoFolder = (Directory<NormalFile>) adapterView.getAdapter().getItem(position);
            if (null != videoFolder) {
                List<Directory<NormalFile>> list = new ArrayList<>();
                list.add(videoFolder);
                refreshData(list);
                if (tv_folder != null)
                    tv_folder.setText(videoFolder.getName());
            }
        });
        mFolderPopupWindow.setMargin(ll_folder.getHeight());
    }

    private void loadData() {
        FileFilter.getFiles(this, directories -> {
            if (isNeedFolderList) {
                ArrayList<Directory> list = new ArrayList<>();
                Directory all = new Directory();
                all.setName(getResources().getString(R.string.vw_all));
                list.add(all);
                list.addAll(directories);
            }
            mAll = directories;
            refreshData(directories);
        }, mSuffix);
    }

    private void refreshData(@NonNull List<Directory<NormalFile>> directories) {
        mProgressBar.setVisibility(View.GONE);
        List<NormalFile> list = new ArrayList<>();
        for (Directory<NormalFile> directory : directories) {
            list.addAll(directory.getFiles());
        }
        for (NormalFile file : mSelectedList) {
            int index = list.indexOf(file);
            if (index != -1) {
                list.get(index).setSelected(true);
            }
        }
        mAdapter.refresh(list);
    }
}
