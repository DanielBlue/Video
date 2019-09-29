package com.am.shortVideo.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import com.am.shortVideo.R;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.SQLite;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import adapter.SearchUserinfoAdapter;
import application.MyApplication;
import bean.MessageSearchinfo;
import bean.SearchUserinfo;
import customeview.LoginPopupwindow;
import customeview.SliderView;
import db.UserModel;
import db.UserModel_Table;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;
import util.PinYinUtil;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by 李杰 on 2019/9/9.
 */

public class SearchUserinfoFragment extends Fragment implements SliderView.ChangeLetter {
    private static final String TAG = "SearchUserinfoFragment";
    private View view;
    private List<SearchUserinfo.DataBean.PageListBean> datas = new ArrayList<>();
    private OktHttpUtil okHttpUtil;
    private SearchUserinfoAdapter searchUserinfoAdapter;
    private PinYinUtil pinYinUtil;
    private SliderView slideView;
    private RecyclerView attentionRecycle;
    private EditText ed_edittext;
    private SwipeRefreshLayout srl_swipeRefresh;
    private int currentPage = 1;
    //目标项是否在最后一个可见项之后
    private boolean mShouldScroll;
    //记录目标项位置
    private int mToPosition;
    private boolean isScolled;
    private int countItem;
    private int lastItem;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    SearchUserinfo searchUserinfo = (SearchUserinfo) msg.obj;
                    if (srl_swipeRefresh.isRefreshing()) {
                        srl_swipeRefresh.setRefreshing(false);
                    }
                    if (currentPage == 1) {
                        datas.clear();
                    }
                    if (searchUserinfo.getCode() == 0) {
                        circleprogressDialog.dismiss();
                        if (searchUserinfo.getData().getPageList() != null && !searchUserinfo.getData().getPageList().isEmpty()) {
                            slideView.setVisibility(View.GONE);
                            datas.addAll(searchUserinfo.getData().getPageList());
                            sortData();
                            searchUserinfoAdapter.notifyDataSetChanged();
                        } else {
                            slideView.setVisibility(View.GONE);
                            Toast.makeText(getActivity(), "没有搜索到相关用户", Toast.LENGTH_SHORT).show();
                        }
                    } else if (searchUserinfo.getCode() == 1005) {
                        circleprogressDialog.dismiss();
                        new LoginPopupwindow(getActivity());
                    } else {
                        Toast.makeText(getActivity(), "没有搜索到相关用户", Toast.LENGTH_SHORT).show();
                    }
                    break;
            }
        }
    };
    private Callback searchCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            ((Activity) getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    circleprogressDialog.dismiss();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String searchResult = response.body().string();
            Log.d(TAG, "onResponse: \n" + searchResult);
            Gson gson = new Gson();
            SearchUserinfo searchUserinfo = gson.fromJson(searchResult, SearchUserinfo.class);
            Message message = new Message();
            message.what = 1;
            message.obj = searchUserinfo;
            handler.sendMessage(message);
        }
    };
    private CircleProgressDialog circleprogressDialog;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_searchuserinfo, container, false);
        pinYinUtil = new PinYinUtil();
        initView();
        okHttpUtil = OktHttpUtil.getInstance();
        circleprogressDialog = new CircleProgressDialog(getActivity());
        EventBus.getDefault().register(this);
        return view;
    }

    private void initView() {
        srl_swipeRefresh = (SwipeRefreshLayout) view.findViewById(R.id.srl_swipeRefresh);

        ed_edittext = (EditText) view.findViewById(R.id.attention_edittext);
        ed_edittext.setCursorVisible(false);
        slideView = (SliderView) view.findViewById(R.id.sliderview);
        attentionRecycle = (RecyclerView) view.findViewById(R.id.fragment_searchuserinfo_recycle);
        slideView.setChangeLetterLinstener(this);
        LinearLayoutManager layoutManger = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        attentionRecycle.setLayoutManager(layoutManger);
        searchUserinfoAdapter = new SearchUserinfoAdapter(datas, getActivity(), pinYinUtil);
        attentionRecycle.setAdapter(searchUserinfoAdapter);
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
                        searchUser(ed_edittext.getText().toString().trim());
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
                searchUser(ed_edittext.getText().toString().trim());
            }
        });

        attentionRecycle.addOnScrollListener(new RecyclerView.OnScrollListener() {
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
                    searchUser(ed_edittext.getText().toString().trim());
                }
            }
        });

    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void getSearchData(MessageSearchinfo messageSearchinfo) {
        ed_edittext.setText(messageSearchinfo.getMessage());
        searchUser(messageSearchinfo.getMessage());
    }

    private void searchUser(String str) {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("key", str);
        maps.put("page", currentPage + "");
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_SEARCH
                , ((MyApplication) (getActivity().getApplication())).getMaps(), maps, searchCallback);
        circleprogressDialog.showDialog();
    }

    //将集合中的汉字转化为字母
    public void sortData() {
        Collections.sort(datas, new Comparator<SearchUserinfo.DataBean.PageListBean>() {
            @Override
            public int compare(SearchUserinfo.DataBean.PageListBean o1, SearchUserinfo.DataBean.PageListBean o2) {
                String compare1 = "";
                String compare2 = "";
                if (o1 != null) {
                    compare1 = pinYinUtil.getPinYin(o1.getNickName());
                    Log.d(TAG, "compare1: " + compare1);
                } else {
                    Log.d(TAG, "compare1: null");
                }
                if (o2 != null) {
                    compare2 = pinYinUtil.getPinYin(o2.getNickName());
                    Log.d(TAG, "compare2: " + compare2);
                } else {
                    Log.d(TAG, "compare2: null");
                }
                return compare1.compareTo(compare2);
            }

        });
    }

    //得到字符匹配的字母
    @Override
    public void getchangeLetter(String letter) {
        int position = 0;
        for (int i = 0; i < datas.size(); i++) {
            //获取名字的首字母
            String str = pinYinUtil.getPinYin(datas.get(i).getNickName()).substring(0, 1).toUpperCase();
            if (str.equals(letter)) {
                //第一次出现的位置
                position = i;
                //将listview滚动到该位置
//                mListView.setSelection(position);
                if (position != -1) {
                    smoothMoveToPosition(attentionRecycle, position);
                } else {
                    smoothMoveToPosition(attentionRecycle, position + 1);
                }
                break;
            }
        }
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
