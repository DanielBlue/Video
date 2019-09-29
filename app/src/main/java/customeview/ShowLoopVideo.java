package customeview;

import android.content.Context;
import android.graphics.Point;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.TextureView;
import android.view.View;
import android.widget.Toast;

import fm.jiecao.jcvideoplayer_lib.JCMediaManager;
import fm.jiecao.jcvideoplayer_lib.JCVideoPlayerStandard;

/**
 * Created by JC on 2019/8/6.
 */

public class ShowLoopVideo extends JCVideoPlayerStandard {
    public ShowLoopVideo(Context context) {
        super(context);
    }

    public ShowLoopVideo(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public void init(Context context) {
        super.init(context);
        loop = true;
        tinyBackImageView.setVisibility(GONE);
        backButton.setVisibility(GONE);
    }

//    @Override
//    public void onCompletion() {
//        super.onCompletion();
//    }
//
//    @Override
//    public void onAutoCompletion() {
//        super.onAutoCompletion();
//    }

//    @Override
//    public void onVideoSizeChanged() {
//        super.onVideoSizeChanged();
//        if (JCMediaManager.textureView != null) {
//            JCMediaManager.textureView.setVideoSize(new Point(textureViewContainer.getWidth(), textureViewContainer.getHeight()));//视频大小与控件大小一致
//        }
//    }

//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case fm.jiecao.jcvideoplayer_lib.R.id.start:
//                if(JCMediaManager.instance().mediaPlayer.isPlaying()){
//                    Log.d(TAG, "onClick001:-->pause ");
//                    this.onEvent(3);
//                    JCMediaManager.instance().mediaPlayer.pause();
//                    setUiWitStateAndScreen(5);
//                    progressBar.setVisibility(GONE);
//                }else{
//                    this.onEvent(4);
//                    Log.d(TAG, "onClick001:-->start ");
//                    JCMediaManager.instance().mediaPlayer.start();
//                    setUiWitStateAndScreen(2);
//                    progressBar.setVisibility(GONE);
//                }
//
//                break;
//            default:
//                if(JCMediaManager.instance().mediaPlayer.isPlaying()){
//                    Log.d(TAG, "onClick001:-->pause ");
//                    this.onEvent(3);
//                    JCMediaManager.instance().mediaPlayer.pause();
//                    setUiWitStateAndScreen(5);
//                    progressBar.setVisibility(GONE);
//                }else{
//                    this.onEvent(4);
//                    Log.d(TAG, "onClick001:-->start ");
//                    JCMediaManager.instance().mediaPlayer.start();
//                    setUiWitStateAndScreen(2);
//                    progressBar.setVisibility(GONE);
//                }
//        }
//
//    }

    @Override
    public void setProgressAndText() {
        super.setProgressAndText();
        currentTimeTextView.setVisibility(GONE);
        totalTimeTextView.setVisibility(GONE);
        fullscreenButton.setVisibility(GONE);
        backButton.setVisibility(GONE);
        tinyBackImageView.setVisibility(GONE);
    }

}
