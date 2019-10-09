package com.am.shortVideo.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PagerSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.ArrayList;
import java.util.List;

import adapter.CaogaoAdapter;
import application.MyApplication;
import base.BaseActivity;
import customeview.ShortVideoPlayer;

/**
 * Created by 李杰 on 2019/9/17.
 */

public class CaogaoPlayingActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "ShortVideoPlayingActivi";
    private RecyclerView mRvList;
    private List<String> datas=new ArrayList<>();
    private PagerSnapHelper mSnapHelper;
    private LinearLayoutManager layoutManger;
    private DrawerLayout drawLayout;
    private RelativeLayout rl_menu;
    private Button bt_find;
    private Button bt_menu;
    private TextView systemmessage;
    private ImageView iv_back;
    private int curChannel;
    private CaogaoAdapter mAdapter;

    @Override
    protected int getLayout() {
        return R.layout.shortvideo_activity;
    }

    @Override
    protected void initEventAndData() {
        addData();
        initView();
        setLinstenr();
        mSnapHelper = new PagerSnapHelper();
        mSnapHelper.attachToRecyclerView(mRvList);
        mAdapter = new CaogaoAdapter(datas);
        mAdapter.setEnableLoadMore(false);
        layoutManger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        mRvList.setLayoutManager(layoutManger);
        mRvList.setAdapter(mAdapter);
        mRvList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                switch (newState) {
                    case RecyclerView.SCROLL_STATE_IDLE://停止滑动
                        View view = mSnapHelper.findSnapView(layoutManger);
                        int position = layoutManger.getPosition(view);
                        if (position != mCurPosition) {
                            startPlay(position);
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
            }
        });
//        getCaoGaoImg(Constant.RECORD_VIDEO_PATH);
        MyApplication.mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (mCurPlayer == null) {
                    startPlay(0);
                }
            }
        },500);
    }

    private int mCurPosition = -1;
    public ShortVideoPlayer mCurPlayer;

    private void startPlay(final int position) {
        if (mCurPlayer != null) {
            //先释放之前的播放器
            mCurPlayer.getCurrentPlayer().release();
        }

        //当前是视频则开始播放
        mRvList.post(new Runnable() {
            @Override
            public void run() {
                BaseViewHolder viewHolder = (BaseViewHolder) mRvList.findViewHolderForLayoutPosition(position);
                if (viewHolder != null) {
                    mCurPlayer = viewHolder.getView(R.id.video_player);
                    //开始播放
                    if (mCurPlayer != null) {
                        mCurPlayer.getCurrentPlayer().startPlayLogic();
                        mCurPosition = position;
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
        Bundle data = intent.getBundleExtra("data");
        datas = (List<String>) data.getSerializable("datas");
        curChannel=data.getInt("type",5);
    }
    private void initView() {
        iv_back=(ImageView)findViewById(R.id.iv_back);
        mRvList =(RecyclerView)findViewById(R.id.recyle_view);
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
    protected void onDestroy() {
        super.onDestroy();
        if (mCurPlayer != null) {
            mCurPlayer.getCurrentPlayer().release();
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
//    public void getCaoGaoImg(String path){
//        File file=new File(path);
//        if(!file.exists()){
//            Toast.makeText(CaogaoPlayingActivity.this,"没有相关文件",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        if(!datas.isEmpty()){
//            datas.clear();
//        }
//        File[] files=file.listFiles();
//        for(int i=0;i<files.length;i++){
//            PublishVideoInfo publishVideoInfo = new PublishVideoInfo();
//            MediaMetadataRetriever media = new MediaMetadataRetriever();
//            media.setDataSource(files[i].getPath());// videoPath 本地视频的路径
//            Bitmap bitmap  = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC );
//            // mIvBigShow.setImageBitmap(bitmap);
//            publishVideoInfo.setLocalurl(files[i].getAbsolutePath());
//            publishVideoInfo.setBitmap(bitmap);
//            datas.add(publishVideoInfo.getLocalurl());
//        }
//        if(datas.isEmpty()){
//            Toast.makeText(CaogaoPlayingActivity.this,"没有相关文件",Toast.LENGTH_SHORT).show();
//            return;
//        }
//        mAdapter.replaceData(datas);
//        if (mCurPlayer == null) {
//            startPlay(0);
//        }
//    }
}
