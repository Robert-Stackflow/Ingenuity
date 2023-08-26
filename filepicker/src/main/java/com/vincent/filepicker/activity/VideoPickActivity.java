package com.vincent.filepicker.activity;

import static com.cloudchewie.util.ui.SizeUtil.dp2px;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.cloudchewie.util.ui.StatusBarUtil;
import com.vincent.filepicker.Constant;
import com.vincent.filepicker.R;
import com.vincent.filepicker.adapter.FolderListAdapter;
import com.vincent.filepicker.adapter.VideoPickAdapter;
import com.vincent.filepicker.filter.FileFilter;
import com.vincent.filepicker.filter.entity.Directory;
import com.vincent.filepicker.filter.entity.VideoFile;
import com.vincent.filepicker.view.FolderPopUpWindow;
import com.vincent.filepicker.view.GridSpacingItemDecoration;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent Woo
 * Date: 2016/10/21
 * Time: 14:02
 */

public class VideoPickActivity extends BaseActivity {
    public static final String THUMBNAIL_PATH = "FilePick";
    public static final String IS_NEED_CAMERA = "IsNeedCamera";
    public static final String IS_TAKEN_AUTO_SELECTED = "IsTakenAutoSelected";

    public static final int DEFAULT_MAX_NUMBER = 9;
    public static final int COLUMN_NUMBER = 3;
    private int mMaxNumber;
    private int mCurrentNumber = 0;
    private RecyclerView mRecyclerView;
    private VideoPickAdapter mAdapter;
    private boolean isNeedCamera;
    private boolean isTakenAutoSelected;
    private ArrayList<VideoFile> mSelectedList = new ArrayList<>();
    private List<Directory<VideoFile>> mAll;
    private ProgressBar mProgressBar;

    private TextView tv_title;
    private TextView tv_folder;
    private Button tv_done;
    private RelativeLayout ll_folder;
    private RelativeLayout rl_done;
    private RelativeLayout tb_pick;

    private FolderListAdapter<VideoFile> mDirectoryAdapter;
    private FolderPopUpWindow mFolderPopupWindow;

    @Override
    void permissionGranted() {
        loadData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vw_activity_video_pick);
        StatusBarUtil.setStatusBarColor(this, getColor(R.color.picker_bar_background_dark));
        StatusBarUtil.setStatusBarTextColor(this, true);
        mMaxNumber = getIntent().getIntExtra(Constant.MAX_NUMBER, DEFAULT_MAX_NUMBER);
        isNeedCamera = getIntent().getBooleanExtra(IS_NEED_CAMERA, true);
        isTakenAutoSelected = getIntent().getBooleanExtra(IS_TAKEN_AUTO_SELECTED, true);
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("选择视频");
        tv_done = findViewById(R.id.tv_done);
        tv_done.setText("完成");
        tv_done.setEnabled(false);
        mRecyclerView = findViewById(R.id.rv_video_pick);
        GridLayoutManager layoutManager = new GridLayoutManager(this, COLUMN_NUMBER);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new GridSpacingItemDecoration(3, dp2px(this, 2), false));

        mAdapter = new VideoPickAdapter(this, isNeedCamera, mMaxNumber);
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

        mProgressBar = findViewById(R.id.pb_video_pick);
        File folder = new File(getExternalCacheDir().getAbsolutePath() + File.separator + THUMBNAIL_PATH);
        if (!folder.exists()) {
            mProgressBar.setVisibility(View.VISIBLE);
        } else {
            mProgressBar.setVisibility(View.GONE);
        }
        tv_done.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(Constant.RESULT_PICK_VIDEO, mSelectedList);
            setResult(RESULT_OK, intent);
            finish();
        });
        tb_pick = findViewById(R.id.tb_pick);
        tb_pick.setBackgroundColor(getColor(R.color.picker_bar_background_dark));

        mDirectoryAdapter = new FolderListAdapter(this, mAll, false);

        ll_folder = findViewById(R.id.ll_folder);
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
            tv_folder = findViewById(R.id.tv_folder);
            tv_folder.setText(getResources().getString(R.string.vw_all));
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_CODE_TAKE_VIDEO:
                if (resultCode == RESULT_OK) {
                    Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                    File file = new File(mAdapter.mVideoPath);
                    Uri contentUri = Uri.fromFile(file);
                    mediaScanIntent.setData(contentUri);
                    sendBroadcast(mediaScanIntent);
                    loadData();
                }
                break;
        }
    }

    /**
     * 创建弹出的ListView
     */
    private void createPopupFolderList() {
        mFolderPopupWindow = new FolderPopUpWindow(this, mDirectoryAdapter);
        mFolderPopupWindow.setOnItemClickListener((adapterView, view, position, l) -> {
            mDirectoryAdapter.setSelectIndex(position);
            mFolderPopupWindow.dismiss();
            Directory videoFolder = (Directory) adapterView.getAdapter().getItem(position);
            if (null != videoFolder) {
                List<Directory<VideoFile>> list = new ArrayList<>();
                list.add(videoFolder);
                refreshData(list);
                if (tv_folder != null)
                    tv_folder.setText(videoFolder.getName());
            }
        });
        mFolderPopupWindow.setMargin(ll_folder.getHeight());
    }

    private void loadData() {
        FileFilter.getVideos(this, directories -> {
            mProgressBar.setVisibility(View.GONE);
            if (isNeedFolderList) {
                ArrayList<Directory> list = new ArrayList<>();
                Directory all = new Directory();
                all.setName(getResources().getString(R.string.vw_all));
                list.add(all);
                list.addAll(directories);
            }

            mAll = directories;
            refreshData(directories);
        });
    }

    private void refreshData(List<Directory<VideoFile>> directories) {
        boolean tryToFindTaken = isTakenAutoSelected;

        // if auto-select taken file is enabled, make sure requirements are met
        if (tryToFindTaken && !TextUtils.isEmpty(mAdapter.mVideoPath)) {
            File takenFile = new File(mAdapter.mVideoPath);
            tryToFindTaken = !mAdapter.isUpToMax() && takenFile.exists(); // try to select taken file only if max isn't reached and the file exists
        }

        List<VideoFile> list = new ArrayList<>();
        for (Directory<VideoFile> directory : directories) {
            list.addAll(directory.getFiles());
            if (tryToFindTaken) {
                tryToFindTaken = findAndAddTaken(directory.getFiles());   // if taken file was found, we're done
            }
        }

        for (VideoFile file : mSelectedList) {
            int index = list.indexOf(file);
            if (index != -1) {
                list.get(index).setSelected(true);
            }
        }
        mAdapter.refresh(list);
    }

    private boolean findAndAddTaken(@NonNull List<VideoFile> list) {
        for (VideoFile videoFile : list) {
            if (videoFile.getPath().equals(mAdapter.mVideoPath)) {
                mSelectedList.add(videoFile);
                mCurrentNumber++;
                mAdapter.setCurrentNumber(mCurrentNumber);
                if (mCurrentNumber <= 0) {
                    tv_done.setEnabled(false);
                    tv_done.setText("完成");
                } else {
                    tv_done.setEnabled(true);
                    tv_done.setText("完成(" + mCurrentNumber + "/" + mMaxNumber + ")");
                }
                return true;
            }
        }
        return false;
    }
}
