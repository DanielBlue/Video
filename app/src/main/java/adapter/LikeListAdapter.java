package adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.am.shortVideo.activity.LikeListActivity;
import com.am.shortVideo.activity.LookFullScreenVideo;
import com.am.shortVideo.activity.OtherUserInfoActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.List;

import application.MyApplication;
import base.MyAllBaseAdapter;
import base.MyBaseViewHolder;
import bean.LikeListShow;
import bean.UserInfo;
import customeview.LoginPopupwindow;
import de.hdodenhof.circleimageview.CircleImageView;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Response;
import util.FootViewHolder;
import util.FormatTime;
import util.HttpUri;

/**
 * Created by JC on 2019/9/4.
 */

public class LikeListAdapter extends MyAllBaseAdapter<LikeListShow.DataBean.PageListBean, MyBaseViewHolder> {
    private static final String TAG = "LikeListAdapter";
    private final OktHttpUtil okHttpUtil;
    private Context context;
    private static  final int DATA_LOAD_NORMAL=0;
    private static final  int DATA_LOAD_FOOTER=1;
    private List<LikeListShow.DataBean.PageListBean> dates;
    public LikeListAdapter(List<LikeListShow.DataBean.PageListBean> dates, Context context) {
        super(dates, context);
        this.context=context;
        this.dates=dates;
        okHttpUtil= OktHttpUtil.getInstance();
    }

    @Override
    public MyBaseViewHolder onAdapterCreaetViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType==NORMAL_VIEW){
            View view= LayoutInflater.from(context).inflate(R.layout.likelist_item,null);
            return new LikeListViewHolder(view);
        }
        else{
            View view= LayoutInflater.from(context).inflate(R.layout.foot_item,viewGroup,false);
            return new FootViewHolder(view);
        }
    }

    @Override
    public void getBindViewHolder(MyBaseViewHolder viewHolder,final int position) {
        if(getItemViewType(position)==DATA_LOAD_NORMAL){
            LikeListViewHolder holder=(LikeListViewHolder)viewHolder;
         Glide.with(context).load(HttpUri.BASE_DOMAIN+dates.get(position).getAvatar()).into( holder.likelist_picture);
            holder.likelist_nickname.setText(dates.get(position).getNickName());
            holder.likelist_videotime.setText(FormatTime.formatTime(dates.get(position).getCreateTime()));
            Glide.with(context).load(HttpUri.BASE_DOMAIN+dates.get(position).getCover()).into( holder.likelist_videoimg);
            holder.likelist_videostart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, LookFullScreenVideo.class);
                    intent.putExtra("videopalyer_url", HttpUri.BASE_DOMAIN+dates.get(position).getVideoUrl());
                    context.startActivity(intent);
                }
            });
            holder.likelist_picture.setOnClickListener(new View.OnClickListener() {
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
                                                if(!userinfo.getData().getUserInfo().getUid().equals(dates.get(position))) {
                                                    Intent intent = new Intent(context, OtherUserInfoActivity.class);
                                                    intent.putExtra("otheruserinfo", dates.get(position).getUid());
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
        }else if(getItemViewType(position)==DATA_LOAD_FOOTER){
            FootViewHolder footViewHolder = (FootViewHolder) viewHolder;
            footViewHolder.text_foot.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(position+1==getItemCount()){
            return DATA_LOAD_FOOTER;
        }
        return DATA_LOAD_NORMAL;
    }

    public class LikeListViewHolder extends MyBaseViewHolder{
        private CircleImageView likelist_picture;
        private TextView likelist_nickname;
        private TextView likelist_videotime;
        private ImageView likelist_videostart;
        private ImageView likelist_videoimg;

        public LikeListViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            likelist_nickname=(TextView)itemView.findViewById(R.id.likelist_nickname);
            likelist_picture=(CircleImageView)itemView.findViewById(R.id.likelist_picture);
            likelist_videotime=(TextView)itemView.findViewById(R.id.likelist_attemtion_time);
            likelist_videostart=(ImageView)itemView.findViewById(R.id.likelist_video_start);
            likelist_videoimg=(ImageView)itemView.findViewById(R.id.likelist_videoimg);

        }
    }
    public void clearAllData(){
        if(!dates.isEmpty()){
            dates.clear();
            notifyDataSetChanged();
        }

    }
}
