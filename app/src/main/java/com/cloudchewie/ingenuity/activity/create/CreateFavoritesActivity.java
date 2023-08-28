package com.cloudchewie.ingenuity.activity.create;

import static com.yalantis.ucrop.UCrop.EXTRA_OUTPUT_URI;
import static com.yalantis.ucrop.UCrop.RESULT_ERROR;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.InputFilter;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.global.BaseActivity;
import com.cloudchewie.ingenuity.bean.ListBottomSheetBean;
import com.cloudchewie.ingenuity.entity.Favorites;
import com.cloudchewie.ingenuity.request.FavoritesCreationRequest;
import com.cloudchewie.ingenuity.request.ImageUploadRequest;
import com.cloudchewie.ingenuity.request.SettingRequest;
import com.cloudchewie.ingenuity.util.image.ImageViewInfo;
import com.cloudchewie.ingenuity.util.image.NineGridUtil;
import com.cloudchewie.ingenuity.util.system.AppSharedPreferenceUtil;
import com.cloudchewie.ui.ThemeUtil;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;
import com.cloudchewie.ui.custom.EntryItem;
import com.cloudchewie.ui.general.CheckBoxItem;
import com.cloudchewie.ui.general.IToast;
import com.cloudchewie.util.system.FileUtil;
import com.cloudchewie.util.ui.KeyBoardUtil;
import com.cloudchewie.util.ui.StatusBarUtil;
import com.lzy.imagepicker.ImagePicker;
import com.lzy.imagepicker.bean.ImageItem;
import com.lzy.imagepicker.ui.ImageGridActivity;
import com.previewlibrary.GPreviewBuilder;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.yalantis.ucrop.UCropActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

public class CreateFavoritesActivity extends BaseActivity implements View.OnClickListener {
    public static String EXTRA_DEFAULT = "default";
    public static String EXTRA_FAVORITES = "favorites";
    boolean isCreateMode = true;
    boolean isDefaultFavorites = false;
    Favorites editFavorites;
    RefreshLayout swipeRefreshLayout;
    EditText nameEdit;
    EditText describeEdit;
    TextView describeCount;
    TextView publishButton;
    TextView cancelButton;
    CheckBoxItem isPublic;
    EntryItem coverEntry;
    File takeImageFile;
    int maxSize = 200;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_create_favorites);
        nameEdit = findViewById(R.id.activity_create_favorites_name);
        describeEdit = findViewById(R.id.activity_create_favorites_describe);
        describeCount = findViewById(R.id.activity_create_favorites_describe_count);
        isPublic = findViewById(R.id.activity_create_favorites_switch_public);
        publishButton = findViewById(R.id.activity_create_favorites_publish);
        cancelButton = findViewById(R.id.activity_create_favorites_cancel);
        coverEntry = findViewById(R.id.activity_create_favorites_cover);
        if (getIntent() != null) {
            editFavorites = (Favorites) getIntent().getSerializableExtra(EXTRA_FAVORITES);
            isDefaultFavorites = getIntent().getBooleanExtra(EXTRA_DEFAULT, false);
        }
        initView();
        initSwipeRefresh();
    }

    void initView() {
        describeCount.setText("0/" + maxSize);
        describeEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(maxSize)});
        nameEdit.setFilters(new InputFilter[]{new InputFilter.LengthFilter(20)});
        describeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                describeCount.setText(s.length() + "/" + maxSize);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        nameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int start, int before, int count) {
                setPublishEnable(charSequence.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        cancelButton.setOnClickListener(v -> finish());
        publishButton.setOnClickListener(v -> {
            KeyBoardUtil.hideKeyBoard(CreateFavoritesActivity.this);
            if (!isDefaultFavorites) {
                editFavorites.setUserId(AppSharedPreferenceUtil.getUserId(CreateFavoritesActivity.this));
                editFavorites.setGmtCreate(new Date());
                editFavorites.setPublic(isPublic.isChecked());
                editFavorites.setName(nameEdit.getText().toString());
                editFavorites.setDescription(describeEdit.getText().toString());
                Intent intent = getIntent();
                Bundle bundle = new Bundle();
                bundle.putSerializable(EXTRA_FAVORITES, editFavorites);
                intent.putExtras(bundle);
                setResult(RESULT_OK, intent);
                if (isCreateMode) FavoritesCreationRequest.create(editFavorites);
                else FavoritesCreationRequest.update(editFavorites);
            } else {
                SettingRequest.setDefaultFavoritesPublic(isPublic.isChecked());
            }
            finish();
        });
        setPublishEnable(false);
        coverEntry.setOnClickListener(v -> {
            KeyBoardUtil.hideKeyBoard(CreateFavoritesActivity.this);
            List<String> strings;
            if (!TextUtils.isEmpty(editFavorites.getCover())) {
                strings = Arrays.asList(getResources().getStringArray(R.array.edit_cover));
                ListBottomSheet bottomSheet = new ListBottomSheet(CreateFavoritesActivity.this, ListBottomSheetBean.strToBean(strings));
                bottomSheet.setOnItemClickedListener(position -> {
                    if (position == 0) {
                        if (!TextUtils.isEmpty(editFavorites.getCover())) {
                            ImageViewInfo viewInfo = new ImageViewInfo(editFavorites.getCover());
                            List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
                            viewInfo.setBounds(NineGridUtil.getBounds(coverEntry.getImageView()));
                            mThumbViewInfoList.add(viewInfo);
                            GPreviewBuilder.from(CreateFavoritesActivity.this).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
                        }
                    } else if (position == 1) {
                        selectImage(106, false);
                    } else if (position == 2) {
                        takePicture(107);
                    } else if (position == 3) {
                        coverEntry.showTip();
                        editFavorites.setCover(null);
                        coverEntry.getImageView().setImageBitmap(null);
                    }
                    bottomSheet.dismiss();
                });
                bottomSheet.show();
            } else {
                strings = Arrays.asList(getResources().getStringArray(R.array.edit_cover_without_preview));
                ListBottomSheet bottomSheet = new ListBottomSheet(CreateFavoritesActivity.this, ListBottomSheetBean.strToBean(strings));
                bottomSheet.setOnItemClickedListener(position -> {
                    if (position == 0) {
                        selectImage(106, false);
                    } else if (position == 1) {
                        takePicture(107);
                    }
                    bottomSheet.dismiss();
                });
                bottomSheet.show();
            }
        });
        if (editFavorites != null) {
            isCreateMode = false;
            ((TextView) findViewById(R.id.activity_create_favorites_page_title)).setText("编辑收藏夹");
            nameEdit.setText(editFavorites.getName());
            describeEdit.setText(editFavorites.getDescription());
            isPublic.setChecked(editFavorites.isPublic());
            coverEntry.showImage();
            Glide.with(this).load((editFavorites.getCover())).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(coverEntry.getImageView());
        } else {
            isCreateMode = true;
            editFavorites = new Favorites();
        }
        if (isDefaultFavorites) {
            nameEdit.setEnabled(false);
            describeEdit.setVisibility(View.GONE);
            coverEntry.setVisibility(View.GONE);
            describeCount.setVisibility(View.GONE);
        }
    }

    void setPublishEnable(boolean enable) {
        if (enable) {
            publishButton.setSelected(true);
            publishButton.setEnabled(true);
            publishButton.setTextColor(ThemeUtil.getPrimaryColor(this));
        } else {
            publishButton.setSelected(false);
            publishButton.setEnabled(false);
            publishButton.setTextColor(getColor(R.color.color_light_gray));
        }
    }

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_create_favorites_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
    }

    public void takePicture(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takeImageFile = FileUtil.createFileInternal(CreateFavoritesActivity.this, "IMG_", ".jpg");
            if (takeImageFile != null) {
                Uri uri;
                if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.M) {
                    uri = Uri.fromFile(takeImageFile);
                } else {
                    uri = FileProvider.getUriForFile(this, FileUtil.getFileProviderName(this), takeImageFile);
                    List<ResolveInfo> resInfoList = getPackageManager().queryIntentActivities(takePictureIntent, PackageManager.MATCH_DEFAULT_ONLY);
                    for (ResolveInfo resolveInfo : resInfoList) {
                        String packageName = resolveInfo.activityInfo.packageName;
                        grantUriPermission(packageName, uri, Intent.FLAG_GRANT_WRITE_URI_PERMISSION | Intent.FLAG_GRANT_READ_URI_PERMISSION);
                    }
                }
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, uri);
            }
        }
        startActivityForResult(takePictureIntent, requestCode);
    }

    public void selectImage(int requestCode, boolean isCrop) {
        ImagePicker imagePicker = ImagePicker.getInstance();
        imagePicker.setCrop(false);
        imagePicker.setMultiMode(false);
        imagePicker.setShowCamera(true);
        imagePicker.setSelectLimit(1);
        Intent intent = new Intent(this, ImageGridActivity.class);
        startActivityForResult(intent, requestCode);
    }

    public void startCrop(int requestCode, String filePath) {
        Intent intent = new Intent(this, UCropActivity.class);
        intent.putExtra("filePath", filePath);
        intent.putExtra("outPath", FileUtil.createFileInternal(this, "IMG_", ".png").getAbsolutePath());
        startActivityForResult(intent, requestCode);
    }

    public void setCoverUrl(String path) {
        coverEntry.showImage();
        editFavorites.setCover(ImageUploadRequest.upload("file://" + path));
        Glide.with(this).load((editFavorites.getCover())).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(coverEntry.getImageView());
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 106:
                if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                    List<ImageItem> selectImages = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    startCrop(12, selectImages.get(0).path);
                }
                break;
            case 107:
                if (resultCode == RESULT_OK) {
                    if (takeImageFile != null) {
                        String path = takeImageFile.getAbsolutePath();
                        setCoverUrl(path);
                        startCrop(12, path);
                    }
                }
                break;
            case 12:
                if (resultCode == RESULT_OK) {
                    String outPath = data.getStringExtra(EXTRA_OUTPUT_URI);
                    if (!TextUtils.isEmpty(outPath)) {
                        setCoverUrl(outPath);
                    }
                } else if (resultCode == RESULT_ERROR) {
                    IToast.makeTextBottom(this, "图片类型不支持", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}