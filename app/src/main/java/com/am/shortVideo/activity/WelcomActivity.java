package com.am.shortVideo.activity;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;

import com.am.shortVideo.R;

import base.BaseActivity;

/**
 * Created by 李杰 on 2019/9/16.
 */

public class WelcomActivity extends BaseActivity implements View.OnClickListener {
    private TextView tv_welcome;
    private int count = 3;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    count--;
                    if (count != 0) {
                        tv_welcome.setText(count + "s跳过");
                        handler.sendEmptyMessageDelayed(1, 1000);
                    } else {
                        count = 3;
                        Intent intent = new Intent(WelcomActivity.this, MainActivity.class);
                        startActivity(intent);
                        finish();
                    }

                    break;
            }
        }
    };

    private void initView() {
        tv_welcome = (TextView) findViewById(R.id.tv_welcome);
        tv_welcome.setOnClickListener(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        tv_welcome.setText(count + "s跳过");
        handler.sendEmptyMessageDelayed(1, 1000);
    }

    @Override
    protected int getLayout() {
        return R.layout.welcome_layout;
    }

    @Override
    protected void initEventAndData() {
        initView();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_welcome:
                Intent intent = new Intent(WelcomActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK && event.getRepeatCount() == 0) {
            return true;
        }
        return false;
    }
}
