package com.cloudchewie.ingenuity.widget

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import com.cloudchewie.ingenuity.R
import com.cloudchewie.ingenuity.activity.authenticator.AuthenticatorDetailActivity
import com.cloudchewie.ingenuity.entity.OtpToken
import com.cloudchewie.ingenuity.entity.TokenCode
import com.cloudchewie.ingenuity.util.authenticator.OtpTokenParser
import com.cloudchewie.ingenuity.util.authenticator.TokenImageUtil
import com.cloudchewie.ingenuity.util.enumeration.OtpTokenType
import com.cloudchewie.ui.custom.ImageDialog
import com.google.zxing.BarcodeFormat
import com.google.zxing.qrcode.QRCodeWriter


class TokenLayout : RelativeLayout, View.OnClickListener, Runnable {
    private val tag = TokenLayout::class.java.simpleName
    private lateinit var mProgressInner: ProgressCircle
    private lateinit var mProgressOuter: ProgressCircle

    private lateinit var mImage: ImageView
    private lateinit var mCode: TextView
    private lateinit var mIssuer: TextView
    private lateinit var mAccount: TextView
    private lateinit var mDetail: ImageView
    private lateinit var mQrcode: ImageView

    private var mCodes: TokenCode? = null
    private var mType: OtpTokenType? = null
    private var mPlaceholder: String? = null
    private var mStartTime: Long = 0

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context, attrs, defStyle
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        mProgressInner = findViewById<View>(R.id.item_token_progress_inner) as ProgressCircle
        mProgressOuter = findViewById<View>(R.id.item_token_progress_outer) as ProgressCircle
        mImage = findViewById<View>(R.id.item_token_image) as ImageView
        mCode = findViewById<View>(R.id.item_token_code) as TextView
        mIssuer = findViewById<View>(R.id.item_token_issuer) as TextView
        mAccount = findViewById<View>(R.id.item_token_account) as TextView
        mDetail = findViewById<View>(R.id.item_token_detail) as ImageView
        mQrcode = findViewById<View>(R.id.item_token_qrcode) as ImageView
    }

    fun bind(token: OtpToken) {
        mCodes = null

        // Cancel all active animations.
        isEnabled = true
        removeCallbacks(this)
        mImage.clearAnimation()
        mProgressInner.clearAnimation()
        mProgressOuter.clearAnimation()
        mProgressInner.visibility = View.GONE
        mProgressOuter.visibility = View.GONE

        // Get the code placeholder.
        val placeholder = CharArray(token.digits)
        for (i in placeholder.indices) placeholder[i] = '*'
        mPlaceholder = String(placeholder)

        // Show the image.
        TokenImageUtil.setTokenImage(mImage, token)

        mAccount.text = token.account
        mIssuer.text = token.issuer
        mCode.text = mPlaceholder
        if (mIssuer.text.isEmpty()) {
            mIssuer.text = token.account
            mAccount.visibility = View.GONE
        } else {
            mAccount.visibility = View.VISIBLE
        }

        mDetail.setOnClickListener {
            val intent = Intent(
                context,
                AuthenticatorDetailActivity::class.java
            ).setAction(Intent.ACTION_DEFAULT)
            intent.putExtra("token_id", token.id)
            startActivity(context, intent, null)
        }
        mQrcode.setOnClickListener {
            val dialog = ImageDialog(context)
            dialog.setTitle(token.issuer)
            dialog.setButtonText("完成")
            dialog.show()
            dialog.tipTv.visibility = GONE
            dialog.imageView.setImageBitmap(generateQrCode(token))
        }
    }

    private fun generateQrCode(token: OtpToken): Bitmap? {
        val qrcodeWriter = QRCodeWriter()
        val qrCodeSize = resources.getDimensionPixelSize(R.dimen.dp250)
        val encoded = qrcodeWriter.encode(
            OtpTokenParser.toUri(token).toString(),
            BarcodeFormat.QR_CODE,
            qrCodeSize,
            qrCodeSize
        )
        Log.d("xuruida", "encoded:$encoded")
        val pixels = IntArray(qrCodeSize * qrCodeSize)
        for (x in 0 until qrCodeSize) {
            for (y in 0 until qrCodeSize) {
                if (encoded.get(x, y)) {
                    pixels[x * qrCodeSize + y] = Color.BLACK
                } else {
                    pixels[x * qrCodeSize + y] = Color.WHITE
                }
            }
        }
        return Bitmap.createBitmap(pixels, qrCodeSize, qrCodeSize, Bitmap.Config.RGB_565)
    }

    private fun animate(view: View, anim: Int, animate: Boolean) {
        val a = AnimationUtils.loadAnimation(view.context, anim)
        if (!animate) a.duration = 0
        view.startAnimation(a)
    }

    fun start(type: OtpTokenType, codes: TokenCode, animate: Boolean) {
        mCodes = codes
        mType = type

        // Start animations.
        mProgressInner.visibility = View.VISIBLE
        animate(mProgressInner, R.anim.anim_fade_in, animate)

        when (type) {
            OtpTokenType.HOTP -> isEnabled = false
            OtpTokenType.TOTP -> {
                mProgressOuter.visibility = View.VISIBLE
                animate(mProgressOuter, R.anim.anim_fade_in, animate)
            }
        }

        mStartTime = System.currentTimeMillis()
        post(this)
    }

    override fun onClick(v: View) {
    }

    override fun run() {
        val code = mCodes?.currentCode ?: run {
            mCode.text = mPlaceholder
            mProgressInner.visibility = View.GONE
            mProgressOuter.visibility = View.GONE
            return
        }

        val currentProgress = mCodes?.currentProgress ?: run {
            Log.w(tag, "Token current progress is null")
            return
        }

        val totalProgress = mCodes?.totalProgress ?: run {
            Log.w(tag, "Token total progress is null")
            return
        }

        if (!isEnabled) isEnabled = System.currentTimeMillis() - mStartTime > 5000

        mCode.text = code
        mProgressInner.setProgress(currentProgress)
        if (mType != OtpTokenType.HOTP) mProgressOuter.setProgress(totalProgress)

        postDelayed(this, 100)
    }
}
