package adapter;

import android.support.annotation.Nullable;
import android.widget.ImageView;

import com.am.shortVideo.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.AttentionPerson;
import util.GlideUtils;
import util.HttpUri;
import util.PinYinUtil;

/**
 * Created by maoqi on 2019/10/9.
 */
public class AtPersonListAdapter extends BaseQuickAdapter<AttentionPerson.DataBean.PageListBean, BaseViewHolder> {
    public AtPersonListAdapter(@Nullable List<AttentionPerson.DataBean.PageListBean> data) {
        super(R.layout.attentionperson_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, AttentionPerson.DataBean.PageListBean item) {
        char letter = getfontLetter(item);
        if (isFirstShowLetter(letter) == helper.getAdapterPosition()) {
            helper.setVisible(R.id.tv_tag,true)
                    .setText(R.id.tv_tag,String.valueOf(letter));
        } else {
            helper.setVisible(R.id.tv_tag,false);
        }
        GlideUtils.showHeader(mContext,HttpUri.BASE_DOMAIN + item.getAvatar(),(ImageView) helper.getView(R.id.attentionperson_picture));
        helper.setText(R.id.tv_attention_count,item.getUid())
                .setText(R.id.tv_attenionperson_name,item.getNickName())
                .setVisible(R.id.bt_attentionperson,false);
    }

    private char getfontLetter(AttentionPerson.DataBean.PageListBean item) {
        char letter = new PinYinUtil().getPinYin(item.getNickName()).charAt(0);
        return letter;
    }

    private int isFirstShowLetter(char c) {
        for (int i = 0; i < getItemCount() - 1; i++) {
            String letter = new PinYinUtil().getPinYin(getData().get(i).getNickName());
            if (letter.charAt(0) == (c)) {
                return i;
            }
        }
        return 0;
    }
}
