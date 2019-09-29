package com.am.shortVideo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import adapter.FansShowAdapter;
import application.MyApplication;
import base.BaseActivity;
import bean.AttentionPerson;
import bean.FansShow;
import bean.MessageWrap;
import customeview.LoginPopupwindow;
import customeview.SliderView;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;
import util.PinYinUtil;
import util.RecycleViewDivider;


/**
 * Created by JC on 2019/8/21.
 */

public class FansActivity extends BaseActivity implements View.OnClickListener, SwipeRefreshLayout.OnRefreshListener, SliderView.ChangeLetter {
    private static final String TAG = "FansActivity";
    private TextView fans_title;
    private RecyclerView fans_recyleview;
    private OktHttpUtil okHttpUtil;
    private ImageView iv_back;
    private TextView tv_fansemptyview;
    private SwipeRefreshLayout fans_swipefreshlayout;
    private SliderView slideView;
    private PinYinUtil pinyinUtil;
    private FansShowAdapter fasnshowAdapter;
    private List<FansShow.DataBean.PageListBean> dates;
    private CircleProgressDialog circleprogress;

    @Override
    protected int getLayout() {
        return R.layout.fans_activity;
    }

    @Override
    protected void initEventAndData() {
        okHttpUtil = ((MyApplication) getApplication()).getOkHttpUtil();
        pinyinUtil = new PinYinUtil();
        circleprogress = new CircleProgressDialog(this);
        EventBus.getDefault().register(this);
        initData();
        initView();
        setOnClickLinstener();
    }

    private void initData() {
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.MESSAGE.REQUEST_HEADER_ATTENTIONFANS
                , ((MyApplication) getApplicationContext()).getMaps(), fansCallBack);
        circleprogress.showDialog();
    }

    private void initView() {
        fans_title = (TextView) findViewById(R.id.bt_systemmessage);
        tv_fansemptyview = (TextView) findViewById(R.id.tv_fansemptyview);
        fans_title.setText(getResources().getString(R.string.title_fans));
        iv_back = (ImageView) findViewById(R.id.iv_back);
        fans_recyleview = (RecyclerView) findViewById(R.id.fans_recycle);
        slideView = (SliderView) findViewById(R.id.sliderview);
        slideView.setChangeLetterLinstener(this);
        fans_swipefreshlayout = (SwipeRefreshLayout) findViewById(R.id.fans_swipefreshlayout);
        LinearLayoutManager linearLayout = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        fans_recyleview.setLayoutManager(linearLayout);
        fans_recyleview.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL, 2, 0, 0, getResources().getColor(R.color.colorblack)));
    }

    private void setOnClickLinstener() {
        iv_back.setOnClickListener(this);
        fans_swipefreshlayout.setOnRefreshListener(this);
    }

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    FansShow fanshow = (FansShow) msg.obj;
                    if (fans_swipefreshlayout.isRefreshing()) {
                        fans_swipefreshlayout.setRefreshing(false);
                    }
                    if (fanshow.getCode() == 0) {
                        circleprogress.dismiss();
                        if (!fanshow.getData().getPageList().isEmpty()) {
                            slideView.setVisibility(View.VISIBLE);
                            dates = fanshow.getData().getPageList();
                            sortData();
                            fasnshowAdapter = new FansShowAdapter(dates, FansActivity.this, pinyinUtil);
                            fans_recyleview.setAdapter(fasnshowAdapter);
                        } else {
                            slideView.setVisibility(View.GONE);
                            tv_fansemptyview.setVisibility(View.VISIBLE);
                        }
                    } else if (fanshow.getCode() == 1005) {
                        circleprogress.dismiss();
                        if (fasnshowAdapter != null) {
                            fasnshowAdapter.clearAllData();
                        }
                        new LoginPopupwindow(FansActivity.this);
                    }
                    break;
            }
        }
    };

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLoginStatus(MessageWrap messageWrap) {
        Log.d(TAG, "getLoginStatus: ");
        if (messageWrap.getMessage().equals("true")) {
            initData();
        }
    }

    private Callback fansCallBack = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    circleprogress.dismiss();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String fansresult = response.body().string();
            Log.d(TAG, "onResponse: \n" + fansresult);
            Gson gson = new Gson();
            FansShow fanshow = gson.fromJson(fansresult, FansShow.class);
            Message message = new Message();
            message.obj = fanshow;
            message.what = 1;
            handler.sendMessage(message);
        }
    };

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    //将集合中的汉字转化为字母
    public void sortData() {
        Collections.sort(dates, new Comparator<FansShow.DataBean.PageListBean>() {

            @Override
            public int compare(FansShow.DataBean.PageListBean pageListBean, FansShow.DataBean.PageListBean t1) {
                String compare1 = "";
                String compare2 = "";
                if (pageListBean != null) {
                    compare1 = pinyinUtil.getPinYin(pageListBean.getNickName());
                }
                if (t1 != null) {
                    compare2 = pinyinUtil.getPinYin(t1.getNickName());
                }

                return compare1.compareTo(compare2);

            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        initData();
    }

    @Override
    public void getchangeLetter(String letter) {
        int position = fasnshowAdapter.isFirstShowLetter(letter.charAt(0));
        fans_recyleview.scrollToPosition(position);
    }
}
