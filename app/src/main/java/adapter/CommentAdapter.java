package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.am.shortVideo.activity.OtherUserInfoActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import application.MyApplication;
import base.MyAllBaseAdapter;
import base.MyBaseViewHolder;
import bean.CommentSupport;
import bean.CommentSupportOrUn;
import bean.UserInfo;
import bean.VideoComment;
import customeview.LoginPopupwindow;
import de.hdodenhof.circleimageview.CircleImageView;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.FootViewHolder;
import util.FormatTime;
import util.HttpUri;

/**
 * Created by JC on 2019/8/20.
 */

public class CommentAdapter extends MyAllBaseAdapter<VideoComment.DataBean.CommentListBean, MyBaseViewHolder> {
    private final OktHttpUtil okHttpUtil;
    private List<VideoComment.DataBean.CommentListBean> datas;
    private Context context;
    private static final String TAG = "CommentAdapter";
    private boolean isAuthority;
    private AuthorityReplayCallBack callBack;

    public CommentAdapter(List<VideoComment.DataBean.CommentListBean> dates, Context context, boolean isAuthority) {
        super(dates, context);
        this.datas = dates;
        this.context = context;
        okHttpUtil = OktHttpUtil.getInstance();
        this.isAuthority = isAuthority;
    }

    @Override
    public MyBaseViewHolder onAdapterCreaetViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == 0) {
            View view = LayoutInflater.from(context).inflate(R.layout.comment_item, viewGroup, false);
            return new CommentViewHolder(view);
        } else if (viewType == 1) {
            View view = LayoutInflater.from(context).inflate(R.layout.foot_item, null);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void getBindViewHolder(MyBaseViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == 0) {
            final CommentViewHolder commentViewHolder = (CommentViewHolder) viewHolder;
            Glide.with(context).load(HttpUri.BASE_DOMAIN + datas.get(position).getAvatar()).into(((CommentViewHolder) viewHolder).userImg);
            commentViewHolder.tv_commentTime.setText(FormatTime.formatTime(datas.get(position).getCommentTime()));
            commentViewHolder.tv_userName.setText(datas.get(position).getNickName());
            if (datas.get(position).getAtType() == 1) {//有@用户
                commentViewHolder.tv_atnickname.setVisibility(View.VISIBLE);
                commentViewHolder.tv_atnickname.setText("@" + datas.get(position).getAtContent().getAtNickName());
            } else if (datas.get(position).getAtType() == 1) {//没有@用户
                commentViewHolder.tv_atnickname.setVisibility(View.GONE);

            }
            commentViewHolder.tv_commentContent.setText(datas.get(position).getContent());
            if (datas.get(position).isLikeStatus()) {
                commentViewHolder.iv_usersupport.setImageResource(R.mipmap.guanzhu);
            } else if (!datas.get(position).isLikeStatus()) {
                commentViewHolder.iv_usersupport.setImageResource(R.mipmap.guanzhu_1);
            }
            if (datas.get(position).isReplyState()) {
                commentViewHolder.rl_authoritycomment.setVisibility(View.VISIBLE);
                Glide.with(context).load(HttpUri.BASE_DOMAIN + datas.get(position).getAuthorReply().getAuthorAvatar()).into(commentViewHolder.authorityImg);
                commentViewHolder.tv_authority_commentTime.setText(FormatTime.formatTime(datas.get(position).getAuthorReply().getReplyTime()));
                commentViewHolder.tv_authorityName.setText(datas.get(position).getAuthorReply().getAuthorNickName());
                commentViewHolder.tv_authority_replay.setText("回复" + datas.get(position).getNickName());
                commentViewHolder.tv_authority_commentContent.setText(datas.get(position).getAuthorReply().getReplyContent());
            } else {
                commentViewHolder.rl_authoritycomment.setVisibility(View.GONE);
            }

            commentViewHolder.userImg.setOnClickListener(new View.OnClickListener() {
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
                                                    Intent intent = new Intent(context, OtherUserInfoActivity.class);
                                                    intent.putExtra("otheruserinfo", datas.get(position).getCommentUid());
                                                    context.startActivity(intent);
                                                } else {
                                                    Toast.makeText(context, "请在个人中心查看信息", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        });
                                    } else if (userinfo.getCode() == 1005) {
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
            commentViewHolder.iv_usersupport.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String, String> maps = new HashMap<>();
                    maps.put("cid", datas.get(position).getId() + "");
                    okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.VIDEO.REQUEST_HEADER_COMMENTLIKE
                            , ((MyApplication) context.getApplicationContext()).getMaps(), maps, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String commentSupportResult = response.body().string();
                                    Log.d(TAG, "onResponse: \n" + commentSupportResult);
                                    Gson gson = new Gson();
                                    CommentSupport commentSupport = gson.fromJson(commentSupportResult, CommentSupport.class);
                                    if (commentSupport.getCode() == 0) {
                                        if (commentSupport.getData().isLikeStatus()) {
                                            commentViewHolder.iv_usersupport.setImageResource(R.mipmap.guanzhu);
                                        } else {
                                            commentViewHolder.iv_usersupport.setImageResource(R.mipmap.guanzhu_1);
                                        }
                                    } else if (commentSupport.getCode() == 1005) {
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
            if (isAuthority) {
                commentViewHolder.rl_comment_item.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callBack.authorityReplayData(position);
                    }
                });
            }
        } else if (getItemViewType(position) == 1) {

        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getItemCount() == position + 1) {
            return 1;
        }
        return 0;
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }

    public class CommentViewHolder extends MyBaseViewHolder {
        private RelativeLayout rl_authoritycomment;
        private CircleImageView userImg;
        private TextView tv_userName;
        private TextView tv_commentTime;
        private TextView tv_commentContent;
        private CircleImageView authorityImg;
        private TextView tv_authorityName;
        private TextView tv_authority_commentTime;
        private TextView tv_authority_commentContent;
        private TextView tv_authority_replay;
        private ImageView iv_usersupport;
        private TextView tv_atnickname;
        private RelativeLayout rl_comment_item;

        public CommentViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            rl_comment_item = (RelativeLayout) view.findViewById(R.id.rl_comment_item);
            userImg = (CircleImageView) view.findViewById(R.id.iv_comment_picture);
            rl_authoritycomment = (RelativeLayout) view.findViewById(R.id.rl_authoritycomment);
            tv_userName = (TextView) view.findViewById(R.id.tv_commentName);
            tv_commentTime = (TextView) view.findViewById(R.id.tv_commenttime);
            tv_commentContent = (TextView) view.findViewById(R.id.tv_commentContent);
            authorityImg = (CircleImageView) view.findViewById(R.id.iv_comment_picture);
            tv_authorityName = (TextView) view.findViewById(R.id.tv_secondcommentName);
            tv_authority_commentTime = (TextView) view.findViewById(R.id.tv_secondcommenttime);
            tv_authority_commentContent = (TextView) view.findViewById(R.id.tv_secondcommentContent);
            tv_authority_replay = (TextView) view.findViewById(R.id.tv_replayname);
            tv_atnickname = (TextView) view.findViewById(R.id.tv_atnickname);
            iv_usersupport = (ImageView) view.findViewById(R.id.iv_usersuppport);
        }
    }

    public void setOnAuthorityReplayLinstener(AuthorityReplayCallBack callBack) {
        this.callBack = callBack;
    }

    public interface AuthorityReplayCallBack {
        void authorityReplayData(int position);
    }
}
