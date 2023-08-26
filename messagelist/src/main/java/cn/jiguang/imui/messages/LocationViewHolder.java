package cn.jiguang.imui.messages;

import android.annotation.SuppressLint;
import android.text.TextUtils;
import android.util.TypedValue;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.cloudchewie.ui.general.RoundImageView;

import org.jetbrains.annotations.Contract;

import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.view.RoundTextView;

public class LocationViewHolder<Message extends IMessage> extends BaseMessageViewHolder<Message> implements MsgListAdapter.DefaultMessageViewHolder {

    private final RoundTextView mDateTv;
    private final RoundImageView mImageAvatar;
    private final ImageView mImageCover;
    private final TextView mTvDescribe;
    private final TextView mTvName;
    private TextView mDisplayNameTv;
    private boolean mIsSender;
    private ProgressBar mSendingPb;
    private ImageButton mResendIb;
    private ConstraintLayout mContainer;

    public LocationViewHolder(View itemView, boolean isSender) {
        super(itemView);
        this.mIsSender = isSender;
        mDateTv = itemView.findViewById(R.id.aurora_tv_msgitem_date);
        mImageAvatar = itemView.findViewById(R.id.aurora_iv_msgitem_avatar);
        mImageCover = itemView.findViewById(R.id.aurora_tv_location_image);
        mTvDescribe = itemView.findViewById(R.id.aurora_tv_location_describe);
        mTvName = itemView.findViewById(R.id.aurora_tv_location_name);
        mContainer = itemView.findViewById(R.id.aurora_tv_location_container);
        if (isSender) {
            mSendingPb = itemView.findViewById(R.id.aurora_pb_msgitem_sending);
            mResendIb = itemView.findViewById(R.id.aurora_ib_msgitem_resend);
            mDisplayNameTv = itemView.findViewById(R.id.aurora_tv_msgitem_sender_display_name);
        } else {
            mDisplayNameTv = itemView.findViewById(R.id.aurora_tv_msgitem_receiver_display_name);
        }
    }

    @NonNull
    @Contract(pure = true)
    public static String getDurationString(long duration) {
        long hours = (duration % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60);
        long minutes = (duration % (1000 * 60 * 60)) / (1000 * 60);
        long seconds = (duration % (1000 * 60)) / 1000;

        String hourStr = (hours < 10) ? "0" + hours : hours + "";
        String minuteStr = (minutes < 10) ? "0" + minutes : minutes + "";
        String secondStr = (seconds < 10) ? "0" + seconds : seconds + "";

        if (hours != 0) {
            return hourStr + ":" + minuteStr + ":" + secondStr;
        } else {
            return minuteStr + ":" + secondStr;
        }
    }

    @SuppressLint("DefaultLocale")
    @NonNull
    @Contract(pure = true)
    public static String getSizeString(long size) {
        double size_kb = size / 1000.0;
        if (size_kb <= 0)
            return size + "B";
        double size_mb = size_kb / 1000.0;
        if (size_mb <= 0)
            return String.format("%.2fKB", size_kb);
        return String.format("%.2fMB", size_mb);
    }

    @Override
    public void onBind(@NonNull final Message message) {
        String timeString = message.getTimeString();
        mDateTv.setVisibility(View.VISIBLE);
        if (timeString != null && !TextUtils.isEmpty(timeString)) {
            mDateTv.setText(timeString);
        } else {
            mDateTv.setVisibility(View.GONE);
        }
        boolean isAvatarExists = message.getFromUser().getAvatarFilePath() != null && !message.getFromUser().getAvatarFilePath().isEmpty();
        mContainer.setOnClickListener(view -> {
            if (mMsgClickListener != null) mMsgClickListener.onMessageClick(view, message, this);
        });
        mContainer.setOnLongClickListener(view -> {
            if (mMsgLongClickListener != null)
                mMsgLongClickListener.onMessageLongClick(view, message);
            return false;
        });
        mTvName.setText(message.getLocationName());
        mTvDescribe.setText(message.getLocationDetail());
        if (mDisplayNameTv.getVisibility() == View.VISIBLE) {
            mDisplayNameTv.setText(message.getFromUser().getDisplayName());
        }
        if (mImageLoader != null) {
            if (message.getLocationScreenShot() != null && !message.getLocationScreenShot().isEmpty()) {
                mImageLoader.loadImage(mImageCover, message.getLocationScreenShot(), 10, false, false, true, true);
            }
        }
        if (mIsSender) {
            switch (message.getMessageStatus()) {
                case SEND_GOING:
                    mSendingPb.setVisibility(View.VISIBLE);
                    mResendIb.setVisibility(View.GONE);
                    break;
                case SEND_FAILED:
                    mSendingPb.setVisibility(View.GONE);
                    mResendIb.setVisibility(View.VISIBLE);
                    mResendIb.setOnClickListener(v -> {
                        if (mMsgStatusViewClickListener != null) {
                            mMsgStatusViewClickListener.onStatusViewClick(message);
                        }
                    });
                    break;
                case SEND_SUCCEED:
                    mSendingPb.setVisibility(View.GONE);
                    mResendIb.setVisibility(View.GONE);
                    break;
            }
        }

        if (mImageLoader != null) {
            if (isAvatarExists) {
                mImageLoader.loadAvatarImage(mImageAvatar, message.getFromUser().getAvatarFilePath());
            }
        }

        mImageAvatar.setOnClickListener(view -> {
            if (mAvatarClickListener != null) {
                mAvatarClickListener.onAvatarClick(message);
            }
        });
    }

    @Override
    public void applyStyle(MessageListStyle style) {
        mDateTv.setTextSize(style.getDateTextSize());
        mDateTv.setTextColor(style.getDateTextColor());
        mDateTv.setPadding(style.getDatePaddingLeft(), style.getDatePaddingTop(), style.getDatePaddingRight(), style.getDatePaddingBottom());
        mDateTv.setBgCornerRadius(style.getDateBgCornerRadius());
        mDateTv.setBgColor(style.getDateBgColor());
        if (mIsSender) {
            if (style.getSendingProgressDrawable() != null) {
                mSendingPb.setProgressDrawable(style.getSendingProgressDrawable());
            }
            if (style.getSendingIndeterminateDrawable() != null) {
                mSendingPb.setIndeterminateDrawable(style.getSendingIndeterminateDrawable());
            }
            if (style.getShowSenderDisplayName()) {
                mDisplayNameTv.setVisibility(View.VISIBLE);
            } else {
                mDisplayNameTv.setVisibility(View.GONE);
            }
        } else {
            if (style.getShowReceiverDisplayName()) {
                mDisplayNameTv.setVisibility(View.VISIBLE);
            } else {
                mDisplayNameTv.setVisibility(View.GONE);
            }
        }
        mDisplayNameTv.setTextSize(style.getDisplayNameTextSize());
        mDisplayNameTv.setTextColor(style.getDisplayNameTextColor());
        mDisplayNameTv.setPadding(style.getDisplayNamePaddingLeft(), style.getDisplayNamePaddingTop(), style.getDisplayNamePaddingRight(), style.getDisplayNamePaddingBottom());
        mDisplayNameTv.setEms(style.getDisplayNameEmsNumber());
        android.view.ViewGroup.LayoutParams layoutParams = mImageAvatar.getLayoutParams();
        layoutParams.width = style.getAvatarWidth();
        layoutParams.height = style.getAvatarHeight();
        mImageAvatar.setLayoutParams(layoutParams);
        mImageAvatar.setBorderRadius(TypedValue.COMPLEX_UNIT_PX, style.getAvatarRadius());
    }
}

