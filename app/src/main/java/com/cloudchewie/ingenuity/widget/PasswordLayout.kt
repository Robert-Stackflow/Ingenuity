package com.cloudchewie.ingenuity.widget

import android.content.Context
import android.content.Intent
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import com.cloudchewie.ingenuity.R
import com.cloudchewie.ingenuity.activity.passwordbox.AuthPasswordDetailActivity
import com.cloudchewie.ingenuity.activity.passwordbox.BackupPasswordDetailActivity
import com.cloudchewie.ingenuity.activity.passwordbox.CommonPasswordDetailActivity
import com.cloudchewie.ingenuity.entity.Password
import com.cloudchewie.ingenuity.entity.PasswordGroup
import com.cloudchewie.ingenuity.util.authenticator.TokenImageUtil


class PasswordLayout : RelativeLayout, View.OnClickListener {
    private val tag = PasswordLayout::class.java.simpleName
    private lateinit var mImage: ImageView
    private lateinit var mCode: TextView
    private lateinit var mIssuer: TextView
    private lateinit var mUsername: TextView
    private lateinit var mDetail: ImageView

    private var mType: PasswordGroup.PasswordType? = null
    private var mPlaceholder: String? = null
    private var mStartTime: Long = 0
    private var mTruePassword: String? = null

    constructor(context: Context) : super(context)

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs)

    constructor(context: Context, attrs: AttributeSet, defStyle: Int) : super(
        context, attrs, defStyle
    )

    override fun onFinishInflate() {
        super.onFinishInflate()
        mImage = findViewById<View>(R.id.item_password_image) as ImageView
        mCode = findViewById<View>(R.id.item_password_code) as TextView
        mIssuer = findViewById<View>(R.id.item_password_issuer) as TextView
        mUsername = findViewById<View>(R.id.item_password_username) as TextView
        mDetail = findViewById<View>(R.id.item_password_detail) as ImageView
    }

    fun bind(passwordGroup: PasswordGroup, password: Password) {
        isEnabled = true
        mImage.clearAnimation()

        val length = if (password.password != null) password.password.length else 0
        val placeholder = CharArray(length)
        for (i in placeholder.indices) placeholder[i] = '*'
        mPlaceholder = String(placeholder)
        mTruePassword = password.password

        TokenImageUtil.setPasswordImage(mImage, password)

        mUsername.text = password.username
        mIssuer.text = password.issuer
        mCode.text = mPlaceholder
        if (mIssuer.text.isEmpty()) {
            mIssuer.text = password.username
            mUsername.visibility = View.GONE
        } else {
            mUsername.visibility = View.VISIBLE
        }

        mDetail.setOnClickListener {
            when (passwordGroup.type) {
                PasswordGroup.PasswordType.COMMON -> {
                    val intent: Intent = Intent(
                        context,
                        CommonPasswordDetailActivity::class.java
                    ).setAction(Intent.ACTION_DEFAULT)
                    intent.putExtra(
                        CommonPasswordDetailActivity.EXTRA_GROUP_ID,
                        passwordGroup.id
                    )
                    intent.putExtra(
                        CommonPasswordDetailActivity.EXTRA_PASSWORD_ID,
                        password.id
                    )
                    context.startActivity(intent)
                }

                PasswordGroup.PasswordType.AUTH -> {
                    val intent: Intent = Intent(
                        context,
                        AuthPasswordDetailActivity::class.java
                    ).setAction(Intent.ACTION_DEFAULT)
                    intent.putExtra(
                        AuthPasswordDetailActivity.EXTRA_GROUP_ID,
                        passwordGroup.id
                    )
                    intent.putExtra(
                        AuthPasswordDetailActivity.EXTRA_PASSWORD_ID,
                        password.id
                    )
                    context.startActivity(intent)
                }

                PasswordGroup.PasswordType.BACKUP -> {
                    val intent: Intent = Intent(
                        context,
                        BackupPasswordDetailActivity::class.java
                    ).setAction(Intent.ACTION_DEFAULT)
                    intent.putExtra(
                        BackupPasswordDetailActivity.EXTRA_GROUP_ID,
                        passwordGroup.id
                    )
                    intent.putExtra(
                        BackupPasswordDetailActivity.EXTRA_PASSWORD_ID,
                        password.id
                    )
                    context.startActivity(intent)
                }
            }
        }
    }

    override fun onClick(v: View) {
    }

    fun setState(show: Boolean) {
        if (show) {
            mCode.text = mTruePassword
        } else {
            mCode.text = mPlaceholder
        }
    }

    fun toggle() {
        if (mCode.text == mPlaceholder) {
            mCode.text = mTruePassword
        } else {
            mCode.text = mPlaceholder
        }
    }
}
