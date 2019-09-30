package customeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.am.shortVideo.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import util.LogUtils;

/**
 * Created by maoqi on 2019/9/30.
 */
public class ShortVideoPlayer extends StandardGSYVideoPlayer {
    private PlayListener mPlayListener;

    public ShortVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
        init();
    }

    public ShortVideoPlayer(Context context) {
        super(context);
        init();
    }

    public ShortVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    @Override
    protected void setStateAndUi(int state) {
        super.setStateAndUi(state);
        switch (state) {
            case CURRENT_STATE_NORMAL:
                //正常
                LogUtils.d(this, "正常");
                break;
            case CURRENT_STATE_PREPAREING:
                //准备中
                LogUtils.d(this, "准备中");
                break;
            case CURRENT_STATE_PLAYING_BUFFERING_START:
                //开始缓冲
                LogUtils.d(this, "开始缓冲");
                if (mPlayListener != null) {
                    mPlayListener.onBuffering();
                }
                break;
            case CURRENT_STATE_PLAYING:
                //播放中
                LogUtils.d(this, "播放中");
                if (mPlayListener != null) {
                    mPlayListener.onPlay();
                }
                findViewById(R.id.iv_pause).setVisibility(View.INVISIBLE);
                break;
            case CURRENT_STATE_PAUSE:
                //暂停
                LogUtils.d(this, "暂停");
                if (mPlayListener != null) {
                    mPlayListener.onPause();
                }
                findViewById(R.id.iv_pause).setVisibility(View.VISIBLE);
                break;
            case CURRENT_STATE_ERROR:
                //错误状态
                LogUtils.d(this, "错误状态");
                break;
            case CURRENT_STATE_AUTO_COMPLETE:
                //自动播放结束
                LogUtils.d(this, "自动播放结束");
                break;
        }
    }

    @Override
    public int getLayoutId() {
        return R.layout.empty_control_video;
    }

    private void init() {
        gestureDetector = new GestureDetector(mContext, new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onDoubleTap(MotionEvent e) {
                return super.onDoubleTap(e);
            }

            @Override
            public boolean onSingleTapConfirmed(MotionEvent e) {
                if (!mChangePosition && !mChangeVolume && !mBrightness) {
                    onClickUiToggle();
                }
                return super.onSingleTapConfirmed(e);
            }
        });
    }

    @Override
    protected void touchSurfaceMoveFullLogic(float absDeltaX, float absDeltaY) {
        super.touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
        //不给触摸快进，如果需要，屏蔽下方代码即可
        mChangePosition = false;

        //不给触摸音量，如果需要，屏蔽下方代码即可
        mChangeVolume = false;

        //不给触摸亮度，如果需要，屏蔽下方代码即可
        mBrightness = false;
    }

    @Override
    protected void touchDoubleUp() {
//        super.touchDoubleUp();
        //不需要双击暂停
    }

    @Override
    protected void onClickUiToggle() {
        super.touchDoubleUp();
    }

    public PlayListener getPlayListener() {
        return mPlayListener;
    }

    public void setPlayListener(PlayListener playListener) {
        mPlayListener = playListener;
    }

    public interface PlayListener {

        void onPlay();

        void onPause();

        void onBuffering();
    }

}
