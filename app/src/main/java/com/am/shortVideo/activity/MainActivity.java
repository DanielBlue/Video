package com.am.shortVideo.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.PersistableBundle;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.am.shortVideo.R;
import com.google.gson.Gson;
import com.tiktokdemo.lky.tiktokdemo.record.RecordVideoActivity;
import com.umeng.socialize.UMShareAPI;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.List;

import application.MyApplication;
import base.BaseActivity;
import bean.MessageWrap;
import bean.UserInfo;
import customeview.LoginPopupwindow;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;
import util.PreferencesUtil;

public class MainActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "MainActivity";
    private OktHttpUtil oktHttpUtil;
    private boolean noPermission;
    private AlertDialog.Builder alterDialog;
    private ImageView iv_home;
    private ImageView iv_attention;
    private Button bt_capture;
    private ImageView iv_message;
    private ImageView iv_me;
    private FragmentManager fragmentManger;
    private int curFragment = 0;
    private HomeFragment homeFragment;
    private AttentionFragment attentionFragment;
    private MessageFragment messageFragment;
    private MeFragment meFragment;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    UserInfo userInfo = (UserInfo) msg.obj;
                    if (userInfo.getCode() == 0) {
                        isLogin = true;
                        Intent intent = new Intent(MainActivity.this, RecordVideoActivity.class);
                        startActivity(intent);
                    } else if (userInfo.getCode() == 1005) {
                        isLogin = false;
                        new LoginPopupwindow(MainActivity.this);
                    }
                    break;

            }
        }
    };


    private Callback userinfoCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(TAG, "onFailure: userinfoCallback");

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String userinfoResult = response.body().string();
            Log.d(TAG, "onResponse: userinfoCallback\n" + userinfoResult);
            Gson gson = new Gson();
            UserInfo userinfo = gson.fromJson(userinfoResult, UserInfo.class);
            PreferencesUtil.put(MainActivity.this, PreferencesUtil.SP_USER_INFO, new Gson().toJson(userinfo.getData().getUserInfo()));
//            MyApplication.getInstance().setUserInfo(userinfo.getData().getUserInfo());
            Message message = new Message();
            message.what = 1;
            message.obj = userinfo;
            mHandler.sendMessage(message);
        }
    };
    private boolean isLogin;


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        oktHttpUtil = ((MyApplication) getApplicationContext()).getOkHttpUtil();
        initPermission();
        EventBus.getDefault().register(this);
        initView();
        setLinstener();
    }

    @Override
    protected void initEventAndData(Bundle savedInstanceState) {
        super.initEventAndData(savedInstanceState);
        if (savedInstanceState != null) {
            homeFragment = (HomeFragment) fragmentManger.findFragmentByTag(HomeFragment.class.getName());
            attentionFragment = (AttentionFragment) fragmentManger.findFragmentByTag(AttentionFragment.class.getName());
            messageFragment = (MessageFragment) fragmentManger.findFragmentByTag(MessageFragment.class.getName());
            meFragment = (MeFragment) fragmentManger.findFragmentByTag(MeFragment.class.getName());
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
//        super.onSaveInstanceState(outState, outPersistentState);
        outState.putInt("POSITIONKEY", curFragment);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        // 获取保存的数组下标
        curFragment = savedInstanceState.getInt("POSITIONKEY");
        // 回复视图状态，恢复为fragmen的切换状态
        switchFragment(curFragment);
        super.onRestoreInstanceState(savedInstanceState);
    }

    private void setLinstener() {
        iv_home.setOnClickListener(this);
        iv_attention.setOnClickListener(this);
        bt_capture.setOnClickListener(this);
        iv_message.setOnClickListener(this);
        iv_me.setOnClickListener(this);
    }

    private void initView() {
        isLogin = MyApplication.getInstance().getUserInfo() != null;

        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_attention = (ImageView) findViewById(R.id.iv_attention);
        bt_capture = (Button) findViewById(R.id.bt_capture);
        iv_message = (ImageView) findViewById(R.id.iv_message);
        iv_me = (ImageView) findViewById(R.id.iv_me);
        fragmentManger = getSupportFragmentManager();
        switchFragment(curFragment);
    }

    private void initPermission() {
        AndPermission.with(this)
                .runtime()
                .permission(android.Manifest.permission.CAMERA,
                        android.Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        android.Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.RECORD_AUDIO,
                        Manifest.permission.READ_PHONE_STATE)
                .onGranted(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {

                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {

                    }
                })
                .start();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLoginStatus(MessageWrap messageWrap) {
        Log.d(TAG, "getLoginStatus: ");
        if (messageWrap.getMessage().equals("true")) {
            isLogin = true;
        } else if (messageWrap.getMessage().equals("false")) {
            isLogin = false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            for (int i = 0; i < grantResults.length; i++) {
                if (grantResults[i] == -1) {
                    noPermission = true;
                }
            }
            if (noPermission) {
                showDialog();
            }
        }
    }

    public void showDialog() {
        alterDialog = new AlertDialog.Builder(this);
        alterDialog.setMessage("请手动设置授权");
        alterDialog.setPositiveButton("设置", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS, Uri.parse("packageName:" + "com.am.shortvideo"));
                startActivity(intent);
            }
        });
        alterDialog.setNegativeButton("取消", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alterDialog.create();
        alterDialog.show();
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_home:
                if (curFragment != 0) {
                    curFragment = 0;
                    switchFragment(curFragment);

                }
                break;
            case R.id.iv_attention:
                if (curFragment != 1) {
                    curFragment = 1;
                    switchFragment(curFragment);

                }
                break;
            case R.id.bt_capture:
//                if (curFragment != 2) {
//                    curFragment = 2;
//                    switchFragment(curFragment);
//
//                }
                if (isLogin) {
                    Intent intent = new Intent(MainActivity.this, RecordVideoActivity.class);
                    startActivity(intent);
                } else {
                    oktHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_PERSONINFO
                            , ((MyApplication) getApplicationContext()).getMaps(), userinfoCallback);
                }
                break;
            case R.id.iv_message:
                if (curFragment != 3) {
                    curFragment = 3;
                    switchFragment(curFragment);

                }
                break;
            case R.id.iv_me:
                if (curFragment != 4) {
                    curFragment = 4;
                    switchFragment(curFragment);

                }
                break;
            default:
        }
    }

    public void switchFragment(int switchType) {
        FragmentTransaction fragmentTran = fragmentManger.beginTransaction();
        hideFragment(fragmentTran);
        switchBottomButton(switchType);
        if (switchType != 0) {
            if (homeFragment != null && homeFragment.getCurPlayer() != null) {
                homeFragment.getCurPlayer().release();
            }
        }else {
            if (homeFragment != null && homeFragment.getCurPlayer() != null) {
                homeFragment.getCurPlayer().startPlayLogic();
            }
        }
        switch (switchType) {
            case 0:
                if (homeFragment != null) {
                    fragmentTran.show(homeFragment);
                } else {
                    homeFragment = new HomeFragment();
                    fragmentTran.add(R.id.fl_container, homeFragment);
                }

                break;
            case 1:
                if (attentionFragment == null) {
                    attentionFragment = new AttentionFragment();
                    fragmentTran.add(R.id.fl_container, attentionFragment);
                } else {
                    fragmentTran.show(attentionFragment);
                }
                break;
            case 2:
//                HomeFragment homeFragment=new HomeFragment();
//                fragmentTran.add(R.id.fl_container,homeFragment);
                break;
            case 3:
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                    fragmentTran.add(R.id.fl_container, messageFragment);
                } else {
                    fragmentTran.show(messageFragment);
                }

                break;
            case 4:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                    fragmentTran.add(R.id.fl_container, meFragment);
                } else {
                    fragmentTran.show(meFragment);
                }
                break;
            default:
        }
        fragmentTran.commit();
    }

    public void hideFragment(FragmentTransaction fragmentTransaction) {
        if (homeFragment != null) {
            fragmentTransaction.hide(homeFragment);
        }
        if (attentionFragment != null) {
            fragmentTransaction.hide(attentionFragment);
        }
        if (messageFragment != null) {
            fragmentTransaction.hide(messageFragment);
        }
        if (meFragment != null) {
            fragmentTransaction.hide(meFragment);
        }
    }

    public void switchBottomButton(int type) {
        switch (type) {
            case 0:
                iv_home.setImageDrawable(getResources().getDrawable(R.mipmap.home));
                iv_attention.setImageDrawable(getResources().getDrawable(R.mipmap.guanzhu_1));
                iv_message.setImageDrawable(getResources().getDrawable(R.mipmap.xiaoxi_1));
                iv_me.setImageDrawable(getResources().getDrawable(R.mipmap.me_1));
                break;
            case 1:
                iv_home.setImageDrawable(getResources().getDrawable(R.mipmap.home_1));
                iv_attention.setImageDrawable(getResources().getDrawable(R.mipmap.guanzhu));
                iv_message.setImageDrawable(getResources().getDrawable(R.mipmap.xiaoxi_1));
                iv_me.setImageDrawable(getResources().getDrawable(R.mipmap.me_1));
                break;
            case 3:
                iv_home.setImageDrawable(getResources().getDrawable(R.mipmap.home_1));
                iv_attention.setImageDrawable(getResources().getDrawable(R.mipmap.guanzhu_1));
                iv_message.setImageDrawable(getResources().getDrawable(R.mipmap.xiaoxi));
                iv_me.setImageDrawable(getResources().getDrawable(R.mipmap.me_1));
                break;
            case 4:
                iv_home.setImageDrawable(getResources().getDrawable(R.mipmap.home_1));
                iv_attention.setImageDrawable(getResources().getDrawable(R.mipmap.guanzhu_1));
                iv_message.setImageDrawable(getResources().getDrawable(R.mipmap.xiaoxi_1));
                iv_me.setImageDrawable(getResources().getDrawable(R.mipmap.me));
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }


    private long mExitTime = 0;

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if ((System.currentTimeMillis() - mExitTime) > 2000) {
                mExitTime = System.currentTimeMillis();
            } else {
                //模拟Home键操作
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                intent.addCategory(Intent.CATEGORY_HOME);
                startActivity(intent);
            }
            return true;
        }

        return super.onKeyDown(keyCode, event);
    }

}
