package com.am.shortVideo.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.am.shortVideo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import bean.MessageWrap;

/**
 * Created by 李杰 on 2019/8/12.
 */

public class AttentionFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "AttentionFragment";
    private View view;
    private TextView bt_attentiontitle;
    private TextView bt_videotitle;
    private AttentionPersonFragment attentionPersonFragment;
    private VideoShowFragment videoShowFragment;
    private int curSelect = 0;
    private View attention_line1;
    private View attention_line2;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.attention_fragment, container, false);
        EventBus.getDefault().register(this);
        initView();
        setOnLinstener();
        switchFragment(curSelect);
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }

    private void setOnLinstener() {
        bt_attentiontitle.setOnClickListener(this);
        bt_videotitle.setOnClickListener(this);
    }

    private void initView() {
        bt_attentiontitle = (TextView) view.findViewById(R.id.tv_attentionlist);
        attention_line1 = (View) view.findViewById(R.id.attention_line1);
        bt_videotitle = (TextView) view.findViewById(R.id.tv_videolist);
        attention_line2 = (View) view.findViewById(R.id.attention_line2);

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.tv_attentionlist:
                if (curSelect != 0) {
                    curSelect = 0;
                    switchFragment(curSelect);
                }
                break;
            case R.id.tv_videolist:
                if (curSelect != 1) {
                    curSelect = 1;
                    switchFragment(curSelect);
                }
                break;
            default:
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLoginStatus(MessageWrap messageWrap) {
        Log.d(TAG, "getLoginStatus: ");
        if (messageWrap.getMessage().equals("true")) {

        } else if (messageWrap.getMessage().equals("false")) {

            getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
        }
    }

    public void switchFragment(int type) {
        FragmentTransaction fragmentTransaction = getChildFragmentManager().beginTransaction();
        hideAllFragment(fragmentTransaction);
        if (type == 0) {
            if (attentionPersonFragment == null) {
                attentionPersonFragment = new AttentionPersonFragment();
                fragmentTransaction.add(R.id.attention_framelayout, attentionPersonFragment);
            } else {
                fragmentTransaction.show(attentionPersonFragment);
            }
            attention_line1.setVisibility(View.VISIBLE);
            attention_line2.setVisibility(View.GONE);
        } else if (type == 1) {
            if (videoShowFragment == null) {
                videoShowFragment = new VideoShowFragment();
                fragmentTransaction.add(R.id.attention_framelayout, videoShowFragment);
            } else {
                fragmentTransaction.show(videoShowFragment);
            }
            attention_line2.setVisibility(View.VISIBLE);
            attention_line1.setVisibility(View.GONE);
        }
        fragmentTransaction.commit();
    }

    public void hideAllFragment(FragmentTransaction fragmentTransaction) {
        if (attentionPersonFragment != null) {
            fragmentTransaction.hide(attentionPersonFragment);
        }
        if (videoShowFragment != null) {
            fragmentTransaction.hide(videoShowFragment);
        }
    }
}
