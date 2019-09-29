package util;

import android.graphics.Rect;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by 李杰 on 2019/9/18.
 */

public class MeSpaceItemDecoration extends  RecyclerView.ItemDecoration {
    private int space;
    public MeSpaceItemDecoration(int space) {
        this.space=space;
    }

    @Override
    public void getItemOffsets(Rect outRect, View view, RecyclerView parent, RecyclerView.State state) {
        super.getItemOffsets(outRect, view, parent, state);
        outRect.top=space;
        if(parent.getChildLayoutPosition(view)%3==0){
            outRect.left=0;
        }else{
            outRect.left=space;
        }
    }
}
