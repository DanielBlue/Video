package com.am.shortVideo.activity;

import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;

import com.am.shortVideo.R;

import base.BaseActivity;
import butterknife.BindView;
import butterknife.OnClick;

public class UserAgreementActivity extends BaseActivity {
    @BindView(R.id.iv_back)
    ImageView ivBack;
    @BindView(R.id.bt_systemmessage)
    Button btSystemmessage;
    @BindView(R.id.wv_web)
    WebView wvWeb;

    @Override
    protected int getLayout() {
        return R.layout.activity_user_agreement;
    }

    @Override
    protected void initEventAndData() {
        btSystemmessage.setText(getString(R.string.tv2_info));

        wvWeb.loadUrl("file:///android_asset/user_agreement.html");
    }

    @OnClick(R.id.iv_back)
    public void onViewClicked() {
        finish();
    }
}
