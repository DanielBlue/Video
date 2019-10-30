package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.tiktokdemo.lky.tiktokdemo.Constant;

import java.io.File;
import java.util.List;

import base.MyBaseAdapter;
import base.MyBaseViewHolder;
import bean.SelectMusicBean;
import util.FootViewHolder;
import util.FormatTime;

/**
 * Created by 李杰 on 2019/9/13.
 */

public class SelectMusicAdapter extends MyBaseAdapter<SelectMusicBean.DataBean.IndexListBean, MyBaseViewHolder> {
    private Context context;
    private List<SelectMusicBean.DataBean.IndexListBean> datas;
    private SelectBgmStatus selectBgmStatus;

    public SelectMusicAdapter(List<SelectMusicBean.DataBean.IndexListBean> datas, Context context) {
        super(datas, context);
        this.context = context;
        this.datas = datas;
    }

    @Override
    public MyBaseViewHolder onCreateHolder(ViewGroup parent, int position) {
        if (position == NORMAL_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.selectmusic_item, parent, false);
            return new SelectMusicViewHolder(view);
        } else if (position == FOOT_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.foot_item, parent, false);
            return new FootViewHolder(view);
        }
        return null;
    }

    @Override
    public void onHolder(MyBaseViewHolder holder, final int position) {
        if (getItemViewType(position) == NORMAL_VIEW) {
            SelectMusicViewHolder selectMusicViewHolder = (SelectMusicViewHolder) holder;
            selectMusicViewHolder.tv_musicName.setText(datas.get(position).getName());
            selectMusicViewHolder.tv_musicDuration.setText(FormatTime.formatDuration(datas.get(position).getDuration()));
            selectMusicViewHolder.rl_selectmusicitem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    File file = new File(Constant.DOWNBGM);
                    if (!file.exists()) {
                        file.mkdirs();
                    }
                    File musicFile = new File(Constant.DOWNBGM + File.separator + String.format(datas.get(position).getName(), "utf-8") + ".mp3");
                    if (musicFile.exists()) {
                        selectBgmStatus.selectBgmExits(true, datas.get(position), datas.get(position).getId());
                    } else {
                        selectBgmStatus.selectBgmExits(false, datas.get(position), datas.get(position).getId());
                    }
                }
            });
        } else if (getItemViewType(position) == FOOT_VIEW) {
            FootViewHolder footViewHolder = (FootViewHolder) holder;
            footViewHolder.text_foot.setVisibility(View.GONE);
        }
    }

    public class SelectMusicViewHolder extends MyBaseViewHolder {
        private TextView tv_musicName;
        private TextView tv_musicDuration;
        private RelativeLayout rl_selectmusicitem;

        public SelectMusicViewHolder(View itemView) {
            super(itemView);
            rl_selectmusicitem = (RelativeLayout) itemView.findViewById(R.id.rl_selectitem);
            tv_musicName = (TextView) itemView.findViewById(R.id.music_name);
            tv_musicDuration = (TextView) itemView.findViewById(R.id.music_duration);
        }
    }

    public void setOnSelectBgmExitsLinstener(SelectBgmStatus selectBgmStatus) {
        this.selectBgmStatus = selectBgmStatus;
    }

    public interface SelectBgmStatus {
        void selectBgmExits(boolean isSlect, SelectMusicBean.DataBean.IndexListBean value
                , String audioId);
    }
}
