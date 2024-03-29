package com.am.shortVideo.activity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.am.shortVideo.R;
import com.google.gson.Gson;
import com.tiktokdemo.lky.tiktokdemo.record.RecordVideoActivity;
import com.umeng.socialize.UMShareAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import application.MyApplication;
import base.BaseActivity;
import bean.MessageWrap;
import bean.UserInfo;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.BaseUtils;
import util.HttpUri;
import util.PreferencesUtil;
import util.SpUtils;

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
    private View mViewDotMessage;
    private FragmentManager fragmentManger;
    private int curFragment = -1;
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
                        Intent intent = new Intent(MainActivity.this, RecordVideoActivity.class);
                        startActivity(intent);
                    } else if (userInfo.getCode() == 1005) {
                        BaseUtils.getLoginDialog(MainActivity.this).show();
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


    @Override
    protected int getLayout() {
        return R.layout.activity_main;
    }

    @Override
    protected void initEventAndData() {
        oktHttpUtil = ((MyApplication) getApplicationContext()).getOkHttpUtil();
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
    protected void onStart() {
        super.onStart();
        if (curFragment == 0) {
            if (homeFragment != null && homeFragment.getCurPlayer() != null) {
                homeFragment.getCurPlayer().startPlayLogic();
            }
        }
    }

//    @Override
//    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
////        super.onSaveInstanceState(outState, outPersistentState);
//        outState.putInt("POSITIONKEY", curFragment);
//    }

//    @Override
//    protected void onRestoreInstanceState(Bundle savedInstanceState) {
//        // 获取保存的数组下标
//        curFragment = savedInstanceState.getInt("POSITIONKEY");
//        // 回复视图状态，恢复为fragmen的切换状态
//        switchFragment(curFragment);
//        super.onRestoreInstanceState(savedInstanceState);
//    }

    private void setLinstener() {
        iv_home.setOnClickListener(this);
        iv_attention.setOnClickListener(this);
        bt_capture.setOnClickListener(this);
        iv_message.setOnClickListener(this);
        iv_me.setOnClickListener(this);
    }

    private void initView() {
        iv_home = (ImageView) findViewById(R.id.iv_home);
        iv_attention = (ImageView) findViewById(R.id.iv_attention);
        bt_capture = (Button) findViewById(R.id.bt_capture);
        iv_message = (ImageView) findViewById(R.id.iv_message);
        iv_me = (ImageView) findViewById(R.id.iv_me);
        mViewDotMessage = findViewById(R.id.view_dot_message);
        fragmentManger = getSupportFragmentManager();
        switchFragment(0);
        if (SpUtils.getBoolean("is_first",true)){
            new AlertDialog.Builder(MainActivity.this)
                    .setMessage("\"健德视播\"在联网的过程中才可以使用,使用过程中会获取手机权限:\n" +
                            "包含存储,定位,相机功能,您在使用健德视播过程中,对于任何及所有与您对健德视播任何服务的使用有关的任何第三方的作为、不作为和行为，健德视播概不承担责任。如果涉及违法行为,将依法向相关部门举报.\n" +
                            "如果您不同意我们采集信息或者不同意获取相关的手机权限,会导致相关功能无法正常使用,我们将无法为您提供更好的服务.请先仔细阅读本软件用户使用协议和隐私政策.\n" +
                            "\n" +
                            "点击\"同意\",即表示您同意上述内容以及\"健德视播用户协议\"和\"健德视播隐私政策\"")
                    .setPositiveButton("同意", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            dialogInterface.dismiss();
                            SpUtils.putBoolean("is_first",false);
                        }
                    })
                    .setNegativeButton("取消",new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            finish();
                        }
                    }).show();
        }
    }

    public void updateHomeRedDotState(boolean isShow) {
        mViewDotMessage.setVisibility(isShow ? View.VISIBLE : View.INVISIBLE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLoginStatus(MessageWrap messageWrap) {
        Log.d(TAG, "getLoginStatus: ");
        if (messageWrap.getMessage().equals("false")) {
            updateHomeRedDotState(false);
            switchFragment(0);
        } else {
            switchFragment(0);
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
                switchFragment(0);
                break;
            case R.id.iv_attention:
                if (MyApplication.getInstance().getUserInfo() != null) {
                    switchFragment(1);
                } else {
                    BaseUtils.getLoginDialog(MainActivity.this).show();
                }
                break;
            case R.id.bt_capture:
                if (MyApplication.getInstance().getUserInfo() != null) {
                    Intent intent = new Intent(MainActivity.this, RecordVideoActivity.class);
                    startActivity(intent);
                } else {
                    oktHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_PERSONINFO
                            , ((MyApplication) getApplicationContext()).getMaps(), userinfoCallback);
                }
                break;
            case R.id.iv_message:
                if (MyApplication.getInstance().getUserInfo() != null) {
                    switchFragment(3);
                } else {
                    BaseUtils.getLoginDialog(MainActivity.this).show();
                }
                break;
            case R.id.iv_me:
                switchFragment(4);
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
        } else {
            if (homeFragment != null && homeFragment.getCurPlayer() != null) {
                homeFragment.getCurPlayer().startPlayLogic();
            }
        }
        switch (switchType) {
            case 0:
                if (homeFragment != null) {
                    fragmentTran.show(homeFragment);
                    if (curFragment == 0) {
                        homeFragment.refreshVideo();
                    }
                } else {
                    homeFragment = new HomeFragment();
                    fragmentTran.add(R.id.fl_container, homeFragment);
                }

                break;
            case 1:
                if (attentionFragment == null) {
                    attentionFragment = new AttentionFragment();
                }
                if (attentionFragment.isAdded()) {
                    fragmentTran.show(attentionFragment);
                } else {
                    fragmentTran.add(R.id.fl_container, attentionFragment);
                }
                break;
            case 2:
//                HomeFragment homeFragment=new HomeFragment();
//                fragmentTran.add(R.id.fl_container,homeFragment);
                break;
            case 3:
                if (messageFragment == null) {
                    messageFragment = new MessageFragment();
                }
                if (messageFragment.isAdded()) {
                    fragmentTran.show(messageFragment);
                } else {
                    fragmentTran.add(R.id.fl_container, messageFragment);
                }

                break;
            case 4:
                if (meFragment == null) {
                    meFragment = new MeFragment();
                }
                if (meFragment.isAdded()) {
                    fragmentTran.show(meFragment);
                    if (MyApplication.getInstance().getUserInfo() != null) {
                        meFragment.initData();
                    }
                } else {
                    fragmentTran.add(R.id.fl_container, meFragment).show(meFragment);
                }
                break;
            default:
        }
        fragmentTran.commit();
        curFragment = switchType;
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
        UMShareAPI.get(this).release();
        EventBus.getDefault().unregister(this);
    }


    private long mExitTime = 0;

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }

}
