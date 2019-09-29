package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.am.shortVideo.R;

import java.util.List;

import base.MyBaseAdapter;
import base.MyBaseViewHolder;
import bean.AttentionPerson;
import bean.VideoComment;
import util.FootViewHolder;

/**
 * Created by 李杰 on 2019/9/12.
 */

public class AtUserNickAdapter extends MyBaseAdapter<AttentionPerson.DataBean.PageListBean, MyBaseViewHolder> {
    private Context context;
    private AtNickCallBack nickItemLinstener;
    private List<AttentionPerson.DataBean.PageListBean> datas;

    public AtUserNickAdapter(List<AttentionPerson.DataBean.PageListBean> datas, Context context) {
        super(datas, context);
        this.context = context;
        this.datas = datas;
    }

    public void setDatas(List<AttentionPerson.DataBean.PageListBean> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    @Override
    public MyBaseViewHolder onCreateHolder(ViewGroup parent, int position) {
        if (position == NORMAL_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.atusernick_item, parent, false);
            return new AtUserNickViewHolder(view);
        } else if (position == FOOT_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.foot_item, parent, false);
            return new FootViewHolder(view);

        }
        return null;
    }

    @Override
    public void onHolder(final MyBaseViewHolder holder, final int position) {
        if (getItemViewType(position) == NORMAL_VIEW) {
            AtUserNickViewHolder atUserNickViewHolder = (AtUserNickViewHolder) holder;
            atUserNickViewHolder.tv_nickname.setText("@" + datas.get(position).getNickName());
            atUserNickViewHolder.rl_at.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    nickItemLinstener.getAtName(datas.get(position).getNickName(), datas.get(position).getUid());
                }
            });
        } else if (getItemViewType(position) == FOOT_VIEW) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            footViewHolder.text_foot.setVisibility(View.GONE);
        }
    }

    public class AtUserNickViewHolder extends MyBaseViewHolder {
        private RelativeLayout rl_at;
        private TextView tv_nickname;

        public AtUserNickViewHolder(View itemView) {
            super(itemView);
            tv_nickname = (TextView) itemView.findViewById(R.id.tv_at_usernickname);
            rl_at = (RelativeLayout) itemView.findViewById(R.id.rl_at);
        }
    }

    public void setOnNickItemLinstener(AtNickCallBack nickItemLinstener) {
        this.nickItemLinstener = nickItemLinstener;
    }

    public interface AtNickCallBack {
        void getAtName(String nicknama, String id);
    }
}
