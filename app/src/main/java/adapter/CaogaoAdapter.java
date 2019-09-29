package adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.am.shortVideo.R;

import java.net.URI;
import java.util.List;

import base.MyAllBaseAdapter;
import base.MyBaseAdapter;
import base.MyBaseViewHolder;
import bean.PublishVideoInfo;
import customeview.ShowLoopVideo;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;
import util.FootViewHolder;
import util.HttpUri;

/**
 * Created by 李杰 on 2019/9/17.
 */

public class CaogaoAdapter extends MyBaseAdapter<String,MyBaseViewHolder> {
    private static final String TAG = "CaogaoAdapter";
    private Context context;
    private List<String> dates;
    public CaogaoAdapter(List<String> dates, Context context) {
        super(dates, context);
        this.context=context;
        this.dates=dates;
    }

    @Override
    public MyBaseViewHolder onCreateHolder(ViewGroup viewGroup, int viewType) {
        if(viewType==NORMAL_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.caogao_item, viewGroup, false);
            return new CaogaoViewHolder(view);
        }else if(viewType==FOOT_VIEW){
            View view = LayoutInflater.from(context).inflate(R.layout.foot_item, viewGroup, false);
            return  new FootViewHolder(view);
        }
        return  null;
    }

    @Override
    public void onHolder(MyBaseViewHolder viewHolder, int position) {
        if(getItemViewType(position)==NORMAL_VIEW){
         CaogaoViewHolder caogaoViewHolder= (CaogaoViewHolder)viewHolder;
            Log.d(TAG, "getBindViewHolder: "+dates.get(position));
            caogaoViewHolder.showLoopVideo.setUp(dates.get(position), JCVideoPlayerStandard.SCREEN_WINDOW_FULLSCREEN,"");
            if (position == 0) {
                caogaoViewHolder.showLoopVideo.startVideo();
            }
        }else if(getItemViewType(position)==FOOT_VIEW){
         FootViewHolder holder=  (FootViewHolder)viewHolder;
         holder.text_foot.setVisibility(View.GONE);
        }
    }


    public class CaogaoViewHolder extends MyBaseViewHolder{
        public ShowLoopVideo showLoopVideo;
        public CaogaoViewHolder(View itemView) {
            super(itemView);
            showLoopVideo=(ShowLoopVideo)itemView.findViewById(R.id.videoplayer);
        }
    }
}
