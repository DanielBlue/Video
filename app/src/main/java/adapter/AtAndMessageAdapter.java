package adapter;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import java.util.List;

import base.MyBaseAdapter;
import base.MyBaseViewHolder;
import bean.MessageCommentShow;

/**
 * Created by JC on 2019/9/12.
 */

public class AtAndMessageAdapter extends MyBaseAdapter<MessageCommentShow.DataBean.PageListBean, AtAndMessageAdapter.AtAndMessageViewHolder>{
    private Context context;
    public AtAndMessageAdapter(List<MessageCommentShow.DataBean.PageListBean> datas, Context context) {
        super(datas, context);
        this.context=context;
    }

    @Override
    public AtAndMessageViewHolder onCreateHolder(ViewGroup parentm,int postion) {
        return null;
    }

    @Override
    public void onHolder(AtAndMessageViewHolder holder, int position) {

    }

    public class AtAndMessageViewHolder extends MyBaseViewHolder{

        public AtAndMessageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
