package adapter;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import com.am.shortVideo.EventBean.AttentionEvent;
import com.am.shortVideo.R;
import com.am.shortVideo.activity.LoginActivity;
import com.am.shortVideo.activity.OtherUserInfoActivity;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.google.gson.Gson;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import com.umeng.socialize.shareboard.SnsPlatform;
import com.umeng.socialize.utils.ShareBoardlistener;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import application.MyApplication;
import bean.AttentionOrCancelPerson;
import bean.HomeAttentionEvent;
import bean.HomeVideoImg;
import bean.ShareVideo;
import bean.UserInfo;
import bean.VideoSupportOrUn;
import customeview.CommentDialog;
import customeview.ShortVideoPlayer;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.BaseUtils;
import util.HttpUri;
import util.SystemUtils;

/**
 * Created by 李杰 on 2019/8/5.
 */

public class ShortVideoAdapter extends BaseQuickAdapter<HomeVideoImg.DataBean.IndexListBean, BaseViewHolder> {
    private OktHttpUtil oktHttpUtil;

    public ShortVideoAdapter(@Nullable List<HomeVideoImg.DataBean.IndexListBean> data) {
        super(R.layout.shortvideo_item, data);
        oktHttpUtil = MyApplication.getOkHttpUtil();
    }

    @Override
    protected void convert(final BaseViewHolder helper, final HomeVideoImg.DataBean.IndexListBean item) {
        helper.setText(R.id.tv_authoritycity, item.getVideoPath())
                .setText(R.id.tv_likecount, item.getLikeCounts() + "")
                .setText(R.id.tv_commentcount, item.getCommentCounts() + "")
                .setText(R.id.tv_sharecount, item.getShareCounts() + "")
                .setText(R.id.tv_foodname, item.getVideoDesc())
                .setVisible(R.id.rl_bugfood, !TextUtils.isEmpty(item.getGoodsName()))
                .setText(R.id.tv_recommendfood, item.getGoodsName());
//        Glide.with(mContext).load(HttpUri.BASE_DOMAIN + item.getVideoUrl()).into(shortViewHolder.videoPlay.thumbImageView);
        Glide.with(mContext).load(HttpUri.BASE_DOMAIN + item.getAvatar()).into((ImageView) helper.getView(R.id.user_circleImage));

        if (item.isFollowStatus() || TextUtils.equals(item.getUid(), ((MyApplication) mContext.getApplicationContext()).getUseruid())) {
            helper.setVisible(R.id.user_attention, true)
                    .setBackgroundRes(R.id.user_attention, R.mipmap.list_follow);
        } else {
            helper.setVisible(R.id.user_attention, true)
                    .setBackgroundRes(R.id.user_attention, R.mipmap.add);
        }
        if (item.isLikeStatus()) {
            helper.setImageResource(R.id.iv_support, R.mipmap.list_xihuan);
        } else {
            helper.setImageResource(R.id.iv_support, R.mipmap.dianzan);
        }

        //播放器
        ShortVideoPlayer videoPlayer = helper.getView(R.id.video_player);

        videoPlayer.setPlayListener(new ShortVideoPlayer.PlayListener() {
            @Override
            public void onPlay() {
            }

            @Override
            public void onPause() {
            }

            @Override
            public void onBuffering() {

            }
        });

        GSYVideoOptionBuilder videoBuilder = new GSYVideoOptionBuilder();
        videoBuilder
                .setRotateViewAuto(false)
                .setIsTouchWiget(false)
                .setLooping(true)
                .setAutoFullWithSize(false)
                .setIsTouchWiget(false)
                .setIsTouchWigetFull(false)
                .setShowFullAnimation(false)
                .setUrl(HttpUri.BASE_DOMAIN + item.getVideoUrl())
                .setCacheWithPlay(true)
                .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                    @Override
                    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {

                    }
                })
                .setVideoAllCallBack(new GSYSampleCallBack() {

                    @Override
                    public void onStartPrepared(String url, Object... objects) {
                        super.onStartPrepared(url, objects);
                    }

                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                    }

                    @Override
                    public void onClickStop(String url, Object... objects) {

                    }

                    @Override
                    public void onClickResume(String url, Object... objects) {

                    }

                    @Override
                    public void onPlayError(String url, Object... objects) {
                        super.onPlayError(url, objects);
                    }
                })
                .build(videoPlayer);

        //用户点赞
        helper.getView(R.id.iv_support).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                HashMap<String, String> maps = new HashMap<>();
                maps.put("vid", item.getVid());
                oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_VIDEOLIKE
                        , ((MyApplication) mContext.getApplicationContext()).getMaps(), maps, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String body = response.body().string();
                                Gson gson = new Gson();
                                final VideoSupportOrUn videoSupport = gson.fromJson(body, VideoSupportOrUn.class);
                                if (videoSupport.getCode() == 0) {
                                    ((Activity) mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (videoSupport.getData().isLikeStatus()) {
                                                helper.setImageResource(R.id.iv_support, R.mipmap.list_xihuan);
                                            } else {
                                                helper.setImageResource(R.id.iv_support, R.mipmap.dianzan);
                                            }
                                            item.setLikeStatus(videoSupport.getData().isLikeStatus());
                                            item.setLikeCounts(videoSupport.getData().getLikeCounts());
                                            helper.setText(R.id.tv_likecount, "" + videoSupport.getData().getLikeCounts());
                                        }
                                    });
                                } else if (videoSupport.getCode() == 1005) {
                                    ((Activity) mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            BaseUtils.getLoginDialog(mContext).show();
                                        }
                                    });
                                }
                            }
                        });

            }
        });

        //用户评论
        helper.getView(R.id.iv_usercomment).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (MyApplication.getInstance().getUserInfo() != null) {
                    if (mContext instanceof AppCompatActivity) {
                        ((AppCompatActivity) mContext).getSupportFragmentManager().beginTransaction().add(CommentDialog.newInstance(getData(), helper.getAdapterPosition()), "CommentDialog").commitAllowingStateLoss();
                    }
                } else {
                    Toast.makeText(mContext, "请先登录", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(mContext, LoginActivity.class);
                    mContext.startActivity(intent);
                }

            }
        });

        //用户分享
        helper.getView(R.id.iv_usershare).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final UMWeb web = new UMWeb(HttpUri.BASE_DOMAIN + item.getVideoUrl());
                web.setTitle(item.getNickName());
//                web.setThumb(umImage);
                web.setDescription(item.getVideoDesc());
                new ShareAction((Activity) mContext).setDisplayList(SHARE_MEDIA.QQ,
                        SHARE_MEDIA.QZONE, SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE)
                        .setShareboardclickCallback(new ShareBoardlistener() {
                            @Override
                            public void onclick(SnsPlatform snsPlatform, SHARE_MEDIA share_media) {
                                if (share_media == SHARE_MEDIA.QQ) {
                                    new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.QQ)
                                            .withMedia(web)
                                            .setCallback(new UMShareListener() {
                                                @Override
                                                public void onStart(SHARE_MEDIA share_media) {
                                                }

                                                @Override
                                                public void onResult(SHARE_MEDIA share_media) {
                                                    HashMap<String, String> maps = new HashMap<>();
                                                    maps.put("vid", item.getVid());
                                                    oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
                                                            , ((MyApplication) mContext.getApplicationContext()).getMaps(), maps, new Callback() {
                                                                @Override
                                                                public void onFailure(Call call, IOException e) {

                                                                }

                                                                @Override
                                                                public void onResponse(Call call, Response response) throws IOException {
                                                                    String shareResult = response.body().string();
                                                                    Gson gson = new Gson();
                                                                    ShareVideo shareVideo = gson.fromJson(shareResult, ShareVideo.class);
                                                                    if (shareVideo.getCode() == 0) {
                                                                        helper.setText(R.id.tv_sharecount, "" + shareVideo.getData().getShareCounts());
                                                                    }
                                                                }
                                                            });
                                                }

                                                @Override
                                                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                    Toast.makeText(mContext, share_media + " 分享失败啦", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancel(SHARE_MEDIA share_media) {
                                                    Toast.makeText(mContext, share_media + " 分享取消啦", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .share();
                                } else if (share_media == SHARE_MEDIA.QZONE) {
                                    new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.QZONE)
                                            .withMedia(web)
                                            .setCallback(new UMShareListener() {
                                                @Override
                                                public void onStart(SHARE_MEDIA share_media) {

                                                }

                                                @Override
                                                public void onResult(SHARE_MEDIA share_media) {
                                                    HashMap<String, String> maps = new HashMap<>();
                                                    maps.put("vid", item.getVid());
                                                    oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
                                                            , ((MyApplication) mContext.getApplicationContext()).getMaps(), maps, new Callback() {
                                                                @Override
                                                                public void onFailure(Call call, IOException e) {

                                                                }

                                                                @Override
                                                                public void onResponse(Call call, Response response) throws IOException {
                                                                    String shareResult = response.body().string();
                                                                    Gson gson = new Gson();
                                                                    ShareVideo shareVideo = gson.fromJson(shareResult, ShareVideo.class);
                                                                    if (shareVideo.getCode() == 0) {
                                                                        helper.setText(R.id.tv_sharecount, "" + shareVideo.getData().getShareCounts());
                                                                    }
                                                                }
                                                            });
                                                }

                                                @Override
                                                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                    Toast.makeText(mContext, share_media + " 分享失败啦", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancel(SHARE_MEDIA share_media) {
                                                    Toast.makeText(mContext, share_media + " 分享取消啦", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .share();
                                } else if (share_media == SHARE_MEDIA.WEIXIN) {
                                    new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.WEIXIN)
                                            .withMedia(web)
                                            .setCallback(new UMShareListener() {
                                                @Override
                                                public void onStart(SHARE_MEDIA share_media) {

                                                }

                                                @Override
                                                public void onResult(SHARE_MEDIA share_media) {
                                                    HashMap<String, String> maps = new HashMap<>();
                                                    maps.put("vid", item.getVid());
                                                    oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
                                                            , ((MyApplication) mContext.getApplicationContext()).getMaps(), maps, new Callback() {
                                                                @Override
                                                                public void onFailure(Call call, IOException e) {

                                                                }

                                                                @Override
                                                                public void onResponse(Call call, Response response) throws IOException {
                                                                    HashMap<String, String> maps = new HashMap<>();
                                                                    maps.put("vid", item.getVid());
                                                                    oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
                                                                            , ((MyApplication) mContext.getApplicationContext()).getMaps(), maps, new Callback() {
                                                                                @Override
                                                                                public void onFailure(Call call, IOException e) {

                                                                                }

                                                                                @Override
                                                                                public void onResponse(Call call, Response response) throws IOException {
                                                                                    String shareResult = response.body().string();
                                                                                    Gson gson = new Gson();
                                                                                    ShareVideo shareVideo = gson.fromJson(shareResult, ShareVideo.class);
                                                                                    if (shareVideo.getCode() == 0) {
                                                                                        helper.setText(R.id.tv_sharecount, "" + shareVideo.getData().getShareCounts());
                                                                                    }
                                                                                }
                                                                            });
                                                                }
                                                            });
                                                }

                                                @Override
                                                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                    Toast.makeText(mContext, share_media + " 分享失败啦", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancel(SHARE_MEDIA share_media) {
                                                    Toast.makeText(mContext, share_media + " 分享取消啦", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .share();
                                } else if (share_media == SHARE_MEDIA.WEIXIN_CIRCLE) {
                                    new ShareAction((Activity) mContext).setPlatform(SHARE_MEDIA.WEIXIN_CIRCLE)
                                            .withMedia(web)
                                            .setCallback(new UMShareListener() {
                                                @Override
                                                public void onStart(SHARE_MEDIA share_media) {

                                                }

                                                @Override
                                                public void onResult(SHARE_MEDIA share_media) {
                                                    HashMap<String, String> maps = new HashMap<>();
                                                    maps.put("vid", item.getVid());
                                                    oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
                                                            , ((MyApplication) mContext.getApplicationContext()).getMaps(), maps, new Callback() {
                                                                @Override
                                                                public void onFailure(Call call, IOException e) {

                                                                }

                                                                @Override
                                                                public void onResponse(Call call, Response response) throws IOException {
                                                                    String shareResult = response.body().string();
                                                                    Gson gson = new Gson();
                                                                    ShareVideo shareVideo = gson.fromJson(shareResult, ShareVideo.class);
                                                                    if (shareVideo.getCode() == 0) {
                                                                        helper.setText(R.id.tv_sharecount, "" + shareVideo.getData().getShareCounts());
                                                                    }
                                                                }
                                                            });
                                                }

                                                @Override
                                                public void onError(SHARE_MEDIA share_media, Throwable throwable) {
                                                    Toast.makeText(mContext, share_media + " 分享失败啦", Toast.LENGTH_SHORT).show();
                                                }

                                                @Override
                                                public void onCancel(SHARE_MEDIA share_media) {
                                                    Toast.makeText(mContext, share_media + " 分享取消啦", Toast.LENGTH_SHORT).show();
                                                }
                                            })
                                            .share();
                                }
                            }
                        }).open();

            }
        });

        helper.getView(R.id.user_circleImage).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                oktHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_PERSONINFO
                        , ((MyApplication) mContext.getApplicationContext()).getMaps(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String userinfoResult = response.body().string();
                                Gson gson = new Gson();
                                final UserInfo userinfo = gson.fromJson(userinfoResult, UserInfo.class);
                                if (userinfo.getCode() == 0) {
                                    ((Activity) mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!userinfo.getData().getUserInfo().getUid().equals(item)) {
                                                OtherUserInfoActivity.start(mContext,item.getUid(),item.isFollowStatus());
                                            } else {
                                                Toast.makeText(mContext, "请在个人中心查看信息", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    });
                                } else if (userinfo.getCode() == 1005) {
                                    ((Activity) mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            BaseUtils.getLoginDialog(mContext).show();
                                        }
                                    });
                                }
                            }
                        });

            }
        });


        //用户关注
        helper.getView(R.id.user_attention).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                HashMap<String, String> attentionmaps = new HashMap<String, String>();
                attentionmaps.put("uid", item.getUid());
                oktHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_ATTENTIONUSERSTATUS
                        , ((MyApplication) mContext.getApplicationContext()).getMaps(), attentionmaps, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String attentionResult = response.body().string();
                                Gson gson = new Gson();
                                final AttentionOrCancelPerson cancelPerson = gson.fromJson(attentionResult, AttentionOrCancelPerson.class);
                                if (cancelPerson.getCode() == 0) {
                                    ((Activity) mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            getData().get(helper.getLayoutPosition()).setFollowStatus(cancelPerson.getData().isFollowStatus());
                                            if (cancelPerson.getData().isFollowStatus()) {
                                                helper.setVisible(R.id.user_attention, true)
                                                        .setBackgroundRes(R.id.user_attention, R.mipmap.list_follow);
                                            } else {
                                                helper.setVisible(R.id.user_attention, true)
                                                        .setBackgroundRes(R.id.user_attention, R.mipmap.add);
                                            }
                                            item.setFollowStatus(cancelPerson.getData().isFollowStatus());
                                            EventBus.getDefault().post(new AttentionEvent(item.getUid(), cancelPerson.getData().isFollowStatus()));
                                            EventBus.getDefault().post(new HomeAttentionEvent());
                                        }
                                    });
                                } else if (cancelPerson.getCode() == 1005) {
                                    BaseUtils.getLoginDialog(mContext).show();
                                }
                            }
                        });
            }
        });
        helper.getView(R.id.rl_bugfood).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                oktHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_PERSONINFO
                        , ((MyApplication) mContext.getApplicationContext()).getMaps(), new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String userinfoResult = response.body().string();
                                Gson gson = new Gson();
                                UserInfo userinfo = gson.fromJson(userinfoResult, UserInfo.class);
                                if (userinfo.getCode() == 0) {
                                    ((Activity) mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            if (!SystemUtils.isAvilible(mContext, "com.android.healthapp")) {
                                                Toast.makeText(mContext, "请先下载健德购购App", Toast.LENGTH_SHORT).show();
                                                return;
                                            }
                                            Intent intent = new Intent();
                                            ComponentName componentName = new ComponentName("com.android.healthapp",
                                                    "com.android.healthapp.ui.activity.GoodConventionActivity");
                                            intent.setComponent(componentName);
                                            intent.putExtra("good_common_id", Integer.parseInt(item.getGoodsId()));//id
//                    intent = context.getPackageManager().getLaunchIntentForPackage("com.android.healthapp");
//                    intent.putExtra("good_common_id", "123456");//id
                                            mContext.startActivity(intent);
                                        }
                                    });
                                } else if (userinfo.getCode() == 1005) {
                                    ((Activity) mContext).runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            BaseUtils.getLoginDialog(mContext).show();
                                        }
                                    });
                                }
                            }
                        });


            }
        });
    }
}
