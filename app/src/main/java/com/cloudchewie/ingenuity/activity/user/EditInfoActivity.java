package com.cloudchewie.ingenuity.activity.user;

import static com.yalantis.ucrop.UCrop.EXTRA_OUTPUT_URI;
import static com.yalantis.ucrop.UCrop.RESULT_ERROR;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.TextUtils;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.global.BaseActivity;
import com.cloudchewie.ingenuity.bean.ListBottomSheetBean;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.ingenuity.request.ImageUploadRequest;
import com.cloudchewie.ingenuity.request.UserAuthRequest;
import com.cloudchewie.ingenuity.util.image.ImageViewInfo;
import com.cloudchewie.ingenuity.util.image.NineGridUtil;
import com.cloudchewie.ingenuity.widget.InputBottomSheet;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;
import com.cloudchewie.ui.custom.EntryItem;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.general.CircleImageView;
import com.cloudchewie.ui.general.IToast;
import com.cloudchewie.util.basic.DateFormatUtil;
import com.cloudchewie.util.system.FileUtil;
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
import java.util.List;

public class EditInfoActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    User user;
    CircleImageView avatarEntry;
    EntryItem nickNameEntry;
    EntryItem backroundEntry;
    EntryItem signatureEntry;
    EntryItem birthdayEntry;
    EntryItem genderEntry;
    EntryItem locationEntry;
    File takeImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_edit_info);
        ((TitleBar) findViewById(R.id.activity_edit_info_titlebar)).setLeftButtonClickListener(v -> finish());
        avatarEntry = findViewById(R.id.entry_edit_avatar);
        nickNameEntry = findViewById(R.id.entry_edit_nickname);
        backroundEntry = findViewById(R.id.entry_edit_background);
        signatureEntry = findViewById(R.id.entry_edit_signature);
        birthdayEntry = findViewById(R.id.entry_edit_birthday);
        genderEntry = findViewById(R.id.entry_edit_gender);
        locationEntry = findViewById(R.id.entry_edit_location);
        initView();
        initSwipeRefresh();
    }

    void initView() {
        Intent intent = getIntent();
        if (intent != null) user = (User) intent.getSerializableExtra("user");
        if (user != null) {
            Glide.with(EditInfoActivity.this).load(user.getAvatar()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(avatarEntry);
            Glide.with(EditInfoActivity.this).load(user.getBackground()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(backroundEntry.getImageView());
            backroundEntry.showImage();
            nickNameEntry.setTipText(user.getNickname());
            signatureEntry.setTipText(user.getSignature());
            genderEntry.setTipText(String.valueOf(user.getGender()));
            locationEntry.setTipText(user.getCurrentLocation());
            if (user.getGmtBirth() != null)
                birthdayEntry.setTipText(DateFormatUtil.getSimpleDateFormat(DateFormatUtil.YMD_FORMAT_WITH_BAR).format(user.getGmtBirth()));
            else birthdayEntry.setTipText("未设置");
        }
        nickNameEntry.setOnClickListener(v -> {
            InputBottomSheet bottomSheet = new InputBottomSheet(this, nickNameEntry.getTitle(), nickNameEntry.getTip(), 30, true);
            bottomSheet.setOnConfirmClickedListener(content -> {
                nickNameEntry.setTipText(content);
                user.setNickname(content);
                UserAuthRequest.update(user);
            });
            bottomSheet.setOnDismissListener(dialog -> bottomSheet.hideKeyBoard());
            bottomSheet.show();
        });
        signatureEntry.setOnClickListener(v -> {
            InputBottomSheet bottomSheet = new InputBottomSheet(this, signatureEntry.getTitle(), signatureEntry.getTip(), 100, true, 3);
            bottomSheet.setOnConfirmClickedListener(content -> {
                signatureEntry.setTipText(content);
                user.setSignature(content);
                UserAuthRequest.update(user);
            });
            bottomSheet.setOnDismissListener(dialog -> bottomSheet.hideKeyBoard());
            bottomSheet.show();
        });
        avatarEntry.setOnClickListener(v -> {
            List<String> strings = Arrays.asList(getResources().getStringArray(R.array.edit_avatar));
            ListBottomSheet bottomSheet = new ListBottomSheet(EditInfoActivity.this, ListBottomSheetBean.strToBean(strings));
            bottomSheet.setOnItemClickedListener(position -> {
                if (position == 0) {
                    ImageViewInfo viewInfo = new ImageViewInfo(user.getAvatar());
                    List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
                    viewInfo.setBounds(NineGridUtil.getBounds(avatarEntry));
                    mThumbViewInfoList.add(viewInfo);
                    GPreviewBuilder.from(EditInfoActivity.this).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
                } else if (position == 1) {
                    selectImage(104, true);
                } else if (position == 2) {
                    takePicture(105);
                }
                bottomSheet.dismiss();
            });
            bottomSheet.show();
        });
        backroundEntry.setOnClickListener(v -> {
            List<String> strings = Arrays.asList(getResources().getStringArray(R.array.edit_background));
            ListBottomSheet bottomSheet = new ListBottomSheet(EditInfoActivity.this, ListBottomSheetBean.strToBean(strings));
            bottomSheet.setOnItemClickedListener(position -> {
                if (position == 0) {
                    ImageViewInfo viewInfo = new ImageViewInfo(user.getBackground());
                    List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
                    viewInfo.setBounds(NineGridUtil.getBounds(backroundEntry.getImageView()));
                    mThumbViewInfoList.add(viewInfo);
                    GPreviewBuilder.from(EditInfoActivity.this).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
                } else if (position == 1) {
                    selectImage(106, false);
                } else if (position == 2) {
                    takePicture(107);
                }
                bottomSheet.dismiss();
            });
            bottomSheet.show();
        });
        genderEntry.setOnClickListener(v -> {
            List<String> strings = Arrays.asList(getResources().getStringArray(R.array.edit_gender));
            ListBottomSheet bottomSheet = new ListBottomSheet(EditInfoActivity.this, ListBottomSheetBean.strToBean(strings));
            bottomSheet.setOnItemClickedListener(position -> {
                genderEntry.setTipText(strings.get(position));
                user.setGender(strings.get(position).charAt(0));
                UserAuthRequest.update(user);
                bottomSheet.dismiss();
            });
            bottomSheet.show();
        });
    }

    public void takePicture(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takeImageFile = FileUtil.createFileInternal(EditInfoActivity.this, "IMG_", ".jpg");
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

    void initSwipeRefresh() {
        swipeRefreshLayout = findViewById(R.id.activity_edit_info_swipe_refresh);
        swipeRefreshLayout.setEnableOverScrollDrag(true);
        swipeRefreshLayout.setEnableOverScrollBounce(true);
        swipeRefreshLayout.setEnableLoadMore(false);
        swipeRefreshLayout.setEnablePureScrollMode(true);
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

    public void setAvatarUrl(String path) {
        user.setAvatar(ImageUploadRequest.upload("file://" + path));
        UserAuthRequest.update(user);
        Glide.with(EditInfoActivity.this).load(user.getAvatar()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(avatarEntry);
    }

    public void setBackgroundUrl(String path) {
        user.setBackground(ImageUploadRequest.upload("file://" + path));
        UserAuthRequest.update(user);
        Glide.with(EditInfoActivity.this).load(user.getBackground()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(backroundEntry.getImageView());
    }

    @Override
    public void onClick(View view) {

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 104:
                if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                    List<ImageItem> selectImages = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    setAvatarUrl(selectImages.get(0).path);
                    startCrop(11, selectImages.get(0).path);
                }
                break;
            case 105:
                if (resultCode == RESULT_OK) {
                    if (takeImageFile != null) {
                        String path = takeImageFile.getAbsolutePath();
                        setAvatarUrl(path);
                        startCrop(11, path);
                    }
                }
                break;
            case 106:
                if (resultCode == ImagePicker.RESULT_CODE_ITEMS) {
                    List<ImageItem> selectImages = (List<ImageItem>) data.getSerializableExtra(ImagePicker.EXTRA_RESULT_ITEMS);
                    setBackgroundUrl(selectImages.get(0).path);
                    startCrop(12, selectImages.get(0).path);
                }
                break;
            case 107:
                if (resultCode == RESULT_OK) {
                    if (takeImageFile != null) {
                        String path = takeImageFile.getAbsolutePath();
                        setBackgroundUrl(path);
                        startCrop(12, path);
                    }
                }
                break;
            case 11:
                if (resultCode == RESULT_OK) {
                    String outPath = data.getStringExtra(EXTRA_OUTPUT_URI);
                    if (!TextUtils.isEmpty(outPath)) {
                        setAvatarUrl(outPath);
                    }
                } else if (resultCode == RESULT_ERROR) {
                    IToast.makeTextBottom(this, "图片类型不支持", Toast.LENGTH_SHORT).show();

                }
                break;
            case 12:
                if (resultCode == RESULT_OK) {
                    String outPath = data.getStringExtra(EXTRA_OUTPUT_URI);
                    if (!TextUtils.isEmpty(outPath)) {
                        setBackgroundUrl(outPath);
                    }
                } else if (resultCode == RESULT_ERROR) {
                    IToast.makeTextBottom(this, "图片类型不支持", Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }
}
