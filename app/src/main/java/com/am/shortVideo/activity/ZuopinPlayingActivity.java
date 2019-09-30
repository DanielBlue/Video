package com.am.shortVideo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.umeng.socialize.UMShareAPI;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.ZuopinAdapter;
import application.MyApplication;
import base.BaseActivity;
import bean.SerachPublishVideo;
import customeview.ShortVideoPlayer;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;

/**
 * Created by 李杰 on 2019/9/17.
 */

public class ZuopinPlayingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ShortVideoPlayingActivi";
    private RecyclerView mRvList;
    private List<SerachPublishVideo.DataBean.IndexListBean> datas = new ArrayList<>();
    private PagerSnapHelper mSnapHelper;
    private LinearLayoutManager layoutManger;
    private DrawerLayout drawLayout;
    private RelativeLayout rl_menu;
    private Button bt_find;
    private Button bt_menu;
    private TextView systemmessage;
    private ImageView iv_back;
    private int curChannel;
    private OktHttpUtil okHttpUtil;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    SerachPublishVideo serachPublishVideo = (SerachPublishVideo) msg.obj;

                    if (serachPublishVideo.getMessage().equals("成功")) {
                        currentPage++;
                        Log.d(TAG, "handleMessage: 请求数据成功");
                        if (serachPublishVideo.getData() != null) {
                            if (serachPublishVideo.getData().getIndexList().size() > 0) {
                                mAdapter.addData(serachPublishVideo.getData().getIndexList());
                                mAdapter.loadMoreComplete();
                            } else {
                                mAdapter.loadMoreEnd();
                            }
                        }
                    } else {
                        Toast.makeText(ZuopinPlayingActivity.this, "请求数据失败", Toast.LENGTH_SHORT).show();
                        mAdapter.loadMoreFail();
                    }
                    break;
                case 2:
                    break;
                default:
            }
        }
    };
//    Callback homevideoCallback = new Callback() {
//        @Override
//        public void onFailure(Call call, IOException e) {
//
//        }
//
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            String dataes = response.body().string();
//            Gson gson = new Gson();
//            HomeVideoImg hovideImg = gson.fromJson(dataes, HomeVideoImg.class);
//            Log.d(TAG, "onResponse:\n " + dataes.toString());
//            Message video_message = new Message();
//            video_message.what = 1;
//            video_message.obj = hovideImg;
//            handler.sendMessage(video_message);
//        }
//
//    };
    private Callback video_zuopingCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {


        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String userinfoResult = response.body().string();
            Log.d(TAG, "onResponse: video_zuopingCallback\n" + userinfoResult);
            Gson gson = new Gson();
            SerachPublishVideo serachPublishVideo = gson.fromJson(userinfoResult, SerachPublishVideo.class);
            Message message = new Message();
            message.what = 1;
            message.obj = serachPublishVideo;
            handler.sendMessage(message);
        }
    };
    private String curUid;
    private int position;
    private int currentPage;
    private ZuopinAdapter mAdapter;

    @Override
    protected int getLayout() {
        return R.layout.shortvideo_activity;
    }

    @Override
    protected void initEventAndData() {
        okHttpUtil = OktHttpUtil.getInstance();
        addData();
        initView();
        setLinstenr();
        mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(mRvList);
        layoutManger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvList.setLayoutManager(layoutManger);
        mAdapter = new ZuopinAdapter(datas);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requsetVideo();
            }
        }, mRvList);
        mRvList.setAdapter(mAdapter);
        mRvList.scrollToPosition(position);
        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滑动
                        View view = mSnapHelper.findSnapView(layoutManger);
                        int position = layoutManger.getPosition(view);
                        startPlay(position);
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://拖动
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://惯性滑动
                        break;
                    default:
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
//                    switch (curChannel) {
//                        case 0:
//                            HashMap<String, String> maps0 = new HashMap<>();
//                            maps0.put("channelType", "all");
//                            okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_CHANNEl,
//                                    ((MyApplication) getApplication()).getMaps(), maps0, homevideoCallback);
//                            break;
//                        case 1:
//                            HashMap<String, String> maps1 = new HashMap<>();
//                            maps1.put("channelType", "city");
//                            maps1.put("city", "武汉");
//                            okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_CHANNEl,
//                                    ((MyApplication) getApplication()).getMaps(), maps1, homevideoCallback);
//                            break;
//                        case 2:
//                            HashMap<String, String> maps2 = new HashMap<>();
//                            maps2.put("channelType", "vip");
//                            okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_CHANNEl,
//                                    ((MyApplication) getApplication()).getMaps(), maps2, homevideoCallback);
//                            break;
//                        case 3:
//                            HashMap<String, String> maps3 = new HashMap<>();
//                            maps3.put("channelType", "hot");
//                            okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_CHANNEl,
//                                    ((MyApplication) getApplication()).getMaps(), maps3, homevideoCallback);
//
//                            break;
//                        case 4:
//                            HashMap<String, String> maps4 = new HashMap<>();
//                            maps4.put("channelType", "vip");
//                            maps4.put("uid", curUid);
//                            okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_CHANNEl,
//                                    ((MyApplication) getApplication()).getMaps(), maps4, video_zuopingCallback);
//
//                            break;
//                    }
            }
        });
    }

    private int mCurPosition = -1;
    private ShortVideoPlayer mCurPlayer;

    private void startPlay(final int position) {
        if (position != mCurPosition) {
            if (mCurPlayer != null) {
                //先释放之前的播放器
                mCurPlayer.getCurrentPlayer().release();
            }

            //当前是视频则开始播放
            mRvList.post(new Runnable() {
                @Override
                public void run() {
                    BaseViewHolder viewHolder = (BaseViewHolder) mRvList.findViewHolderForLayoutPosition(position);
                    if (viewHolder != null) {
                        mCurPlayer = viewHolder.getView(R.id.video_player);
                        //开始播放
                        if (mCurPlayer != null) {
                            mCurPlayer.getCurrentPlayer().startPlayLogic();
                        }
                    }
                }
            });
            mCurPosition = position;
        }

    }

    private void requsetVideo() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("uid", curUid);
        maps.put("page", currentPage + "");
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SEARCHUSERVIDEO,
                ((MyApplication) getApplication()).getMaps(), maps, video_zuopingCallback);
    }

    private void setLinstenr() {
//        bt_find.setOnClickListener(this);
//        bt_menu.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void addData() {
        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("homeVideoImg");
        datas = (List<SerachPublishVideo.DataBean.IndexListBean>) bundle.getSerializable("datas");
        position = intent.getIntExtra("position", 0);
        currentPage = intent.getIntExtra("currentPage", 0);
//        SerachPublishVideo.DataBean.IndexListBean data = (SerachPublishVideo.DataBean.IndexListBean) bundle.getSerializable("videourl");
//        curChannel = bundle.getInt("type");
        curUid = intent.getStringExtra("user_uid");
//        datas.add(data);
    }

    private void initView() {
        iv_back = (ImageView) findViewById(R.id.iv_back);
        mRvList = (RecyclerView) findViewById(R.id.recyle_view);
//        drawLayout=(DrawerLayout)findViewById(R.id.ac_drawlayout);
//          rl_menu=(RelativeLayout)findViewById(R.id.rl);
//          bt_menu=(Button)findViewById(R.id.bt_menu);
//           bt_find=(Button)findViewById(R.id.bt_serach);
        systemmessage = (TextView) findViewById(R.id.bt_systemmessage);
//        if (curChannel == 0) {
//            systemmessage.setText("全部");
//        } else if (curChannel == 1) {
//            systemmessage.setText("同城");
//        } else if (curChannel == 2) {
//            systemmessage.setText("推荐");
//        } else if (curChannel == 3) {
//            systemmessage.setText("大V");
//        } else if (curChannel == 4) {
        systemmessage.setText("作品");
//        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mCurPlayer != null) {
            mCurPlayer.getCurrentPlayer().release();
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_serach:
                break;
            case R.id.bt_systeminfo:
                if (!drawLayout.isDrawerOpen(rl_menu)) {
                    drawLayout.openDrawer(rl_menu);
                } else {
                    drawLayout.closeDrawer(rl_menu);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
//            case R.id.bt_comment:
//               new CommentPopupWindow(this);
//                break;

            default:
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

}
