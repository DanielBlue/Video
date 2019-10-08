package jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import cn.jpush.android.api.JPushInterface;

/**
 * Created by JC on 2019/9/10.
 */

public class JPushReceiver extends BroadcastReceiver {
    private static final String TAG = "JIGUANG";
    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d(TAG, "onReceive: ");
        try {
            Bundle bundle = intent.getExtras();
            if (JPushInterface.ACTION_REGISTRATION_ID.equals(intent.getAction())) {
                Log.d(TAG, "onReceive: ACTION_REGISTRATION_ID");
                //极光服务器分配的Registration Id，
                String regId = bundle.getString(JPushInterface.EXTRA_REGISTRATION_ID);
            } else if (JPushInterface.ACTION_MESSAGE_RECEIVED.equals(intent.getAction())) {
                //自定义消息
                Log.d(TAG, "onReceive: ACTION_MESSAGE_RECEIVED");
            } else if (JPushInterface.ACTION_NOTIFICATION_RECEIVED.equals(intent.getAction())) {
                //推送通知
                Log.d(TAG, "onReceive:ACTION_NOTIFICATION_RECEIVED ");
            } else if (JPushInterface.ACTION_NOTIFICATION_OPENED.equals(intent.getAction())) {
                Log.d(TAG, "onReceive: ACTION_NOTIFICATION_OPENED");
                //当用户点击通知时的操作,打开自定义的Activity
                String extraJson = bundle.getString(JPushInterface.EXTRA_EXTRA);

            }
        } catch (Exception e) {
            Log.d(TAG, "onReceive:Exception ");
        }
    }
}
