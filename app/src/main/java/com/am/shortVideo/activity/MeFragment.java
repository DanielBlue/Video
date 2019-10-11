package com.am.shortVideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.media.MediaMetadataRetriever;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.jeremyfeinstein.slidingmenu.lib.SlidingMenu;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;
import com.tiktokdemo.lky.tiktokdemo.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import adapter.CaogaoImgAdapter;
import adapter.UserVideoAdapter;
import application.MyApplication;
import base.onLoadMoreLinstener;
import bean.LoginEvent;
import bean.LogoutInfo;
import bean.MessageWrap;
import bean.PublishVideoInfo;
import bean.SerachPublishVideo;
import bean.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;
import util.MeSpaceItemDecoration;
import util.PreferencesUtil;

/**
 * Created by 李杰 on 2019/8/12.
 */

public class MeFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MeFragment";
    private View view;
    private OktHttpUtil oktHttpUtil;
    private TextView bt_changpersoninfo;
    private CircleImageView me_circleImageView;
    private TextView me_nickname;
    private TextView me_useraccount;
    private TextView me_personalcontent;
    private TextView me_attentioncount;
    private TextView me_fanscount;
    private TextView me_zanscount;
    private Button me_zuoping;
    private Button me_caogao;
    private RecyclerView me_recycleview;
    private SwipeRefreshLayout me_swiprefreshlayout;
    private boolean islogin = false;
    private View me_line2;
    private View me_line3;
    private HashMap<String, String> maps;
    private ArrayList<PublishVideoInfo> bitmaps = new ArrayList<>();
    private int curSelect = 0;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    UserInfo userInfo = (UserInfo) msg.obj;
                    if (userInfo.getCode() == 0) {
                        PreferencesUtil.put(getActivity(), PreferencesUtil.SP_USER_INFO, new Gson().toJson(userInfo.getData().getUserInfo()));
//                        MyApplication.getInstance().setUserInfo(userInfo.getData().getUserInfo());
                        circleprogress.dismiss();
                        me_swiprefreshlayout.setRefreshing(false);
                        islogin = false;
                        bt_changpersoninfo.setText(getActivity().getResources().getString(R.string.me_editorinfo));
                        if (!userInfo.getData().getUserInfo().getAvatar().isEmpty()) {
                            Glide.with(getActivity()).load(HttpUri.BASE_DOMAIN + userInfo.getData().getUserInfo().getAvatar())
                                    .into(me_circleImageView);
                        }
                        if (!userInfo.getData().getUserInfo().getNickname().isEmpty()) {
                            me_nickname.setText(userInfo.getData().getUserInfo().getNickname());
                        }
                        if (!userInfo.getData().getUserInfo().getSignature().isEmpty()) {
                            me_personalcontent.setText(userInfo.getData().getUserInfo().getSignature());
                        }
                        if (!userInfo.getData().getUserInfo().getUid().isEmpty()) {
                            me_useraccount.setText(userInfo.getData().getUserInfo().getUid());
                        }
                        me_fanscount.setText("" + userInfo.getData().getUserInfo().getFansCount());
                        me_attentioncount.setText("" + userInfo.getData().getUserInfo().getFollowCount());
                        me_zanscount.setText("" + userInfo.getData().getUserInfo().getGetLikeCount());
                        maps = new HashMap<>();
                        maps.put("uid", userInfo.getData().getUserInfo().getUid());
                        ((MyApplication) getActivity().getApplicationContext()).setUserUid(userInfo.getData().getUserInfo().getUid());
                        oktHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SEARCHUSERVIDEO
                                , ((MyApplication) getActivity().getApplicationContext()).getMaps(), maps, video_zuopingCallback);
                    } else if (userInfo.getCode() == 1005) {
                        circleprogress.dismiss();
                        islogin = true;
                        bt_changpersoninfo.setText("登录");
                        bt_changpersoninfo.setBackground(getActivity().getResources().getDrawable(R.drawable.bg_round_da6464));
                        me_recycleview.setAdapter(null);
                        me_recycleview2.setAdapter(null);
                    }
                    break;
                case 2:
                    SerachPublishVideo serachPublishVideo = (SerachPublishVideo) msg.obj;
                    if (serachPublishVideo.getCode() == 0) {
                        if (serachPublishVideo.getData() != null) {
                            UserVideoAdapter userVideoAdapter = new UserVideoAdapter(serachPublishVideo
                                    .getData().getIndexList(), getActivity());
                            me_recycleview.setAdapter(userVideoAdapter);
                        }
                    }
                    break;
                case 3:
                    LogoutInfo logoutInfo = (LogoutInfo) msg.obj;
                    if (logoutInfo.code == 0) {
//                        MyApplication.getInstance().setUserInfo(null);
                        PreferencesUtil.remove(getActivity(), PreferencesUtil.SP_USER_INFO);
                        HashMap<String, String> maps = new HashMap<>();
                        maps.put("cookie", "");
                        ((MyApplication) getActivity().getApplication()).setMaps(maps, "exit");
                        EventBus.getDefault().post(MessageWrap.getInstance("false"));
                        EventBus.getDefault().post(new LoginEvent(LoginEvent.LOGINOUT));
                        slidinmenu.showContent();
                        slidinmenu.toggle();
                    }
                    Toast.makeText(getActivity(), logoutInfo.message, Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };
    private Callback video_zuopingCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(TAG, "onFailure: userinfoCallback");

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String userinfoResult = response.body().string();
            Log.d(TAG, "onResponse: userinfoCallback\n" + userinfoResult);
            Gson gson = new Gson();
            SerachPublishVideo serachPublishVideo = gson.fromJson(userinfoResult, SerachPublishVideo.class);
            Message message = new Message();
            message.what = 2;
            message.obj = serachPublishVideo;
            mHandler.sendMessage(message);
        }
    };
    private Callback userinfoCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(TAG, "onFailure: userinfoCallback");
            ((Activity) getActivity()).runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    circleprogress.dismiss();
                    me_swiprefreshlayout.setRefreshing(false);
                }
            });

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String userinfoResult = response.body().string();
            Log.d(TAG, "onResponse: userinfoCallback\n" + userinfoResult);
            Gson gson = new Gson();
            UserInfo userinfo = gson.fromJson(userinfoResult, UserInfo.class);
            Message message = new Message();
            message.what = 1;
            message.obj = userinfo;
            mHandler.sendMessage(message);
        }
    };
    private RelativeLayout hide_relativelayout;
    private DrawerLayout me_drawlayout;
    private Button bt_menu;
    private LinearLayout ll1;
    private LinearLayout ll2;
    private LinearLayout ll3;
    private LinearLayout ll4;
    private LinearLayout ll5;
    private LinearLayout ll6;
    private SlidingMenu slidinmenu;
    private PublishVideoInfo publishVideoInfo;
    private RecyclerView me_recycleview2;
    private CircleProgressDialog circleprogress;
    private UserVideoAdapter userVideoAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.me_fragment, container, false);
        oktHttpUtil = ((MyApplication) getActivity().getApplication()).getOkHttpUtil();
        EventBus.getDefault().register(this);
        circleprogress = new CircleProgressDialog(getActivity());
        initView();
//        initData();
        slidinmenu = new SlidingMenu(getActivity());
        slidinmenu.setMode(SlidingMenu.RIGHT);
        slidinmenu.setTouchModeAbove(SlidingMenu.TOUCHMODE_NONE);
        slidinmenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
        // 设置渐入渐出效果的值
        slidinmenu.setFadeDegree(0.35f);
        slidinmenu.setMenu(getSlidingMenu());
        slidinmenu.attachToActivity(getActivity(), SlidingMenu.SLIDING_CONTENT);
//        slidinmenu.showContent();
//        slidinmenu.toggle();
        return view;
    }

    private void initData() {
        oktHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_PERSONINFO
                , ((MyApplication) getActivity().getApplicationContext()).getMaps(), userinfoCallback);
        circleprogress.showDialog();
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private View getSlidingMenu() {
        View view1 = null;
        view1 = LayoutInflater.from(getActivity()).inflate(R.layout.layout_right_menu, null);
        ll1 = (LinearLayout) view1.findViewById(R.id.ll1);
        ll2 = (LinearLayout) view1.findViewById(R.id.ll2);
        ll3 = (LinearLayout) view1.findViewById(R.id.ll3);
        ll4 = (LinearLayout) view1.findViewById(R.id.ll4);
        ll5 = (LinearLayout) view1.findViewById(R.id.ll5);
        ll6 = (LinearLayout) view1.findViewById(R.id.ll6);
        ll1.setOnClickListener(this);
        ll2.setOnClickListener(this);
        ll3.setOnClickListener(this);
        ll4.setOnClickListener(this);
        ll5.setOnClickListener(this);
        ll6.setOnClickListener(this);

        return view1;
    }

    private void initView() {
        me_circleImageView = (CircleImageView) view.findViewById(R.id.me_circleimageview);
        me_nickname = (TextView) view.findViewById(R.id.me_nickname);
        me_useraccount = (TextView) view.findViewById(R.id.me_useraccount);
        me_personalcontent = (TextView) view.findViewById(R.id.tv_personalcontent);
        me_attentioncount = (TextView) view.findViewById(R.id.tv_me_attentioncount);
        me_fanscount = (TextView) view.findViewById(R.id.tv_me_fanscount);
        me_zanscount = (TextView) view.findViewById(R.id.tv_me_zanscount);
        me_zuoping = (Button) view.findViewById(R.id.bt_mezuoping);
        me_caogao = (Button) view.findViewById(R.id.bt_mecaogao);
        me_recycleview = (RecyclerView) view.findViewById(R.id.me_recycleview);
        me_recycleview2 = (RecyclerView) view.findViewById(R.id.me_recycleview2);
        me_swiprefreshlayout = (SwipeRefreshLayout) view.findViewById(R.id.me_swipeFreshLayout);
        me_swiprefreshlayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                if (curSelect==0){
                    initData();
                }else {
                    getCaoGaoImg(Constant.RECORD_VIDEO_PATH);
                }
            }
        });
        bt_changpersoninfo = (TextView) view.findViewById(R.id.me_editorinfo);
        me_line2 = (View) view.findViewById(R.id.me_line2);
        me_line3 = (View) view.findViewById(R.id.me_line3);
        bt_menu = (Button) view.findViewById(R.id.bt_menu);
        bt_changpersoninfo.setOnClickListener(this);
        me_caogao.setOnClickListener(this);
        me_zuoping.setOnClickListener(this);
        bt_menu.setOnClickListener(this);
        GridLayoutManager gridmanger = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        me_recycleview.setLayoutManager(gridmanger);
        me_recycleview.addItemDecoration(new MeSpaceItemDecoration(10));
        me_recycleview.addOnScrollListener(new onLoadMoreLinstener() {
            @Override
            protected void onLoadMoreing(int countItem, int lastItem) {
                //加载更多
//                oktHttpUtil.sendGetRequest(HttpUri.BASE_URL+HttpUri.VIDEO.REQUEST_HEADER_SEARCHUSERVIDEO
//                        ,((MyApplication)getActivity().getApplication()).getMaps(),maps,video_zuopingCallback);
            }
        });
        GridLayoutManager gridmanger1 = new GridLayoutManager(getActivity(), 3, GridLayoutManager.VERTICAL, false);
        me_recycleview2.setLayoutManager(gridmanger1);
        me_recycleview2.addItemDecoration(new MeSpaceItemDecoration(10));
        me_recycleview2.addOnScrollListener(new onLoadMoreLinstener() {
            @Override
            protected void onLoadMoreing(int countItem, int lastItem) {
                //加载更多
//                oktHttpUtil.sendGetRequest(HttpUri.BASE_URL+HttpUri.VIDEO.REQUEST_HEADER_SEARCHUSERVIDEO
//                        ,((MyApplication)getActivity().getApplication()).getMaps(),maps,video_zuopingCallback);
            }
        });
        me_recycleview2.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLoginStatus(MessageWrap messageWrap) {
        Log.d(TAG, "getLoginStatus: ");
        if (messageWrap.getMessage().equals("true")) {
            initData();
        } else if (messageWrap.getMessage().equals("false")) {
            me_circleImageView.setImageDrawable(getActivity().getResources().getDrawable(R.mipmap.app_icon));
            me_nickname.setText("");
            me_useraccount.setText("");
            me_personalcontent.setText("");
            me_fanscount.setText("");
            me_zanscount.setText("");
            me_attentioncount.setText("");
            initData();
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.me_editorinfo:
                if (islogin) {
                    Intent intent = new Intent(getActivity(), LoginActivity.class);
                    getActivity().startActivity(intent);
                } else {
                    Intent intent = new Intent(getActivity(), PersonInfoActivity.class);
                    getActivity().startActivity(intent);
                }
                break;
            case R.id.bt_mecaogao:
                if (curSelect != 1) {
                    curSelect = 1;
                    me_recycleview.setVisibility(View.GONE);
                    me_recycleview2.setVisibility(View.VISIBLE);
                    me_line2.setVisibility(View.VISIBLE);
                    me_line3.setVisibility(View.GONE);
                    getCaoGaoImg(Constant.RECORD_VIDEO_PATH);

                }
                break;
            case R.id.bt_mezuoping:
                if (curSelect != 0) {
                    curSelect = 0;
                    me_recycleview.setVisibility(View.VISIBLE);
                    me_recycleview2.setVisibility(View.GONE);
                    me_line3.setVisibility(View.VISIBLE);
                    me_line2.setVisibility(View.GONE);
                    initData();
                }
                break;
            case R.id.bt_menu:
//                Log.d(TAG, "onClick: bt_menu");
//              SlidingMenu  slidinmenu=new SlidingMenu(getActivity());
//                slidinmenu.setMode(SlidingMenu.RIGHT);
//                slidinmenu.setTouchModeAbove( SlidingMenu.TOUCHMODE_FULLSCREEN );
//                slidinmenu.setBehindOffsetRes(R.dimen.slidingmenu_offset);
//                // 设置渐入渐出效果的值
//                slidinmenu.setFadeDegree(0.35f);
//                slidinmenu.setMenu(getSlidingMenu());
//                slidinmenu.attachToActivity (getActivity() , SlidingMenu.SLIDING_WINDOW );
                slidinmenu.showContent();
                slidinmenu.toggle();
                break;
            case R.id.ll1://关于我们
                Log.d(TAG, "onClick: ll1");
                startActivity(new Intent(getActivity(), AboutUsActivity.class));
                break;
            case R.id.ll2://用户协议
                Log.d(TAG, "onClick: ll2");
                startActivity(new Intent(getActivity(), UserAgreementActivity.class));
                break;
            case R.id.ll3://账号管理
                Log.d(TAG, "onClick: ll3");
                if (MyApplication.getInstance().getUserInfo() == null) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent1 = new Intent(getActivity(), ResetPasswordActivity.class);
                startActivity(intent1);
                break;
            case R.id.ll4://通知管理
                if (MyApplication.getInstance().getUserInfo() == null) {
                    Toast.makeText(getActivity(), "请先登录", Toast.LENGTH_SHORT).show();
                    return;
                }
                Intent intent2 = new Intent(getActivity(), SelectOnOrOffMessageActivity.class);
                startActivity(intent2);
                Log.d(TAG, "onClick: ll4");
                break;
            case R.id.ll5://检测更新
                Log.d(TAG, "onClick: ll5");
                Intent intent = new Intent(getActivity(), UpdateAppActivity.class);
                startActivity(intent);
                break;
            case R.id.ll6://退出登录
                Log.d(TAG, "onClick: ll6");
                if (TextUtils.isEmpty(MyApplication.getInstance().getUseruid())) {
                    return;
                }

                HashMap<String, String> maps = new HashMap<>();
                maps.put("user_id", MyApplication.getInstance().getUseruid());
                oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.LoginOrRegister.REQUEST_HEADER_LOGOUT
                        , ((MyApplication) getActivity().getApplicationContext()).getMaps(), maps, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String logout = response.body().string();
                                Log.d(TAG, "onResponse: logout\n" + logout);
                                Gson gson = new Gson();
                                LogoutInfo logoutInfo = gson.fromJson(logout, LogoutInfo.class);
                                Message message = new Message();
                                message.what = 3;
                                message.obj = logoutInfo;
                                mHandler.sendMessage(message);

                            }
                        });
                break;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    public void getCaoGaoImg(String path) {
        File file = new File(path);
        if (!file.exists()) {
            Toast.makeText(getActivity(), "没有相关文件", Toast.LENGTH_SHORT).show();
            return;
        }
        if (!bitmaps.isEmpty()) {
            bitmaps.clear();
        }
        File[] files = file.listFiles();
        for (int i = 0; i < files.length; i++) {
            publishVideoInfo = new PublishVideoInfo();
            MediaMetadataRetriever media = new MediaMetadataRetriever();
            media.setDataSource(files[i].getPath());// videoPath 本地视频的路径
            Bitmap bitmap = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
            // mIvBigShow.setImageBitmap(bitmap);
            publishVideoInfo.setLocalurl(files[i].getAbsolutePath());
            publishVideoInfo.setBitmap(bitmap);
            bitmaps.add(publishVideoInfo);
        }
        if (bitmaps.isEmpty()) {
            Toast.makeText(getActivity(), "没有相关文件", Toast.LENGTH_SHORT).show();
            return;
        }
        CaogaoImgAdapter caogaoImgAdapter = new CaogaoImgAdapter(bitmaps, getActivity());
        me_recycleview2.setAdapter(caogaoImgAdapter);
    }

}
