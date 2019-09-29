package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import bean.AttentionOrCancelPerson;
import bean.FansShow;
import bean.UserInfo;
import customeview.LoginPopupwindow;
import de.hdodenhof.circleimageview.CircleImageView;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.FootViewHolder;
import util.FormatTime;
import util.HttpUri;
import util.PinYinUtil;

/**
 * Created by JC on 2019/9/4.
 */

public class FansShowAdapter extends MyAllBaseAdapter<FansShow.DataBean.PageListBean,MyBaseViewHolder> {
    private static final String TAG = "FansShowAdapter";
    private final OktHttpUtil okHttpUtil;
    private List<FansShow.DataBean.PageListBean> datas;
    private Context context;
    private PinYinUtil pinYinUtil;
    public FansShowAdapter(List<FansShow.DataBean.PageListBean> dates, Context context, PinYinUtil pinYinUtil) {
        super(dates, context);
        this.datas=dates;
        this.context=context;
        this.pinYinUtil=pinYinUtil;
        okHttpUtil= OktHttpUtil.getInstance();
    }

    @Override
    public MyBaseViewHolder onAdapterCreaetViewHolder(ViewGroup viewGroup, int viewType) {
       if(viewType==NORMAL_VIEW){
           View view= LayoutInflater.from(context).inflate(R.layout.fansshow_item,viewGroup,false);
           return new FansShowViewHolder(view);
       }else{
           View view= LayoutInflater.from(context).inflate(R.layout.foot_item,viewGroup,false);
           return new FootViewHolder(view);
       }

    }

    @Override
    public void getBindViewHolder(MyBaseViewHolder viewHolder, final int position) {
        if(getItemViewType(position)==NORMAL_VIEW){
          final FansShowViewHolder  holder=(FansShowViewHolder)viewHolder;
            char letter=getfontLetter(position);
            if(isFirstShowLetter(letter)==position){
                holder.tv_tag.setVisibility(View.VISIBLE);
                holder.tv_tag.setText(""+letter);
            }else{
                holder.tv_tag.setVisibility(View.GONE);
            }
            Glide.with(context).load(HttpUri.BASE_DOMAIN+datas.get(position).getAvatar()).into(holder.fansCircleImagview);
            holder.fans_nickname.setText(datas.get(position).getNickName());
            holder.fans_attentionTime.setText(FormatTime.formatTime(datas.get(position).getFollowDate()));
            if(datas.get(position).isFollowStatus()){
                holder.bt_fans_attention.setText("互相关注");
                holder.bt_fans_attention.setBackgroundResource(R.color.colorGray);
            }else{
                holder.bt_fans_attention.setText(context.getResources().getString(R.string.bt_attention));
                holder.bt_fans_attention.setBackgroundResource(R.color.red);
            }
            holder.bt_fans_attention.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String,String> maps=new HashMap<>();
                    maps.put("uid",datas.get(position).getUid());
                    okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_ATTENTIONUSERSTATUS
                            , ((MyApplication) context.getApplicationContext()).getMaps(), maps, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    e.printStackTrace();
                                    Log.d(TAG, "onFailure: ");
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String attentionResult=response.body().string();
                                    Log.d(TAG, "onclick-->onResponse:\n "+attentionResult);
                                    Gson gson=new Gson();
                                    AttentionOrCancelPerson attentionPersonorcancel=gson.fromJson(attentionResult, AttentionOrCancelPerson.class);
                                    if(attentionPersonorcancel.getCode()==0){
                                        if(attentionPersonorcancel.getData().isFollowStatus()){
                                            ((Activity)context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    datas.get(position).setFollowStatus(true);
                                                   notifyItemChanged(position);

                                                }
                                            });
                                        }else{
                                            ((Activity)context).runOnUiThread(new Runnable() {
                                                @Override
                                                public void run() {
                                                    datas.get(position).setFollowStatus(false);
                                                    notifyItemChanged(position);
                                                }
                                            });
                                        }
                                    }else if(attentionPersonorcancel.getCode()==1005){
                                        new LoginPopupwindow(context);
                                    }

                                }
                            });
                }
            });
            holder.fansCircleImagview.setOnClickListener(new View.OnClickListener() {
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
                                                if(!userinfo.getData().getUserInfo().getUid().equals(datas.get(position))) {
                                                    Intent intent = new Intent(context, OtherUserInfoActivity.class);
                                                    intent.putExtra("otheruserinfo", datas.get(position).getUid());
                                                    context.startActivity(intent);
                                                }else{
                                                    Toast.makeText(context,"请在个人中心查看信息",Toast.LENGTH_SHORT).show();
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
        }else if(getItemViewType(position)==FOOT_VIEW){
            Log.d(TAG, "getBindViewHolder: ");
            FootViewHolder footViewHolder = (FootViewHolder) viewHolder;
            footViewHolder.text_foot.setVisibility(View.GONE);
        }
    }

    public class  FansShowViewHolder extends MyBaseViewHolder{
        private TextView tv_tag;
            private CircleImageView fansCircleImagview;
        private TextView fans_nickname;
        private TextView fans_attentionTime;
        private TextView bt_fans_attention;
        public FansShowViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
        fansCircleImagview=(CircleImageView)itemView.findViewById(R.id.fans_picture);
        fans_nickname=(TextView)itemView.findViewById(R.id.fans_nickname);
        fans_attentionTime=(TextView)itemView.findViewById(R.id.fans_attemtion_time);
        tv_tag=(TextView)itemView.findViewById(R.id.tv_tag);
        bt_fans_attention=(TextView)itemView.findViewById(R.id.bt_fans_attention);
        }
    }
    public char getfontLetter(int position){
        char letter=pinYinUtil.getPinYin(datas.get(position).getNickName()).charAt(0);
        return letter;
    }

    public int  isFirstShowLetter(char position){
        for(int i=0;i<getItemCount()-1;i++){
            String letter=pinYinUtil.getPinYin(datas.get(i).getNickName());
            if(letter.charAt(0)==(position)){
                return i;
            }
        }
        return 0;
    }
    public void clearAllData(){
        if(!datas.isEmpty()){
            datas.clear();
            notifyDataSetChanged();
        }
    }
}
