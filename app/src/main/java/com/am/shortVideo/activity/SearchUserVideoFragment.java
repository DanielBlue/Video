package com.am.shortVideo.activity;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

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

import adapter.AttentionPersonVideoAdapter;
import application.MyApplication;
import bean.AttentionPersonVideo;
import bean.MessageSearchinfo;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.BaseUtils;
import util.HttpUri;
import util.RecycleViewDivider;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by 李杰 on 2019/9/9.
 */

public class SearchUserVideoFragment extends Fragment {
    private static final String TAG = "SearchUserVideoFragment";
    private View view;
    private RecyclerView fragment_searchuservideo_recycle;
    private OktHttpUtil okHttpUtil;

    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;
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
                        datas.clear();
                    }

                    if (srl_swipeRefresh.isRefreshing()) {
                        srl_swipeRefresh.setRefreshing(false);
                    }
                    if (attentionPersonVidep.getCode() == 0) {
                        currentPage++;
                        cicleprogress.dismiss();
                        if (attentionPersonVidep.getData().getIndexList() != null && !attentionPersonVidep.getData().getIndexList().isEmpty()) {
                            datas.addAll(attentionPersonVidep.getData().getIndexList());
                            attentionPersonVideoAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "没有搜索到相关视频", Toast.LENGTH_SHORT).show();
                        }
                    } else if (attentionPersonVidep.getCode() == 1005) {
                        cicleprogress.dismiss();
                        BaseUtils.getLoginDialog(getActivity()).show();
                    }
                    break;
            }
        }
    };

    private Callback attentionPersonVideoCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            ((Activity) getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cicleprogress.dismiss();
                }
            });
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
    private EditText ed_edittext;
    private SwipeRefreshLayout srl_swipeRefresh;
    private CircleProgressDialog cicleprogress;
    private List<AttentionPersonVideo.DataBean.IndexListBean> datas = new ArrayList<>();
    private AttentionPersonVideoAdapter attentionPersonVideoAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_searchuservideo, container, false);
        okHttpUtil = ((MyApplication) getActivity().getApplication()).getOkHttpUtil();
        EventBus.getDefault().register(this);
        cicleprogress = new CircleProgressDialog(getActivity());
        initView();
        return view;
    }

    private void initView() {
        ed_edittext = (EditText) view.findViewById(R.id.attention_edittext);
        srl_swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_swipeRefresh);
        ed_edittext.setCursorVisible(false);
        fragment_searchuservideo_recycle = (RecyclerView) view.findViewById(R.id.fragment_searchuservideo_recycle);
        LinearLayoutManager layoutManger = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        fragment_searchuservideo_recycle.setLayoutManager(layoutManger);
        fragment_searchuservideo_recycle.addItemDecoration(new RecycleViewDivider(getActivity(), LinearLayout.VERTICAL, 2, 0, 0, getResources().getColor(R.color.white)));
        attentionPersonVideoAdapter = new AttentionPersonVideoAdapter(datas, getActivity());
        fragment_searchuservideo_recycle.setAdapter(attentionPersonVideoAdapter);
        ed_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_edittext.setCursorVisible(true);
            }
        });
        ed_edittext.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //这里注意要作判断处理，ActionDown、ActionUp都会回调到这里，不作处理的话就会调用两次
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    //处理事件
                    closeKeybord(ed_edittext, getActivity());
                    if (!ed_edittext.getText().toString().trim().isEmpty()) {
                        currentPage = 1;
                        searchVideo(ed_edittext.getText().toString().trim());
                    }
                    return true;
                }
                return false;
            }
        });

        srl_swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_HOMEVIDEO, homevideoCallback);
                currentPage = 1;
                searchVideo(ed_edittext.getText().toString().trim());
            }
        });

        fragment_searchuservideo_recycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    searchVideo(ed_edittext.getText().toString().trim());
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getSearchData(MessageSearchinfo messageSearchinfo) {
        Log.d(TAG, "getSearchData: \n" + messageSearchinfo.getMessage());
        ed_edittext.setText(messageSearchinfo.getMessage());
        searchVideo(messageSearchinfo.getMessage());

    }

    private void searchVideo(String str) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("key", str);
        maps.put("page", currentPage + "");
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SEARCHVIDEO,
                ((MyApplication) getActivity().getApplication()).getMaps(), maps, attentionPersonVideoCallback);
        cicleprogress.showDialog();
    }

    //关闭软键盘
    public void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().removeAllStickyEvents();
        EventBus.getDefault().unregister(this);

    }

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
}
