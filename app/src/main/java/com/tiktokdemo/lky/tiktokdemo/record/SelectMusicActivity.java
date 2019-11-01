package com.tiktokdemo.lky.tiktokdemo.record;

import android.app.ProgressDialog;
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
import com.tiktokdemo.lky.tiktokdemo.Constant;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;

import adapter.SelectMusicAdapter;
import application.MyApplication;
import bean.SelectMusicBean;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.BaseUtils;
import util.HttpUri;
import util.RecycleViewDivider;
import util.StatusBarUtil;

/**
 * Created by 李杰 on 2019/9/13.
 */

public class SelectMusicActivity extends AppCompatActivity implements View.OnClickListener,
        SelectMusicAdapter.SelectBgmStatus{
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
    //    private DownBGMPopupWindow downbgmPopupWindow;
    private String curId;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.selectmusic_layout);
        okHttpUtil = OktHttpUtil.getInstance();
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
        initView();
    }

    private void initView() {
        progressDialog = new ProgressDialog(SelectMusicActivity.this);
        progressDialog.setMessage("加载中...");
        progressDialog.setCanceledOnTouchOutside(false);
        progressDialog.setCancelable(false);

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
        File file = new File(Constant.DOWNBGM);
        if (file.exists()) {
            for (File childFile : file.listFiles()) {
                if (childFile.isFile()) {
                    childFile.delete();
                }
            }
        }
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
    public void selectBgmExits(boolean isSlect, final SelectMusicBean.DataBean.IndexListBean value, String curid) {
        this.value = value;
        curId = curid;
        if (isSlect) {
            isSelectMusic = true;
            Toast.makeText(this, "选择成功", Toast.LENGTH_SHORT).show();
            onClick(iv_back);
        } else {
//            Toast.makeText(this, "未下载歌曲,请下载", Toast.LENGTH_SHORT).show();
//            downbgmPopupWindow = new DownBGMPopupWindow(this, value);
//            downbgmPopupWindow.setDownStatusLinstener(this);

//            new AlertDialog.Builder(this, R.style.Theme_AppCompat_Light_Dialog_Alert)
//                    .setMessage(R.string.tv_downbgm)
//                    .setNegativeButton(R.string.bt_eidtorcancel, new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//
//                        }
//                    })
//                    .setPositiveButton(R.string.bt_eidtorconfirm, new DialogInterface.OnClickListener() {
//
//                        @Override
//                        public void onClick(DialogInterface dialog, int which) {
//                            dialog.dismiss();
//                        }
//                    })
//                    .show();

            OktHttpUtil.getInstance().downMusicFile(HttpUri.BASE_DOMAIN + value.getAudioUrl(), new Callback() {
                @Override
                public void onFailure(Call call, IOException e) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isSelectMusic = false;
                            Toast.makeText(SelectMusicActivity.this, "加载失败，请重试", Toast.LENGTH_SHORT).show();
                            progressDialog.dismiss();
                        }
                    });
                }

                @Override
                public void onResponse(Call call, Response response) throws IOException {
                    writeFile(response, value);
                }
            });

            progressDialog.show();
        }
    }

    private void writeFile(Response response, SelectMusicBean.DataBean.IndexListBean value) {
        InputStream is = null;
        FileOutputStream fos = null;
        is = response.body().byteStream();
        File file = new File(Constant.DOWNBGM, value.getName() + ".mp3");
        try {
            fos = new FileOutputStream(file);
            byte[] bytes = new byte[1024];
            int len = 0;
            //获取下载的文件的大小
            long fileSize = response.body().contentLength();
            long sum = 0;
            int porSize = 0;
            while ((len = is.read(bytes)) != -1) {
                fos.write(bytes);
                sum += len;
                porSize = (int) ((sum * 1.0f / fileSize) * 100);
                Log.d("myTag", "writeFile: " + porSize);
                if (porSize == 100) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            isSelectMusic = true;
                            progressDialog.dismiss();
                            onClick(iv_back);
                            Toast.makeText(SelectMusicActivity.this, "选择成功", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        } catch (Exception e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    isSelectMusic = false;
                    progressDialog.dismiss();
                    Toast.makeText(SelectMusicActivity.this, "加载失败请重试", Toast.LENGTH_SHORT).show();
                }
            });
            e.printStackTrace();
        } finally {
            try {
                if (is != null) {
                    is.close();
                }
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

//    @Override
//    public void downisSucess(boolean isSucess) {
//        if (isSucess) {
//            isSelectMusic = true;
//            Toast.makeText(this, "下载成功", Toast.LENGTH_SHORT).show();
//        } else {
//            isSelectMusic = false;
//            Toast.makeText(this, "下载失败", Toast.LENGTH_SHORT).show();
//
//        }
//
//    }
}
