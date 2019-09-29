package com.am.shortVideo.activity;

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
import com.google.gson.Gson;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.ShorVideoAdapter;
import application.MyApplication;
import bean.HomeVideoImg;
import bean.MessageWrap;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;

import okhttp3.Response;
import util.HttpUri;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;


/**
 * Created by 李杰 on 2019/8/12.
 */

public class HomeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "HomeFragment";
    View view;
    private RecyclerView home_recycle;
    private SwipeRefreshLayout home_swipeRefresh;
    private OktHttpUtil okHttpUtil;
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
                        //isCanPlay=true;
                        circleDialog.dismiss();
                        Log.d(TAG, "handleMessage: 请求数据成功");
                        if (home_swipeRefresh.isRefreshing()) {
                            home_swipeRefresh.setRefreshing(false);
                        }
                        datas.addAll(homevideImg.getData().getIndexList());
                        shortvideoadapter.notifyDataSetChanged();
                    } else {
                        circleDialog.dismiss();
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
        public void onFailure(Call call, IOException e) {
            e.printStackTrace();
            ((Activity) getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    circleDialog.dismiss();
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
    private int countItem;
    private int lastItem;
    private boolean isScolled;
    private boolean isCanPlay;
    private TextView home_all;
    private TextView home_samecity;
    private TextView home_recommend;
    private TextView home_superman;
    private CircleProgressDialog circleDialog;
    private boolean isResume = false;
    private int currentPage = 1;
    private ShorVideoAdapter shortvideoadapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        view = inflater.inflate(R.layout.home_fragment, container, false);
        EventBus.getDefault().register(this);
        initView();
        setButtonOnClickListener();
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (isResume) {
            View view = pagerSnapHelper.findSnapView(layoutManger);
            JCVideoPlayer.releaseAllVideos();
            if (view != null) {
                RecyclerView.ViewHolder holder = home_recycle.getChildViewHolder(view);
                if (holder != null && holder instanceof ShorVideoAdapter.ShortViewHolder) {
                    ((ShorVideoAdapter.ShortViewHolder) holder).videoPlay.resetProgressAndTime();
                    ((ShorVideoAdapter.ShortViewHolder) holder).videoPlay.startVideo();

                }
            }
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        isResume = true;
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
        home_recycle = (RecyclerView) view.findViewById(R.id.home_recycle);
        home_swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.home_swipeRefresh);
        //bt_menu=(Button)view.findViewById(R.id.bt_menu);
        iv_serach = (ImageView) view.findViewById(R.id.iv_serach);
        home_all = (TextView) view.findViewById(R.id.tv_channelall);
        home_samecity = (TextView) view.findViewById(R.id.tv_channelsame);
        home_recommend = (TextView) view.findViewById(R.id.tv_channelrecommend);
        home_superman = (TextView) view.findViewById(R.id.tv_channelsuperman);
        okHttpUtil = ((MyApplication) getActivity().getApplication()).getOkHttpUtil();
        pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(home_recycle);
        layoutManger = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        home_recycle.setLayoutManager(layoutManger);

        shortvideoadapter = new ShorVideoAdapter(datas, getActivity());
        home_recycle.setAdapter(shortvideoadapter);
        circleDialog = new CircleProgressDialog(getActivity());
        home_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_DRAGGING || newState == SCROLL_STATE_SETTLING) {
                    isScolled = true;
                } else {
                    isScolled = false;
                }
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滑动
//                        if(isCanPlay){
//                            JCVideoPlayer.releaseAllVideos();
//                            isCanPlay=false;
//                            break;
//                        }
                        View view = pagerSnapHelper.findSnapView(layoutManger);
                        JCVideoPlayer.releaseAllVideos();
                        if (view != null) {
                            RecyclerView.ViewHolder holder = home_recycle.getChildViewHolder(view);
                            if (holder != null && holder instanceof ShorVideoAdapter.ShortViewHolder) {
                                ((ShorVideoAdapter.ShortViewHolder) holder).videoPlay.resetProgressAndTime();
                                ((ShorVideoAdapter.ShortViewHolder) holder).videoPlay.startVideo();

                            }
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
                if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
                    RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
                    countItem = layoutManager.getItemCount();
                    lastItem = ((LinearLayoutManager) layoutManager).findLastCompletelyVisibleItemPosition();
                }
                if (isScolled && countItem != lastItem && lastItem == countItem - 1) {
                    Log.d(TAG, "onScrolled: ");
                    requestVideo();
                }
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

    @Subscribe()
    public void changeCommentCount(CommentCountEvent commentCountEvent) {
      if (commentCountEvent != null){
          View view = pagerSnapHelper.findSnapView(layoutManger);
          if (view != null) {
              RecyclerView.ViewHolder holder = home_recycle.getChildViewHolder(view);
              if (holder != null && holder instanceof ShorVideoAdapter.ShortViewHolder) {
                  ((ShorVideoAdapter.ShortViewHolder) holder).tv_commentcount.setText(commentCountEvent.count + "");
              }
          }
      }
    }

    @Subscribe()
    public void changeAttention(AttentionEvent attentionEvent) {
      if (attentionEvent != null){
          for (int i = 0; i < datas.size(); i++) {
              if (TextUtils.equals(datas.get(i).getUid(), attentionEvent.uid)){
                  datas.get(i).setFollowStatus(attentionEvent.isAttent);
              }
          }
        shortvideoadapter.notifyDataSetChanged();
      }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
