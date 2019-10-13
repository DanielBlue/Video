package util;

import android.content.Context;
import android.widget.ImageView;

import com.am.shortVideo.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;

/**
 * Created by maoqi on 2019/10/13.
 */
public class GlideUtils {
    public static void displayImage(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .skipMemoryCache(false)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .centerCrop()
                .into(view);
    }

    public static void showHeader(Context context, String url, ImageView view) {
        Glide.with(context)
                .load(url)
                .dontAnimate()
                .placeholder(R.drawable.default_avatar)
                .error(R.drawable.default_avatar)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(view);

    }
}
