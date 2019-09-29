package com.am.shortVideo.activity;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import application.MyApplication;
import base.BaseActivity;
import bean.MessageWrap;
import bean.UserLogin;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Headers;
import okhttp3.Response;
import util.HttpUri;
import util.PreferencesUtil;

/**
 * Created by JC on 2019/8/30.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "LoginActivity";
    private EditText et_inputpassphone;
    private EditText et_inputpassword;
    private TextView tv_register;
    private TextView tv_login;
    private OktHttpUtil okHttpUtil;
    private boolean isLoginSuccess = false;
    private HashMap<String, String> sessionMaps = new HashMap<>();
    private Callback loginCallBack = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String result = response.body().string();
            Log.d(TAG, "onResponse: \n" + result);
            Gson gson = new Gson();
            final UserLogin login = gson.fromJson(result, UserLogin.class);
            if (login.getCode() == 0) {
                Headers headers = response.headers();
                PreferencesUtil.put(LoginActivity.this, PreferencesUtil.SP_USER_INFO, new Gson().toJson(login.getData().user));
//                MyApplication.getInstance().setUserInfo(login.getData().user);
                Log.d(TAG, "onResponse:Headers " + headers);
                List<String> cookies = headers.values("Set-Cookie");
                for (String value : cookies) {
                    Log.d(TAG, "onResponse:value \n" + value);
                }
                String session = cookies.get(0);
                Log.d(TAG, "onResponse: session" + session);
                String s = session.substring(0, session.indexOf(";"));
                Log.d(TAG, "onResponse: " + s);
                // String s1=session.substring(session.indexOf("=")+1,session.indexOf(";"));
                //Log.d(TAG, "onResponse1: "+s1);
                sessionMaps.put("cookie", s);
                ((MyApplication) getApplication()).setMaps(sessionMaps, "save");
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isLoginSuccess = true;
                        Toast.makeText(LoginActivity.this, "登录成功", Toast.LENGTH_SHORT).show();
                        if (isLoginSuccess) {
                            EventBus.getDefault().post(MessageWrap.getInstance("true"));
                        }

                        finish();
                    }
                });
            } else if (login.getCode() == 1012) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isLoginSuccess = false;
                        Toast.makeText(LoginActivity.this, "密码错误", Toast.LENGTH_SHORT).show();

                    }
                });
            } else if (login.getCode() == 1011) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isLoginSuccess = false;
                        Toast.makeText(LoginActivity.this, "用户不存在", Toast.LENGTH_SHORT).show();
                    }
                });

            } else if (login.getCode() == 1008) {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isLoginSuccess = false;
                        Toast.makeText(LoginActivity.this, "手机号码异常", Toast.LENGTH_SHORT).show();
                    }
                });

            } else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        isLoginSuccess = false;
                        Toast.makeText(LoginActivity.this, login.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }
    };
    private TextView fans_title;
    private ImageView iv_back;

    @Override
    protected int getLayout() {
        return R.layout.login_layout;
    }

    @Override
    protected void initEventAndData() {
        okHttpUtil = ((MyApplication) getApplication()).getOkHttpUtil();
        initView();

        setOnClickLinstener();
    }

    private void setOnClickLinstener() {
        tv_login.setOnClickListener(this);
        tv_register.setOnClickListener(this);
    }

    private void initView() {
        fans_title = (TextView) findViewById(R.id.bt_systemmessage);
        fans_title.setText(getResources().getString(R.string.user_login));
        iv_back = (ImageView) findViewById(R.id.iv_back);
        et_inputpassphone = (EditText) findViewById(R.id.login_inputphone);
        et_inputpassword = (EditText) findViewById(R.id.login_inputpassword);
        tv_register = (TextView) findViewById(R.id.tv_register);
        tv_login = (TextView) findViewById(R.id.tv_login);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_login:
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                HashMap<String, String> maps = new HashMap<>();
                maps.put("phone", et_inputpassphone.getText().toString().trim());
                maps.put("password", et_inputpassword.getText().toString().trim());
                maps.put("type", "2");
                if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    return;
                }
                maps.put("deviceId", tm.getDeviceId());
                maps.put("deviceType", "1");
                okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.LoginOrRegister.REQUEST_HEADER_LOGIN
                        , maps, loginCallBack);
                break;
            case R.id.tv_register:
                Intent intent = new Intent(this, RegisterActivity.class);
                startActivity(intent);
                break;
            case R.id.iv_back:
//                if(isLoginSuccess){
//                    EventBus.getDefault().post(MessageWrap.getInstance("true"));
//                }

                finish();
                break;
            default:
        }
    }
}
