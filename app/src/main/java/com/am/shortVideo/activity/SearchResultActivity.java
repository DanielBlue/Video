package com.am.shortVideo.activity;

import android.app.Activity;
import android.app.FragmentManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;

import adapter.AttentionSearchPersonAdapter;
import adapter.HistoryMessageAdapter;
import adapter.SearchUserinfoAdapter;
import application.MyApplication;
import base.BaseActivity;
import bean.SearchUserinfo;
import customeview.LoginPopupwindow;
import customeview.SliderView;
import db.UserModel;
import db.UserModel_Table;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;
import util.PinYinUtil;

/**
 * Created by JC on 2019/8/21.
 */

public class SearchResultActivity extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "AttentionPersonFragment";
    private int curSelect = 0;
    private AttentionSearchPersonAdapter attenPersonAdapter;
    private TextView tv_findUser;
    private TextView tv_findvideo;
    private Button bt_findinfo;
    private ImageView iv_back;
    private SearchUserinfoFragment searchUserinfoFragment;
    private SearchUserVideoFragment searchUserVideoFragment;
    private android.support.v4.app.FragmentManager fragmentManger;
    private View attention_line1;
    private View attention_line2;

    @Override
    protected int getLayout() {
        return R.layout.serachattentionperson_layout;
    }

    @Override
    protected void initEventAndData() {
        fragmentManger = getSupportFragmentManager();
        initView();
        switchFragment(curSelect);
    }

    private void initView() {
        tv_findUser = (TextView) findViewById(R.id.tv_findUser);
        tv_findvideo = (TextView) findViewById(R.id.tv_findvideo);
        iv_back = (ImageView) findViewById(R.id.iv_back);
        tv_findUser.setOnClickListener(this);
        tv_findvideo.setOnClickListener(this);
        iv_back.setOnClickListener(this);
        attention_line1 = (View) findViewById(R.id.attention_line1);
        attention_line2 = (View) findViewById(R.id.attention_line2);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_findUser:
                if (curSelect != 0) {
                    curSelect = 0;
                    switchFragment(curSelect);
                }
                break;
            case R.id.tv_findvideo:
                if (curSelect != 1) {
                    curSelect = 1;
                    switchFragment(curSelect);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
        }

    }

    public void switchFragment(int type) {
        FragmentTransaction fragmentTransaction = fragmentManger.beginTransaction();
        hideAllFragment(fragmentTransaction);
        if (type == 0) {
            if (searchUserinfoFragment == null) {
                searchUserinfoFragment = new SearchUserinfoFragment();
                fragmentTransaction.add(R.id.search_fragment, searchUserinfoFragment);
            } else {
                fragmentTransaction.show(searchUserinfoFragment);
            }
            attention_line1.setVisibility(View.VISIBLE);
            attention_line2.setVisibility(View.GONE);
        } else if (type == 1) {
            if (searchUserVideoFragment == null) {
                searchUserVideoFragment = new SearchUserVideoFragment();
                fragmentTransaction.add(R.id.search_fragment, searchUserVideoFragment);
            } else {
                fragmentTransaction.show(searchUserVideoFragment);
            }
            attention_line2.setVisibility(View.VISIBLE);
            attention_line1.setVisibility(View.GONE);
        }
        fragmentTransaction.commit();
    }

    public void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (searchUserinfoFragment != null) {
            fragmentTransaction.hide(searchUserinfoFragment);
        }
        if (searchUserVideoFragment != null) {
            fragmentTransaction.hide(searchUserVideoFragment);
        }
    }
}
