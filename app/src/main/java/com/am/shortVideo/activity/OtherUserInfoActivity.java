package com.am.shortVideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.am.shortVideo.EventBean.AttentionEvent;
import com.am.shortVideo.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;
import com.tiktokdemo.lky.tiktokdemo.Constant;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.UserVideoAdapter;
import application.MyApplication;
import base.BaseActivity;
import base.onLoadMoreLinstener;
import bean.AttentionOrCancelPerson;
import bean.OtherUserInfo;
import bean.PublishVideoInfo;
import bean.SerachPublishVideo;
import bean.UserInfo;
import customeview.LoginPopupwindow;
import de.hdodenhof.circleimageview.CircleImageView;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;
import util.SpaceItemDecoration;

/**
 * Created by 李杰 on 2019/9/17.
 */

public class OtherUserInfoActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "OtherUserInfoActivity";
    private OktHttpUtil oktHttpUtil;
    private TextView bt_changpersoninfo;
    private CircleImageView me_circleImageView;
    private TextView me_nickname;
    private TextView me_useraccount;
    private TextView me_personalcontent;
    private TextView me_attentioncount;
    private TextView me_fanscount;
    private TextView me_zanscount;
    private Button me_zuoping;
    private Button me_caogao;
    private RecyclerView me_recycleview;
    private SwipeRefreshLayout me_swiprefreshlayout;
    private View me_line2;
    private View me_line3;
    private ArrayList<PublishVideoInfo> bitmaps = new ArrayList<>();
    private boolean curFoolowStatus;
    private CircleProgressDialog circleprogressDialog;
    private String other_userinfo;
    private TextView fans_title;

    private boolean isScolled;
    private int countItem;
    private int lastItem;
    private int currentPage = 1;

    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    OtherUserInfo userInfo = (OtherUserInfo) msg.obj;
                    if (userInfo.getCode() == 0) {
                        circleprogressDialog.dismiss();
                        if (!userInfo.getData().getUserInfo().getAvatar().isEmpty()) {
                            Glide.with(OtherUserInfoActivity.this).load(HttpUri.BASE_DOMAIN + userInfo.getData().getUserInfo().getAvatar())
                                    .into(me_circleImageView);
                        }
                        if (!userInfo.getData().getUserInfo().getNickname().isEmpty()) {
                            me_nickname.setText(userInfo.getData().getUserInfo().getNickname());
                        }
                        if (!userInfo.getData().getUserInfo().getSignature().isEmpty()) {
                            me_personalcontent.setText(userInfo.getData().getUserInfo().getSignature());
                        }
                        if (!userInfo.getData().getUserInfo().getUid().isEmpty()) {
                            me_useraccount.setText(userInfo.getData().getUserInfo().getUid());
                        }
                        if (userInfo.getData().getUserInfo().isFollowState()) {
                            curFoolowStatus = true;
                            bt_changpersoninfo.setText("取消关注");
                        } else {
                            curFoolowStatus = false;
                            bt_changpersoninfo.setText("关注");
                        }
                        me_fanscount.setText("" + userInfo.getData().getUserInfo().getFansCount());
                        me_attentioncount.setText("" + userInfo.getData().getUserInfo().getFollowCount());
                        me_zanscount.setText("" + userInfo.getData().getUserInfo().getGetLikeCount());
                        requstVideo();
                    } else if (userInfo.getCode() == 1005) {
                        circleprogressDialog.dismiss();
                        new LoginPopupwindow(OtherUserInfoActivity.this);
                    }
                    break;
                case 2:
                    SerachPublishVideo serachPublishVideo = (SerachPublishVideo) msg.obj;
                    if (me_swiprefreshlayout.isRefreshing()) {
                        me_swiprefreshlayout.setRefreshing(false);
                    }
                    if (currentPage == 1) {
                        dates.clear();
                    }
                    if (serachPublishVideo.getCode() == 0) {
                        currentPage++;
                        circleprogressDialog.dismiss();
                        if (serachPublishVideo.getData() != null) {
                            dates.addAll(serachPublishVideo.getData().getIndexList());
                        }
                        userVideoAdapter.notifyDataSetChanged();
                    }
                    break;
            }
        }
    };
    private Callback video_zuopingCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(TAG, "onFailure: video_zuopingCallback");

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String userinfoResult = response.body().string();
            Log.d(TAG, "onResponse: video_zuopingCallback\n" + userinfoResult);
            Gson gson = new Gson();
            SerachPublishVideo serachPublishVideo = gson.fromJson(userinfoResult, SerachPublishVideo.class);
            Message message = new Message();
            message.what = 2;
            message.obj = serachPublishVideo;
            mHandler.sendMessage(message);
        }
    };
    private Callback userinfoCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(TAG, "onFailure: userinfoCallback");
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    circleprogressDialog.dismiss();
                }
            });

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String userinfoResult = response.body().string();
            Log.d(TAG, "onResponse: userinfoCallback\n" + userinfoResult);
            Gson gson = new Gson();
            OtherUserInfo userinfo = gson.fromJson(userinfoResult, OtherUserInfo.class);
            Message message = new Message();
            message.what = 1;
            message.obj = userinfo;
            mHandler.sendMessage(message);
        }
    };
    private ImageView iv_back;
    private List<SerachPublishVideo.DataBean.IndexListBean> dates = new ArrayList<>();
    private UserVideoAdapter userVideoAdapter;

    @Override
    protected int getLayout() {
        return R.layout.other_userinfo;
    }

    @Override
    protected void initEventAndData() {
        circleprogressDialog = new CircleProgressDialog(this);
        other_userinfo = getIntent().getStringExtra("otheruserinfo");
        oktHttpUtil = OktHttpUtil.getInstance();
        initView();
    }

    private void initView() {
        fans_title = (TextView) findViewById(R.id.bt_systemmessage);
        fans_title.setText("用户信息");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        me_circleImageView = (CircleImageView) findViewById(R.id.me_circleimageview);
        me_nickname = (TextView) findViewById(R.id.me_nickname);
        me_useraccount = (TextView) findViewById(R.id.me_useraccount);
        me_personalcontent = (TextView) findViewById(R.id.tv_personalcontent);
        me_attentioncount = (TextView) findViewById(R.id.tv_me_attentioncount);
        me_fanscount = (TextView) findViewById(R.id.tv_me_fanscount);
        me_zanscount = (TextView) findViewById(R.id.tv_me_zanscount);
        me_zuoping = (Button) findViewById(R.id.bt_mezuoping);
        me_recycleview = (RecyclerView) findViewById(R.id.me_recycleview);
        me_swiprefreshlayout = (SwipeRefreshLayout) findViewById(R.id.me_swipeFreshLayout);
        bt_changpersoninfo = (TextView) findViewById(R.id.me_editorinfo);
        bt_changpersoninfo.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        me_zuoping.setOnClickListener(this);
        GridLayoutManager gridmanger = new GridLayoutManager(this, 3, GridLayoutManager.VERTICAL, false);
        me_recycleview.setLayoutManager(gridmanger);
        me_recycleview.addItemDecoration(new SpaceItemDecoration(10));

        userVideoAdapter = new UserVideoAdapter(dates, OtherUserInfoActivity.this);
        me_recycleview.setAdapter(userVideoAdapter);

        HashMap<String, String> maps = new HashMap<>();
        maps.put("uid", other_userinfo);
        oktHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_USEROTHERINFO
                , ((MyApplication) getApplicationContext()).getMaps(), maps, userinfoCallback);

        me_swiprefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                currentPage = 1;
                requstVideo();
            }
        });
        me_recycleview.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滑动
                        isScolled = false;
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://拖动
                        isScolled = true;
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://惯性滑动
                        isScolled = true;
                        break;
                    default:
                        isScolled = false;
                        break;
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    countItem = layoutManager.getItemCount();
                    lastItem = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                }
                if (isScolled && countItem != lastItem && lastItem == countItem - 1) {
                    Log.d(TAG, "onScrolled: ");
                    requstVideo();
                }
            }
        });

        userVideoAdapter.setOnItemClickListerner(new UserVideoAdapter.OnItemClickListerner() {
            @Override
            public void onItemClickListerner(int position) {
                Intent intent = new Intent(OtherUserInfoActivity.this, ZuopinPlayingActivity.class);
                Bundle bundle = new Bundle();
                bundle.putSerializable("datas", (Serializable) dates);
                intent.putExtra("position", position);
                intent.putExtra("currentPage", currentPage);
                intent.putExtra("user_uid", dates.get(position).getUid());
                intent.putExtra("homeVideoImg", bundle);
                startActivity(intent);
            }
        });
    }

    private void requstVideo() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("uid", other_userinfo);
        maps.put("page", currentPage + "");
//                        ((MyApplication) getApplicationContext()).setUserUid(userInfo.getData().getUserInfo().getUid());
        oktHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SEARCHUSERVIDEO
                , ((MyApplication) getApplicationContext()).getMaps(), maps, video_zuopingCallback);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.me_editorinfo:
                HashMap<String, String> maps = new HashMap<>();
                maps.put("uid", other_userinfo);
                oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_ATTENTIONUSERSTATUS
                        , ((MyApplication) getApplicationContext()).getMaps(), maps, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String attentionResult = response.body().string();
                                Log.d(TAG, "onclick-->onResponse:\n " + attentionResult);
                                Gson gson = new Gson();
                                AttentionOrCancelPerson attentionPersonorcancel = gson.fromJson(attentionResult, AttentionOrCancelPerson.class);
                                if (attentionPersonorcancel.getCode() == 0) {
                                    AttentionEvent attentionEvent = new AttentionEvent();
                                    attentionEvent.uid = other_userinfo;
                                    if (attentionPersonorcancel.getData().isFollowStatus()) {
                                        attentionEvent.isAttent = true;
                                        curFoolowStatus = true;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                bt_changpersoninfo.setText("取消关注");
                                            }
                                        });
                                    } else {
                                        attentionEvent.isAttent = false;
                                        curFoolowStatus = false;
                                        runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                bt_changpersoninfo.setText("关注");
                                            }
                                        });
                                    }
                                    EventBus.getDefault().post(attentionEvent);
                                    changeAttention();
                                } else if (attentionPersonorcancel.getCode() == 1005) {
                                    new LoginPopupwindow(OtherUserInfoActivity.this);
                                }
                            }
                        });
                break;
            case R.id.iv_back:
                Log.d(TAG, "onClick: ");
                finish();
                break;
        }
    }

    private void changeAttention() {
        for (int i = 0; i < dates.size(); i++) {
            dates.get(i).setFollowStatus(curFoolowStatus);
        }
    }
}
