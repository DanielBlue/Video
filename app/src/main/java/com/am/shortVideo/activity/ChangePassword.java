package com.am.shortVideo.activity;

import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.google.gson.Gson;
import com.syd.oden.circleprogressdialog.core.CircleProgressDialog;

import java.io.IOException;
import java.util.HashMap;

import application.MyApplication;
import bean.ChangUserpassword;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.BaseUtils;
import util.HttpUri;

/**
 * Created by JC on 2019/8/30.
 */

public class ChangePassword extends AppCompatActivity implements View.OnClickListener {
    private static final String TAG = "ChangePassword";
    private Button bt_confirmchange;
    private EditText ed_changpassword;
    private EditText ed_confirm_changepassword;
    private TextView fans_title;
    private ImageView iv_back;
    private OktHttpUtil okHttpUtil;
    private Handler handler=new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
        switch (msg.what){
            case 1:
                ChangUserpassword changeuserpassword=( ChangUserpassword)msg.obj;
                if(changeuserpassword.getCode()==0){
                    cricleprogressDialog.dismiss();
                    Toast.makeText(ChangePassword.this,"修改密码成功",Toast.LENGTH_SHORT).show();
                    finish();
                }else  if(changeuserpassword.getCode()==1005){
                    cricleprogressDialog.dismiss();
                    BaseUtils.getLoginDialog(ChangePassword.this).show();
                }else if(changeuserpassword.getCode()==1011){
                    cricleprogressDialog.dismiss();
                    Toast.makeText(ChangePassword.this,"用户不存在",Toast.LENGTH_SHORT).show();
                }else if(changeuserpassword.getCode()==1012){
                    cricleprogressDialog.dismiss();
                    Toast.makeText(ChangePassword.this,"旧密码输入错误",Toast.LENGTH_SHORT).show();
                }
                break;
        }
        }
    };
    private Callback changepasswordCallback=new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    cricleprogressDialog.dismiss();
                }
            });
        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String changepasswordresult=response.body().string();
            Log.d(TAG, "onResponse: \n"+changepasswordresult);
            Gson gson=new Gson();
            ChangUserpassword changeUserpassword=gson.fromJson(changepasswordresult,ChangUserpassword.class);
            Message message=new Message();
            message.what=1;
            message.obj=changeUserpassword;
            handler.sendMessage(message);
        }
    };
    private CircleProgressDialog cricleprogressDialog;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_layout);
        okHttpUtil= OktHttpUtil.getInstance();
        cricleprogressDialog=new CircleProgressDialog(this);
        initView();
        setOnClickListener();
    }

    private void initData(HashMap<String,String> maps){
        okHttpUtil.setPostRequest(HttpUri.BASE_URL+HttpUri.PersonInfo.REQUEST_HEADER_CHANGEPASSWORD
        ,((MyApplication)getApplicationContext()).getMaps(),maps,changepasswordCallback);
        cricleprogressDialog.showDialog();
    }
    private void setOnClickListener() {
        bt_confirmchange.setOnClickListener(this);
        iv_back.setOnClickListener(this);
    }

    private void initView() {
        fans_title=(TextView)findViewById(R.id.bt_systemmessage);
        fans_title.setText("修改密码");
        iv_back=(ImageView)findViewById(R.id.iv_back);
        bt_confirmchange=(Button)findViewById(R.id.bt_changpassword);
        ed_changpassword=(EditText)findViewById(R.id.et_changepassword);
        ed_confirm_changepassword=(EditText)findViewById(R.id.et_confirm_changepassword);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.bt_changpassword:
                if(!checkisEmpty()){
                    HashMap<String,String> maps=new HashMap<>();
                    maps.put("oldPwd",ed_changpassword.getText().toString().toString().trim());
                    maps.put("newPwd",ed_confirm_changepassword.getText().toString().toString().trim());
                    initData(maps);
                }
                break;
            case R.id.iv_back:
                finish();
                break;
            default:
        }
    }
        public boolean checkisEmpty(){
        if(ed_changpassword.getText().toString().trim().isEmpty()){
                Toast.makeText(this,"旧密码不能为空",Toast.LENGTH_SHORT).show();
            return true;
        }
        if(ed_confirm_changepassword.getText().toString().trim().isEmpty()){
            Toast.makeText(this,"新密码不能为空",Toast.LENGTH_SHORT).show();
            return true;
        }
        return false;
    }

    //打开软键盘
    public  void openKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext
                .getSystemService(INPUT_METHOD_SERVICE);
        imm.showSoftInput(mEditText, InputMethodManager.RESULT_SHOWN);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED,
                InputMethodManager.HIDE_IMPLICIT_ONLY);

    }


    //关闭软键盘
    public static void closeKeybord(EditText mEditText, Context mContext) {
        InputMethodManager imm = (InputMethodManager) mContext.getSystemService(INPUT_METHOD_SERVICE);
        imm.hideSoftInputFromWindow(mEditText.getWindowToken(), 0);
    }
}
