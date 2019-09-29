package com.am.shortVideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;

import com.am.shortVideo.R;

import customeview.FullScreenLoopVideo;
import customeview.ShowLoopVideo;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by JC on 2019/9/9.
 */

public class LookFullScreenVideo extends AppCompatActivity implements View.OnClickListener, FullScreenLoopVideo.VideoBackCallBack {
    private FullScreenLoopVideo fullscreen_videoplayer;
    private String url_video;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.lookfullscreenvideo_layout);
        getVideoData();
        initView();
        setOnClickListener();
        startPlayVideo();
    }

    private void startPlayVideo() {
        fullscreen_videoplayer.setUp(url_video, JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN);
        fullscreen_videoplayer.startVideo();

    }

    private void setOnClickListener() {
        fullscreen_videoplayer.setOnFininshLinstener(this);
    }

    private void initView() {
        fullscreen_videoplayer=(FullScreenLoopVideo) findViewById(R.id.fullscreen_videoplayer);

    }
    private void  getVideoData(){
            Intent intent=getIntent();
         url_video=intent.getStringExtra("videopalyer_url");
    }


    @Override
    public void onClick(View v) {
        switch(v.getId()){

        }
    }


    @Override
    public void backfinish() {
        JCVideoPlayer.releaseAllVideos();
        finish();
    }
}
