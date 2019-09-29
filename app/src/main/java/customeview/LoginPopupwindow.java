package customeview;

import android.content.Context;
import android.content.Intent;
import android.os.Looper;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.am.shortVideo.R;
import com.am.shortVideo.activity.LoginActivity;

/**
 * Created by 李杰 on 2019/9/7.
 */

public class LoginPopupwindow extends PopupWindow implements View.OnClickListener {
    private Context context;
    private Button bt_logincancel;
    private Button bt_loginconfirm;

    public LoginPopupwindow(Context context) {
        super(context);
        this.context=context;
        initView();
    }
    private void initView() {
        setWidth(RelativeLayout.LayoutParams.WRAP_CONTENT);
        setHeight(RelativeLayout.LayoutParams.WRAP_CONTENT);
        final View view= LayoutInflater.from(context).inflate(R.layout.login_popupwindow,null,false);
        setContentView(view);
//        setAnimationStyle(R.style.Mypopupwindow);
        setOutsideTouchable(true);
        setFocusable(true);

        new Thread(new Runnable() {
            @Override
            public void run() {
                Looper.prepare();//增加部分
                showAtLocation(view, Gravity.CENTER,0,0);
                Looper.loop();//增加部分
            }
        }).start();
        bt_logincancel=(Button)view.findViewById(R.id.bt_logincancel);
        bt_loginconfirm=(Button)view.findViewById(R.id.bt_loginconfirm);
        bt_logincancel.setOnClickListener(this);
        bt_loginconfirm.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.bt_logincancel:
                    dismiss();
                break;
            case R.id.bt_loginconfirm:
                Intent intent=new Intent(context, LoginActivity.class);
                context.startActivity(intent);
                dismiss();
                break;
        }
    }
}
