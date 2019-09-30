package com.am.shortVideo.activity;

import android.os.Handler;
import android.os.Message;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.google.gson.Gson;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;

import adapter.MessaageCommentAdapter;
import application.MyApplication;
import base.BaseActivity;
import bean.CommentAtBean;
import bean.MessageWrap;
import http.OktHttpUtil;
import okhttp3.Callback;
import okhttp3.Response;
import util.BaseUtils;
import util.HttpUri;
import util.RecycleViewDivider;

/**
 * Created by JC on 2019/8/21.
 */

public class CommentAndActivity extends BaseActivity implements View.OnClickListener,SwipeRefreshLayout.OnRefreshListener {
    private RecyclerView commentand_recycle;
    private TextView comment_title;
    private OktHttpUtil okHttpUtil;
    private static final String TAG = "CommentAndActivity";
    private TextView comment_emptyview;
    private ImageView iv_back;
    private SwipeRefreshLayout commentnand_swipefreshlayout;
    private CircleProgressDialog circleprogress;

    @Override
    protected int getLayout() {
        return R.layout.commentand_activity;
    }

    @Override
    protected void initEventAndData() {
        okHttpUtil=MyApplication.getOkHttpUtil();
        EventBus.getDefault().register(this);
        circleprogress=new CircleProgressDialog(this);
        initView();
        setOnClickLinstener();
    }

    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 1:
                    CommentAtBean messagecommentshow= (CommentAtBean) msg.obj;
                    if(commentnand_swipefreshlayout.isRefreshing()){
                        commentnand_swipefreshlayout.setRefreshing(false);
                    }
                    if(messagecommentshow.getCode()==0) {
                        circleprogress.dismiss();
                        if (messagecommentshow.getData().getPageList()!=null) {
                            comment_emptyview.setVisibility(View.GONE);
                            MessaageCommentAdapter messageCommentAdapter = new MessaageCommentAdapter(messagecommentshow.getData().getPageList()
                                    , CommentAndActivity.this);
                            commentand_recycle.setAdapter(messageCommentAdapter);
                        } else {
                            comment_emptyview.setVisibility(View.VISIBLE);
                        }
                    }else if(messagecommentshow.getCode()==1005){
                        circleprogress.dismiss();
                        BaseUtils.getLoginDialog(CommentAndActivity.this).show();
                    }
                    break;
            }
        }
    };
    private Callback replayCallback=new Callback() {
        @Override
        public void onFailure(okhttp3.Call call, IOException e) {
          runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    circleprogress.dismiss();
                }
            });
        }

        @Override
        public void onResponse(okhttp3.Call call, Response response) throws IOException {
        String replayResult=response.body().string();
            Log.d(TAG, "onResponse:\n "+replayResult);
            Gson gson=new Gson();
            CommentAtBean messageshow=gson.fromJson(replayResult, CommentAtBean.class);
            Message message=new Message();
            message.what=1;
            message.obj=messageshow;
            handler.sendMessage(message);
        }
    };
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLoginStatus(MessageWrap messageWrap){
        Log.d(TAG, "getLoginStatus: ");
        if(messageWrap.getMessage().equals("true")){
            okHttpUtil.sendGetRequest(HttpUri.BASE_URL+HttpUri.MESSAGE.REQUEST_HEADER_VIDEOAPPECT,((MyApplication)getApplicationContext()).getMaps()
                    ,replayCallback);
        }
    }
    private void setOnClickLinstener() {
        iv_back.setOnClickListener(this);
        commentnand_swipefreshlayout.setOnRefreshListener(this);
    }
    private void initView() {
        comment_title=(TextView)findViewById(R.id.bt_systemmessage);
        commentnand_swipefreshlayout=(SwipeRefreshLayout) findViewById(R.id.commentnand_swipefreshlayout);
        comment_emptyview=(TextView)findViewById(R.id.tv_commentand_emptyview);
        comment_title.setText(getResources().getString(R.string.title_comment));
        iv_back=(ImageView)findViewById(R.id.iv_back);
        commentand_recycle=(RecyclerView)findViewById(R.id.commentand_recycle);
        LinearLayoutManager linnearLayoutManger=new LinearLayoutManager(this,LinearLayoutManager.VERTICAL,false);
        commentand_recycle.setLayoutManager(linnearLayoutManger);
        commentand_recycle.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL,2,0,0,getResources().getColor(R.color.colorblack)));
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL+HttpUri.MESSAGE.REQUEST_HEADER_VIDEOAPPECT,((MyApplication)getApplicationContext()).getMaps()
        ,replayCallback);
        circleprogress.showDialog();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.iv_back:
                finish();
                break;
        }
    }

    @Override
    public void onRefresh() {
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL+HttpUri.MESSAGE.REQUEST_HEADER_VIDEOAPPECT,((MyApplication)getApplicationContext()).getMaps()
                ,replayCallback);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }
}
