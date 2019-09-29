package com.am.shortVideo.activity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.am.shortVideo.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

public class UserAgreementActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.bt_systemmessage)
    Button btSystemmessage;
    @BindView(R.id.tv_user_agreement)
    TextView tvUserAgreement;

    @Override
    protected int getLayout() {
        return R.layout.activity_user_agreement;
    }

    @Override
    protected void initEventAndData() {
        btSystemmessage.setText(getString(R.string.tv2_info));
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
