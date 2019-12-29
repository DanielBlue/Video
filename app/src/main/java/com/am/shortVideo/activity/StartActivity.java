package com.am.shortVideo.activity;

import android.content.Intent;

import com.am.shortVideo.R;

import application.MyApplication;
import base.BaseActivity;

public class StartActivity extends BaseActivity {

    @Override
    protected int getLayout() {
        return R.layout.activity_start;
    }

    @Override
    protected void initEventAndData() {
        if (MyApplication.getInstance().getUserInfo() != null) {
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
            finish();
        } else {
            Intent intent = new Intent(this, WelcomActivity.class);
            startActivity(intent);
            finish();
        }
    }
}
