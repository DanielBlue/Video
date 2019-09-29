package com.am.shortVideo.activity;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import base.BaseActivity;
import bean.SmsBean;
import bean.UserRegister;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;

/**
 * Created by JC on 2019/8/30.
 */

public class RegisterActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "RegisterActivity";
    private EditText et_inputregister;
    private EditText et_inputphoner;
    private EditText et_indentfycode;
    private EditText et_inputpassword;
    private EditText et_confirmpassword;
    private TextView tv_register;
    private TextView tv_code;
    private TextView fans_title;
    private ImageView iv_back;
    private OktHttpUtil okHttpUtil;
    private CountDownTimer timer;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    startCountTime();
                    break;
            }
        }
    };


    @Override
    protected int getLayout() {
        return R.layout.register_layout;
    }

    @Override
    protected void initEventAndData() {
        okHttpUtil = OktHttpUtil.getInstance();
        initView();
        setOnClickListener();
    }

    private void setOnClickListener() {
        tv_register.setOnClickListener(this);
        tv_code.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void initView() {
        fans_title = (TextView) findViewById(R.id.bt_systemmessage);
        fans_title.setText(getResources().getString(R.string.user_register));
        et_inputphoner = (EditText) findViewById(R.id.input_phone);
        et_indentfycode = (EditText) findViewById(R.id.et_identify_code);
        et_inputpassword = (EditText) findViewById(R.id.input_password);
        et_confirmpassword = (EditText) findViewById(R.id.confirm_password);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_code = (TextView) findViewById(R.id.tv_code);
        iv_back = (ImageView) findViewById(R.id.iv_back);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_register:
                if (isAllRegister()) {
                    HashMap<String, String> maps = new HashMap<>();
                    maps.put("phone", et_inputphoner.getText().toString().trim());
                    maps.put("password", et_inputpassword.getText().toString().trim());
                    maps.put("code", et_indentfycode.getText().toString().trim());
                    okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.LoginOrRegister.REQUEST_HEADER_REGISTER
                            , maps, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String registerResult = response.body().string();
                                    Log.d(TAG, "onResponse: \n" + registerResult);
                                    Gson gson = new Gson();
                                    UserRegister userRegister = gson.fromJson(registerResult, UserRegister.class);
                                    if (userRegister.getCode() == 0) {
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                Toast.makeText(RegisterActivity.this,
                                                        "注册成功", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                                        finish();
                                    }
                                }
                            });
                }
                break;
            case R.id.tv_code:
                if (TextUtils.isEmpty(et_inputphoner.getText().toString().trim())) {
                    Toast.makeText(this, "请输入手机号码", Toast.LENGTH_SHORT).show();
                    return;
                }
                HashMap<String, String> maps = new HashMap<>();
                maps.put("phone", et_inputphoner.getText().toString().trim());
                okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.LoginOrRegister.REQUEST_HEADER_SMS
                        , maps, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String registerResult = response.body().string();
                                Log.d(TAG, "onResponse: \n" + registerResult);
                                Gson gson = new Gson();
                                final SmsBean smsBean = gson.fromJson(registerResult, SmsBean.class);
                                if (smsBean.code == 0) {
                                    mHandler.sendEmptyMessage(0);
                                } else {
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            Toast.makeText(RegisterActivity.this,
                                                    smsBean.message, Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }
                        });

                break;
            case R.id.iv_back:
                finish();
                break;
            default:
        }
    }

    private void startCountTime() {
        tv_code.setEnabled(false);
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tv_code.setText("已发送(" + (l / 1000) + "秒)");
            }

            @Override
            public void onFinish() {
                tv_code.setText("获取验证码");
                tv_code.setEnabled(true);
            }
        };
        timer.start();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
    }

    public boolean isAllRegister() {
        if (et_inputphoner.getText().toString() == null) {
            Toast.makeText(this, "输入不可为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_inputpassword.getText().toString() == null) {
            Toast.makeText(this, "输入不可为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_confirmpassword.getText().toString() == null && !et_confirmpassword.getText()
                .toString().trim().equals(et_inputpassword.getText().toString().trim())) {
            Toast.makeText(this, "输入错误", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_indentfycode.getText().toString() == null) {
            Toast.makeText(this, "输入不可为空", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }
}
