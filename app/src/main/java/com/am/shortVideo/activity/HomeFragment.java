package com.am.shortVideo.activity;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.EventBean.AttentionEvent;
import com.am.shortVideo.EventBean.CommentCountEvent;
import com.am.shortVideo.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.ShortVideoAdapter;
import application.MyApplication;
import bean.HomeVideoImg;
import bean.IndexListBean;
import bean.LoginEvent;
import customeview.ShortVideoPlayer;
import event.MessageEvent;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;
import util.LogUtils;


/**
 * Created by 李杰 on 2019/8/12.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "HomeFragment";
    View view;
    private RecyclerView mRvList;
    private SwipeRefreshLayout home_swipeRefresh;
    private OktHttpUtil okHttpUtil;
    private List<IndexListBean> datas = new ArrayList<>();
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    HomeVideoImg homevideImg = (HomeVideoImg) msg.obj;
                    if (currentPage == 1) {
                        mAdapter.getData().clear();
                    }
                    if (homevideImg.getMessage().equals("成功")) {
                        //isCanPlay=true;
                        circleDialog.dismiss();
                        Log.d(TAG, "handleMessage: 请求数据成功");
                        if (home_swipeRefresh.isRefreshing()) {
                            home_swipeRefresh.setRefreshing(false);
                        }
                        if (homevideImg.getData().getIndexList().size() > 0) {
                            mAdapter.addData(homevideImg.getData().getIndexList());
                            mAdapter.loadMoreComplete();
                        } else {
                            mAdapter.loadMoreEnd();
                        }
                        if (mCurPlayer == null) {
                            startPlay(0);
                        }
                        currentPage++;
                    } else {
                        circleDialog.dismiss();
                        mAdapter.loadMoreFail();
                        Toast.makeText(getActivity(), "请求数据失败，检查网络是否正常", Toast.LENGTH_SHORT).show();
                    }
                    break;
                case 2:
                    break;
                default:
            }

        }
    };

    Callback homevideoCallback = new Callback() {
        @Override
        public void onFailure(Call call, final IOException e) {
            e.printStackTrace();
            ((Activity) getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(getActivity(), "请求失败：" + e.getMessage(), Toast.LENGTH_SHORT).show();
                    circleDialog.dismiss();
                    home_swipeRefresh.setRefreshing(false);
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String dataes = response.body().string();
            Gson gson = new Gson();
            HomeVideoImg hovideImg = gson.fromJson(dataes, HomeVideoImg.class);
            Log.d(TAG, "onResponse:\n " + dataes.toString());
            Message video_message = new Message();
            video_message.what = 1;
            video_message.obj = hovideImg;
            handler.sendMessage(video_message);
        }

    };
    private LinearLayoutManager layoutManger;
    private PagerSnapHelper pagerSnapHelper;
    private Button bt_menu;
    private ImageView iv_serach;
    private TextView home_all;
    private TextView home_samecity;
    private TextView home_recommend;
    private TextView home_superman;
    private CircleProgressDialog circleDialog;
    private int currentPage = 1;
    private ShortVideoAdapter mAdapter;

    public ShortVideoPlayer mCurPlayer;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        view = inflater.inflate(R.layout.home_fragment, container, false);
        EventBus.getDefault().register(this);
        initView();
        setButtonOnClickListener();
        MyApplication.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                initPermission();
            }
        }, 500);
        return view;
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
                        LogUtils.d("MainActivity", "权限被允许：" + data.size());
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {
                        LogUtils.d("MainActivity", "权限被拒绝：" + data.size());
                    }
                })
                .start();
    }


    private void setButtonOnClickListener() {
        //bt_menu.setOnClickListener(this);
        iv_serach.setOnClickListener(this);
        home_all.setOnClickListener(this);
        home_samecity.setOnClickListener(this);
        home_recommend.setOnClickListener(this);
        home_superman.setOnClickListener(this);
    }

    private void initView() {
        mRvList = (RecyclerView) view.findViewById(R.id.home_recycle);
        home_swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.home_swipeRefresh);
        //bt_menu=(Button)view.findViewById(R.id.bt_menu);
        iv_serach = (ImageView) view.findViewById(R.id.iv_serach);
        home_all = (TextView) view.findViewById(R.id.tv_channelall);
        home_samecity = (TextView) view.findViewById(R.id.tv_channelsame);
        home_recommend = (TextView) view.findViewById(R.id.tv_channelrecommend);
        home_superman = (TextView) view.findViewById(R.id.tv_channelsuperman);
        okHttpUtil = ((MyApplication) getActivity().getApplication()).getOkHttpUtil();
        pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(mRvList);
        layoutManger = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        mRvList.setLayoutManager(layoutManger);

        mAdapter = new ShortVideoAdapter(datas);
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                requestVideo();
            }
        }, mRvList);
        mRvList.setAdapter(mAdapter);
        circleDialog = new CircleProgressDialog(getActivity());
        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滑动
                        View view = pagerSnapHelper.findSnapView(layoutManger);
                        int position = layoutManger.getPosition(view);
                        if (position != mCurPosition) {
                            startPlay(position);
                        }
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

            }
        });

        requestVideo();
        circleDialog.showDialog();
        home_swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_HOMEVIDEO, homevideoCallback);
                currentPage = 1;
                requestVideo();
            }
        });
    }

    public ShortVideoPlayer getCurPlayer() {
        return mCurPlayer;
    }

    private int mCurPosition = -1;

    private void startPlay(final int position) {
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
                        if (isRefresh) {
                            mCurPlayer.getCurrentPlayer().onVideoPause();
                            isRefresh = false;
                        }
                        mCurPosition = position;
                    }
                }
            }
        });
    }

    private boolean isRefresh = false;

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mCurPlayer != null) {
            mCurPlayer.getCurrentPlayer().onVideoPause();
        }
    }

    private void requestVideo() {
        HashMap<String, String> map = new HashMap<>();
        map.put("page", currentPage + "");
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_HOMEVIDEO, ((MyApplication) getActivity().getApplicationContext()).getMaps(), map, homevideoCallback);
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.bt_menu:
                Intent intent = new Intent(getActivity(), SwitchChannelActivity.class);
                getActivity().startActivity(intent);
                break;
            case R.id.iv_serach:
                Intent intent1 = new Intent(getActivity(), SearchActivity.class);
                getActivity().startActivity(intent1);
                break;
            case R.id.tv_channelall:
                Intent intent2 = new Intent(getActivity(), SwitchChannelActivity.class);
                intent2.putExtra("hometitle", 0);
                startActivity(intent2);
                break;
            case R.id.tv_channelsame:
                Intent intent3 = new Intent(getActivity(), SwitchChannelActivity.class);
                intent3.putExtra("hometitle", 1);
                startActivity(intent3);
                break;
            case R.id.tv_channelsuperman:
                Intent intent4 = new Intent(getActivity(), SwitchChannelActivity.class);
                intent4.putExtra("hometitle", 3);
                startActivity(intent4);
                break;
            case R.id.tv_channelrecommend:
                Intent intent5 = new Intent(getActivity(), SwitchChannelActivity.class);
                intent5.putExtra("hometitle", 2);
                startActivity(intent5);
                break;
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeCommentCount(CommentCountEvent commentCountEvent) {
        if (commentCountEvent != null) {
            View view = pagerSnapHelper.findSnapView(layoutManger);
            int position = layoutManger.getPosition(view);
            IndexListBean indexListBean = mAdapter.getData().get(position);
            if (indexListBean.getVid().equals(commentCountEvent.vid)){
                indexListBean.setCommentCounts(commentCountEvent.count);
                if (view != null) {
                    BaseViewHolder holder = (BaseViewHolder) mRvList.getChildViewHolder(view);
                    holder.setText(R.id.tv_commentcount, commentCountEvent.count + "");
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void changeAttention(AttentionEvent attentionEvent) {
        if (attentionEvent != null) {
            for (int i = 0; i < mAdapter.getData().size(); i++) {
                if (TextUtils.equals(mAdapter.getData().get(i).getUid(), attentionEvent.uid) && mAdapter.getData().get(i).isFollowStatus() != attentionEvent.isAttent) {
                    mAdapter.getData().get(i).setFollowStatus(attentionEvent.isAttent);
                    mAdapter.notifyItemChanged(i);
                }
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {
        if (event != null) {
            if (event instanceof LoginEvent) {
                currentPage = 1;
                if (mCurPlayer != null) {
                    mCurPlayer.getCurrentPlayer().release();
                    mCurPlayer = null;
                }
                isRefresh = true;
                requestVideo();
            }
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (mCurPlayer != null) {
            mCurPlayer.getCurrentPlayer().release();
        }
        EventBus.getDefault().unregister(this);
    }
}
