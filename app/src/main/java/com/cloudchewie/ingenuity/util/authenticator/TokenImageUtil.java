package com.cloudchewie.ingenuity.util.authenticator;

import android.text.TextUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.cloudchewie.ingenuity.R;
import com.cloudchewie.ingenuity.entity.OtpToken;
import com.cloudchewie.ingenuity.widget.TokenImage;
import com.cloudchewie.ui.textdrawable.ColorGenerator;
import com.cloudchewie.ui.textdrawable.TextDrawable;

import java.util.Locale;

public class TokenImageUtil {
    public static void setTokenImage(ImageView imageView, OtpToken token) {
        if (token.getImagePath() != null) {
            Glide.with(imageView).load(token.getImagePath()).placeholder(R.drawable.ic_light_qrcode).into(imageView);
        } else if (!TextUtils.isEmpty(token.getIssuer())) {
            Integer integer = matchIssuerWithTokenThumbnail(token);
            if (integer != null) {
                imageView.setImageResource(integer);
            } else {
                String tokenText = token.getIssuer() != null ? token.getIssuer().substring(0, 1) : "";
                int color = ColorGenerator.MATERIAL.getColor(tokenText);
                TextDrawable tokenTextDrawable = TextDrawable.builder().buildRoundRect(tokenText, color, 10);
                imageView.setImageDrawable(tokenTextDrawable);
            }
        } else {
            imageView.setImageResource(R.drawable.ic_light_qrcode);
        }
    }

    public static Integer matchIssuerWithTokenThumbnail(OtpToken token) {
        for (TokenImage tokenImage : com.cloudchewie.ingenuity.widget.TokenImage.values()) {
            if (matchToken(tokenImage, token)) {
                return tokenImage.getResource();
            }
        }
        return null;
    }

    public static boolean matchToken(TokenImage tokenImage, OtpToken token) {
        String issuerToMatch = tokenImage.getIssuer() != null ? tokenImage.getIssuer() : tokenImage.name();

        boolean issuerMatched = token.getIssuer().toLowerCase(Locale.getDefault()).contains(issuerToMatch.toLowerCase(Locale.getDefault()));

        if (!issuerMatched && tokenImage.getAlsoMatchLabel()) {
            return token.getAccount().toLowerCase(Locale.getDefault()).contains(issuerToMatch.toLowerCase(Locale.getDefault()));
        } else {
            return issuerMatched;
        }
    }
}
