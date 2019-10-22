package application;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Looper;
import android.support.multidex.MultiDex;
import android.util.Log;

import com.facebook.stetho.Stetho;
import com.google.gson.Gson;
import com.raizlabs.android.dbflow.config.FlowManager;
import com.umeng.commonsdk.BuildConfig;
import com.umeng.commonsdk.UMConfigure;
import com.umeng.socialize.PlatformConfig;

import java.util.HashMap;
import java.util.Map;

import bean.UserInfoBean;
import http.OktHttpUtil;
import util.JPushUtils;
import util.PreferencesUtil;

/**
 * Created by 李杰 on 2019/9/1.
 */

public class MyApplication extends Application {
    public static MyApplication myApp;
    private static final String TAG = "MyApplication";
    private static OktHttpUtil okHttpUtil;
    public static Context mContext;
    private HashMap<String, String> sessionMaps = new HashMap<>();
    private String userinfo_uid;
    private UserInfoBean userInfo;
    public static boolean isDebug = BuildConfig.DEBUG;
    public static Handler mHandler;

    public static MyApplication getInstance() {
//        if (myApp == null) {
//            myApp = new MyApp();
//        }
        return myApp;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "onCreate: ");
        myApp = this;
        FlowManager.init(this);
        Stetho.initializeWithDefaults(this);
        SharedPreferences sharedPreferences = getSharedPreferences("user_cookie", MODE_PRIVATE);
        String sessionId = sharedPreferences.getString("cookie", "");
        if (!sessionId.equals("")) {
            sessionMaps.put("cookie", sessionId);
        }
        JPushUtils.initJPush(this, false);

        //友盟初始化
        UMConfigure.init(this, "5d566c434ca357a3a60006c1", "umeng", UMConfigure.DEVICE_TYPE_PHONE, "");
        UMConfigure.setLogEnabled(BuildConfig.DEBUG);
        /**
         * 友盟相关平台配置。注意友盟官方新文档中没有这项配置，但是如果不配置会吊不起来相关平台的授权界面
         */
        PlatformConfig.setWeixin("wx1ab60a3167b1a375", "2fa6f1edd30cf48f5405746356a2980e");//微信APPID和AppSecret
        PlatformConfig.setQQZone("101783528", "96f5d0c26679ba9e2092bac66af0bbe0");//QQAPPID和AppSecret
        okHttpUtil = OktHttpUtil.getInstance();
        mContext = this;
        mHandler = new Handler(Looper.getMainLooper());
        //测试登录
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //多dex
        MultiDex.install(base);
    }

    public static OktHttpUtil getOkHttpUtil() {
        return okHttpUtil;
    }
    //登录账号

    public HashMap<String, String> getMaps() {
        return sessionMaps;
    }

    public void setMaps(HashMap<String, String> maps, String type) {
        if (type.equals("save")) {
            String sessionId = null;
            for (Map.Entry<String, String> value : maps.entrySet()) {
                sessionId = value.getValue();
            }
            SharedPreferences sharedPreferences = getSharedPreferences("user_cookie", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putString("cookie", sessionId);
            editor.commit();
        } else if (type.equals("exit")) {
            SharedPreferences sharedPreferences = getSharedPreferences("user_cookie", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.clear();
            editor.commit();
        }
        sessionMaps = maps;
    }

    public void setUserUid(String uid) {
        userinfo_uid = uid;
    }

    public String getUseruid() {
        return userinfo_uid;
    }

//    public void setUserInfo(UserInfoBean user) {
//        this.userInfo = user;
//    }

    public UserInfoBean getUserInfo() {
        String json = PreferencesUtil.get(this, PreferencesUtil.SP_USER_INFO, null);
        UserInfoBean loginBean = new Gson().fromJson(json, UserInfoBean.class);
        return loginBean;
    }
}
