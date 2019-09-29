package adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.am.shortVideo.activity.MessageHelpActivity;

import java.util.List;

import base.MyBaseAdapter;
import base.MyBaseViewHolder;
import bean.HistoryMessageBean;
import util.FootViewHolder;

/**
 * Created by JC on 2019/9/12.
 */

public class PushHistoryMessageAdapter extends MyBaseAdapter<HistoryMessageBean.DataBean.PushHistoryBean, MyBaseViewHolder> {
    private Context context;
    private List<HistoryMessageBean.DataBean.PushHistoryBean> datas;

    public PushHistoryMessageAdapter(List<HistoryMessageBean.DataBean.PushHistoryBean> datas, Context context) {
        super(datas, context);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public MyBaseViewHolder onCreateHolder(ViewGroup parent, int position) {
        if (position == NORMAL_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.pushhistory_item, parent, false);
            return new PushHistoryMessageViewHolder(view);
        } else if (position == FOOT_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.foot_item, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onHolder(MyBaseViewHolder holder, int position) {
        if (getItemViewType(position) == NORMAL_VIEW) {
            PushHistoryMessageViewHolder pushhisitorymessageHolder = (PushHistoryMessageViewHolder) holder;
            if (datas.get(position).getId() == -1) {

            } else {
                pushhisitorymessageHolder.pushMessage_content.setText(datas.get(position).getMsgContent());
            }

            pushhisitorymessageHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    context.startActivity(new Intent(context, MessageHelpActivity.class));
                }
            });

        } else if (getItemViewType(position) == FOOT_VIEW) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            footViewHolder.text_foot.setVisibility(View.GONE);
        }
    }

    public class PushHistoryMessageViewHolder extends MyBaseViewHolder {
        TextView pushMessage_content;

        public PushHistoryMessageViewHolder(View itemView) {
            super(itemView);
            pushMessage_content = (TextView) itemView.findViewById(R.id.pushhistory_content);
        }
    }
}
