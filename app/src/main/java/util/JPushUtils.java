package util;

import android.content.Context;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by maoqi on 2018/8/9.
 */
public class JPushUtils {
    public static final int sSetAliasSequence = 100;
    public static final int sDeleteAliasSequence = 101;

    /**
     * 初始化极光推送
     */
    public static void initJPush(Context context, boolean isDebug) {
        // 初始化 JPush
        JPushInterface.init(context);
        // 设置开启日志,发布时请关闭日志
        JPushInterface.setDebugMode(isDebug);
        String registrationID = JPushInterface.getRegistrationID(context);
        Log.d("JPushUtils", "registrationID: " + registrationID);
    }

    public static void setAlias(Context context, String alias) {
        JPushInterface.setAlias(context, sSetAliasSequence, alias);
        Log.d("JPushUtils", "绑定别名: " + alias);
    }
}
