package com.am.shortVideo.activity;

import android.content.Context;
import android.content.Intent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.am.shortVideo.R;
import com.am.shortVideo.fragment.FindPassWordFragment;
import com.am.shortVideo.fragment.ResetPassWordFragment;

import base.BaseActivity;
import butterknife.BindView;


public class ForgetPassWordActivity extends BaseActivity {

    public static void start(Context context) {
        Intent starter = new Intent(context, ForgetPassWordActivity.class);
        context.startActivity(starter);
    }

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.bt_systemmessage)
    Button btSystemmessage;

    public void go2ResetPassword(String phone) {
        btSystemmessage.setText("重置密码");
        getSupportFragmentManager().beginTransaction().replace(R.id.fl_container, ResetPassWordFragment.newInstance(phone)).commit();
    }

    @Override
    protected int getLayout() {
        return R.layout.activity_forget_password;
    }

    @Override
    protected void initEventAndData() {
        ivBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        btSystemmessage.setText("找回密码");
        getSupportFragmentManager().beginTransaction().add(R.id.fl_container, FindPassWordFragment.newInstance()).commit();
    }
}
