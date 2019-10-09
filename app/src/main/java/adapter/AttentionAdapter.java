package adapter;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.EventBean.AttentionEvent;
import com.am.shortVideo.R;
import com.am.shortVideo.activity.OtherUserInfoActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import application.MyApplication;
import base.MyAllBaseAdapter;
import base.MyBaseViewHolder;
import bean.AttentionOrCancelPerson;
import bean.AttentionPerson;
import bean.UserInfo;
import de.hdodenhof.circleimageview.CircleImageView;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.BaseUtils;
import util.HttpUri;
import util.PinYinUtil;

/**
 * Created by 李杰 on 2019/9/3.
 */

public class AttentionAdapter extends MyAllBaseAdapter<AttentionPerson.DataBean.PageListBean, MyBaseViewHolder> {
    private static final String TAG = "AttentionAdapter";
    private final OktHttpUtil okHttpUtil;
    private Context context;
    private List<AttentionPerson.DataBean.PageListBean> datas;
    private PinYinUtil pinYinUtil;

    public AttentionAdapter(List<AttentionPerson.DataBean.PageListBean> dates, Context context
            , PinYinUtil pinYinUtil) {
        super(dates, context);
        this.context = context;
        this.datas = dates;
        okHttpUtil = OktHttpUtil.getInstance();
        this.pinYinUtil = pinYinUtil;
    }

    @Override
    public MyBaseViewHolder onAdapterCreaetViewHolder(ViewGroup viewGroup, int viewType) {
        View view = null;
        if (viewType == NORMAL_VIEW) {
            view = LayoutInflater.from(context).inflate(R.layout.attentionperson_item, null);
            return new MyAttentionPersonViewHolder(view);
        } else {
            view = LayoutInflater.from(context).inflate(R.layout.foot_item, viewGroup, false);
            return new FootViewhHolder(view);
        }

    }


    @Override
    public void getBindViewHolder(final MyBaseViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == NORMAL_VIEW) {
            final MyAttentionPersonViewHolder holder = (MyAttentionPersonViewHolder) viewHolder;
            char letter = getfontLetter(position);
            if (isFirstShowLetter(letter) == position) {
                holder.tv_tag.setVisibility(View.VISIBLE);
                holder.tv_tag.setText("" + letter);
            } else {
                holder.tv_tag.setVisibility(View.GONE);
            }
            Glide.with(context).load(HttpUri.BASE_DOMAIN + datas.get(position).getAvatar()).into(holder.attentionperson_picture);
            holder.attentionperson_account.setText(datas.get(position).getUid());
            holder.attentionperson_name.setText(datas.get(position).getNickName());
            if (datas.get(position).isFollowStatus()) {
                holder.bt_attentionperson.setText("互相关注");
                holder.bt_attentionperson.setBackgroundResource(R.drawable.bg_round_999999);
            } else {
                holder.bt_attentionperson.setText("取消关注");
                holder.bt_attentionperson.setBackgroundResource(R.drawable.bg_round_ed4a4a);
            }
            holder.bt_attentionperson.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String, String> maps = new HashMap<>();
                    maps.put("uid", datas.get(position).getUid());
                    okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_ATTENTIONUSERSTATUS
                            , ((MyApplication) context.getApplicationContext()).getMaps(), maps, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                    Log.d(TAG, "onFailure: ");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String attentionResult = response.body().string();
                                    Log.d(TAG, "onclick-->onResponse:\n " + attentionResult);
                                    Gson gson = new Gson();
                                    AttentionOrCancelPerson attentionPersonorcancel = gson.fromJson(attentionResult, AttentionOrCancelPerson.class);
                                    if (attentionPersonorcancel.getCode() == 0) {
                                        if (!attentionPersonorcancel.getData().isFollowStatus()) {
                                            ((Activity) context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    datas.remove(position);
                                                    notifyDataSetChanged();
                                                }
                                            });
                                        }
                                        EventBus.getDefault().post(new AttentionEvent(datas.get(position).getUid(), attentionPersonorcancel.getData().isFollowStatus()));
                                    } else if (attentionPersonorcancel.getCode() == 1005) {
                                        BaseUtils.getLoginDialog(context).show();
                                    }

                                }
                            });
                }
            });
            holder.attentionperson_picture.setOnClickListener(new View.OnClickListener() {
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
                                                    OtherUserInfoActivity.start(context,datas.get(position).getUid(),datas.get(position).isFollowStatus());
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

        } else if (getItemViewType(position) == FOOT_VIEW) {
            FootViewhHolder holder = (FootViewhHolder) viewHolder;
            holder.text_foot.setVisibility(View.GONE);
        }
    }

    public class MyAttentionPersonViewHolder extends MyBaseViewHolder {
        private TextView tv_tag;
        private CircleImageView attentionperson_picture;
        private TextView attentionperson_name;
        private TextView attentionperson_account;
        private TextView bt_attentionperson;

        public MyAttentionPersonViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tv_tag = (TextView) itemView.findViewById(R.id.tv_tag);
            attentionperson_picture = (CircleImageView) itemView.findViewById(R.id.attentionperson_picture);
            attentionperson_name = (TextView) itemView.findViewById(R.id.tv_attenionperson_name);
            attentionperson_account = (TextView) itemView.findViewById(R.id.tv_attention_count);
            bt_attentionperson = (TextView) itemView.findViewById(R.id.bt_attentionperson);
        }
    }

    public class FootViewhHolder extends MyBaseViewHolder {
        TextView text_foot;

        public FootViewhHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            text_foot = (TextView) itemView.findViewById(R.id.tv_footView);
        }
    }

    public char getfontLetter(int position) {
        char letter = pinYinUtil.getPinYin(datas.get(position).getNickName()).charAt(0);
        return letter;
    }

    public int isFirstShowLetter(char position) {
        for (int i = 0; i < getItemCount() - 1; i++) {
            String letter = pinYinUtil.getPinYin(datas.get(i).getNickName());
            if (letter.charAt(0) == (position)) {
                return i;
            }
        }
        return 0;
    }

    public void clearAllData() {
        if (!datas.isEmpty()) {
            datas.clear();
            notifyDataSetChanged();
        }
    }
}
