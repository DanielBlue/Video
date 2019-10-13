package com.am.shortVideo.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.MessageAdapter;
import application.MyApplication;
import base.BaseActivity;
import bean.HistoryMessageBean;
import butterknife.BindView;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.BaseUtils;
import util.HttpUri;
import util.RecycleViewDivider;

public class MessageHelpActivity extends BaseActivity {
    @BindView(R.id.message_recycle)
    RecyclerView mRvList;

    private static final String TAG = "MessageHelpActivity";
    private OktHttpUtil okHttpUtil;

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://需要修改
                    HistoryMessageBean historymessage = (HistoryMessageBean) msg.obj;
                    List<HistoryMessageBean.DataBean.PushHistoryBean> dataList = historymessage.getData().getPushHistory();
                    if (historymessage.getCode() == 0) {
                        if (dataList.size() > 0) {
                            messageAdapter.addData(dataList);
                            messageAdapter.loadMoreComplete();
                            currentPage++;
                        } else {
                            messageAdapter.loadMoreEnd();
                        }
                    } else if (historymessage.getCode() == 1005) {
                        BaseUtils.getLoginDialog(MessageHelpActivity.this).show();
                    }

                    break;
            }
        }
    };

    private Callback historyCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String historymessageResult = response.body().string();
            Log.d(TAG, "onResponse: \n" + historymessageResult);
            Gson gson = new Gson();
            HistoryMessageBean historymessagbean = gson.fromJson(historymessageResult, HistoryMessageBean.class);
            Message message = new Message();
            message.what = 1;
            message.obj = historymessagbean;
            handler.sendMessage(message);
        }
    };
    private MessageAdapter messageAdapter;

    @Override
    protected int getLayout() {
        return R.layout.activity_message_help;
    }

    @Override
    protected void initEventAndData() {
        okHttpUtil = OktHttpUtil.getInstance();
        initView();
        initData();
    }

    private void initView() {
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, android.support.v7.widget.LinearLayoutManager.VERTICAL, false);
        mRvList.setLayoutManager(linearLayoutManager);
        mRvList.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL, 2, 0, 0, getResources().getColor(R.color.color_e5e5e5)));

        messageAdapter = new MessageAdapter(new ArrayList<HistoryMessageBean.DataBean.PushHistoryBean>());
        messageAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                initData();
            }
        }, mRvList);
        mRvList.setAdapter(messageAdapter);
        ((TextView) findViewById(R.id.bt_systemmessage)).setText("消息助手");
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private int currentPage = 1;

    private void initData() {
        HashMap<String, String> param = new HashMap<>();
        param.put("page", String.valueOf(currentPage));
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.MESSAGE.REQUEST_HEADER_PUSHHISTORY
                , ((MyApplication) this.getApplicationContext()).getMaps(), param, historyCallback);
    }

}
