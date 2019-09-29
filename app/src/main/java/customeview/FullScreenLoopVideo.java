package customeview;

import android.content.Context;
import android.support.v7.widget.LinearLayoutManager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by JC on 2019/9/10.
 */

public class FullScreenLoopVideo extends JCVideoPlayerStandard {
    private FullScreenLoopVideo.VideoBackCallBack videoBackCallBack;
    private LinearLayout.LayoutParams layoutParams;

    public FullScreenLoopVideo(Context context) {
        super(context);
    }

    public FullScreenLoopVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        tinyBackImageView.setVisibility(GONE);
        layoutParams=(LinearLayout.LayoutParams)backButton.getLayoutParams();
        layoutParams.setMargins(10,50,0,0);
        backButton.setLayoutParams(layoutParams);
        loop=true;
    }

    @Override
    public void onCompletion() {
        super.onCompletion();
    }
    @Override
    public void onAutoCompletion() {
        super.onAutoCompletion();
    }

    @Override
    public void setProgressAndText() {
        super.setProgressAndText();
        currentTimeTextView.setVisibility(GONE);
        totalTimeTextView.setVisibility(GONE);
        fullscreenButton.setVisibility(GONE);
        //backButton.setVisibility(GONE);
        tinyBackImageView.setVisibility(GONE);

    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        if(v.getId()==fm.jiecao.jcvideoplayer_lib.R.id.back){
            videoBackCallBack.backfinish();
        }
    }
    public void setOnFininshLinstener(VideoBackCallBack videoBackCallBack){
        this.videoBackCallBack=videoBackCallBack;
    }
    public interface VideoBackCallBack{
        void backfinish();
    }
}
