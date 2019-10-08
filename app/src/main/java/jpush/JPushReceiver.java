package jpush;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import com.am.shortVideo.activity.CommentAndActivity;
import com.am.shortVideo.activity.FansActivity;
import com.am.shortVideo.activity.LikeListActivity;

import org.json.JSONObject;

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
                JSONObject jsonObject = new JSONObject(extraJson);
                int pushType = jsonObject.getInt("push_type");
                if (pushType==1){
                    Intent intent1 = new Intent(context, LikeListActivity.class);
                    intent1.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent1);
                }else if (pushType==2){
                    Intent intent2 = new Intent(context, FansActivity.class);
                    intent2.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent2);
                }else if (pushType==3){
                    Intent intent3 = new Intent(context, CommentAndActivity.class);
                    intent3.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(intent3);
                }
            }
        } catch (Exception e) {
            Log.d(TAG, "onReceive:Exception ");
        }
    }
}
