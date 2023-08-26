package cn.jiguang.imui.messages;

import android.graphics.drawable.AnimationDrawable;
import android.media.AudioManager;
import android.text.TextUtils;
import android.util.Log;
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

import java.io.FileInputStream;
import java.io.IOException;

import cn.jiguang.imui.BuildConfig;
import cn.jiguang.imui.R;
import cn.jiguang.imui.commons.models.IMessage;
import cn.jiguang.imui.view.RoundTextView;

public class VoiceViewHolder<MESSAGE extends IMessage> extends BaseMessageViewHolder<MESSAGE> implements MsgListAdapter.DefaultMessageViewHolder, ViewHolderController.ReplayVoiceListener {

    private boolean mIsSender;
    private RoundTextView mDateTv;
    private TextView mDisplayNameTv;
    private RoundImageView mAvatarIv;
    private ImageView mVoiceIv;
    private TextView mLengthTv;
    private ImageView mUnreadStatusIv;
    private ProgressBar mSendingPb;
    private ImageButton mResendIb;
    private boolean mSetData = false;
    private AnimationDrawable mVoiceAnimation;
    private FileInputStream mFIS;
    private int mSendDrawable;
    private int mReceiveDrawable;
    private int mPlaySendAnim;
    private int mPlayReceiveAnim;
    private ViewHolderController mController;
    private ConstraintLayout mContainer;

    public VoiceViewHolder(View itemView, boolean isSender) {
        super(itemView);
        this.mIsSender = isSender;
        mContainer = itemView.findViewById(R.id.aurora_fl_msgitem_voice_container);
        mDateTv = itemView.findViewById(R.id.aurora_tv_msgitem_date);
        mAvatarIv = itemView.findViewById(R.id.aurora_iv_msgitem_avatar);
        mVoiceIv = itemView.findViewById(R.id.aurora_iv_msgitem_voice_anim);
        mLengthTv = itemView.findViewById(R.id.aurora_tv_voice_length);
        if (!isSender) {
            mUnreadStatusIv = itemView.findViewById(R.id.aurora_iv_msgitem_read_status);
            mDisplayNameTv = itemView.findViewById(R.id.aurora_tv_msgitem_receiver_display_name);
        } else {
            mSendingPb = itemView.findViewById(R.id.aurora_pb_msgitem_sending);
            mDisplayNameTv = itemView.findViewById(R.id.aurora_tv_msgitem_sender_display_name);
        }
        mResendIb = itemView.findViewById(R.id.aurora_ib_msgitem_resend);
        mController = ViewHolderController.getInstance();
        mController.setReplayVoiceListener(this);
    }

    @NonNull
    @Contract(pure = true)
    public static String getDurationString(long duration) {
        return (duration % (1000 * 60)) / 1000 == 0 ? "1''" : (duration % (1000 * 60)) / 1000 + "''";
    }

    @Override
    public void onBind(@NonNull final MESSAGE message) {
        mMediaPlayer.setAudioStreamType(AudioManager.STREAM_RING);
        mMediaPlayer.setOnErrorListener((mp, what, extra) -> false);
        String timeString = message.getTimeString();
        mDateTv.setVisibility(View.VISIBLE);
        if (timeString != null && !TextUtils.isEmpty(timeString)) {
            mDateTv.setText(timeString);
        } else {
            mDateTv.setVisibility(View.GONE);
        }
        boolean isAvatarExists = message.getFromUser().getAvatarFilePath() != null && !message.getFromUser().getAvatarFilePath().isEmpty();
        if (isAvatarExists && mImageLoader != null) {
            mImageLoader.loadAvatarImage(mAvatarIv, message.getFromUser().getAvatarFilePath());
        }
        long duration = message.getDuration();
        int width = (int) (5.526 * (duration / 1000) + 50.214);
        mContainer.setMinWidth((int) (width * mDensity));
        mLengthTv.setText(getDurationString(duration));
        if (mDisplayNameTv.getVisibility() == View.VISIBLE) {
            mDisplayNameTv.setText(message.getFromUser().getDisplayName());
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
        } else {
            switch (message.getMessageStatus()) {
                case RECEIVE_FAILED:
                    mResendIb.setVisibility(View.VISIBLE);
                    mResendIb.setOnClickListener(v -> {
                        if (mMsgStatusViewClickListener != null) {
                            mMsgStatusViewClickListener.onStatusViewClick(message);
                        }
                    });
                    break;
                case RECEIVE_SUCCEED:
                    mResendIb.setVisibility(View.GONE);
                    break;
            }
        }

        mContainer.setOnClickListener(view -> {
            if (mMsgClickListener != null) {
                mMsgClickListener.onMessageClick(view, message, this);
            }
            mController.notifyAnimStop();
            mController.setMessage(message);
            if (mIsSender) {
                mVoiceIv.setImageResource(mPlaySendAnim);
            } else {
                mVoiceIv.setImageResource(mPlayReceiveAnim);
            }
            mVoiceAnimation = (AnimationDrawable) mVoiceIv.getDrawable();
            mController.addView(getAdapterPosition(), mVoiceIv);
            // If audio is playing, pause
            if (mController.getLastPlayPosition() == getAdapterPosition()) {
                if (mMediaPlayer.isPlaying()) {
                    pauseVoice();
                    mVoiceAnimation.stop();
                    if (mIsSender) {
                        mVoiceIv.setImageResource(mSendDrawable);
                    } else {
                        mVoiceIv.setImageResource(mReceiveDrawable);
                    }
                } else if (mSetData) {
                    mMediaPlayer.start();
                    mVoiceAnimation.start();
                } else {
                    playVoice(getAdapterPosition(), message);
                }
            } else {
                playVoice(getAdapterPosition(), message);
            }
        });

        mContainer.setOnLongClickListener(view -> {
            if (mMsgLongClickListener != null) {
                mMsgLongClickListener.onMessageLongClick(view, message);
            } else {
                if (BuildConfig.DEBUG) {
                    Log.w("MsgListAdapter", "Didn't set long click listener! Drop event.");
                }
            }
            return true;
        });

        mAvatarIv.setOnClickListener(view -> {
            if (mAvatarClickListener != null) {
                mAvatarClickListener.onAvatarClick(message);
            }
        });
    }

    public void playVoice(int position, @NonNull MESSAGE message) {
        mController.setLastPlayPosition(position, mIsSender);
        try {
            mMediaPlayer.reset();
            mMediaPlayer.setDataSource(new FileInputStream(message.getFilePath()).getFD());
            mMediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
            mMediaPlayer.prepare();
            mMediaPlayer.setOnPreparedListener(mp -> {
                mVoiceAnimation.start();
                mp.start();
            });
            mMediaPlayer.setOnCompletionListener(mp -> {
                mVoiceAnimation.stop();
                mp.reset();
                mSetData = false;
                if (mIsSender) {
                    mVoiceIv.setImageResource(mSendDrawable);
                } else {
                    mVoiceIv.setImageResource(mReceiveDrawable);
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (mFIS != null) {
                    mFIS.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    public void pauseVoice() {
        mMediaPlayer.pause();
        mVoiceAnimation.stop();
        mSetData = true;
    }

    public void releaseMediaPlayer() {
        if (mMediaPlayer != null) {
            mMediaPlayer.stop();
            mMediaPlayer.reset();
            mMediaPlayer.release();
            mMediaPlayer = null;
        }
        mSetData = true;
    }

    @Override
    public void applyStyle(@NonNull MessageListStyle style) {
        mContainer.setMaxWidth((int) (style.getWindowWidth() * style.getBubbleMaxWidth() / 1.5));
        mDateTv.setTextSize(style.getDateTextSize());
        mDateTv.setTextColor(style.getDateTextColor());
        mDateTv.setPadding(style.getDatePaddingLeft(), style.getDatePaddingTop(), style.getDatePaddingRight(), style.getDatePaddingBottom());
        mDateTv.setBgCornerRadius(style.getDateBgCornerRadius());
        mDateTv.setBgColor(style.getDateBgColor());
        mSendDrawable = style.getSendVoiceDrawable();
        mReceiveDrawable = style.getReceiveVoiceDrawable();
        mController.setDrawable(mSendDrawable, mReceiveDrawable);
        mPlaySendAnim = style.getPlaySendVoiceAnim();
        mPlayReceiveAnim = style.getPlayReceiveVoiceAnim();
        if (mIsSender) {
            mVoiceIv.setImageResource(mSendDrawable);
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
            mVoiceIv.setImageResource(mReceiveDrawable);
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
        android.view.ViewGroup.LayoutParams layoutParams = mAvatarIv.getLayoutParams();
        layoutParams.width = style.getAvatarWidth();
        layoutParams.height = style.getAvatarHeight();
        mAvatarIv.setLayoutParams(layoutParams);
        mAvatarIv.setBorderRadius(TypedValue.COMPLEX_UNIT_PX, style.getAvatarRadius());
    }

    @Override
    public void replayVoice() {
        pauseVoice();
        mController.notifyAnimStop();
        if (mIsSender) {
            mVoiceIv.setImageResource(mPlaySendAnim);
        } else {
            mVoiceIv.setImageResource(mPlayReceiveAnim);
        }
        mVoiceAnimation = (AnimationDrawable) mVoiceIv.getBackground();
        playVoice(mController.getLastPlayPosition(), (MESSAGE) mController.getMessage());
    }
}