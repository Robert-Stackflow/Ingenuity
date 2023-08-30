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
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;

import com.blankj.utilcode.util.ActivityUtils;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.activity.BaseActivity;
import com.cloudchewie.ingenuity.activity.MainActivity;
import com.cloudchewie.ingenuity.api.ImageUploadRequest;
import com.cloudchewie.ingenuity.api.UserAuthRequest;
import com.cloudchewie.ingenuity.bean.ListBottomSheetBean;
import com.cloudchewie.ingenuity.entity.User;
import com.cloudchewie.ingenuity.util.database.AppSharedPreferenceUtil;
import com.cloudchewie.ingenuity.util.image.ImageViewInfo;
import com.cloudchewie.ingenuity.util.image.NineGridUtil;
import com.cloudchewie.ingenuity.widget.InputBottomSheet;
import com.cloudchewie.ingenuity.widget.ListBottomSheet;
import com.cloudchewie.ui.custom.CircleImageView;
import com.cloudchewie.ui.custom.IDialog;
import com.cloudchewie.ui.custom.IToast;
import com.cloudchewie.ui.custom.TitleBar;
import com.cloudchewie.ui.item.EntryItem;
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

public class InfoActivity extends BaseActivity implements View.OnClickListener {
    RefreshLayout swipeRefreshLayout;
    User user;
    CircleImageView avatarEntry;
    EntryItem nickNameEntry;
    EntryItem signatureEntry;
    EntryItem genderEntry;
    Button logoutEntry;
    File takeImageFile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        StatusBarUtil.setStatusBarMarginTop(this);
        setContentView(R.layout.activity_info);
        ((TitleBar) findViewById(R.id.activity_edit_info_titlebar)).setLeftButtonClickListener(v -> finish());
        avatarEntry = findViewById(R.id.entry_edit_avatar);
        nickNameEntry = findViewById(R.id.entry_edit_nickname);
        signatureEntry = findViewById(R.id.entry_edit_signature);
        genderEntry = findViewById(R.id.entry_edit_gender);
        logoutEntry = findViewById(R.id.entry_logout);
        initView();
        initSwipeRefresh();
    }

    void initView() {
        Intent intent = getIntent();
        if (intent != null) user = (User) intent.getSerializableExtra("user");
        if (user != null) {
            Glide.with(InfoActivity.this).load(user.getAvatar()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(avatarEntry);
            nickNameEntry.setTipText(user.getNickname());
            signatureEntry.setTipText(user.getSignature());
            genderEntry.setTipText(String.valueOf(user.getGender()));
        }
        nickNameEntry.setOnClickListener(this);
        signatureEntry.setOnClickListener(this);
        avatarEntry.setOnClickListener(this);
        genderEntry.setOnClickListener(this);
        logoutEntry.setOnClickListener(this);
        if (!AppSharedPreferenceUtil.isLogin(this)) {
            findViewById(R.id.entry_logout).setVisibility(View.GONE);
        }
    }

    @Override
    public void onClick(View view) {
        if (view == nickNameEntry) {
            InputBottomSheet bottomSheet = new InputBottomSheet(this, nickNameEntry.getTitle(), nickNameEntry.getTip(), 30, true);
            bottomSheet.setOnConfirmClickedListener(content -> {
                nickNameEntry.setTipText(content);
                user.setNickname(content);
                UserAuthRequest.update(user);
            });
            bottomSheet.setOnDismissListener(dialog -> bottomSheet.hideKeyBoard());
            bottomSheet.show();
        } else if (view == signatureEntry) {
            InputBottomSheet bottomSheet = new InputBottomSheet(this, signatureEntry.getTitle(), signatureEntry.getTip(), 100, true, 3);
            bottomSheet.setOnConfirmClickedListener(content -> {
                signatureEntry.setTipText(content);
                user.setSignature(content);
                UserAuthRequest.update(user);
            });
            bottomSheet.setOnDismissListener(dialog -> bottomSheet.hideKeyBoard());
            bottomSheet.show();
        } else if (view == genderEntry) {
            List<String> strings = Arrays.asList(getResources().getStringArray(R.array.edit_gender));
            ListBottomSheet bottomSheet = new ListBottomSheet(InfoActivity.this, ListBottomSheetBean.strToBean(strings));
            bottomSheet.setOnItemClickedListener(position -> {
                genderEntry.setTipText(strings.get(position));
                user.setGender(strings.get(position).charAt(0));
                UserAuthRequest.update(user);
                bottomSheet.dismiss();
            });
            bottomSheet.show();
        } else if (view == avatarEntry) {
            List<String> strings = Arrays.asList(getResources().getStringArray(R.array.edit_avatar));
            ListBottomSheet bottomSheet = new ListBottomSheet(InfoActivity.this, ListBottomSheetBean.strToBean(strings));
            bottomSheet.setOnItemClickedListener(position -> {
                if (position == 0) {
                    ImageViewInfo viewInfo = new ImageViewInfo(user.getAvatar());
                    List<ImageViewInfo> mThumbViewInfoList = new ArrayList<>();
                    viewInfo.setBounds(NineGridUtil.getBounds(avatarEntry));
                    mThumbViewInfoList.add(viewInfo);
                    GPreviewBuilder.from(InfoActivity.this).setSingleShowType(false).setIsScale(true).setData(mThumbViewInfoList).setCurrentIndex(0).setSingleFling(true).isDisableDrag(false).setFullscreen(true).start();
                } else if (position == 1) {
                    selectImage(104, true);
                } else if (position == 2) {
                    takePicture(105);
                }
                bottomSheet.dismiss();
            });
            bottomSheet.show();
        } else if (view == logoutEntry) {
            IDialog dialog = new IDialog(InfoActivity.this);
            dialog.setTitle(getString(R.string.dialog_title_logout));
            dialog.setMessage(getString(R.string.dialog_content_logout));
            dialog.setOnClickBottomListener(new IDialog.OnClickBottomListener() {
                @Override
                public void onPositiveClick() {
                    AppSharedPreferenceUtil.logout(InfoActivity.this);
                    ActivityUtils.finishAllActivities();
                    ActivityUtils.startActivity(new Intent(InfoActivity.this, MainActivity.class).setAction(Intent.ACTION_DEFAULT));
                    dialog.dismiss();
                }

                @Override
                public void onNegtiveClick() {
                    dialog.dismiss();
                }

                @Override
                public void onCloseClick() {
                    dialog.dismiss();
                }
            });
            dialog.show();
        }
    }

    public void takePicture(int requestCode) {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        takePictureIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            takeImageFile = FileUtil.createFileInternal(InfoActivity.this, getString(R.string.image_prefix), ".jpg");
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
        intent.putExtra("outPath", FileUtil.createFileInternal(this, getString(R.string.image_prefix), ".png").getAbsolutePath());
        startActivityForResult(intent, requestCode);
    }

    public void setAvatarUrl(String path) {
        user.setAvatar(ImageUploadRequest.upload("file://" + path));
        UserAuthRequest.update(user);
        Glide.with(InfoActivity.this).load(user.getAvatar()).apply(new RequestOptions().error(R.drawable.ic_state_image_load_fail).placeholder(R.drawable.ic_state_background)).into(avatarEntry);
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
            case 11:
                if (resultCode == RESULT_OK) {
                    String outPath = data.getStringExtra(EXTRA_OUTPUT_URI);
                    if (!TextUtils.isEmpty(outPath)) {
                        setAvatarUrl(outPath);
                    }
                } else if (resultCode == RESULT_ERROR) {
                    IToast.makeTextBottom(this, getString(R.string.not_support_image_type), Toast.LENGTH_SHORT).show();

                }
                break;
        }
    }
}
