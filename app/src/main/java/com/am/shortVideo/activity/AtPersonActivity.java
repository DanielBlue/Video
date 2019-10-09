package com.am.shortVideo.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.AtPersonListAdapter;
import application.MyApplication;
import bean.AtPersonEvent;
import bean.AttentionPerson;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;

public class AtPersonActivity extends AppCompatActivity {

    private RecyclerView mRvList;
    private AtPersonListAdapter mAdapter;

    public static void start(Context context) {
        Intent starter = new Intent(context, AtPersonActivity.class);
        context.startActivity(starter);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_at_person);
        initView();
        loadData();
    }

    private void initView() {
        ((TextView) findViewById(R.id.bt_systemmessage)).setText("关注的人");
        findViewById(R.id.iv_back).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        mRvList = findViewById(R.id.rv_list);
        mAdapter = new AtPersonListAdapter(new ArrayList<AttentionPerson.DataBean.PageListBean>());
        mAdapter.setOnLoadMoreListener(new BaseQuickAdapter.RequestLoadMoreListener() {
            @Override
            public void onLoadMoreRequested() {
                loadData();
            }
        }, mRvList);
        mRvList.setLayoutManager(new LinearLayoutManager(this));
        mRvList.setAdapter(mAdapter);
        mAdapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
                AttentionPerson.DataBean.PageListBean pageListBean = mAdapter.getData().get(position);
                EventBus.getDefault().post(new AtPersonEvent(pageListBean.getUid(), pageListBean.getNickName()));
                finish();
            }
        });
    }

    private int currentPage = 1;

    private void loadData() {
        HashMap<String, String> maps = new HashMap<>();
        maps.put("page", currentPage + "");
        MyApplication.getOkHttpUtil().sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_ATTENTIONPERSONQUERY,
                MyApplication.getInstance().getMaps(), maps, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {

                    }

                    @Override
                    public void onResponse(Call call, final Response response) throws IOException {
                        final String attentionperson = response.body().string();
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                Gson gson = new Gson();
                                AttentionPerson attentionPersonVideo = gson.fromJson(attentionperson, AttentionPerson.class);

                                List<AttentionPerson.DataBean.PageListBean> pageList = attentionPersonVideo.getData().getPageList();
                                if (pageList.size() == 0) {
                                    mAdapter.loadMoreEnd();
                                } else {
                                    mAdapter.addData(pageList);
                                    mAdapter.loadMoreComplete();
                                    currentPage++;
                                }
                            }
                        });
                    }
                });
    }
}
