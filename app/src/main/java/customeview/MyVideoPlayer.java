package customeview;

import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewParent;

import com.am.shortVideo.R;
import com.shuyu.gsyvideoplayer.video.StandardGSYVideoPlayer;

import util.LogUtils;


/**
 * Created by maoqi on 2019/7/15.
 */
public class MyVideoPlayer extends StandardGSYVideoPlayer {
    private PlayListener mPlayListener;

    public MyVideoPlayer(Context context, Boolean fullFlag) {
        super(context, fullFlag);
    }

    public MyVideoPlayer(Context context) {
        super(context);
    }

    public MyVideoPlayer(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public void setPlayListener(PlayListener playListener) {
        mPlayListener = playListener;
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
                break;
            case CURRENT_STATE_PAUSE:
                //暂停
                LogUtils.d(this, "暂停");
                if (mPlayListener != null) {
                    mPlayListener.onPause();
                }
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

    @Override
    protected void init(Context context) {
        super.init(context);
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

    protected GestureDetector mGestureDetector = new GestureDetector(getContext().getApplicationContext(), new GestureDetector.SimpleOnGestureListener() {

        @Override
        public boolean onSingleTapConfirmed(MotionEvent e) {
            if (mOnEventListener != null) {
                mOnEventListener.onSingleTapConfirmed();
            }
            return super.onSingleTapConfirmed(e);
        }


        @Override
        public void onLongPress(MotionEvent e) {
            if (mOnEventListener != null) {
                mOnEventListener.onLongPress();
            }
            super.onLongPress(e);
        }

        @Override
        public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
            if (mOnEventListener != null) {
                mOnEventListener.onScroll();
            }
            return super.onScroll(e1, e2, distanceX, distanceY);
        }
    });

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int id = v.getId();
        float x = event.getX();
        float y = event.getY();

        if (mIfCurrentIsFullscreen && mLockCurScreen && mNeedLockFull) {
            onClickUiToggle();
            startDismissControlViewTimer();
            return true;
        }

        if (id == R.id.fullscreen) {
            return false;
        }

        if (id == R.id.surface_container) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:

                    touchSurfaceDown(x, y);

                    break;
                case MotionEvent.ACTION_MOVE:
                    float deltaX = x - mDownX;
                    float deltaY = y - mDownY;
                    float absDeltaX = Math.abs(deltaX);
                    float absDeltaY = Math.abs(deltaY);

                    if ((mIfCurrentIsFullscreen && mIsTouchWigetFull)
                            || (mIsTouchWiget && !mIfCurrentIsFullscreen)) {
                        if (!mChangePosition && !mChangeVolume && !mBrightness) {
                            touchSurfaceMoveFullLogic(absDeltaX, absDeltaY);
                        }
                    }
                    touchSurfaceMove(deltaX, deltaY, y);

                    break;
                case MotionEvent.ACTION_UP:

                    startDismissControlViewTimer();

                    touchSurfaceUp();

                    startProgressTimer();

                    //不要和隐藏虚拟按键后，滑出虚拟按键冲突
                    if (mHideKey && mShowVKey) {
                        return true;
                    }
                    break;
            }
            mGestureDetector.onTouchEvent(event);
        } else if (id == R.id.progress) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    cancelDismissControlViewTimer();
                case MotionEvent.ACTION_MOVE:
                    cancelProgressTimer();
                    ViewParent vpdown = getParent();
                    while (vpdown != null) {
                        vpdown.requestDisallowInterceptTouchEvent(true);
                        vpdown = vpdown.getParent();
                    }
                    break;
                case MotionEvent.ACTION_UP:
                    startDismissControlViewTimer();
                    startProgressTimer();
                    ViewParent vpup = getParent();
                    while (vpup != null) {
                        vpup.requestDisallowInterceptTouchEvent(false);
                        vpup = vpup.getParent();
                    }
                    mBrightnessData = -1f;
                    break;
            }
        }

        return false;
    }

    public void click() {
        if (!mChangePosition && !mChangeVolume && !mBrightness) {
            onClickUiToggle();
        }
    }

    @Override
    protected void onClickUiToggle() {
        super.touchDoubleUp();
    }

    public interface PlayListener {

        void onPlay();

        void onPause();

        void onBuffering();
    }

    private OnEventListener mOnEventListener;

    public OnEventListener getOnEventListener() {
        return mOnEventListener;
    }

    public void setOnEventListener(OnEventListener onEventListener) {
        mOnEventListener = onEventListener;
    }

    public interface OnEventListener {

        void onLongPress();

        void onSingleTapConfirmed();

        void onScroll();
    }
}
