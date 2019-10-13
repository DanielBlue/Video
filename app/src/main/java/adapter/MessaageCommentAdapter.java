package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.am.shortVideo.activity.LookFullScreenVideo;
import com.bumptech.glide.Glide;

import java.util.List;

import base.MyAllBaseAdapter;
import base.MyBaseViewHolder;
import bean.CommentAtBean;
import de.hdodenhof.circleimageview.CircleImageView;
import util.FootViewHolder;
import util.FormatTime;
import util.GlideUtils;
import util.HttpUri;

/**
 * Created by JC on 2019/9/4.
 */

public class MessaageCommentAdapter extends MyAllBaseAdapter<CommentAtBean.DataBean.PageListBean, MyBaseViewHolder> {
    private Context context;
    private static  final int DATA_LOAD_NORMAL=0;
    private static final  int DATA_LOAD_FOOTER=1;
    private List<CommentAtBean.DataBean.PageListBean> datas;
    public MessaageCommentAdapter(List<CommentAtBean.DataBean.PageListBean> datas, Context context) {
        super(datas, context);
        this.context=context;
        this.datas=datas;
    }

    @Override
    public int getItemViewType(int position) {
        if(position+1==getItemCount()){
            return DATA_LOAD_FOOTER;
        }
        return DATA_LOAD_NORMAL;
    }

    @Override
    public MyBaseViewHolder onAdapterCreaetViewHolder(ViewGroup viewGroup, int viewType) {
        if(viewType==NORMAL_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.message_comment_item, viewGroup,false);
            return new MessageCommentViewHolder(view);
        }
         else{
            View view= LayoutInflater.from(context).inflate(R.layout.foot_item,viewGroup,false);
            return new FootViewHolder(view);
        }
    }

    @Override
    public void getBindViewHolder(MyBaseViewHolder viewHolder, final int position) {
        if(getItemViewType(position)==DATA_LOAD_NORMAL){
            MessageCommentViewHolder messageCommentViewHolder=(MessageCommentViewHolder)viewHolder;
            GlideUtils.showHeader(context, HttpUri.BASE_DOMAIN + datas.get(position).getAvatar(), messageCommentViewHolder.messagecomment_picture);
            messageCommentViewHolder.messagecomment_nickname.setText(datas.get(position).getNickName());
            messageCommentViewHolder.message_comment_replay_warn.setText("回复了你的评论");
            messageCommentViewHolder.messagecomment_content.setText(datas.get(position).getContent());
            messageCommentViewHolder.messagecomment_time.setText(FormatTime.formatTime(datas.get(position).getCommentTime()));
            Glide.with(context).load(HttpUri.BASE_DOMAIN+datas.get(position).getCoverPath()).into(messageCommentViewHolder.messagecomment_videoImg);
            messageCommentViewHolder.messagecomment_videostart.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context, LookFullScreenVideo.class);
                    intent.putExtra("videopalyer_url",HttpUri.BASE_DOMAIN+datas.get(position).getVideoUrl());
                    context.startActivity(intent);
                }
            });
        }else if(getItemViewType(position)==DATA_LOAD_FOOTER){
            FootViewHolder footViewHolder = (FootViewHolder) viewHolder;
            footViewHolder.text_foot.setVisibility(View.GONE);
        }
    }

    public class MessageCommentViewHolder extends MyBaseViewHolder{
        private CircleImageView messagecomment_picture;
        private TextView messagecomment_nickname;
        private TextView messagecomment_content;
        private TextView message_comment_replay_warn;
        private TextView messagecomment_time;
        private ImageView messagecomment_videoImg;
        private ImageView messagecomment_videostart;
        public MessageCommentViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            messagecomment_picture=(CircleImageView)itemView.findViewById(R.id.message_comment_picture);
            messagecomment_nickname=(TextView)itemView.findViewById(R.id.message_comment_nickname);
            messagecomment_content=(TextView)itemView.findViewById(R.id.message_comment_content);
            messagecomment_time=(TextView)itemView.findViewById(R.id.message_comment_replay_time);
            message_comment_replay_warn=(TextView)itemView.findViewById(R.id.message_comment_replay_warn);
            messagecomment_videoImg=(ImageView)itemView.findViewById(R.id.message_comment_videoimg);
            messagecomment_videostart=(ImageView)itemView.findViewById(R.id.message_comment_video_start);
        }
    }
}
