package com.am.shortVideo.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;

import application.MyApplication;
import base.BaseActivity;
import bean.SwitchBean;
import bean.SwitchStatusInfo;
import butterknife.BindView;
import butterknife.OnClick;
import ch.ielse.view.SwitchView;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;

/**
 * Created by JC on 2019/9/11.
 */

public class SelectOnOrOffMessageActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.switch_zan)
    SwitchView switchZan;
    @BindView(R.id.switch_guanzhu)
    SwitchView switchGuanzhu;
    @BindView(R.id.switch_pinlun)
    SwitchView switchPinlun;
    private ImageView iv_back;
    private OktHttpUtil okHttpUtil;
    private String TAG = "SelectOnOrOffMessageActivity";
    private boolean isclick = true;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    SwitchBean switchBean = (SwitchBean) msg.obj;
                    if (switchBean.code == 0) {
                        isclick = false;
                        switchZan.setOpened(switchBean.data.push_list.zan);
                        switchGuanzhu.setOpened(switchBean.data.push_list.follow);
                        switchPinlun.setOpened(switchBean.data.push_list.comment);
                        isclick = true;
                    } else if (switchBean.code == 1005) {
                        Toast.makeText(SelectOnOrOffMessageActivity.this, "请先登录", Toast.LENGTH_SHORT).show();
                        finish();
                    } else {
                        Toast.makeText(SelectOnOrOffMessageActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    SwitchStatusInfo switchStatusInfo = (SwitchStatusInfo) msg.obj;
                    int arg1 = msg.arg1;
                    if (arg1 == 1) {
                        switchZan.setOpened(switchStatusInfo.data.state);
                    } else if (arg1 == 2) {
                        switchGuanzhu.setOpened(switchStatusInfo.data.state);
                    } else if (arg1 == 3) {
                        switchPinlun.setOpened(switchStatusInfo.data.state);
                    }
                    break;
                default:
                    break;
            }

        }
    };


    @Override
    protected int getLayout() {
        return R.layout.notifymanger_layout;
    }

    @Override
    protected void initEventAndData() {
        initView();
        setonLinStener();

    }

    private void setonLinStener() {

    }

    private void initView() {
        okHttpUtil = new OktHttpUtil();
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.MESSAGE.REQUEST_HEADER_SWITCH_LIST,
                ((MyApplication) getApplication()).getMaps(), new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String dataes = response.body().string();
                        Log.d(TAG, "onResponse: logout\n" + dataes);
                        Gson gson = new Gson();
                        SwitchBean switchBean = gson.fromJson(dataes, SwitchBean.class);
                        Message video_message = new Message();
                        video_message.what = 1;
                        video_message.obj = switchBean;
                        handler.sendMessage(video_message);
                    }
                });

    }

    @OnClick({R.id.iv_back, R.id.switch_zan, R.id.switch_guanzhu, R.id.switch_pinlun})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.switch_zan:
                checkStatus(1);
                break;
            case R.id.switch_guanzhu:
                checkStatus(2);
                break;
            case R.id.switch_pinlun:
                checkStatus(3);
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    private void checkStatus(final int i) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("push_type", i + "");
        okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.MESSAGE.REQUEST_HEADER_CHANGEPUSHMESSAGE
                , ((MyApplication) this.getApplicationContext()).getMaps(), maps, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String data = response.body().string();
                        Log.d(TAG, "onResponse: logout\n" + data);
                        Gson gson = new Gson();
                        SwitchStatusInfo switchStatusInfo = gson.fromJson(data, SwitchStatusInfo.class);
                        Message message = new Message();
                        message.what = 2;
                        message.obj = switchStatusInfo;
                        message.arg1 = i;
                        handler.sendMessage(message);
                    }
                });

    }
}
