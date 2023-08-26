package com.cloudchewie.ingenuity.fragment.global;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.content.res.AppCompatResources;

import com.cloudchewie.ingenuity.R;

public class StateFragment extends BaseFragment {
    int imageId;
    String str;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            str = getArguments().getString("stateText", "正在开发中~~");
            imageId = getArguments().getInt("stateImageId", R.drawable.ic_state_under_development);
        } else {
            str = "正在开发中~~";
            imageId = R.drawable.ic_state_under_development;
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = View.inflate(getContext(), R.layout.fragment_state, null);
        if (AppCompatResources.getDrawable(getContext(), imageId) != null)
            ((ImageView) view.findViewById(R.id.fragment_state_image)).setImageResource(imageId);
        ((TextView) view.findViewById(R.id.fragment_state_text)).setText(str);
        return view;
    }
}
