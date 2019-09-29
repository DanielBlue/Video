package com.am.shortVideo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.google.gson.Gson;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.PictureShowAdapter;
import application.MyApplication;
import base.BaseActivity;
import base.onLoadMoreLinstener;
import bean.HomeVideoImg;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;
import util.SpaceItemDecoration;

/**
 * Created by 李杰 on 2019/9/1.
 */

public class SwitchChannelActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener, View.OnClickListener {
    private static final String TAG = "SwitchChannelActivity";
    private RecyclerView switchchannelRecycleView;
    private SwipeRefreshLayout switchchannel_swiperefresh;
    private PictureShowAdapter pictureAdapter;
    private int curChannel = 0;
    private List<HomeVideoImg.DataBean.IndexListBean> datas = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    HomeVideoImg homevideImg = (HomeVideoImg) msg.obj;
                    if (currentPage == 1) {
                        datas.clear();
                    }
                    if (homevideImg.getMessage().equals("成功")) {
                        currentPage++;
                        circleprogreesDialog.dismiss();
                        Log.d(TAG, "handleMessage: 请求数据成功");
                        datas.addAll(homevideImg.getData().getIndexList());
                        pictureAdapter.notifyDataSetChanged();
                    } else {
                        circleprogreesDialog.dismiss();
                        Toast.makeText(SwitchChannelActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
                    }
                    pictureAdapter.notifyDataSetChanged();
                    break;
                case 2:
                    break;
                default:
            }

        }
    };
    Callback homevideoCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    circleprogreesDialog.dismiss();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {

            String dataes = response.body().string();
            Gson gson = new Gson();
            HomeVideoImg hovideImg = gson.fromJson(dataes, HomeVideoImg.class);
            Log.d(TAG, "onResponse: " + dataes);
            Message video_message = new Message();
            video_message.what = 1;
            video_message.obj = hovideImg;
            handler.sendMessage(video_message);

        }

    };
    private OktHttpUtil okHttpUtil;
    private TextView home_all;
    private TextView home_samecity;
    private TextView home_recommend;
    private TextView home_superman;
    private View view1;
    private View view2;
    private View view3;
    private View view4;
    private ImageView iv_back;
    private TextView systemmessage;
    private CircleProgressDialog circleprogreesDialog;

    private int currentPage = 1;

    @Override
    protected int getLayout() {
        return R.layout.switchchannel_layout;
    }

    @Override
    protected void initEventAndData() {
        circleprogreesDialog = new CircleProgressDialog(this);
        curChannel = getIntent().getIntExtra("hometitle", 0);
        initView();
        setOnclickLinstener();
        okHttpUtil = ((MyApplication) getApplication()).getOkHttpUtil();
//        HashMap<String,String> maps=new HashMap<>();
//        maps.put("channelType","all");
//        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_CHANNEl,((MyApplication) getApplication()).getMaps(),maps, homevideoCallback);
        initData(curChannel);
        switChannelView(curChannel);
    }

    private void setOnclickLinstener() {
        switchchannel_swiperefresh.setOnRefreshListener(this);
        home_all.setOnClickListener(this);
        home_samecity.setOnClickListener(this);
        home_recommend.setOnClickListener(this);
        home_superman.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        switchchannelRecycleView = (RecyclerView) findViewById(R.id.switchchannel_recycle);
        switchchannel_swiperefresh = (SwipeRefreshLayout) findViewById(R.id.switchchannel_swipeRefresh);
        home_all = (TextView) findViewById(R.id.tv_channelall);
        home_samecity = (TextView) findViewById(R.id.tv_channelsame);
        home_recommend = (TextView) findViewById(R.id.tv_channelrecommend);
        home_superman = (TextView) findViewById(R.id.tv_channelsuperman);
        view1 = (View) findViewById(R.id.home_line1);
        view2 = (View) findViewById(R.id.home_line2);
        view3 = (View) findViewById(R.id.home_line3);
        view4 = (View) findViewById(R.id.home_line4);
        GridLayoutManager gridmanger = new GridLayoutManager(this, 2, GridLayoutManager.VERTICAL, false);
        switchchannelRecycleView.setLayoutManager(gridmanger);
        switchchannelRecycleView.addItemDecoration(new SpaceItemDecoration(10));
        pictureAdapter = new PictureShowAdapter(datas, SwitchChannelActivity.this, curChannel);
        switchchannelRecycleView.setAdapter(pictureAdapter);
        switchchannelRecycleView.addOnScrollListener(new onLoadMoreLinstener() {
            @Override
            protected void onLoadMoreing(int countItem, int lastItem) {
                Log.d(TAG, "onLoading: ");
                initData(curChannel);
            }

        });
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        initData(curChannel);
        //执行刷新操作
        cancelreFresh();
    }

    //取消刷新操作
    public void cancelreFresh() {
        if (switchchannel_swiperefresh.isRefreshing()) {
            switchchannel_swiperefresh.setRefreshing(false);
        }
    }

    @Override
    public void onClick(View view) {
        currentPage = 1;
        switch (view.getId()) {
            case R.id.tv_channelall:
                if (curChannel != 0) {
                    curChannel = 0;
                    switChannelView(curChannel);
                    initData(curChannel);
                }
                break;
            case R.id.tv_channelsame:
                if (curChannel != 1) {
                    curChannel = 1;
                    switChannelView(curChannel);
                    initData(curChannel);
                }
                break;
            case R.id.tv_channelrecommend:
                if (curChannel != 2) {
                    curChannel = 2;
                    switChannelView(curChannel);
                    initData(curChannel);
                }
                break;
            case R.id.tv_channelsuperman:
                if (curChannel != 3) {
                    curChannel = 3;
                    switChannelView(curChannel);
                    initData(curChannel);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
        }
    }

    public void initData(int channel) {
        circleprogreesDialog.showDialog();
        switch (channel) {
            case 0:
                HashMap<String, String> maps0 = new HashMap<>();
                maps0.put("channelType", "all");
                maps0.put("page", currentPage + "");
                okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_CHANNEl,
                        ((MyApplication) getApplication()).getMaps(), maps0, homevideoCallback);
                break;
            case 1:
                HashMap<String, String> maps1 = new HashMap<>();
                maps1.put("channelType", "city");
                maps1.put("city", "武汉");
                maps1.put("page", currentPage + "");
                okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_CHANNEl,
                        ((MyApplication) getApplication()).getMaps(), maps1, homevideoCallback);
                break;
            case 2:
                HashMap<String, String> maps2 = new HashMap<>();
                maps2.put("channelType", "hot");
                maps2.put("page", currentPage + "");
                okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_CHANNEl,
                        ((MyApplication) getApplication()).getMaps(), maps2, homevideoCallback);
                break;
            case 3:
                HashMap<String, String> maps3 = new HashMap<>();
                maps3.put("channelType", "vip");
                maps3.put("page", currentPage + "");
                okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_CHANNEl,
                        ((MyApplication) getApplication()).getMaps(), maps3, homevideoCallback);

                break;
        }
    }

    public void switChannelView(int channel) {
        switch (channel) {
            case 0:
                view1.setVisibility(View.VISIBLE);
                view2.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                break;
            case 1:
                view2.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                break;
            case 2:
                view3.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                view4.setVisibility(View.GONE);
                break;
            case 3:
                view4.setVisibility(View.VISIBLE);
                view1.setVisibility(View.GONE);
                view3.setVisibility(View.GONE);
                view2.setVisibility(View.GONE);
                break;
        }
    }
}
