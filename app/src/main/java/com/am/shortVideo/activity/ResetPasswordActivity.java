package com.am.shortVideo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.am.shortVideo.R;

import base.BaseActivity;

/**
 * Created by JC on 2019/9/11.
 */

public class ResetPasswordActivity extends BaseActivity implements View.OnClickListener {
    private TextView fans_title;
    private ImageView iv_back;
    private RelativeLayout rl_password;

    @Override
    protected int getLayout() {
        return R.layout.resetpassword_layout;
    }

    @Override
    protected void initEventAndData() {
        initView();
    }

    private void initView() {
        fans_title = (TextView) findViewById(R.id.bt_systemmessage);
        fans_title.setText("");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        rl_password = (RelativeLayout) findViewById(R.id.rl_password);
        iv_back.setOnClickListener(this);
        rl_password.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
            case R.id.rl_password:
                Intent intent = new Intent(this, ChangePassword.class);
                startActivity(intent);
                break;
        }
    }
}
