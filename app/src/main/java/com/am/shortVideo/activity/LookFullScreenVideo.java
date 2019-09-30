package com.am.shortVideo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.am.shortVideo.R;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;

import customeview.ShortVideoPlayer;

/**
 * Created by JC on 2019/9/9.
 */

public class LookFullScreenVideo extends AppCompatActivity{
    private ShortVideoPlayer fullscreen_videoplayer;
    private String url_video;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookfullscreenvideo_layout);
        getVideoData();
        initView();
        startPlayVideo();
    }

    private void startPlayVideo() {
        GSYVideoOptionBuilder videoBuilder = new GSYVideoOptionBuilder();
        videoBuilder
                .setRotateViewAuto(true)
                .setIsTouchWiget(false)
                .setLooping(true)
                .setAutoFullWithSize(true)
                .setIsTouchWiget(false)
                .setIsTouchWigetFull(false)
                .setShowFullAnimation(true)
                .setUrl(url_video)
                .setCacheWithPlay(true)
                .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                    @Override
                    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {

                    }
                })
                .setVideoAllCallBack(new GSYSampleCallBack() {

                    @Override
                    public void onStartPrepared(String url, Object... objects) {
                        super.onStartPrepared(url, objects);
                    }

                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                    }

                    @Override
                    public void onClickStop(String url, Object... objects) {

                    }

                    @Override
                    public void onClickResume(String url, Object... objects) {

                    }

                    @Override
                    public void onPlayError(String url, Object... objects) {
                        super.onPlayError(url, objects);
                    }
                })
                .build(fullscreen_videoplayer);
        fullscreen_videoplayer.getCurrentPlayer().startPlayLogic();
    }

    private void initView() {
        fullscreen_videoplayer = findViewById(R.id.fullscreen_videoplayer);
    }

    private void getVideoData() {
        Intent intent = getIntent();
        url_video = intent.getStringExtra("videopalyer_url");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        fullscreen_videoplayer.getCurrentPlayer().release();
    }
}
