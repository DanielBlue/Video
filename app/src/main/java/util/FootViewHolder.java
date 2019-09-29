package util;

import android.view.View;
import android.widget.TextView;

import com.am.shortVideo.R;

import base.MyBaseViewHolder;

/**
 * Created by JC on 2019/8/19.
 */

public class FootViewHolder extends MyBaseViewHolder {
   public TextView text_foot;
    public FootViewHolder(View itemView) {
        super(itemView);
        initView(itemView);
    }

    private void initView(View itemView) {
        text_foot=(TextView)itemView.findViewById(R.id.tv_footView);
    }
}
