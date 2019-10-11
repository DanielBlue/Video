package com.am.shortVideo.activity;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.am.shortVideo.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

public class AboutUsActivity extends BaseActivity {

    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.bt_systemmessage)
    Button btSystemmessage;
    @BindView(R.id.rl_email)
    View rlEmail;
    @BindView(R.id.rl_phone_num)
    View rlPhoneNum;
    @Override
    protected int getLayout() {
        return R.layout.activity_aboout_us;
    }

    @Override
    protected void initEventAndData() {
        btSystemmessage.setText(getString(R.string.tv1_info));

        rlEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                copyText("jdcy@jand.vip");
                Toast.makeText(AboutUsActivity.this,"复制成功",Toast.LENGTH_SHORT).show();
            }
        });

        rlPhoneNum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent=new Intent();
                intent.setAction(Intent.ACTION_DIAL);
                intent.setData(Uri.parse("tel:400-0000-1941"));
                startActivity(intent);
            }
        });

    }

    public void copyText(CharSequence s) {
        ClipboardManager cm = (ClipboardManager) getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData mClipData = ClipData.newPlainText("copy", s);
        cm.setPrimaryClip(mClipData);
    }


    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
