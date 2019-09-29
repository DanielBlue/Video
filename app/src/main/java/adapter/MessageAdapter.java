package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.am.shortVideo.R;

import java.util.List;

import base.MyBaseAdapter;
import base.MyBaseViewHolder;
import bean.HistoryMessageBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import util.FootViewHolder;
import util.FormatTime;

public class MessageAdapter extends MyBaseAdapter<HistoryMessageBean.DataBean.PushHistoryBean, MyBaseViewHolder> {

    private Context context;
    private List<HistoryMessageBean.DataBean.PushHistoryBean> datas;

    public MessageAdapter(Context context, List<HistoryMessageBean.DataBean.PushHistoryBean> datas) {
        super(datas, context);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public MyBaseViewHolder onCreateHolder(ViewGroup parent, int position) {
        if (position == NORMAL_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_message, parent, false);
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
            PushHistoryMessageViewHolder pushHistoryMessageViewHolder = (PushHistoryMessageViewHolder) holder;
            HistoryMessageBean.DataBean.PushHistoryBean pushHistoryBean = datas.get(position);
            pushHistoryMessageViewHolder.tvMessage.setText(pushHistoryBean.getMsgContent());
            pushHistoryMessageViewHolder.tvMessageTime.setText(FormatTime.formatYHDhm(pushHistoryBean.getCreateTime()));

        }else if (getItemViewType(position) == FOOT_VIEW) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            footViewHolder.text_foot.setVisibility(View.GONE);
        }
    }

    public class PushHistoryMessageViewHolder extends MyBaseViewHolder {
        @BindView(R.id.tv_message)
        TextView tvMessage;
        @BindView(R.id.tv_message_time)
        TextView tvMessageTime;

        PushHistoryMessageViewHolder(View view) {
            super(view);
            ButterKnife.bind(this, view);
        }
    }
}
