package com.am.shortVideo.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.am.shortVideo.R;
import com.raizlabs.android.dbflow.sql.language.SQLite;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.List;

import adapter.HistoryMessageAdapter;
import base.BaseActivity;
import bean.MessageSearchinfo;
import db.UserModel;
import db.UserModel_Table;
import util.RecycleViewDivider;

/**
 * Created by 李杰 on 2019/9/9.
 */

public class SearchActivity extends BaseActivity implements View.OnClickListener {
    private ImageView iv_back;
    private TextView like_title;
    private List<UserModel> dbDatas = new ArrayList<>();
    private static final String TAG = "SearchActivity";
    private RecyclerView historymessage_Recycle;
    private HistoryMessageAdapter historyMessageAdatper;
    private EditText ed_edittext;

    @Override
    protected int getLayout() {
        return R.layout.search_userinfo_layout;
    }

    @Override
    protected void initEventAndData() {
        Log.d(TAG, "onCreate: \n" + dbDatas.toString());
        initView();
    }

    @Override
    protected void onResume() {
        super.onResume();
        dbDatas = SQLite.select().from(UserModel.class).orderBy(UserModel_Table.id, false)
                .queryList();
        if (!dbDatas.isEmpty()) {
            historyMessageAdatper = new HistoryMessageAdapter(dbDatas, this);
            historymessage_Recycle.setAdapter(historyMessageAdatper);
        }
    }

    private void initView() {
        like_title = (TextView) findViewById(R.id.bt_systemmessage);
        like_title.setText("搜索");
        iv_back = (ImageView) findViewById(R.id.iv_back);
        ed_edittext = (EditText) findViewById(R.id.attention_edittext);
        ed_edittext.setCursorVisible(false);
        iv_back.setOnClickListener(this);
        historymessage_Recycle = (RecyclerView) findViewById(R.id.historymessage_recycleview);
        LinearLayoutManager layoutManger = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        historymessage_Recycle.setLayoutManager(layoutManger);
        historymessage_Recycle.addItemDecoration(new RecycleViewDivider(this, LinearLayout.VERTICAL, 2, 0, 0, getResources().getColor(R.color.black)));
        ed_edittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ed_edittext.setCursorVisible(true);
            }
        });
        ed_edittext.setOnKeyListener(new View.OnKeyListener() {

            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                //这里注意要作判断处理，ActionDown、ActionUp都会回调到这里，不作处理的话就会调用两次
                if (KeyEvent.KEYCODE_ENTER == keyCode && KeyEvent.ACTION_DOWN == event.getAction()) {
                    //处理事件
                    if (!ed_edittext.getText().toString().trim().isEmpty()) {
                        if (dbDatas.size() < 10) {
                            String searchName = ed_edittext.getText().toString().trim();
                            UserModel userModel = new UserModel();
                            userModel.setName(searchName);
                            userModel.save();

                        } else {
                            SQLite.delete(UserModel.class).where(UserModel_Table.name.eq(dbDatas.get(9).getName())).execute();
                            String searchName = ed_edittext.getText().toString().trim();
                            UserModel userModel = new UserModel();
                            userModel.setName(searchName);
                            userModel.save();
                        }
                        Intent intent = new Intent(SearchActivity.this, SearchResultActivity.class);
                        startActivity(intent);
                        EventBus.getDefault().postSticky(MessageSearchinfo.getInstance(ed_edittext.getText().toString().trim()));
                    }
                    return true;
                }
                return false;
            }
        });

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.iv_back:
                finish();
                break;

        }
    }
}
