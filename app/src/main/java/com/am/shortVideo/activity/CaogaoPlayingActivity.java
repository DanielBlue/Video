package com.am.shortVideo.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.tiktokdemo.lky.tiktokdemo.Constant;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.CaogaoAdapter;
import adapter.CaogaoImgAdapter;
import adapter.ShorVideoAdapter;
import application.MyApplication;
import base.BaseActivity;
import bean.PublishVideoInfo;
import bean.SerachPublishVideo;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayer;
import http.OktHttpUtil;
import util.HttpUri;

import static android.support.v7.widget.RecyclerView.SCROLL_STATE_DRAGGING;
import static android.support.v7.widget.RecyclerView.SCROLL_STATE_SETTLING;

/**
 * Created by 李杰 on 2019/9/17.
 */

public class CaogaoPlayingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ShortVideoPlayingActivi";
    private RecyclerView rl_view;
    private List<String> datas=new ArrayList<>();
    private PagerSnapHelper pagerSnapHelper;
    private LinearLayoutManager layoutManger;
    private DrawerLayout drawLayout;
    private RelativeLayout rl_menu;
    private Button bt_find;
    private Button bt_menu;
    private TextView systemmessage;
    private ImageView iv_back;
    private int curChannel;
    private boolean isScolled;
    private int countItem;
    private int lastItem;
    private OktHttpUtil okHttpUtil;
    private boolean isfirst=true;

    @Override
    protected int getLayout() {
        return R.layout.shortvideo_activity;
    }

    @Override
    protected void initEventAndData() {
        okHttpUtil = OktHttpUtil.getInstance();
        addData();
        initView();
        setLinstenr();
        pagerSnapHelper = new PagerSnapHelper();
        pagerSnapHelper.attachToRecyclerView(rl_view);
        CaogaoAdapter shortvideoadapter = new CaogaoAdapter(datas, this);
        layoutManger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        rl_view.setLayoutManager(layoutManger);
        rl_view.setAdapter(shortvideoadapter);
        rl_view.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == SCROLL_STATE_DRAGGING || newState == SCROLL_STATE_SETTLING) {
                    isScolled = true;
                } else {
                    isScolled = false;
                }
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滑动
                        View view = pagerSnapHelper.findSnapView(layoutManger);
                        JCVideoPlayer.releaseAllVideos();
                        if (view != null) {
                            RecyclerView.ViewHolder holder = rl_view.getChildViewHolder(view);
                            if (holder != null && holder instanceof CaogaoAdapter.CaogaoViewHolder) {
                                ((CaogaoAdapter.CaogaoViewHolder) holder).showLoopVideo.resetProgressAndTime();
                                ((CaogaoAdapter.CaogaoViewHolder) holder).showLoopVideo.startVideo();

                            }
                        }
                        break;
                    case RecyclerView.SCROLL_STATE_DRAGGING://拖动
                        break;
                    case RecyclerView.SCROLL_STATE_SETTLING://惯性滑动
                        break;
                    default:
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
                Log.d(TAG, "countItem: "+countItem+"lastItem:"+lastItem);
                if (isScolled && countItem != lastItem && lastItem == countItem - 2) {
                    Log.d(TAG, "onScrolled:caogao111 ");
                    if(isfirst){
                        Log.d(TAG, "onScrolled:caogao ");
                        isfirst=false;
                        getCaoGaoImg(Constant.RECORD_VIDEO_PATH);
                    }
                }
            }
        });
    }

    private void setLinstenr() {
//        bt_find.setOnClickListener(this);
//        bt_menu.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }
    private void addData() {
        Intent intent=getIntent();
        String data = intent.getStringExtra("videourl");
        curChannel=intent.getIntExtra("type",5);
        datas.add(data);
    }
    private void initView() {
        iv_back=(ImageView)findViewById(R.id.iv_back);
        rl_view=(RecyclerView)findViewById(R.id.recyle_view);
//        drawLayout=(DrawerLayout)findViewById(R.id.ac_drawlayout);
//          rl_menu=(RelativeLayout)findViewById(R.id.rl);
//          bt_menu=(Button)findViewById(R.id.bt_menu);
//           bt_find=(Button)findViewById(R.id.bt_serach);
        systemmessage=(TextView)findViewById(R.id.bt_systemmessage);
        if(curChannel==0){
            systemmessage.setText("全部");
        }else if(curChannel==1){
            systemmessage.setText("同城");
        }else if(curChannel==2){
            systemmessage.setText("推荐");
        }else if(curChannel==3){
            systemmessage.setText("大V");
        }else if(curChannel==4){
            systemmessage.setText("作品");
        }else if(curChannel==5){
            systemmessage.setText("草稿");
        }
    }
    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_serach:
                break;
            case R.id.bt_systeminfo:
                if(!drawLayout.isDrawerOpen(rl_menu)){
                    drawLayout.openDrawer(rl_menu);
                }else{
                    drawLayout.closeDrawer(rl_menu);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
//            case R.id.bt_comment:
//               new CommentPopupWindow(this);
//                break;

            default:
        }
    }
    public void getCaoGaoImg(String path){
        File file=new File(path);
        if(!file.exists()){
            Toast.makeText(CaogaoPlayingActivity.this,"没有相关文件",Toast.LENGTH_SHORT).show();
            return;
        }
        if(!datas.isEmpty()){
            datas.clear();
        }
        File[] files=file.listFiles();
        for(int i=0;i<files.length;i++){
            PublishVideoInfo publishVideoInfo = new PublishVideoInfo();
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(files[i].getPath());// videoPath 本地视频的路径
            Bitmap bitmap  = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC );
            // mIvBigShow.setImageBitmap(bitmap);
            publishVideoInfo.setLocalurl(files[i].getAbsolutePath());
            publishVideoInfo.setBitmap(bitmap);
            datas.add(publishVideoInfo.getLocalurl());
        }
        if(datas.isEmpty()){
            Toast.makeText(CaogaoPlayingActivity.this,"没有相关文件",Toast.LENGTH_SHORT).show();
            return;
        }
        CaogaoAdapter shortvideoadapter = new CaogaoAdapter(datas, this);
        rl_view.setAdapter(shortvideoadapter);
    }
}
