package com.am.shortVideo.activity;

import android.content.Context;
import android.content.Intent;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import adapter.PushHistoryMessageAdapter;
import bean.HistoryMessageBean;
import bean.MessageWrap;
import event.MessageEvent;
import event.RedDotEvent;
import http.OktHttpUtil;

/**
 * Created by 李杰 on 2019/8/12.
 */

public class MessageFragment extends Fragment implements View.OnClickListener {
    private static final String TAG = "MessageFragment";
    private View view;
    private Button bt_location;
    private TextView tv_fans;
    private TextView tv_support;
    private TextView tv_messagecomment;
    private LocationManager locationManger;
    private LinearLayout ll_fans;
    private LinearLayout ll_support;
    private LinearLayout ll_messagecomment;
    private RecyclerView message_recylce;
    private OktHttpUtil okHttpUtil;
    private List<HistoryMessageBean.DataBean.PushHistoryBean> datas = new ArrayList<>();
    //    private Handler handler = new Handler() {
//        @Override
//        public void handleMessage(Message msg) {
//            super.handleMessage(msg);
//            switch (msg.what) {
//                case 1://需要修改
//                    HistoryMessageBean historymessage = (HistoryMessageBean) msg.obj;
//                    datas.clear();
//                    datas.add(messageHelp);
//                    if (historymessage.getCode() == 0) {
//                        datas.addAll(historymessage.getData().getPushHistory());
//                        if (datas != null && !datas.isEmpty()) {
//                            pushhistoryAdapter.notifyDataSetChanged();
//                        } else {
//
//                        }
//                    } else if (historymessage.getCode() == 1005) {
//                        new LoginPopupwindow(getActivity());
//                    }
//
//                    break;
//            }
//        }
//    };
//    private Callback historyCallback = new Callback() {
//        @Override
//        public void onFailure(Call call, IOException e) {
//
//        }
//
//        @Override
//        public void onResponse(Call call, Response response) throws IOException {
//            String historymessageResult = response.body().string();
//            Log.d(TAG, "onResponse: \n" + historymessageResult);
//            Gson gson = new Gson();
//            HistoryMessageBean historymessagbean = gson.fromJson(historymessageResult, HistoryMessageBean.class);
//            Message message = new Message();
//            message.what = 1;
//            message.obj = historymessagbean;
//            handler.sendMessage(message);
//        }
//    };
    private PushHistoryMessageAdapter pushhistoryAdapter;
    private HistoryMessageBean.DataBean.PushHistoryBean messageHelp;
    private View mViewDotFans;
    private View mViewDotSupport;
    private View mViewDotComment;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.message_fragment, container, false);
        EventBus.getDefault().register(this);
        okHttpUtil = OktHttpUtil.getInstance();
        initView();
//        initData();
        setOnclickLinstenr();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        EventBus.getDefault().unregister(this);
    }


    private void setOnclickLinstenr() {
        bt_location.setOnClickListener(this);
        ll_fans.setOnClickListener(this);
        ll_support.setOnClickListener(this);
        ll_messagecomment.setOnClickListener(this);
        tv_fans.setOnClickListener(this);
        tv_support.setOnClickListener(this);
        tv_messagecomment.setOnClickListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    private void initData() {
//        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.MESSAGE.REQUEST_HEADER_PUSHHISTORY
//                , ((MyApplication) getActivity().getApplicationContext()).getMaps(), historyCallback);
    }

    private void initView() {
        bt_location = (Button) view.findViewById(R.id.bt_location);
        ll_fans = (LinearLayout) view.findViewById(R.id.ll_fans);
        ll_support = (LinearLayout) view.findViewById(R.id.ll_support);
        ll_messagecomment = (LinearLayout) view.findViewById(R.id.ll_messagecommentand);
        tv_fans = (TextView) view.findViewById(R.id.tv_fans);
        tv_support = (TextView) view.findViewById(R.id.tv_messagesupport);
        tv_messagecomment = (TextView) view.findViewById(R.id.tv_messagecomment);
        message_recylce = (RecyclerView) view.findViewById(R.id.message_recycle);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity(), android.support.v7.widget.LinearLayoutManager.VERTICAL, false);
        message_recylce.setLayoutManager(linearLayoutManager);
        messageHelp = new HistoryMessageBean.DataBean.PushHistoryBean();
        messageHelp.setId(-1);
        datas.add(messageHelp);
        pushhistoryAdapter = new PushHistoryMessageAdapter(datas, getActivity());
        message_recylce.setAdapter(pushhistoryAdapter);

        mViewDotFans = view.findViewById(R.id.view_dot_fans);
        mViewDotSupport = view.findViewById(R.id.view_dot_support);
        mViewDotComment = view.findViewById(R.id.view_dot_comment);
    }

    public void getLocationAddress() {
        locationManger = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(true);
        String providers = locationManger.getBestProvider(criteria, false);
        Log.d(TAG, "getLocationAddress: " + providers);
        ;
        //locationManger.requestLocationUpdates("networks",0,0,locationListenr);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onEvent(MessageEvent event) {
        if (event != null) {
            if (event instanceof RedDotEvent) {
                int pushType = ((RedDotEvent) event).getPushType();
                boolean isShow = ((RedDotEvent) event).isShow();
                if (pushType == 1) {
                    mViewDotSupport.setVisibility(isShow?View.VISIBLE:View.INVISIBLE);
                } else if (pushType == 2) {
                    mViewDotFans.setVisibility(isShow?View.VISIBLE:View.INVISIBLE);
                } else if (pushType == 3) {
                    mViewDotComment.setVisibility(isShow?View.VISIBLE:View.INVISIBLE);
                }

                updateHomeRedDotState();
            }
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void getLoginStatus(MessageWrap messageWrap) {
        Log.d(TAG, "getLoginStatus: ");
        if (messageWrap.getMessage().equals("true")) {

        } else if (messageWrap.getMessage().equals("false")) {
            mViewDotSupport.setVisibility(View.INVISIBLE);
            mViewDotFans.setVisibility(View.INVISIBLE);
            mViewDotComment.setVisibility(View.INVISIBLE);
        }
    }

    /**
     * 更新首页红点
     */
    private void updateHomeRedDotState() {
        FragmentActivity activity = getActivity();
        if (activity instanceof MainActivity) {
            ((MainActivity) activity).updateHomeRedDotState(mViewDotSupport.getVisibility() == View.VISIBLE ||
                    mViewDotFans.getVisibility() == View.VISIBLE ||
                    mViewDotComment.getVisibility() == View.VISIBLE);
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_location:
                break;
            case R.id.ll_fans:
                Intent intent = new Intent(getActivity(), FansActivity.class);
                startActivity(intent);
                mViewDotFans.setVisibility(View.INVISIBLE);
                updateHomeRedDotState();
                break;
            case R.id.ll_messagecommentand:
                Intent intent1 = new Intent(getActivity(), CommentAndActivity.class);
                startActivity(intent1);
                mViewDotComment.setVisibility(View.INVISIBLE);
                updateHomeRedDotState();
                break;
            case R.id.ll_support:
                Intent intent2 = new Intent(getActivity(), LikeListActivity.class);
                startActivity(intent2);
                mViewDotSupport.setVisibility(View.INVISIBLE);
                updateHomeRedDotState();
                break;
            default:
        }
    }

    private LocationListener locationListenr = new LocationListener() {
        @Override
        public void onLocationChanged(Location location) {
            float accuracy = location.getAccuracy();//获取精确位置
            double altitude = location.getAltitude();//获取海拔
            final double latitude = location.getLatitude();//获取纬度，平行
            final double longitude = location.getLongitude();//获取经度，垂直
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Address> addsList = null;
                    Geocoder geocoder = new Geocoder(getActivity());
                    try {
                        addsList = geocoder.getFromLocation(latitude, longitude, 10);//得到的位置可能有多个当前只取其中一个
                        Log.e("打印拿到的城市", addsList.toString());
                    } catch (IOException e) {

                        e.printStackTrace();
                    }
                    if (addsList != null && addsList.size() > 0) {
                        for (int i = 0; i < addsList.size(); i++) {
                            final Address ads = addsList.get(i);
                            final String latLongString = ads.getLocality();//拿到城市
//                            latLongString = ads.getAddressLine(0);//拿到地址
                            getActivity().runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    Log.e("打印拿到的城市的地址", latLongString + ads.getAddressLine(0) + ads.getAddressLine(1) + ads.getAddressLine(4));
                                    Toast.makeText(getActivity(), "当前所在的城市为" + latLongString + ads.getAddressLine(0) + ads.getAddressLine(4) + ads.getAddressLine(1), Toast.LENGTH_LONG).show();
                                }
                            });
                        }
                    }
                }
            }).start();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }
    };
}
