package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.am.shortVideo.activity.LookFullScreenVideo;
import com.am.shortVideo.activity.OtherUserInfoActivity;
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
import java.util.HashMap;
import java.util.List;

import application.MyApplication;
import base.MyAllBaseAdapter;
import base.MyBaseViewHolder;
import bean.AttentionPersonVideo;
import bean.ShareVideo;
import bean.UserInfo;
import bean.VideoSupportOrUn;
import customeview.ShowVideoPopUpWindow;
import de.hdodenhof.circleimageview.CircleImageView;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.BaseUtils;
import util.FootViewHolder;
import util.FormatTime;
import util.GlideUtils;
import util.HttpUri;

import static android.content.Context.INPUT_METHOD_SERVICE;

/**
 * Created by JC on 2019/9/4.
 */

public class AttentionPersonVideoAdapter extends MyAllBaseAdapter<AttentionPersonVideo.DataBean.IndexListBean
        , MyBaseViewHolder> {
    private static final String TAG = "AttentionPersonVideoAda";
    private final OktHttpUtil okHttpUtil;
    private final UMImage umImage;
    private Context context;
    private List<AttentionPersonVideo.DataBean.IndexListBean> datas;
    private UMWeb web;

    public AttentionPersonVideoAdapter(List<AttentionPersonVideo.DataBean.IndexListBean> dates, Context context) {
        super(dates, context);
        this.context = context;
        this.datas = dates;
        okHttpUtil = OktHttpUtil.getInstance();
        umImage = new UMImage(context, R.mipmap.app_icon);
    }

    @Override
    public MyBaseViewHolder onAdapterCreaetViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == NORMAL_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.attentionpersonvideo_item, viewGroup, false);
            return new AttentionPersonVideoViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.foot_item, viewGroup, false);
            return new FootViewHolder(view);
        }

    }

    @Override
    public void getBindViewHolder(MyBaseViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == NORMAL_VIEW) {
            final AttentionPersonVideoViewHolder holder = (AttentionPersonVideoViewHolder) viewHolder;
            Glide.with(context).load(HttpUri.BASE_DOMAIN + datas.get(position).getAvatar()).into(holder.attentionpersonvideo_picture);
            holder.attentionpersonvideo_nickname.setText(datas.get(position).getNickName());
            holder.attentionpersonvideo_commenttime.setText(FormatTime.formatTime(datas.get(position).getCreateTime()));
            holder.attentionpersonvideo_commentcontent.setText(datas.get(position).getVideoDesc());
            holder.attentionpersonvideo_sharecount.setText(datas.get(position).getShareCounts() + "");
            holder.attentionpersonvideo_likecount.setText(datas.get(position).getLikeCounts() + "");
            holder.attentionpersonvideo_commentcount.setText(datas.get(position).getCommentCounts() + "");
            holder.attentionpersonvideo_Allcommentcount.setText(datas.get(position).getCommentCounts() + "人评论过");
            GlideUtils.displayRoundImage(context,HttpUri.BASE_DOMAIN + datas.get(position).getCoverPath(),holder.attentionpersonvideo_videoimg,4);
            if (datas.get(position).getComment() != null) {
                holder.attentionpersonvideo_usercommentnickname.setVisibility(View.VISIBLE);
                holder.attentionpersonvideo_usercommentcontent.setVisibility(View.VISIBLE);
                holder.attentionpersonvideo_usercommentnickname.setText(datas.get(position).getComment().getCommentNickName() + ":");
                holder.attentionpersonvideo_usercommentcontent.setText(datas.get(position).getComment().getContent());
            } else {
                holder.attentionpersonvideo_usercommentnickname.setVisibility(View.GONE);
                holder.attentionpersonvideo_usercommentcontent.setVisibility(View.GONE);
            }
            holder.iv_attentionperson_likeimg.setImageDrawable(datas.get(position).isLikeStatus() ? context.getResources().getDrawable(R.mipmap.guanzhu) : context.getResources().getDrawable(R.mipmap.guanzhu_1));
            holder.attentionpersonvideo_videoimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, LookFullScreenVideo.class);
                    intent.putExtra("videopalyer_url", HttpUri.BASE_DOMAIN + datas.get(position).getVideoUrl());
                    context.startActivity(intent);
                }
            });
            holder.et_attentionperson_sendcontent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ShowVideoPopUpWindow(context, datas, position);
                    //new CommentPopupWindow(context,datas.get(position).)
                }
            });
            holder.attentionpersonvideo_picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_PERSONINFO
                            , ((MyApplication) context.getApplicationContext()).getMaps(), new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String userinfoResult = response.body().string();
                                    Log.d(TAG, "onResponse: userinfoCallback\n" + userinfoResult);
                                    Gson gson = new Gson();
                                    final UserInfo userinfo = gson.fromJson(userinfoResult, UserInfo.class);
                                    if (userinfo.getCode() == 0) {
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                if (!userinfo.getData().getUserInfo().getUid().equals(datas.get(position))) {
                                                    OtherUserInfoActivity.start(context,datas.get(position).getUid(),true);
                                                } else {
                                                    Toast.makeText(context, "请在个人中心查看信息", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else if (userinfo.getCode() == 1005) {
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                BaseUtils.getLoginDialog(context).show();
                                            }
                                        });
                                    }
                                }
                            });
                }
            });
            holder.iv_attentionperson_shareimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    web = new UMWeb(HttpUri.BASE_DOMAIN + datas.get(position).getVideoUrl());
                    web.setTitle(datas.get(position).getNickName());
//                    web.setThumb(umImage);
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
                                                        okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
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
                                                                            holder.attentionpersonvideo_sharecount.setText("" + shareVideo.getData().getShareCounts());
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
                                                        okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
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
                                                                            holder.attentionpersonvideo_sharecount.setText("" + shareVideo.getData().getShareCounts());
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
                                                        okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
                                                                , ((MyApplication) context.getApplicationContext()).getMaps(), maps, new Callback() {
                                                                    @Override
                                                                    public void onFailure(Call call, IOException e) {

                                                                    }

                                                                    @Override
                                                                    public void onResponse(Call call, Response response) throws IOException {
                                                                        HashMap<String, String> maps = new HashMap<>();
                                                                        maps.put("vid", datas.get(position).getVid());
                                                                        okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
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
                                                                                            holder.attentionpersonvideo_sharecount.setText("" + shareVideo.getData().getShareCounts());
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
                                                        okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_SHAREVIDEO
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
                                                                            holder.attentionpersonvideo_sharecount.setText("" + shareVideo.getData().getShareCounts());
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
            holder.iv_attentionperson_commentimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    new ShowVideoPopUpWindow(context, datas, position);
                }
            });
            holder.iv_attentionperson_likeimg.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d(TAG, "bt_support-->onClick: ");
                    HashMap<String, String> maps = new HashMap<>();
                    maps.put("vid", datas.get(position).getVid());
                    okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_VIDEOLIKE
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
                                                    holder.iv_attentionperson_likeimg.setImageResource(R.mipmap.guanzhu);
                                                } else {
                                                    Log.d(TAG, "handleMessage: 2-->false" + position);
                                                    holder.iv_attentionperson_likeimg.setImageResource(R.mipmap.guanzhu_1);
                                                }
                                                datas.get(position).setLikeStatus(videoSupport.getData().isLikeStatus());
                                                datas.get(position).setLikeCounts(videoSupport.getData().getLikeCounts());
                                                holder.attentionpersonvideo_likecount.setText("" + videoSupport.getData().getLikeCounts());
                                            }
                                        });
                                    } else if (videoSupport.getCode() == 1005) {
                                        Log.d(TAG, "onResponse: 1005");
                                        ((Activity) context).runOnUiThread(new Runnable() {
                                            @Override
                                            public void run() {
                                                BaseUtils.getLoginDialog(context).show();
                                            }
                                        });
                                    }
                                }
                            });

                }
            });
        } else if (getItemViewType(position) == FOOT_VIEW) {
            FootViewHolder holder1 = (FootViewHolder) viewHolder;
            holder1.text_foot.setVisibility(View.GONE);
        }
    }

    public class AttentionPersonVideoViewHolder extends MyBaseViewHolder {
        private CircleImageView attentionpersonvideo_picture;
        private TextView attentionpersonvideo_nickname;
        private TextView attentionpersonvideo_commenttime;
        private ImageView attentionpersonvideo_videoimg;
        private TextView attentionpersonvideo_commentcontent;
        private TextView attentionpersonvideo_sharecount;
        private TextView attentionpersonvideo_commentcount;
        private TextView attentionpersonvideo_likecount;
        private TextView attentionpersonvideo_Allcommentcount;
        private TextView attentionpersonvideo_usercommentcontent;
        private TextView attentionpersonvideo_usercommentnickname;
        private TextView et_attentionperson_sendcontent;
        private ImageView iv_attentionperson_likeimg;
        private ImageView iv_attentionperson_shareimg;
        private ImageView iv_attentionperson_commentimg;

        public AttentionPersonVideoViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            attentionpersonvideo_picture = (CircleImageView) itemView.findViewById(R.id.attention_personvideo_picture);
            attentionpersonvideo_nickname = (TextView) itemView.findViewById(R.id.tv_attentionvideo_nickname);
            attentionpersonvideo_commenttime = (TextView) itemView.findViewById(R.id.tv_attentionvideo_time);
            attentionpersonvideo_commentcontent = (TextView) itemView.findViewById(R.id.tv_attentionvideo_content);
            attentionpersonvideo_videoimg = (ImageView) itemView.findViewById(R.id.iv_attentionpersonvideo_videoimg);
            attentionpersonvideo_sharecount = (TextView) itemView.findViewById(R.id.tv_attentionperson_sharecount);
            attentionpersonvideo_commentcount = (TextView) itemView.findViewById(R.id.tv_attentionperson_count);
            attentionpersonvideo_likecount = (TextView) itemView.findViewById(R.id.tv_attentionperson_likecount);
            attentionpersonvideo_Allcommentcount = (TextView) itemView.findViewById(R.id.tv_attentionperson_commentcount);
            attentionpersonvideo_usercommentcontent = (TextView) itemView.findViewById(R.id.tv_usercomment_comentcontent);
            attentionpersonvideo_usercommentnickname = (TextView) itemView.findViewById(R.id.tv_usercomment_nickname);
            et_attentionperson_sendcontent = (TextView) itemView.findViewById(R.id.et_attentionperson_sendcontent);
            iv_attentionperson_shareimg = (ImageView) itemView.findViewById(R.id.iv_attentionperson_share);
            iv_attentionperson_commentimg = (ImageView) itemView.findViewById(R.id.iv_attentionperson_comment);
            iv_attentionperson_likeimg = (ImageView) itemView.findViewById(R.id.iv_attentionperson_like);

        }
    }

    public void clearAllData() {
        if (!datas.isEmpty()) {
            datas.clear();
            notifyDataSetChanged();
        }

    }

    //关闭软键盘
    public void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
