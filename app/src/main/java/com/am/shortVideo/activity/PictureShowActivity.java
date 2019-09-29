package com.am.shortVideo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.am.shortVideo.R;

import java.util.ArrayList;
import java.util.List;

import base.BaseActivity;

/**
 * Created by JC on 2019/8/6.
 */

public class PictureShowActivity extends BaseActivity {
    private RecyclerView recycle_picture;
    private List<String> picturedatas = new ArrayList<>();

    @Override
    protected int getLayout() {
        return R.layout.recycle_loadpicture;
    }

    @Override
    protected void initEventAndData() {
        initView();
        initData();
        setRecycleParams();

    }

    private void setRecycleParams() {

    }

    private void initData() {
        picturedatas.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201805/100651/201805181532123423.mp4");
        picturedatas.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201803/100651/201803151735198462.mp4");
        picturedatas.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201803/100651/201803150923220770.mp4");
        picturedatas.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201803/100651/201803150922255785.mp4");
        picturedatas.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201803/100651/201803150920130302.mp4");
        picturedatas.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201803/100651/201803141625005241.mp4");
        picturedatas.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201803/100651/201803141624378522.mp4");
        picturedatas.add("http://chuangfen.oss-cn-hangzhou.aliyuncs.com/public/attachment/201803/100651/201803131546119319.mp4");

    }

    private void initView() {
        recycle_picture = (RecyclerView) findViewById(R.id.recycle_picture);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(this, 2);
        recycle_picture.setLayoutManager(gridLayoutManager);

    }

}
