package com.tiktokdemo.lky.tiktokdemo.record;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.google.gson.Gson;

import java.io.IOException;

import adapter.SelectMusicAdapter;
import application.MyApplication;
import bean.SelectMusicBean;
import customeview.DownBGMPopupWindow;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.BaseUtils;
import util.HttpUri;
import util.RecycleViewDivider;

/**
 * Created by 李杰 on 2019/9/13.
 */

public class SelectMusicActivity extends AppCompatActivity implements View.OnClickListener,
        SelectMusicAdapter.SelectBgmStatus, DownBGMPopupWindow.DownBGMIsSucess {
    private TextView fans_title;
    private ImageView iv_back;
    private RecyclerView music_recycleview;
    private OktHttpUtil okHttpUtil;
    private SelectMusicBean.DataBean.IndexListBean value;
    private boolean isSelectMusic;
    private static final String TAG = "SelectMusicActivity";
    private SelectMusicAdapter selectmusicAdatper;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    SelectMusicBean musicBean = (SelectMusicBean) msg.obj;
                    if (musicBean.getCode() == 0) {
                        if (musicBean.getData() != null && musicBean.getData().getIndexList() != null) {
                            selectmusicAdatper = new SelectMusicAdapter(musicBean.getData().getIndexList(), SelectMusicActivity.this);
                            selectmusicAdatper.setOnSelectBgmExitsLinstener(SelectMusicActivity.this);
                            music_recycleview.setAdapter(selectmusicAdatper);
                        }
                    } else if (musicBean.getCode() == 1005) {
                        BaseUtils.getLoginDialog(SelectMusicActivity.this).show();
                    }
                    break;
            }
        }
    };
    private Callback selectMusicCallBack = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String musicResult = response.body().string();
            Log.d(TAG, "onResponse: \n" + musicResult);
            Gson gson = new Gson();
            SelectMusicBean musicBean = gson.fromJson(musicResult, SelectMusicBean.class);
            Message message = new Message();
            message.what = 1;
            message.obj = musicBean;
            handler.sendMessage(message);
        }
    };
    private DownBGMPopupWindow downbgmPopupWindow;
    private String curId;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectmusic_layout);
        okHttpUtil = OktHttpUtil.getInstance();
        initView();
    }

    private void initView() {
        fans_title = (TextView) findViewById(R.id.bt_systemmessage);
        fans_title.setText("音乐列表");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        iv_back.setOnClickListener(this);
        music_recycleview = (RecyclerView) findViewById(R.id.music_recycleview);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        music_recycleview.setLayoutManager(linearLayoutManager);
        music_recycleview.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL, 2, 0, 0, getResources().getColor(R.color.gray_normal)));
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.BGM.REQUEST_HEADER_BGM,
                ((MyApplication) getApplicationContext()).getMaps(), selectMusicCallBack);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                if (value != null && curId != null) {
                    Intent intent = new Intent();
                    intent.putExtra("result1", isSelectMusic);
                    intent.putExtra("result2", value.getName());
                    intent.putExtra("result3", curId);
                    setResult(RESULT_OK, intent);
                }
                finish();
                break;
        }
    }

    @Override
    public void selectBgmExits(boolean isSlect, SelectMusicBean.DataBean.IndexListBean value, String curid) {
        this.value = value;
        curId = curid;
        if (isSlect) {
            isSelectMusic = true;
            Toast.makeText(this, "选择成功", Toast.LENGTH_SHORT).show();
            onClick(iv_back);
        } else {
            Toast.makeText(this, "未下载歌曲,请下载", Toast.LENGTH_SHORT).show();
            downbgmPopupWindow = new DownBGMPopupWindow(this, value);
            downbgmPopupWindow.setDownStatusLinstener(this);
        }
    }

    @Override
    public void downisSucess(boolean isSucess) {
        if (isSucess) {
            isSelectMusic = true;
            Toast.makeText(this, "下载成功", Toast.LENGTH_SHORT).show();
        } else {
            isSelectMusic = false;
            Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT).show();

        }

    }
}
