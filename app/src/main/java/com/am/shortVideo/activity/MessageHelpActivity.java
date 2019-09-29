package com.am.shortVideo.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.LinearLayout;

import com.am.shortVideo.R;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.MessageAdapter;
import application.MyApplication;
import base.BaseActivity;
import bean.HistoryMessageBean;
import butterknife.BindView;
import customeview.LoginPopupwindow;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;
import util.RecycleViewDivider;

public class MessageHelpActivity extends BaseActivity {
    @BindView(R.id.message_recycle)
    RecyclerView messageRecycle;

    private static final String TAG = "MessageHelpActivity";
    private OktHttpUtil okHttpUtil;
    private List<HistoryMessageBean.DataBean.PushHistoryBean> datas = new ArrayList<>();

    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1://需要修改
                    HistoryMessageBean historymessage = (HistoryMessageBean) msg.obj;
                    datas.clear();
                    if (historymessage.getCode() == 0) {
                        datas.addAll(historymessage.getData().getPushHistory());
                        messageAdapter.notifyDataSetChanged();
                    } else if (historymessage.getCode() == 1005) {
                        new LoginPopupwindow(MessageHelpActivity.this);
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
        messageRecycle.setLayoutManager(linearLayoutManager);
        messageRecycle.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL, 2, 0, 0, getResources().getColor(R.color.color_e5e5e5)));

        messageAdapter = new MessageAdapter(this, datas);
        messageRecycle.setAdapter(messageAdapter);

    }

    private void initData() {
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.MESSAGE.REQUEST_HEADER_PUSHHISTORY
                , ((MyApplication) this.getApplicationContext()).getMaps(), historyCallback);
    }

}
