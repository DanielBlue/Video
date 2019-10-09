package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.am.shortVideo.activity.CaogaoPlayingActivity;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import base.MyAllBaseAdapter;
import base.MyBaseViewHolder;
import bean.PublishVideoInfo;
import util.FootViewHolder;

/**
 * Created by 李杰 on 2019/9/15.
 */

public class CaogaoImgAdapter extends MyAllBaseAdapter<PublishVideoInfo,MyBaseViewHolder> {
    private static final String TAG = "CaogaoImgAdapter";
   private  List<PublishVideoInfo> dates;
   private Context context;

    public CaogaoImgAdapter(List<PublishVideoInfo> dates, Context context) {
        super(dates, context);
        this.context=context;
        this.dates=dates;
    }

    @Override
    public MyBaseViewHolder onAdapterCreaetViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType==NORMAL_VIEW){
            View view= LayoutInflater.from(context).inflate(R.layout.userpublishvideo_item,viewGroup,false);
            return new CaogaoImgAdapter.UserVideoViewHolder(view);
        }else{
            View view= LayoutInflater.from(context).inflate(R.layout.foot_item,viewGroup,false);
            return new FootViewHolder(view);
        }
    }

    @Override
    public void getBindViewHolder(MyBaseViewHolder viewHolder,final int position) {
        if(getItemViewType(position)==NORMAL_VIEW){
            CaogaoImgAdapter.UserVideoViewHolder userVideoViewHolder=(CaogaoImgAdapter.UserVideoViewHolder) viewHolder;
            Log.d(TAG, "onBindViewHolder:0 ");
            userVideoViewHolder.iv_user_publishvideo.setImageBitmap(dates.get(position).getBitmap());
            userVideoViewHolder.tv_user_likecount.setVisibility(View.GONE);
            userVideoViewHolder.iv_user_like.setVisibility(View.GONE);
            userVideoViewHolder.iv_user_publishvideo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    List<String> videoUrlList = new ArrayList<>();
                    for (PublishVideoInfo bean:dates){
                        videoUrlList.add(bean.getLocalurl());
                    }
                    Intent intent = new Intent(context, CaogaoPlayingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("datas", (Serializable) videoUrlList);
                    bundle.putInt("type",5);
                    intent.putExtra("data",bundle);
                    context.startActivity(intent);
                }
            });
        }else if(getItemViewType(position)==FOOT_VIEW){
            Log.d(TAG, "onBindViewHolder:1 ");
            FootViewHolder footViewHolder=(FootViewHolder)viewHolder;
            footViewHolder.text_foot.setVisibility(View.GONE);
        }
    }

    public class UserVideoViewHolder extends MyBaseViewHolder {
        private ImageView iv_user_publishvideo;
        private TextView tv_user_likecount;
        private ImageView iv_user_like;

        public UserVideoViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            iv_user_publishvideo=(ImageView)view.findViewById(R.id.iv_user_publishvideo);
            tv_user_likecount=(TextView)view.findViewById(R.id.tv_user_likecount);
            iv_user_like=(ImageView)view.findViewById(R.id.iv_user_like);
        }
    }
}
