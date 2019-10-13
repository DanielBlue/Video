package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.am.shortVideo.activity.ShortVideoPlayingActivity;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import java.util.List;

import base.MyBaseViewHolder;
import bean.HomeVideoImg;
import de.hdodenhof.circleimageview.CircleImageView;
import util.GlideUtils;
import util.HttpUri;

/**
 * Created by JC on 2019/8/6.
 */

public class PictureShowAdapter extends RecyclerView.Adapter<MyBaseViewHolder> {
    private static final String TAG = "PictureShowAdapter";
    private static final int DATA_LOAD_NORMAL = 0;
    private static final int DATA_LOAD_FOOTER = 1;
    Context context;
    private List<HomeVideoImg.DataBean.IndexListBean> datas;
    private int type;

    public PictureShowAdapter(List<HomeVideoImg.DataBean.IndexListBean> datas, Context context, int type) {
        this.context = context;
        this.datas = datas;
        this.type = type;
    }


    @Override
    public MyBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.d(TAG, "onCreateViewHolder: ");
        if (viewType == DATA_LOAD_NORMAL) {
            Log.d(TAG, "onCreateViewHolder:0 ");
            View view = LayoutInflater.from(context).inflate(R.layout.home_item, null);
            return new PictureViewhHolder(view);
        } else if (viewType == DATA_LOAD_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.foot_item, null);
            Log.d(TAG, "onCreateViewHolder:1");
            return new FootViewhHolder(view);
        }
        Log.d(TAG, "onCreateViewHolder:2 ");
        return null;
    }

    @Override
    public void onBindViewHolder(MyBaseViewHolder holder, final int position) {
        Log.d(TAG, "onBindViewHolder: ");
        if (getItemViewType(position) == DATA_LOAD_NORMAL) {
            Log.d(TAG, "onBindViewHolder:0 ");
            GlideUtils.displayImage(context, HttpUri.BASE_DOMAIN + datas.get(position).getCoverPath(), ((PictureViewhHolder) holder).picture_thum);
            GlideUtils.showHeader(context, HttpUri.BASE_DOMAIN + datas.get(position).getAvatar(), ((PictureViewhHolder) holder).iv_userimg);

            ((PictureViewhHolder) holder).tv_videotmp.setText(datas.get(position).getLikeCounts() + "");
            ((PictureViewhHolder) holder).tv_usercity.setText(datas.get(position).getVideoPath());
            ((PictureViewhHolder) holder).picture_thum.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, ShortVideoPlayingActivity.class);
                    intent.putExtra("data", new Gson().toJson(datas));
                    intent.putExtra("position", position);
                    context.startActivity(intent);
                }
            });
        } else if (getItemViewType(position) == DATA_LOAD_FOOTER) {
            Log.d(TAG, "onBindViewHolder:1 ");
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position + 1 == getItemCount()) {
            return DATA_LOAD_FOOTER;
        }
        return DATA_LOAD_NORMAL;
    }

    @Override
    public int getItemCount() {
        return datas.size() + 1;
    }

    public class PictureViewhHolder extends MyBaseViewHolder {
        ImageView picture_thum;
        private TextView tv_videotmp;
        private CircleImageView iv_userimg;
        private TextView tv_usercity;

        public PictureViewhHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            picture_thum = (ImageView) itemView.findViewById(R.id.iv_videoshow);
            iv_userimg = (CircleImageView) itemView.findViewById(R.id.circleImageView);
            tv_videotmp = (TextView) itemView.findViewById(R.id.tv_videotmp);
            tv_usercity = (TextView) itemView.findViewById(R.id.tv_usercity);
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

    @Override
    public void onAttachedToRecyclerView(RecyclerView recyclerView) {
        super.onAttachedToRecyclerView(recyclerView);
        RecyclerView.LayoutManager layoutManager = recyclerView.getLayoutManager();
        if (layoutManager instanceof GridLayoutManager) {
            final GridLayoutManager gridLayoutManager = (GridLayoutManager) layoutManager;
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    if (getItemViewType(position) == DATA_LOAD_FOOTER) {
                        return gridLayoutManager.getSpanCount();
                    }
                    return 1;
                }
            });
        }
    }

}
