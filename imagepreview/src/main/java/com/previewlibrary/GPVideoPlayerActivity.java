package com.previewlibrary;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.WindowManager;
import android.widget.Toast;
import android.widget.VideoView;

import com.cloudchewie.util.ui.StatusBarUtil;

public class GPVideoPlayerActivity extends Activity {

    VideoView videoView;

    /***
     * 启动播放视频
     * @param   context context
     * @param  url url
     * ***/
    public static void startActivity(Context context, String url) {
        Intent intent = new Intent(context, GPVideoPlayerActivity.class);
        intent.putExtra("url", url);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_gpvideoplayer);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        StatusBarUtil.setStatusBarTransparent(this);
        videoView = findViewById(R.id.gpVideo);
        videoView.setVideoPath(getIntent().getStringExtra("url"));
        videoView.setOnErrorListener((mp, what, extra) -> {
            Toast.makeText(GPVideoPlayerActivity.this, R.string.Playback_failed, Toast.LENGTH_SHORT).show();
            return false;
        });
        videoView.start();

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (!videoView.isPlaying()) {
            videoView.start();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        videoView.pause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        videoView.stopPlayback();
    }
}
