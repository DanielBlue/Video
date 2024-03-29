package http;

import android.widget.Toast;

import com.facebook.stetho.okhttp3.StethoInterceptor;
import com.shuyu.gsyvideoplayer.utils.NetworkUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import application.MyApplication;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.Headers;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;

/**
 * Created by 李杰 on 2019/7/31.
 */

public class OktHttpUtil {
    private static OkHttpClient okHttpClient;
    static OktHttpUtil oktHttpUtil;

    public OktHttpUtil() {
        okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(60, TimeUnit.SECONDS)
                .readTimeout(60, TimeUnit.SECONDS)
                .writeTimeout(60, TimeUnit.SECONDS)
                .addNetworkInterceptor(new StethoInterceptor())
//                .addNetworkInterceptor(new SessionInterceptor())
                .build();

    }

    public static OktHttpUtil getInstance() {
        if (okHttpClient == null) {
            synchronized (OktHttpUtil.class) {
                if (okHttpClient == null) {
                    oktHttpUtil = new OktHttpUtil();
                }
            }
        }
        return oktHttpUtil;

    }

    public void sendGetRequest(String url, Callback callback) {
        if (!checkNetUsed()) return;
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);

    }

    public boolean checkNetUsed() {
        int netWorkType = NetworkUtils.getNetWorkType(MyApplication.mContext);
        boolean b = netWorkType != NetworkUtils.NETWORK_NO;
        if (!b) {
            MyApplication.mHandler.post(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(MyApplication.mContext,"无网络",Toast.LENGTH_SHORT).show();
                }
            });
        }
        return b;
    }

    public void sendGetRequest(String url, HashMap<String, String> sessionMaps, Callback callback) {
        if (!checkNetUsed()) return;
        Headers.Builder headbuilder = new Headers.Builder();
        for (Map.Entry<String, String> head : sessionMaps.entrySet()) {
            headbuilder.add(head.getKey(), head.getValue());
        }
        Request request = new Request.Builder().url(url).headers(headbuilder.build()).build();
        okHttpClient.newCall(request).enqueue(callback);

    }

    //设置带参数的GET请求
    public void sendGetRequest(String url, HashMap<String, String> sessionMaps, HashMap<String, String> maps, Callback callback) {
        if (!checkNetUsed()) return;
        Request.Builder builder = new Request.Builder();
        Headers.Builder headbuilder = new Headers.Builder();
        HttpUrl.Builder urlBuilder = HttpUrl.parse(url).newBuilder();
        for (Map.Entry<String, String> key : maps.entrySet()) {
            urlBuilder.addQueryParameter(key.getKey(), key.getValue());
        }
        for (Map.Entry<String, String> head : sessionMaps.entrySet()) {
            headbuilder.add(head.getKey(), head.getValue());
        }
        builder.url(urlBuilder.build()).headers(headbuilder.build());
        okHttpClient.newCall(builder.build()).enqueue(callback);
    }

    public void setPostRequest(String url, HashMap<String, String> headmaps, HashMap<String, String> maps, Callback callback) {
        if (!checkNetUsed()) return;
        FormBody.Builder builder = new FormBody.Builder();
        Headers.Builder headbuilder = new Headers.Builder();
        for (Map.Entry<String, String> key : maps.entrySet()) {
            builder.add(key.getKey(), (String) key.getValue());
        }
        for (Map.Entry<String, String> head : headmaps.entrySet()) {
            headbuilder.add(head.getKey(), head.getValue());
        }

        Request request = new Request.Builder().url(url).headers(headbuilder.build()).post(builder.build()).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void setPostRequest(String url, HashMap<String, String> headmaps, String path, Callback callback) {
        if (!checkNetUsed()) return;
        File file = new File(path);
        RequestBody filebody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
        RequestBody requestBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("uploadFile", file.getName(), filebody).build();
        Headers.Builder headbuilder = new Headers.Builder();
        for (Map.Entry<String, String> head : headmaps.entrySet()) {
            headbuilder.add(head.getKey(), head.getValue());
        }

        Request request = new Request.Builder().url(url).headers(headbuilder.build()).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void setPostRequest(String url, HashMap<String, String> headmaps, HashMap<String, String> map1,
                               int type, Callback callback) {

        if (!checkNetUsed()) return;
        MultipartBody.Builder multipartBody = new MultipartBody.Builder();
        multipartBody.setType(MultipartBody.FORM);
        for (Map.Entry<String, String> head1 : map1.entrySet()) {
            if (head1.getKey().equals("cover") || head1.getKey().equals("video")) {
                File file = new File(head1.getValue());
                if (file.exists()){
                    RequestBody filebody = RequestBody.create(MediaType.parse("application/octet-stream"), file);
                    multipartBody.addFormDataPart(head1.getKey(), file.getName(), filebody);
                }
            } else {
                if (head1.getKey()!=null&&head1.getValue()!=null){
                    multipartBody.addFormDataPart(head1.getKey(), head1.getValue());
                }
            }
        }
        Headers.Builder headbuilder = new Headers.Builder();
        RequestBody requestBody = multipartBody.build();
        for (Map.Entry<String, String> head : headmaps.entrySet()) {
            headbuilder.add(head.getKey(), head.getValue());
        }

        Request request = new Request.Builder().url(url).headers(headbuilder.build()).post(requestBody).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void setPostRequest(String url, HashMap<String, String> maps, Callback callback) {
        if (!checkNetUsed()) return;
        FormBody.Builder builder = new FormBody.Builder();
        for (Map.Entry<String, String> key : maps.entrySet()) {
            builder.add(key.getKey(), key.getValue());
        }

        Request request = new Request.Builder().url(url).post(builder.build()).build();
        okHttpClient.newCall(request).enqueue(callback);
    }

    public void downMusicFile(String url, Callback callback) {
        if (!checkNetUsed()) return;
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(callback);
    }
}
