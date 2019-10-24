package adapter;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.am.shortVideo.activity.ZuopinPlayingActivity;
import com.bumptech.glide.Glide;

import java.io.Serializable;
import java.util.List;

import base.MyAllBaseAdapter;
import base.MyBaseViewHolder;
import bean.IndexListBean;
import util.FootViewHolder;
import util.HttpUri;

/**
 * Created by 李杰 on 2019/9/7.
 */

public class UserVideoAdapter extends MyAllBaseAdapter<IndexListBean, MyBaseViewHolder> {
    private static final String TAG = "UserVideoAdapter";
    private Context context;
    private List<IndexListBean> dates;

    public UserVideoAdapter(List<IndexListBean> dates, Context context) {
        super(dates, context);
        this.context = context;
        this.dates = dates;
    }

    @Override
    public MyBaseViewHolder onAdapterCreaetViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == NORMAL_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.userpublishvideo_item, viewGroup, false);
            return new UserVideoViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.foot_item, viewGroup, false);
            return new FootViewHolder(view);
        }

    }

    @Override
    public void getBindViewHolder(MyBaseViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == NORMAL_VIEW) {
            UserVideoViewHolder userVideoViewHolder = (UserVideoViewHolder) viewHolder;
            Log.d(TAG, "onBindViewHolder:0 ");
            Glide.with(context).load(HttpUri.BASE_DOMAIN + dates.get(position).getCoverPath()).into(userVideoViewHolder.iv_user_publishvideo);
            userVideoViewHolder.tv_user_likecount.setText("" + dates.get(position).getLikeCounts());
            userVideoViewHolder.rl_channelitme.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (onItemClickListerner != null) {
                        onItemClickListerner.onItemClickListerner(position);
                    } else {
                    Intent intent = new Intent(context, ZuopinPlayingActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("datas", (Serializable) dates);
                    bundle.putInt("position", position);
                    bundle.putSerializable("videourl", dates.get(position));
                    bundle.putInt("type", 4);
                    intent.putExtra("homeVideoImg", bundle);
                    intent.putExtra("user_uid", dates.get(position).getUid());
                    context.startActivity(intent);
                    }
                }
            });
        } else if (getItemViewType(position) == FOOT_VIEW) {
            Log.d(TAG, "onBindViewHolder:1 ");
            FootViewHolder footViewHolder = (FootViewHolder) viewHolder;
            footViewHolder.text_foot.setVisibility(View.GONE);
        }
    }

    public class UserVideoViewHolder extends MyBaseViewHolder {
        private ImageView iv_user_publishvideo;
        private TextView tv_user_likecount;
        private RelativeLayout rl_channelitme;

        public UserVideoViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View view) {
            rl_channelitme = (RelativeLayout) view.findViewById(R.id.rl_channelitem);
            iv_user_publishvideo = (ImageView) view.findViewById(R.id.iv_user_publishvideo);
            tv_user_likecount = (TextView) view.findViewById(R.id.tv_user_likecount);
        }
    }

    private OnItemClickListerner onItemClickListerner;

    public interface OnItemClickListerner {
        void onItemClickListerner(int position);
    }

    public void setOnItemClickListerner(OnItemClickListerner onItemClickListerner) {
        this.onItemClickListerner = onItemClickListerner;
    }
}
