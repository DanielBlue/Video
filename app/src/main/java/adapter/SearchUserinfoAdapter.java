package adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.am.shortVideo.activity.OtherUserInfoActivity;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import application.MyApplication;
import base.MyAllBaseAdapter;
import base.MyBaseViewHolder;
import bean.OtherUserInfo;
import bean.SearchUserinfo;
import de.hdodenhof.circleimageview.CircleImageView;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.FootViewHolder;
import util.GlideUtils;
import util.HttpUri;
import util.PinYinUtil;

/**
 * Created by 李杰 on 2019/9/9.
 */

public class SearchUserinfoAdapter extends MyAllBaseAdapter<SearchUserinfo.DataBean.PageListBean, MyBaseViewHolder> {
    private Context context;
    private List<SearchUserinfo.DataBean.PageListBean> dates;
    private PinYinUtil pinYinUtil;

    public SearchUserinfoAdapter(List<SearchUserinfo.DataBean.PageListBean> dates, Context context, PinYinUtil pinYinUtil) {
        super(dates, context);
        this.context = context;
        this.dates = dates;
        this.pinYinUtil = pinYinUtil;
    }

    @Override
    public MyBaseViewHolder onAdapterCreaetViewHolder(ViewGroup viewGroup, int viewType) {
        if (viewType == NORMAL_VIEW) {
            View view = LayoutInflater.from(context).inflate(R.layout.search_item, viewGroup, false);
            return new SearchUserinfoViewHolder(view);
        } else {
            View view = LayoutInflater.from(context).inflate(R.layout.foot_item, viewGroup, false);
            return new FootViewHolder(view);
        }
    }

    @Override
    public void getBindViewHolder(MyBaseViewHolder viewHolder, final int position) {
        if (getItemViewType(position) == NORMAL_VIEW) {
            final SearchUserinfoViewHolder holder = (SearchUserinfoViewHolder) viewHolder;
            char letter = getfontLetter(position);
            if (isFirstShowLetter(letter) == position) {
                holder.tv_tag.setVisibility(View.VISIBLE);
                holder.tv_tag.setText("" + letter);
            } else {
                holder.tv_tag.setVisibility(View.GONE);
            }
            GlideUtils.showHeader(context, HttpUri.BASE_DOMAIN + dates.get(position).getAvatar(), holder.attentionperson_picture);
            holder.attentionperson_account.setText(dates.get(position).getUid());
            holder.attentionperson_name.setText(dates.get(position).getNickName());
            holder.attentionperson_picture.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    HashMap<String, String> map = new HashMap<>();
                    map.put("uid", dates.get(position).getUid());
                    OktHttpUtil.getInstance().sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_USEROTHERINFO,
                            MyApplication.getInstance().getMaps(), map, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {

                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String userinfoResult = response.body().string();
                                    Gson gson = new Gson();
                                    OtherUserInfo userinfo = gson.fromJson(userinfoResult, OtherUserInfo.class);
                                    OtherUserInfoActivity.start(context, dates.get(position).getUid(), userinfo.getData().getUserInfo().isFollowState());
                                }
                            });
                }
            });
        } else if (getItemViewType(position) == FOOT_VIEW) {
            FootViewHolder holder1 = (FootViewHolder) viewHolder;
            holder1.text_foot.setVisibility(View.GONE);
        }

    }

    public class SearchUserinfoViewHolder extends MyBaseViewHolder {
        private TextView tv_tag;
        private CircleImageView attentionperson_picture;
        private TextView attentionperson_name;
        private TextView attentionperson_account;

        public SearchUserinfoViewHolder(View itemView) {
            super(itemView);
            initView(itemView);
        }

        private void initView(View itemView) {
            tv_tag = (TextView) itemView.findViewById(R.id.tv_tag);
            attentionperson_picture = (CircleImageView) itemView.findViewById(R.id.attentionperson_picture);
            attentionperson_name = (TextView) itemView.findViewById(R.id.tv_attenionperson_name);
            attentionperson_account = (TextView) itemView.findViewById(R.id.tv_attention_count);
        }


    }

    public char getfontLetter(int position) {
        char letter = pinYinUtil.getPinYin(dates.get(position).getNickName()).charAt(0);
        return letter;
    }

    public int isFirstShowLetter(char position) {
        for (int i = 0; i < getItemCount() - 1; i++) {
            String letter = pinYinUtil.getPinYin(dates.get(i).getNickName());
            if (letter.charAt(0) == (position)) {
                return i;
            }
        }
        return 0;
    }
}
