package com.cloudchewie.ingenuity.util.authenticator

import android.widget.ImageView
import com.bumptech.glide.Glide
import com.cloudchewie.ingenuity.R
import com.cloudchewie.ingenuity.entity.OtpToken
import com.cloudchewie.ui.textdrawable.ColorGenerator
import com.cloudchewie.ui.textdrawable.TextDrawable
import com.cloudchewie.ingenuity.widget.TokenImage
import com.cloudchewie.ingenuity.widget.matchToken

fun ImageView.setTokenImage(token: OtpToken) {
    when {
        token.imagePath != null -> {
            Glide.with(this)
                .load(token.imagePath)
                .placeholder(R.drawable.ic_light_qrcode)
                .into(this)
        }

        !token.issuer.isNullOrBlank() -> {
            matchIssuerWithTokenThumbnail(token)?.let {
                setImageResource(it)
            } ?: run {
                val tokenText = token.issuer?.substring(0, 1) ?: ""
                val color = ColorGenerator.MATERIAL.getColor(tokenText)
                val tokenTextDrawable = TextDrawable.builder().buildRoundRect(tokenText, color, 10)
                setImageDrawable(tokenTextDrawable)
            }
        }

        else -> {
            setImageResource(R.drawable.ic_light_qrcode)
        }
    }
}

private fun matchIssuerWithTokenThumbnail(token: OtpToken): Int? {
    return TokenImage.values().firstOrNull {
        it.matchToken(token.issuer, token.account)
    }?.resource
}