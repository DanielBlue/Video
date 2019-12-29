package com.am.shortVideo.fragment;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.am.shortVideo.activity.ForgetPassWordActivity;
import com.google.gson.Gson;
import com.tiktokdemo.lky.tiktokdemo.utils.ToastTool;

import java.io.IOException;
import java.util.HashMap;

import application.MyApplication;
import bean.SmsBean;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;

/**
 * Created by maoqi on 2019/12/29.
 */
public class FindPassWordFragment extends Fragment {

    @BindView(R.id.input_phone)
    EditText inputPhone;
    @BindView(R.id.et_identify_code)
    EditText etIdentifyCode;
    @BindView(R.id.tv_code)
    TextView tvCode;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    Unbinder unbinder;

    public static FindPassWordFragment newInstance() {
        Bundle args = new Bundle();

        FindPassWordFragment fragment = new FindPassWordFragment();
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_find_password, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvCode.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sendCode();
            }
        });

        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submit();
            }
        });
    }

    private void submit() {
        final String phone = inputPhone.getText().toString().trim();
        String code = etIdentifyCode.getText().toString().trim();
        if (phone.isEmpty()) {
            ToastTool.showShort(getActivity(), "手机号不能为空");
            return;
        }
        if (code.isEmpty()) {
            ToastTool.showShort(getActivity(), "验证码不能为空");
            return;
        }

        HashMap<String, String> maps = new HashMap<>();
        maps.put("phone", phone);
        maps.put("code", code);
        OktHttpUtil
                .getInstance()
                .setPostRequest(HttpUri.BASE_URL + "/api/sms/check"
                        , maps, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String registerResult = response.body().string();
                                Gson gson = new Gson();
                                final SmsBean smsBean = gson.fromJson(registerResult, SmsBean.class);
                                MyApplication.mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (smsBean.code == 0) {
                                            Toast.makeText(getActivity(),
                                                    "校验成功", Toast.LENGTH_SHORT).show();
                                            FragmentActivity activity = getActivity();
                                            if (activity instanceof ForgetPassWordActivity) {
                                                ((ForgetPassWordActivity) activity).go2ResetPassword(phone);
                                            }
                                        } else {
                                            Toast.makeText(getActivity(),
                                                    smsBean.message, Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                            }
                        });
    }


    private void sendCode() {
        final String phone = inputPhone.getText().toString().trim();
        if (phone.isEmpty()) {
            ToastTool.showShort(getActivity(), "手机号不能为空");
            return;
        }

        HashMap<String, String> maps = new HashMap<>();
        maps.put("phone", phone);
        maps.put("type", "2");
        OktHttpUtil
                .getInstance()
                .setPostRequest(HttpUri.BASE_URL + HttpUri.LoginOrRegister.REQUEST_HEADER_SMS
                        , maps, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {

                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String registerResult = response.body().string();
                                Gson gson = new Gson();
                                final SmsBean smsBean = gson.fromJson(registerResult, SmsBean.class);
                                MyApplication.mHandler.post(new Runnable() {
                                    @Override
                                    public void run() {
                                        if (smsBean.code == 0) {
                                            Toast.makeText(getActivity(),
                                                    "发送成功", Toast.LENGTH_SHORT).show();
                                            startCountTime();
                                        } else {
                                            Toast.makeText(getActivity(),
                                                    smsBean.message, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            }
                        });
    }

    private CountDownTimer timer;

    private void startCountTime() {
        tvCode.setEnabled(false);
        timer = new CountDownTimer(60 * 1000, 1000) {
            @Override
            public void onTick(long l) {
                tvCode.setText("已发送(" + (l / 1000) + "秒)");
            }

            @Override
            public void onFinish() {
                tvCode.setText("获取验证码");
                tvCode.setEnabled(true);
            }
        };
        timer.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
        if (timer != null) {
            timer.cancel();
        }
    }
}
