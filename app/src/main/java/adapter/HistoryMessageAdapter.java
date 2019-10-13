package adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.Size;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.am.shortVideo.activity.SearchActivity;
import com.am.shortVideo.activity.SearchResultActivity;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;

import java.util.List;

import base.MyAllBaseAdapter;
import base.MyBaseViewHolder;
import bean.MessageSearchinfo;
import db.UserModel;
import db.UserModel_Table;
import util.SizeUtils;

/**
 * Created by JC on 2019/9/9.
 */

public class HistoryMessageAdapter extends MyAllBaseAdapter<UserModel, MyBaseViewHolder> {
    private Context context;
    private List<UserModel> dates;
    private static final int DATA_LOAD_NORMAL = 0;
    private static final int DATA_LOAD_FOOTER = 1;

    public HistoryMessageAdapter(List<UserModel> dates, Context context) {
        super(dates, context);
        this.context = context;
        this.dates = dates;
    }

    @Override
    public MyBaseViewHolder onAdapterCreaetViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == DATA_LOAD_NORMAL) {
            View view = LayoutInflater.from(context).inflate(R.layout.searchone_item, viewGroup, false);
            return new HistoryMessageViewHolder(view);
        } else if (viewType == DATA_LOAD_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.foot_item, null);
            return new FootViewhHolder(view);
        }
        return null;
    }

    @Override
    public void getBindViewHolder(MyBaseViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == DATA_LOAD_NORMAL) {
            HistoryMessageViewHolder historyMessageViewHolder = (HistoryMessageViewHolder) viewHolder;
            historyMessageViewHolder.tv_historymessage.setText(dates.get(position).getName());
            historyMessageViewHolder.iv_imgdel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SQLite.delete(UserModel.class).where(UserModel_Table.name.glob(dates.get(position).getName())).execute();
                    dates.remove(position);
                    notifyDataSetChanged();
                }
            });
            historyMessageViewHolder.relativeLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(context, SearchResultActivity.class);
                    context.startActivity(intent);
                    EventBus.getDefault().postSticky(MessageSearchinfo.getInstance(dates.get(position).getName()));
                }
            });
        } else if (getItemViewType(position) == DATA_LOAD_FOOTER) {
            final FootViewhHolder footViewhHolder = (FootViewhHolder) viewHolder;
            if (dates.isEmpty()) {
                footViewhHolder.text_foot.setVisibility(View.GONE);
            }
            footViewhHolder.text_foot.setText("清除所有历史记录");
            footViewhHolder.text_foot.setPadding(0, SizeUtils.dp2px(15), 0, 0);
            footViewhHolder.text_foot.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < dates.size(); i++) {
                        SQLite.delete(UserModel.class).where(UserModel_Table.name.eq(dates.get(i).getName())).execute();
                    }
                    dates.clear();
                    notifyDataSetChanged();
                }
            });
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
        return dates.size() + 1;
    }

    public class HistoryMessageViewHolder extends MyBaseViewHolder {
        private TextView tv_historymessage;
        private ImageView iv_imgdel;
        private RelativeLayout relativeLayout;

        public HistoryMessageViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }


        private void initView(View itemView) {
            relativeLayout = (RelativeLayout) itemView.findViewById(R.id.rl_userinfo);
            tv_historymessage = (TextView) itemView.findViewById(R.id.tv_historymessage);
            iv_imgdel = (ImageView) itemView.findViewById(R.id.iv_imagedel);

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

}
