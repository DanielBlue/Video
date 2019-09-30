package adapter;

import android.support.annotation.Nullable;

import com.am.shortVideo.R;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.shuyu.gsyvideoplayer.builder.GSYVideoOptionBuilder;
import com.shuyu.gsyvideoplayer.listener.GSYSampleCallBack;
import com.shuyu.gsyvideoplayer.listener.GSYVideoProgressListener;

import java.util.List;

import customeview.ShortVideoPlayer;

/**
 * Created by 李杰 on 2019/9/17.
 */

public class CaogaoAdapter extends BaseQuickAdapter<String, BaseViewHolder> {

    public CaogaoAdapter(@Nullable List<String> data) {
        super(R.layout.caogao_item,data);
    }

    @Override
    protected void convert(BaseViewHolder helper, String item) {

        ShortVideoPlayer videoPlayer = helper.getView(R.id.video_player);
        GSYVideoOptionBuilder videoBuilder = new GSYVideoOptionBuilder();
        videoBuilder
                .setRotateViewAuto(false)
                .setIsTouchWiget(false)
                .setLooping(true)
                .setAutoFullWithSize(false)
                .setIsTouchWiget(false)
                .setIsTouchWigetFull(false)
                .setShowFullAnimation(false)
                .setUrl(item)
                .setCacheWithPlay(true)
                .setGSYVideoProgressListener(new GSYVideoProgressListener() {
                    @Override
                    public void onProgress(int progress, int secProgress, int currentPosition, int duration) {

                    }
                })
                .setVideoAllCallBack(new GSYSampleCallBack() {

                    @Override
                    public void onStartPrepared(String url, Object... objects) {
                        super.onStartPrepared(url, objects);
                    }

                    @Override
                    public void onPrepared(String url, Object... objects) {
                        super.onPrepared(url, objects);
                    }

                    @Override
                    public void onClickStop(String url, Object... objects) {

                    }

                    @Override
                    public void onClickResume(String url, Object... objects) {

                    }

                    @Override
                    public void onPlayError(String url, Object... objects) {
                        super.onPlayError(url, objects);
                    }
                })
                .build(videoPlayer);
    }
}
