package com.am.shortVideo.activity;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import adapter.ShorVideoAdapter;
import application.MyApplication;
import base.MyBaseAdapter;
import base.MyBaseViewHolder;
import bean.AttentionOrCancelPerson;
import bean.HomeVideoImg;
import bean.SerachPublishVideo;
import bean.ShareVideo;
import bean.VideoComment;
import bean.VideoSupportOrUn;
import customeview.CommentPopupWindow;
import customeview.LoginPopupwindow;
import customeview.ShowLoopVideo;
import customeview.ZuopinCommentPopUpwindow;
import de.hdodenhof.circleimageview.CircleImageView;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.FootViewHolder;
import util.HttpUri;
import util.SystemUtils;

/**
 * Created by 李杰 on 2019/9/17.
 */

public class ZuopinAdapter extends MyBaseAdapter<SerachPublishVideo.DataBean.IndexListBean, MyBaseViewHolder> {
    private static final String TAG = "ShorVideoAdapter";
    private final UMImage umImage;
    private Context context;
    private OktHttpUtil oktHttpUtil;
    List<SerachPublishVideo.DataBean.IndexListBean> datas;
    private List<VideoComment.DataBean.CommentListBean> commentDatas = new ArrayList<>();
    private UMWeb web;

    public ZuopinAdapter(List<SerachPublishVideo.DataBean.IndexListBean> datas, Context context) {
        super(datas, context);
        this.context = context;
        this.datas = datas;
        oktHttpUtil = ((MyApplication) context.getApplicationContext()).getOkHttpUtil();
        umImage = new UMImage(context, R.mipmap.app_icon);
    }

    @Override
    public MyBaseViewHolder onCreateHolder(ViewGroup partent, int viewType) {
        if (viewType == NORMAL_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.shortvideo_item, partent, false);
//        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        view.setLayoutParams(layoutParams);
            return new ZuopinAdapter.ShortViewHolder(view);
        } else if (viewType == FOOT_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.foot_item, partent, false);
//        RelativeLayout.LayoutParams layoutParams=new RelativeLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT);
//        view.setLayoutParams(layoutParams);
            return new FootViewHolder(view);
        }
        View view = LayoutInflater.from(context).inflate(R.layout.shortvideo_item, partent, false);
        return new ZuopinAdapter.ShortViewHolder(view);
    }


    @Override
    public void onHolder(MyBaseViewHolder holder, final int position) {
        Log.d(TAG, "onHolder: " + position + "value\n" + datas.toString());
        if (getItemViewType(position) == NORMAL_VIEW) {
            if (holder != null) {
                final ZuopinAdapter.ShortViewHolder shortViewHolder = (ZuopinAdapter.ShortViewHolder) holder;
                shortViewHolder.videoPlay.setUp(HttpUri.BASE_DOMAIN + datas.get(position).getVideoUrl(), JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN);
                if (position == 0) {
                    shortViewHolder.videoPlay.startVideo();
                }
                shortViewHolder.tv_authortycity.setText(datas.get(position).getVideoPath());
                shortViewHolder.tv_likecount.setText(datas.get(position).getLikeCounts() + "");
                shortViewHolder.tv_commentcount.setText(datas.get(position).getCommentCounts() + "");
                shortViewHolder.tv_sharecount.setText(datas.get(position).getShareCounts() + "");
                shortViewHolder.tv_foodname.setText(datas.get(position).getVideoDesc());
                shortViewHolder.tv_recommendfood.setText(datas.get(position).getGoodsName());
                Glide.with(context).load(HttpUri.BASE_DOMAIN + datas.get(position).getVideoUrl()).into(shortViewHolder.videoPlay.thumbImageView);
                Glide.with(context).load(HttpUri.BASE_DOMAIN + datas.get(position).getAvatar()).into(shortViewHolder.user_circleimage);
                if (datas.get(position).isFollowStatus()) {
                    Log.d(TAG, "onHolder:FollowStatus-->true");
                    shortViewHolder.user_attention.setVisibility(View.GONE);
                } else {
                    Log.d(TAG, "onHolder:FollowStatus-->false");
                    shortViewHolder.user_attention.setVisibility(View.VISIBLE);
                    shortViewHolder.user_attention.setBackgroundResource(R.mipmap.add);
                }
                if (datas.get(position).isLikeStatus()) {
                    Log.d(TAG, "onHolder:isLikeStatus-->true");
                    shortViewHolder.iv_support.setImageResource(R.mipmap.list_xihuan);
                } else {
                    Log.d(TAG, "onHolder:isLikeStatus-->false");
                    shortViewHolder.iv_support.setImageResource(R.mipmap.dianzan);
                }
                //用户点赞
                shortViewHolder.iv_support.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Log.d(TAG, "iv_support-->onClick: ");
                        HashMap<String, String> maps = new HashMap<>();
                        maps.put("vid", datas.get(position).getVid());
                        oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_VIDEOLIKE
                                , ((MyApplication) context.getApplicationContext()).getMaps(), maps, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.e(TAG, "likeCallBackonResponse: error");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String body = response.body().string();
                                        Log.d(TAG, "likeCallBackonResponse: \n" + body);
                                        Gson gson = new Gson();
                                        final VideoSupportOrUn videoSupport = gson.fromJson(body, VideoSupportOrUn.class);
                                        if (videoSupport.getCode() == 0) {
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (videoSupport.getData().isLikeStatus()) {
                                                        Log.d(TAG, "handleMessage: 2-->true" + position);
                                                        shortViewHolder.iv_support.setImageResource(R.mipmap.list_xihuan);
                                                    } else {
                                                        Log.d(TAG, "handleMessage: 2-->false" + position);
                                                        shortViewHolder.iv_support.setImageResource(R.mipmap.dianzan);
                                                    }
                                                    datas.get(position).setLikeStatus(videoSupport.getData().isLikeStatus());
                                                    datas.get(position).setLikeCounts(videoSupport.getData().getLikeCounts());
                                                    shortViewHolder.tv_likecount.setText("" + videoSupport.getData().getLikeCounts());
                                                }
                                            });
                                        } else if (videoSupport.getCode() == 1005) {
                                            Log.d(TAG, "onResponse: 1005");
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    new LoginPopupwindow(context);
                                                }
                                            });
                                        }
                                    }
                                });

                    }
                });

                //用户评论
                shortViewHolder.iv_usercomment.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        new ZuopinCommentPopUpwindow(context, datas, position);


                    }
                });

                //用户分享
                shortViewHolder.iv_usershare.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        web = new UMWeb(HttpUri.BASE_DOMAIN + datas.get(position).getVideoUrl());
                        web.setTitle(datas.get(position).getNickName());
                        web.setThumb(umImage);
                        web.setDescription(datas.get(position).getVideoDesc());
                        new ShareAction((Activity) context).setDisplayList(SHARE_MEDIA.QQ,
                                SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                                .setShareboardclickCallback(new ShareBoardlistener() {
                                    @Override
                                    public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                        if (share_media == SHARE_MEDIA.QQ) {
                                            new ShareAction((Activity) context).setPlatform(SHARE_MEDIA.QQ)
                                                    .withMedia(web)
                                                    .setCallback(new UMShareListener() {
                                                        @Override
                                                        public void onStart(SHARE_MEDIA share_media) {
                                                            Log.d(TAG, "qq->onStart: ");
                                                        }

                                                        @Override
                                                        public void onResult(SHARE_MEDIA share_media) {
                                                            Log.d(TAG, "qq->onResult: ");
                                                            HashMap<String, String> maps = new HashMap<>();
                                                            maps.put("vid", datas.get(position).getVid());
                                                            oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
                                                                    , ((MyApplication) context.getApplicationContext()).getMaps(), maps, new Callback() {
                                                                        @Override
                                                                        public void onFailure(Call call, IOException e) {

                                                                        }

                                                                        @Override
                                                                        public void onResponse(Call call, Response response) throws IOException {
                                                                            String shareResult = response.body().string();
                                                                            Log.d(TAG, "onResponse: \n" + shareResult);
                                                                            Gson gson = new Gson();
                                                                            ShareVideo shareVideo = gson.fromJson(shareResult, ShareVideo.class);
                                                                            if (shareVideo.getCode() == 0) {
                                                                                shortViewHolder.tv_sharecount.setText("" + shareVideo.getData().getShareCounts());
                                                                            }
                                                                        }
                                                                    });
                                                        }

                                                        @Override
                                                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                            Toast.makeText(context, share_media + " 分享失败啦", Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onCancel(SHARE_MEDIA share_media) {
                                                            Toast.makeText(context, share_media + " 分享取消啦", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .share();
                                        } else if (share_media == SHARE_MEDIA.QZONE) {
                                            new ShareAction((Activity) context).setPlatform(SHARE_MEDIA.QZONE)
                                                    .withMedia(web)
                                                    .setCallback(new UMShareListener() {
                                                        @Override
                                                        public void onStart(SHARE_MEDIA share_media) {

                                                        }

                                                        @Override
                                                        public void onResult(SHARE_MEDIA share_media) {
                                                            HashMap<String, String> maps = new HashMap<>();
                                                            maps.put("vid", datas.get(position).getVid());
                                                            oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
                                                                    , ((MyApplication) context.getApplicationContext()).getMaps(), maps, new Callback() {
                                                                        @Override
                                                                        public void onFailure(Call call, IOException e) {

                                                                        }

                                                                        @Override
                                                                        public void onResponse(Call call, Response response) throws IOException {
                                                                            String shareResult = response.body().string();
                                                                            Log.d(TAG, "onResponse: \n" + shareResult);
                                                                            Gson gson = new Gson();
                                                                            ShareVideo shareVideo = gson.fromJson(shareResult, ShareVideo.class);
                                                                            if (shareVideo.getCode() == 0) {
                                                                                shortViewHolder.tv_sharecount.setText("" + shareVideo.getData().getShareCounts());
                                                                            }
                                                                        }
                                                                    });
                                                        }

                                                        @Override
                                                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                            Toast.makeText(context, share_media + " 分享失败啦", Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onCancel(SHARE_MEDIA share_media) {
                                                            Toast.makeText(context, share_media + " 分享取消啦", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .share();
                                        } else if (share_media == SHARE_MEDIA.WEIXIN) {
                                            new ShareAction((Activity) context).setPlatform(SHARE_MEDIA.WEIXIN)
                                                    .withMedia(web)
                                                    .setCallback(new UMShareListener() {
                                                        @Override
                                                        public void onStart(SHARE_MEDIA share_media) {

                                                        }

                                                        @Override
                                                        public void onResult(SHARE_MEDIA share_media) {
                                                            HashMap<String, String> maps = new HashMap<>();
                                                            maps.put("vid", datas.get(position).getVid());
                                                            oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
                                                                    , ((MyApplication) context.getApplicationContext()).getMaps(), maps, new Callback() {
                                                                        @Override
                                                                        public void onFailure(Call call, IOException e) {

                                                                        }

                                                                        @Override
                                                                        public void onResponse(Call call, Response response) throws IOException {
                                                                            HashMap<String, String> maps = new HashMap<>();
                                                                            maps.put("vid", datas.get(position).getVid());
                                                                            oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
                                                                                    , ((MyApplication) context.getApplicationContext()).getMaps(), maps, new Callback() {
                                                                                        @Override
                                                                                        public void onFailure(Call call, IOException e) {

                                                                                        }

                                                                                        @Override
                                                                                        public void onResponse(Call call, Response response) throws IOException {
                                                                                            String shareResult = response.body().string();
                                                                                            Log.d(TAG, "onResponse: \n" + shareResult);
                                                                                            Gson gson = new Gson();
                                                                                            ShareVideo shareVideo = gson.fromJson(shareResult, ShareVideo.class);
                                                                                            if (shareVideo.getCode() == 0) {
                                                                                                shortViewHolder.tv_sharecount.setText("" + shareVideo.getData().getShareCounts());
                                                                                            }
                                                                                        }
                                                                                    });
                                                                        }
                                                                    });
                                                        }

                                                        @Override
                                                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                            Toast.makeText(context, share_media + " 分享失败啦", Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onCancel(SHARE_MEDIA share_media) {
                                                            Toast.makeText(context, share_media + " 分享取消啦", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .share();
                                        } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                                            new ShareAction((Activity) context).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                                    .withMedia(web)
                                                    .setCallback(new UMShareListener() {
                                                        @Override
                                                        public void onStart(SHARE_MEDIA share_media) {

                                                        }

                                                        @Override
                                                        public void onResult(SHARE_MEDIA share_media) {
                                                            HashMap<String, String> maps = new HashMap<>();
                                                            maps.put("vid", datas.get(position).getVid());
                                                            oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
                                                                    , ((MyApplication) context.getApplicationContext()).getMaps(), maps, new Callback() {
                                                                        @Override
                                                                        public void onFailure(Call call, IOException e) {

                                                                        }

                                                                        @Override
                                                                        public void onResponse(Call call, Response response) throws IOException {
                                                                            String shareResult = response.body().string();
                                                                            Log.d(TAG, "onResponse: \n" + shareResult);
                                                                            Gson gson = new Gson();
                                                                            ShareVideo shareVideo = gson.fromJson(shareResult, ShareVideo.class);
                                                                            if (shareVideo.getCode() == 0) {
                                                                                shortViewHolder.tv_sharecount.setText("" + shareVideo.getData().getShareCounts());
                                                                            }
                                                                        }
                                                                    });
                                                        }

                                                        @Override
                                                        public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                            Toast.makeText(context, share_media + " 分享失败啦", Toast.LENGTH_SHORT).show();
                                                        }

                                                        @Override
                                                        public void onCancel(SHARE_MEDIA share_media) {
                                                            Toast.makeText(context, share_media + " 分享取消啦", Toast.LENGTH_SHORT).show();
                                                        }
                                                    })
                                                    .share();
                                        }
                                    }
                                }).open();

                    }
                });

                //用户关注
                shortViewHolder.rl_attention.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {
                        HashMap<String, String> attentionmaps = new HashMap<String, String>();
                        attentionmaps.put("uid", datas.get(position).getUid());
                        oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_ATTENTIONUSERSTATUS
                                , ((MyApplication) context.getApplicationContext()).getMaps(), attentionmaps, new Callback() {
                                    @Override
                                    public void onFailure(Call call, IOException e) {
                                        Log.e(TAG, "attentionResponse: error");
                                    }

                                    @Override
                                    public void onResponse(Call call, Response response) throws IOException {
                                        String attentionResult = response.body().string();
                                        Log.d(TAG, "attentionResponse: \n" + attentionResult);
                                        Gson gson = new Gson();
                                        final AttentionOrCancelPerson cancelPerson = gson.fromJson(attentionResult, AttentionOrCancelPerson.class);
                                        if (cancelPerson.getCode() == 0) {
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    if (cancelPerson.getData().isFollowStatus()) {
                                                        Log.d(TAG, "handleMessage: 3-->true");
                                                        shortViewHolder.user_attention.setVisibility(View.GONE);
                                                        shortViewHolder.user_attention.setBackgroundResource(R.mipmap.list_xihuan);
                                                    } else {
                                                        Log.d(TAG, "handleMessage: 3-->false");
                                                        shortViewHolder.user_attention.setVisibility(View.VISIBLE);
                                                        shortViewHolder.user_attention.setBackgroundResource(R.mipmap.add);
                                                    }
                                                    datas.get(position).setFollowStatus(cancelPerson.getData().isFollowStatus());
                                                }
                                            });
                                        } else if (cancelPerson.getCode() == 1005) {
                                            new LoginPopupwindow(context);
                                        }
                                    }
                                });
                    }
                });
                shortViewHolder.rl_bugfood.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (!((MyApplication) context.getApplicationContext()).getMaps().isEmpty()) {
                            Log.d(TAG, "onClick: rl_bugfood" + ((MyApplication) context.getApplicationContext()).getMaps());
                            if (!SystemUtils.isAvilible(context, "com.android.healthapp")) {
                                Toast.makeText(context, "请先下载健德购购App", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            Intent intent = new Intent();
                            ComponentName componentName = new ComponentName("com.android.healthapp",
                                    "com.android.healthapp.ui.activity.GoodConventionActivity");
                            intent.setComponent(componentName);
                            intent.putExtra("good_common_id", datas.get(position).getGoodsId());//id
                            Log.d(TAG, "onClick: " + datas.get(position).getGoodsId());
//                    intent = context.getPackageManager().getLaunchIntentForPackage("com.android.healthapp");
//                    intent.putExtra("good_common_id", "123456");//id
                            context.startActivity(intent);
                        } else {
                            new LoginPopupwindow(context);
                        }
                    }
                });

            }
        } else if (getItemViewType(position) == FOOT_VIEW) {

        }
    }


    public class ShortViewHolder extends MyBaseViewHolder {


        public ShowLoopVideo videoPlay;
        private RelativeLayout rl_attention;
        private RelativeLayout rl_bugfood;
        private ImageView iv_support;
        private ImageView iv_usercomment;
        private ImageView iv_usershare;
        private TextView tv_foodname;
        private TextView tv_authortycity;
        private TextView tv_recommendfood;
        private TextView tv_likecount;
        private TextView tv_commentcount;
        private TextView tv_sharecount;
        private CircleImageView user_circleimage;
        private Button user_attention;

        public ShortViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            videoPlay = (ShowLoopVideo) view.findViewById(R.id.videoplayer);
            rl_attention = (RelativeLayout) view.findViewById(R.id.rl_attention);
            rl_bugfood = (RelativeLayout) view.findViewById(R.id.rl_bugfood);
            user_circleimage = (CircleImageView) view.findViewById(R.id.user_circleImage);
            user_attention = (Button) view.findViewById(R.id.user_attention);
            iv_support = (ImageView) view.findViewById(R.id.iv_support);
            iv_usercomment = (ImageView) view.findViewById(R.id.iv_usercomment);
            iv_usershare = (ImageView) view.findViewById(R.id.iv_usershare);
            tv_foodname = (TextView) view.findViewById(R.id.tv_foodname);
            tv_authortycity = (TextView) view.findViewById(R.id.tv_authoritycity);
            tv_recommendfood = (TextView) view.findViewById(R.id.tv_recommendfood);
            tv_likecount = (TextView) view.findViewById(R.id.tv_likecount);
            tv_commentcount = (TextView) view.findViewById(R.id.tv_commentcount);
            tv_sharecount = (TextView) view.findViewById(R.id.tv_sharecount);

        }
    }
}
