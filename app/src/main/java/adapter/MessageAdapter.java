package adapter;

import android.support.annotation.Nullable;

import com.am.shortVideo.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.HistoryMessageBean;
import util.FormatTime;

public class MessageAdapter extends BaseQuickAdapter<HistoryMessageBean.DataBean.PushHistoryBean, BaseViewHolder> {


    public MessageAdapter(@Nullable List<HistoryMessageBean.DataBean.PushHistoryBean> data) {
        super(R.layout.item_message, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, HistoryMessageBean.DataBean.PushHistoryBean item) {
        helper.setText(R.id.tv_message, item.getMsgTitle())
                .setText(R.id.tv_message_time, FormatTime.formatYHDhm(item.getCreateTime()));
        if (item.getMsgType() == 3) {
            helper.setVisible(R.id.tv_content, true)
                    .setText(R.id.tv_content, item.getMsgContent());
        } else {
            helper.setVisible(R.id.tv_content, false);
        }
    }
}
