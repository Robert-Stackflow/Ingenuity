package com.previewlibrary.view;

import android.content.Context;

import androidx.annotation.NonNull;

import com.cloudchewie.ui.general.BottomSheet;
import com.cloudchewie.ui.custom.IToast;
import com.previewlibrary.GPreviewActivity;
import com.previewlibrary.R;
import com.previewlibrary.enitity.IThumbViewInfo;

public class MyImageOperationButtonClickedListener implements GPreviewActivity.OnOperationButtonClickedListener {
    @Override
    public void onCloseClicked(Context context) {
    }

    @Override
    public void onDownloadClicked(Context context, @NonNull IThumbViewInfo imageViewInfo) {
        IToast.showBottom(context, context.getString(R.string.download) + imageViewInfo.getUrl());
    }

    @Override
    public void onMoreClicked(Context context, @NonNull IThumbViewInfo imageViewInfo) {
        BottomSheet bottomSheet = new BottomSheet(context);
        bottomSheet.setMainLayout(com.cloudchewie.ui.R.layout.layout_image_operation);
        bottomSheet.show();
        bottomSheet.findViewById(R.id.image_operation_cancel).setOnClickListener(v1 -> bottomSheet.cancel());
    }
}