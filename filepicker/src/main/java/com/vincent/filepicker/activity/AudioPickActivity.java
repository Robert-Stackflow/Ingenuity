package com.vincent.filepicker.activity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vincent.filepicker.Constant;
import com.vincent.filepicker.R;
import com.vincent.filepicker.adapter.AudioPickAdapter;
import com.vincent.filepicker.adapter.FolderListAdapter;
import com.vincent.filepicker.filter.FileFilter;
import com.vincent.filepicker.filter.entity.AudioFile;
import com.vincent.filepicker.filter.entity.Directory;
import com.vincent.filepicker.util.ToastUtil;
import com.vincent.filepicker.util.Util;
import com.vincent.filepicker.view.DividerListItemDecoration;
import com.vincent.filepicker.view.FolderPopUpWindow;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Vincent Woo
 * Date: 2016/10/21
 * Time: 17:31
 */

public class AudioPickActivity extends BaseActivity {
    public static final String IS_TAKEN_AUTO_SELECTED = "IsTakenAutoSelected";

    public static final int DEFAULT_MAX_NUMBER = 9;
    private int mMaxNumber;
    private int mCurrentNumber = 0;
    private RecyclerView mRecyclerView;
    private AudioPickAdapter mAdapter;
    private boolean isTakenAutoSelected;
    private ArrayList<AudioFile> mSelectedList = new ArrayList<>();
    private List<Directory<AudioFile>> mAll;
    private String mAudioPath;

    private TextView tv_title;
    private TextView tv_folder;
    private Button tv_done;
    private RelativeLayout ll_folder;
    private RelativeLayout tb_pick;

    private FolderListAdapter<AudioFile> mDirectoryAdapter;
    private FolderPopUpWindow mFolderPopupWindow;

    @Override
    void permissionGranted() {
        loadData();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.vw_activity_audio_pick);
        mMaxNumber = getIntent().getIntExtra(Constant.MAX_NUMBER, DEFAULT_MAX_NUMBER);
        isTakenAutoSelected = getIntent().getBooleanExtra(IS_TAKEN_AUTO_SELECTED, true);
        initView();
    }

    private void initView() {
        tv_title = findViewById(R.id.tv_title);
        tv_title.setText("选择音频");
        tv_done = findViewById(R.id.tv_done);
        tv_done.setText("完成");
        tv_done.setEnabled(false);
        mRecyclerView = findViewById(R.id.rv_audio_pick);
        LinearLayoutManager layoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(layoutManager);
        mRecyclerView.addItemDecoration(new DividerListItemDecoration(this, LinearLayoutManager.VERTICAL, R.drawable.shape_divider));
        mAdapter = new AudioPickAdapter(this, mMaxNumber);
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

        tv_done.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putParcelableArrayListExtra(Constant.RESULT_PICK_AUDIO, mSelectedList);
            setResult(RESULT_OK, intent);
            finish();
        });

        mDirectoryAdapter = new FolderListAdapter(this, mAll, true);

        tb_pick = findViewById(R.id.tb_pick);
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

    private void createPopupFolderList() {
        mFolderPopupWindow = new FolderPopUpWindow(this, mDirectoryAdapter);
        mFolderPopupWindow.setOnItemClickListener((adapterView, view, position, l) -> {
            mDirectoryAdapter.setSelectIndex(position);
            mFolderPopupWindow.dismiss();
            Directory<AudioFile> videoFolder = (Directory<AudioFile>) adapterView.getAdapter().getItem(position);
            if (null != videoFolder) {
                List<Directory<AudioFile>> list = new ArrayList<>();
                list.add(videoFolder);
                refreshData(list);
                if (tv_folder != null)
                    tv_folder.setText(videoFolder.getName());
            }
        });
        mFolderPopupWindow.setMargin(ll_folder.getHeight());
    }

    private void record() {
        Intent intent = new Intent(MediaStore.Audio.Media.RECORD_SOUND_ACTION);
        if (Util.detectIntent(AudioPickActivity.this, intent)) {
            startActivityForResult(intent, Constant.REQUEST_CODE_TAKE_AUDIO);
        } else {
            ToastUtil.getInstance(AudioPickActivity.this).showToast(getString(R.string.vw_no_audio_app));
        }
    }

    private void loadData() {
        FileFilter.getAudios(this, directories -> {
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

    private void refreshData(List<Directory<AudioFile>> directories) {
        boolean tryToFindTaken = isTakenAutoSelected;

        // if auto-select taken file is enabled, make sure requirements are met
        if (tryToFindTaken && !TextUtils.isEmpty(mAudioPath)) {
            File takenFile = new File(mAudioPath);
            tryToFindTaken = !mAdapter.isUpToMax() && takenFile.exists(); // try to select taken file only if max isn't reached and the file exists
        }

        List<AudioFile> list = new ArrayList<>();
        for (Directory<AudioFile> directory : directories) {
            list.addAll(directory.getFiles());

            // auto-select taken file?
            if (tryToFindTaken) {
                tryToFindTaken = findAndAddTaken(directory.getFiles());   // if taken file was found, we're done
            }
        }

        for (AudioFile file : mSelectedList) {
            int index = list.indexOf(file);
            if (index != -1) {
                list.get(index).setSelected(true);
            }
        }
        mAdapter.refresh(list);
    }

    private boolean findAndAddTaken(List<AudioFile> list) {
        for (AudioFile audioFile : list) {
            if (audioFile.getPath().equals(mAudioPath)) {
                mSelectedList.add(audioFile);
                mCurrentNumber++;
                mAdapter.setCurrentNumber(mCurrentNumber);
                if (mCurrentNumber <= 0) {
                    tv_done.setEnabled(false);
                    tv_done.setText("完成");
                } else {
                    tv_done.setEnabled(true);
                    tv_done.setText("完成(" + mCurrentNumber + "/" + mMaxNumber + ")");
                }

                return true;   // taken file was found and added
            }
        }
        return false;    // taken file wasn't found
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case Constant.REQUEST_CODE_TAKE_AUDIO:
                if (resultCode == RESULT_OK) {
                    if (data.getData() != null) {
                        mAudioPath = data.getData().getPath();
                    }
                    loadData();
                }
                break;
        }
    }
}
