package com.am.shortVideo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.google.gson.Gson;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import adapter.LikeListAdapter;
import application.MyApplication;
import base.BaseActivity;
import bean.LikeListShow;
import bean.MessageWrap;
import customeview.LoginPopupwindow;
import http.OktHttpUtil;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;
import util.RecycleViewDivider;

/**
 * Created by JC on 2019/9/4.
 */

public class LikeListActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView likelist_recycle;
    private static final String TAG = "LikeListActivity";
    private TextView like_title;
    private LikeListAdapter likelistAdapter;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    LikeListShow likelistshow = (LikeListShow) msg.obj;
                    if (likelist_swipefreshlayout.isRefreshing()) {
                        likelist_swipefreshlayout.setRefreshing(false);
                    }
                    if (likelistshow.getCode() == 0) {
                        circleprogree.dismiss();
                        if (likelistshow.getData() != null && likelistshow.getData().getPageList() != null) {
                            likelist_emptyview.setVisibility(View.GONE);
                            likelistAdapter = new LikeListAdapter(likelistshow.getData().getPageList()
                                    , LikeListActivity.this);
                            likelist_recycle.setAdapter(likelistAdapter);
                        } else {
                            likelist_emptyview.setVisibility(View.VISIBLE);
                        }
                    } else if (likelistshow.getCode() == 1005) {
                        circleprogree.dismiss();
                        if (likelistAdapter != null) {
                            likelistAdapter.clearAllData();
                        }
                        new LoginPopupwindow(LikeListActivity.this);
                    }
                    break;
                default:
            }
        }
    };
    private Callback likelistCallback = new Callback() {
        @Override
        public void onFailure(okhttp3.Call call, IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    circleprogree.dismiss();
                }
            });
        }

        @Override
        public void onResponse(okhttp3.Call call, Response response) throws IOException {
            String likelistResult = response.body().string();
            Log.d(TAG, "onResponse:\n " + likelistResult);
            Gson gson = new Gson();
            LikeListShow messageshow = gson.fromJson(likelistResult, LikeListShow.class);
            Message message = new Message();
            message.what = 1;
            message.obj = messageshow;
            handler.sendMessage(message);
        }
    };
    private OktHttpUtil okHttpUtil;
    private TextView likelist_emptyview;
    private ImageView iv_back;
    private SwipeRefreshLayout likelist_swipefreshlayout;
    private CircleProgressDialog circleprogree;

    @Override
    protected int getLayout() {
        return R.layout.likelist_layout;
    }

    @Override
    protected void initEventAndData() {
        okHttpUtil = ((MyApplication) getApplication()).getOkHttpUtil();
        EventBus.getDefault().register(this);
        circleprogree = new CircleProgressDialog(this);
        initView();
        setOnClickLinstener();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLoginStatus(MessageWrap messageWrap) {
        Log.d(TAG, "getLoginStatus: ");
        if (messageWrap.getMessage().equals("true")) {
            okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.MESSAGE.REQUEST_HEADER_VIDEOLIKELIST, ((MyApplication) getApplicationContext()).getMaps()
                    , likelistCallback);
        }
    }

    private void setOnClickLinstener() {
        iv_back.setOnClickListener(this);
        likelist_swipefreshlayout.setOnRefreshListener(this);
    }

    private void initView() {
        like_title = (TextView) findViewById(R.id.bt_systemmessage);
        likelist_swipefreshlayout = (SwipeRefreshLayout) findViewById(R.id.likelist_swipefreshlayout);
        like_title.setText(getResources().getString(R.string.title_support));
        likelist_recycle = (RecyclerView) findViewById(R.id.likelist_recycle);
        likelist_emptyview = (TextView) findViewById(R.id.tv_likelistemptyview);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        LinearLayoutManager linnearLayoutManger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        likelist_recycle.setLayoutManager(linnearLayoutManger);
        likelist_recycle.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL, 2, 0, 0, getResources().getColor(R.color.colorDarkGray)));
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.MESSAGE.REQUEST_HEADER_VIDEOLIKELIST, ((MyApplication) getApplicationContext()).getMaps()
                , likelistCallback);
        circleprogree.showDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }

    }

    @Override
    public void onRefresh() {
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.MESSAGE.REQUEST_HEADER_VIDEOLIKELIST, ((MyApplication) getApplicationContext()).getMaps()
                , likelistCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
