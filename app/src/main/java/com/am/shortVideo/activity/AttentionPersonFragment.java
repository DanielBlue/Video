package com.am.shortVideo.activity;

import android.app.Activity;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
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
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import adapter.AttentionAdapter;
import application.MyApplication;
import bean.AttentionPerson;
import bean.HomeAttentionEvent;
import bean.MessageWrap;
import customeview.SliderView;
import event.MessageEvent;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.BaseUtils;
import util.HttpUri;
import util.PinYinUtil;
import util.RecycleViewDivider;


/**
 * Created by 李杰 on 2019/8/18.
 */

public class AttentionPersonFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener, SliderView.ChangeLetter {
    private static final String TAG = "AttentionPersonFragment";
    private View view;
    private SwipeRefreshLayout attentionswipte;
    private AttentionAdapter attenPersonAdapter;
    private SwipeRefreshLayout secondFragment_swipte;
    private RecyclerView secondFragment_recycle;
    private OktHttpUtil okHttpUtil;
    private List<AttentionPerson.DataBean.PageListBean> dates = new ArrayList<>();

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
                    AttentionPerson attentionPerson = (AttentionPerson) msg.obj;
                    if (secondFragment_swipte.isRefreshing()) {
                        secondFragment_swipte.setRefreshing(false);
                    }
                    if (currentPage == 1) {
                        dates.clear();
                    }
                    if (attentionPerson.getCode() == 0) {
                        currentPage++;
//                        circleprogressDialog.dismiss();
                        dates.addAll(attentionPerson.getData().getPageList());
                        sortData();
                        attenPersonAdapter.notifyDataSetChanged();
                        slideView.setVisibility(dates.isEmpty() ? View.GONE : View.VISIBLE);
                    } else if (attentionPerson.getCode() == 1005) {
//                        circleprogressDialog.dismiss();
                        if (attenPersonAdapter != null) {
                            attenPersonAdapter.clearAllData();
                        }
                        BaseUtils.getLoginDialog(getActivity()).show();
                    }
                    break;
                default:
            }
        }
    };
    private Callback attenPersonCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(TAG, "attentionlist-->onFailure: ");
            ((Activity) getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
//                    circleprogressDialog.dismiss();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String attentionperson = response.body().string();
            Log.d(TAG, "attentionlist-->onResponse: \n" + attentionperson);
            Gson gson = new Gson();
            AttentionPerson attentionPersonVideo = gson.fromJson(attentionperson, AttentionPerson.class);
            Message message = new Message();
            message.obj = attentionPersonVideo;
            message.what = 1;
            handler.sendMessage(message);
        }
    };
    private PagerSnapHelper pagerSnapHelper;
    private LinearLayoutManager layoutManger;
    private PinYinUtil pinYinUtil;
    private SliderView slideView;
//    private CircleProgressDialog circleprogressDialog;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView: ");
        view = inflater.inflate(R.layout.second_attentionfragment, container, false);
        okHttpUtil = MyApplication.getOkHttpUtil();
        pinYinUtil = new PinYinUtil();
        EventBus.getDefault().register(this);
//        circleprogressDialog = new CircleProgressDialog(getActivity());
        initData();
        initView();
        return view;
    }

    private void initView() {
        secondFragment_swipte = (SwipeRefreshLayout) view.findViewById(R.id.secondfragment_swiperefresh);
        secondFragment_recycle = (RecyclerView) view.findViewById(R.id.secondfragment_recycle);
        secondFragment_swipte.setOnRefreshListener(this);
        layoutManger = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        secondFragment_recycle.setLayoutManager(layoutManger);
        secondFragment_recycle.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.VERTICAL
                , 2, 0, 0, getResources().getColor(R.color.colorblack)));

        attenPersonAdapter = new AttentionAdapter(dates, getActivity(), pinYinUtil);
        secondFragment_recycle.setAdapter(attenPersonAdapter);
        slideView = (SliderView) view.findViewById(R.id.sliderview);
        slideView.setChangeLetterLinstener(this);
        secondFragment_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (mShouldScroll) {
                    mShouldScroll = false;
                    smoothMoveToPosition(recyclerView, mToPosition);
                }
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

    private void initData() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("page", currentPage + "");
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_ATTENTIONPERSONQUERY,
                ((MyApplication) getActivity().getApplicationContext()).getMaps(), maps, attenPersonCallback);
//        circleprogressDialog.showDialog();
    }

    //将集合中的汉字转化为字母
    public void sortData() {
        Collections.sort(dates, new Comparator<AttentionPerson.DataBean.PageListBean>() {
            @Override
            public int compare(AttentionPerson.DataBean.PageListBean indexListBean, AttentionPerson.DataBean.PageListBean t1) {
                String compare1 = "";
                String compare2 = "";
                if (indexListBean != null) {
                    compare1 = pinYinUtil.getPinYin(indexListBean.getNickName());
                } else {
                    compare1 = pinYinUtil.getPinYin("z");
                }
                if (t1 != null) {
                    compare2 = pinYinUtil.getPinYin(t1.getNickName());
                } else {
                    compare2 = pinYinUtil.getPinYin("z");
                }

                return compare1.compareTo(compare2);
            }

        });
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLoginStatus(MessageWrap messageWrap) {
        Log.d(TAG, "getLoginStatus: ");
        if (messageWrap.getMessage().equals("true")) {
            currentPage = 1;
            initData();
//            okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_ATTENTIONPERSONQUERY,
//                    ((MyApplication) getActivity().getApplication()).getMaps(), attenPersonCallback);
        } else if (messageWrap.getMessage().equals("false")) {

        }
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

    @Override
    public void getchangeLetter(String letter) {
//        int position = attenPersonAdapter.isFirstShowLetter(letter.charAt(0));
//        secondFragment_recycle.scrollToPosition(position);
        int position = 0;
        for (int i = 0; i < dates.size(); i++) {
            //获取名字的首字母
            String str = pinYinUtil.getPinYin(dates.get(i).getNickName()).substring(0, 1).toUpperCase();
            if (str.equals(letter)) {
                //第一次出现的位置
                position = i;
                //将listview滚动到该位置
//                mListView.setSelection(position);
                if (position != -1) {
                    smoothMoveToPosition(secondFragment_recycle, position);
                } else {
                    smoothMoveToPosition(secondFragment_recycle, position + 1);
                }
                break;
            }
        }
    }

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;

    /**
     * 滑动到指定位置
     */
    private void smoothMoveToPosition(RecyclerView mRecyclerView, final int position) {
        // 第一个可见位置
        int firstItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(0));
        // 最后一个可见位置
        int lastItem = mRecyclerView.getChildLayoutPosition(mRecyclerView.getChildAt(mRecyclerView.getChildCount() - 1));
        if (position < firstItem) {
            // 第一种可能:跳转位置在第一个可见位置之前
            mRecyclerView.smoothScrollToPosition(position);
        } else if (position <= lastItem) {
            // 第二种可能:跳转位置在第一个可见位置之后
            int movePosition = position - firstItem;
            if (movePosition >= 0 && movePosition < mRecyclerView.getChildCount()) {
                int top = mRecyclerView.getChildAt(movePosition).getTop();
                mRecyclerView.smoothScrollBy(0, top);
            }
        } else {
            // 第三种可能:跳转位置在最后可见项之后
            mRecyclerView.smoothScrollToPosition(position);
            mToPosition = position;
            mShouldScroll = true;
        }
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

