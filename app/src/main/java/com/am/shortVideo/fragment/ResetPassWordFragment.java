package com.am.shortVideo.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
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
public class ResetPassWordFragment extends Fragment {

    @BindView(R.id.input_new_password)
    EditText inputNewPassword;
    @BindView(R.id.input_confirm_password)
    EditText inputConfirmPassword;
    @BindView(R.id.tv_submit)
    TextView tvSubmit;
    Unbinder unbinder;

    public static ResetPassWordFragment newInstance(String phone) {
        Bundle args = new Bundle();
        ResetPassWordFragment fragment = new ResetPassWordFragment();
        args.putString("phone", phone);
        fragment.setArguments(args);
        return fragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_reset_password, null);
        unbinder = ButterKnife.bind(this, view);
        initView();
        return view;
    }

    private void initView() {
        tvSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });
    }

    private void resetPassword() {
        String newPassword = inputNewPassword.getText().toString().trim();
        String confirmPassword = inputConfirmPassword.getText().toString().trim();
        if (newPassword.isEmpty()) {
            ToastTool.showShort(getActivity(), "新密码不能为空");
            return;
        }
        if (confirmPassword.isEmpty()) {
            ToastTool.showShort(getActivity(), "确认密码不能为空");
            return;
        }
        if (!newPassword.equals(confirmPassword)) {
            ToastTool.showShort(getActivity(), "两次输入密码不一致");
            return;
        }

        HashMap<String, String> maps = new HashMap<>();
        maps.put("phone", getArguments().getString("phone"));
        maps.put("password", newPassword);
        OktHttpUtil
                .getInstance()
                .setPostRequest(HttpUri.BASE_URL + "/api/reset"
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
                                                    "密码重置成功", Toast.LENGTH_SHORT).show();
                                            getActivity().finish();
                                        } else {
                                            Toast.makeText(getActivity(),
                                                    smsBean.message, Toast.LENGTH_SHORT).show();
                                        }

                                    }
                                });

                            }
                        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
