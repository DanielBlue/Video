package adapter;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.ImageView;

import com.am.shortVideo.R;
import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;

import java.util.List;

import bean.IndexListBean;
import util.HttpUri;

/**
 * Created by 李杰 on 2019/9/7.
 */

public class MeVideoAdapter extends BaseQuickAdapter<IndexListBean, BaseViewHolder> {

    public MeVideoAdapter(@Nullable List<IndexListBean> data) {
        super(R.layout.userpublishvideo_item, data);
    }

    @Override
    protected void convert(BaseViewHolder helper, IndexListBean item) {
        Log.d(TAG, "onBindViewHolder:0 ");
        Glide.with(mContext).load(HttpUri.BASE_DOMAIN + item.getCoverPath()).into((ImageView) helper.getView(R.id.iv_user_publishvideo));
        helper.setText(R.id.tv_user_likecount, String.valueOf(item.getLikeCounts()))
                .setVisible(R.id.btn_del, true)
                .addOnClickListener(R.id.btn_del);
    }
}
