package jpush;

import android.content.Context;
import android.util.Log;

import application.MyApplication;
import cn.jpush.android.api.JPushMessage;
import cn.jpush.android.service.JPushMessageReceiver;
import util.JPushUtils;

/**
 * Created by maoqi on 2019/9/4.
 */
public class JPushLocalReceiver extends JPushMessageReceiver {

    @Override
    public void onAliasOperatorResult(final Context context, JPushMessage jPushMessage) {
        super.onAliasOperatorResult(context, jPushMessage);
        int errorCode = jPushMessage.getErrorCode();
        if (errorCode == 0) {
            Log.d("JPushLocalReceiver", "设置别名成功");
        } else if (errorCode == 6002 || errorCode == 6014) {
            Log.d("JPushLocalReceiver", "设置别名超时，1秒后重试");
            final String phone = MyApplication.getInstance().getUserInfo().phone;
            if (phone != null && !phone.isEmpty()) {
                MyApplication.mHandler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        JPushUtils.setAlias(context, phone);
                    }
                }, 1000);
            } else {
                Log.d("JPushLocalReceiver", "phone为空，设置别名失败");
            }
        } else {
            Log.d("JPushLocalReceiver", "设置别名失败,错误码：" + jPushMessage.getErrorCode());
        }

    }
}
