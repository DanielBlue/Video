package com.am.shortVideo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.am.shortVideo.R;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.AttentionPersonVideoAdapter;
import application.MyApplication;
import bean.AttentionPersonVideo;
import bean.HomeAttentionEvent;
import bean.MessageWrap;
import event.MessageEvent;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.BaseUtils;
import util.HttpUri;
import util.RecycleViewDivider;

/**
 * Created by 李杰 on 2019/8/18.
 */

public class VideoShowFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener {
    private static final String TAG = "VideoShowFragment";
    private View view;
    private SwipeRefreshLayout attention_personvideo_swipe;
    private RecyclerView attention_personvideo_recycle;
    private OktHttpUtil okHttpUtil;
    private AttentionPersonVideoAdapter attentionPersonVideoAdapter;
    private boolean isScolled;
    private int countItem;
    private int lastItem;
    private int currentPage = 1;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    AttentionPersonVideo attentionPersonVidep = (AttentionPersonVideo) msg.obj;
                    if (currentPage == 1) {
                        dates.clear();
                    }
                    if (attention_personvideo_swipe.isRefreshing()) {
                        attention_personvideo_swipe.setRefreshing(false);
                    }
                    if (attentionPersonVidep.getCode() == 0) {
                        currentPage++;
//                        circleProgressDialog.dismiss();
                        if (attentionPersonVidep.getData().getIndexList() != null) {
                            dates.addAll(attentionPersonVidep.getData().getIndexList());
                            attentionPersonVideoAdapter.notifyDataSetChanged();
                        }
                    } else if (attentionPersonVidep.getCode() == 1005) {
//                        circleProgressDialog.dismiss();
                        if (attentionPersonVideoAdapter != null) {
                            attentionPersonVideoAdapter.clearAllData();
                        }
                        BaseUtils.getLoginDialog(getActivity()).show();
                    }
                    break;
            }
        }
    };
//    private CircleProgressDialog circleProgressDialog;
    private List<AttentionPersonVideo.DataBean.IndexListBean> dates = new ArrayList<>();

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        view = inflater.inflate(R.layout.attentionpersonvideo_fargment, container, false);
        okHttpUtil = ((MyApplication) getActivity().getApplication()).getOkHttpUtil();
        EventBus.getDefault().register(this);
//        circleProgressDialog = new CircleProgressDialog(getActivity());
        initView();
        initData();
        return view;
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLoginStatus(MessageWrap messageWrap) {
        Log.d(TAG, "getLoginStatus: ");
        if (messageWrap.getMessage().equals("true")) {
            initData();
        }
    }

    private void initData() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("page", currentPage + "");
//        circleProgressDialog.showDialog();
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_ATTENTIONUSERVIDEO,
                ((MyApplication) getActivity().getApplication()).getMaps(), maps, attentionPersonVideoCallback);
    }


    private Callback attentionPersonVideoCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            ((Activity) getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    circleProgressDialog.dismiss();
                }
            });
            Log.d(TAG, "onFailure: ");
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String attention_personvideoresult = response.body().string();
            Log.d(TAG, "onResponse: \n" + attention_personvideoresult);
            Gson gson = new Gson();
            AttentionPersonVideo attentionperonVideo = gson.fromJson(attention_personvideoresult, AttentionPersonVideo.class);
            Message message = new Message();
            message.obj = attentionperonVideo;
            message.what = 1;
            handler.sendMessage(message);

        }
    };

    private void initView() {
        attention_personvideo_recycle = (RecyclerView) view.findViewById(R.id.attention_personvideo_recycle);
        attention_personvideo_swipe = (SwipeRefreshLayout) view.findViewById(R.id.attention_personvideo_swipelayout);
        LinearLayoutManager linearLayoutManger = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        attention_personvideo_recycle.setLayoutManager(linearLayoutManger);
        attention_personvideo_recycle.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.VERTICAL, 2, 0, 0, getResources().getColor(R.color.colorWhite)));
        attentionPersonVideoAdapter = new AttentionPersonVideoAdapter(dates, getActivity());
        attention_personvideo_recycle.setAdapter(attentionPersonVideoAdapter);
        attention_personvideo_swipe.setOnRefreshListener(this);
        attention_personvideo_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    initData();
                }
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    public void onRefresh() {
        currentPage = 1;
        initData();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {
        if (event != null) {
            if (event instanceof HomeAttentionEvent) {
                onRefresh();
            }
        }
    }
}
