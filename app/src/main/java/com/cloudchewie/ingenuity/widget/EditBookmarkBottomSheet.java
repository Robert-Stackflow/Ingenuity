package com.cloudchewie.ingenuity.widget;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;

import com.blankj.utilcode.util.KeyboardUtils;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ui.general.BottomSheet;
import com.cloudchewie.ui.item.InputLayout;

public class EditBookmarkBottomSheet extends BottomSheet {
    Context context;
    String title;
    String name;
    String url;
    EditText nameEdit;
    EditText urlEdit;
    InputLayout nameLayout;
    InputLayout urlLayout;
    Button confirmButton;
    Button cancelButton;
    OnConfirmClickedListener onConfirmClickedListener;

    public EditBookmarkBottomSheet(@NonNull Context context, String title, String name, String url) {
        super(context);
        this.context = context;
        this.title = title;
        this.name = name;
        this.url = url;
        initView();
    }

    public OnConfirmClickedListener getOnConfirmClickedListener() {
        return onConfirmClickedListener;
    }

    public void setOnConfirmClickedListener(OnConfirmClickedListener onConfirmClickedListener) {
        this.onConfirmClickedListener = onConfirmClickedListener;
    }

    @SuppressLint("SetTextI18n")
    void initView() {
        setOnShowListener(dialog -> {
            KeyboardUtils.showSoftInput((Activity) context);
            nameEdit.requestFocus();
        });
        setTitle(title);
        setDragBarVisible(false);
        leftButton.setVisibility(View.GONE);
        rightButton.setVisibility(View.GONE);
        setBackGroundTint(R.color.card_background);
        setTitleBarBackGroundTint(R.color.card_background);
        setMainLayout(R.layout.layout_edit_bookmark);
        confirmButton = mainView.findViewById(R.id.layout_edit_bookmark_confirm);
        nameLayout = mainView.findViewById(R.id.layout_edit_bookmark_name);
        urlLayout = mainView.findViewById(R.id.layout_edit_bookmark_url);
        cancelButton = mainView.findViewById(R.id.layout_edit_bookmark_cancel);
        nameEdit = nameLayout.getEditText();
        urlEdit = urlLayout.getEditText();
        nameEdit.setText(name);
        urlEdit.setText(url);
        nameEdit.setHint(context.getResources().getString(R.string.bookmark_name));
        urlEdit.setHint(context.getResources().getString(R.string.bookmark_url));
        nameEdit.setSelection(nameEdit.getText().toString().length());
        urlEdit.setSelection(urlEdit.getText().toString().length());
        confirmButton.setOnClickListener(v -> {
            if (onConfirmClickedListener != null)
                onConfirmClickedListener.OnConfirmClicked(nameLayout.getText(), urlLayout.getText());
            dismiss();
        });
        cancelButton.setOnClickListener(v -> dismiss());
    }

    public void hideKeyBoard() {
        KeyboardUtils.hideSoftInput((Activity) context);
    }

    public EditText getNameEdit() {
        return nameEdit;
    }

    public EditText getUrlEdit() {
        return urlEdit;
    }

    public interface OnConfirmClickedListener {
        void OnConfirmClicked(String name, String url);
    }
}