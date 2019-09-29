package com.am.shortVideo.activity;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;

import base.BaseActivity;

/**
 * Created by JC on 2019/9/11.
 */

public class UpdateAppActivity extends BaseActivity implements View.OnClickListener {
    private TextView fans_title;
    private ImageView iv_back;
    private TextView tv_appversion;
    private Button bt_checkupdate;

    @Override
    protected int getLayout() {
        return R.layout.updateapp_layout;
    }

    @Override
    protected void initEventAndData() {
        initView();
    }

    private void initView() {
        fans_title = (TextView) findViewById(R.id.bt_systemmessage);
        tv_appversion = (TextView) findViewById(R.id.tv_appversion);
        bt_checkupdate = (Button) findViewById(R.id.bt_checkupdate);
        fans_title.setText("检查更新");
        tv_appversion.setText("V" + packageName(this));
        iv_back = (ImageView) findViewById(R.id.iv_back);
        bt_checkupdate.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_checkupdate:
                Toast.makeText(this, "当前版本已经是最新版本", Toast.LENGTH_SHORT).show();
                break;
            case R.id.iv_back:
                finish();
                break;
        }
    }

    public String packageName(Context context) {
        PackageManager manager = context.getPackageManager();
        String name = null;
        try {
            PackageInfo info = manager.getPackageInfo(context.getPackageName(), 0);
            name = info.versionName;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        return name;
    }
}
