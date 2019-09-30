package util;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AlertDialog;

import com.am.shortVideo.R;
import com.am.shortVideo.activity.LoginActivity;

/**
 * Created by maoqi on 2019/9/30.
 */
public class BaseUtils {
    public static AlertDialog getLoginDialog(final Context context) {
        return new AlertDialog.Builder(context, R.style.Theme_AppCompat_Light_Dialog_Alert)
                .setTitle("警告")
                .setMessage(R.string.tv_logincontent)
                .setNegativeButton(R.string.bt_eidtorcancel, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                })
                .setPositiveButton(R.string.bt_eidtorconfirm, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, LoginActivity.class);
                        context.startActivity(intent);
                    }
                })
                .create();
    }
}
