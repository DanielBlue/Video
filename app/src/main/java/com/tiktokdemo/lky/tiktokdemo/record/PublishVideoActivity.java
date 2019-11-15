package com.tiktokdemo.lky.tiktokdemo.record;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Address;
import android.location.Criteria;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaMetadataRetriever;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.MediaStore;
import android.provider.Settings;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.google.gson.Gson;
import com.tiktokdemo.lky.tiktokdemo.Constant;
import com.tiktokdemo.lky.tiktokdemo.record.bean.TidalPatRecordDraftBean;
import com.tiktokdemo.lky.tiktokdemo.utils.FileUtils;
import com.tiktokdemo.lky.tiktokdemo.utils.ToastTool;
import com.yanzhenjie.permission.Action;
import com.yanzhenjie.permission.AndPermission;

import org.greenrobot.eventbus.EventBus;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import application.MyApplication;
import bean.PublishVideo;
import bean.PublishVideoEvent;
import bean.PublishVideoInfo;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.HttpUri;
import util.StatusBarUtil;

/**
 * Created by 李杰 on 2019/9/13.
 */

public class PublishVideoActivity extends AppCompatActivity implements View.OnClickListener, TextWatcher {
    private static final String TAG = "PublishVideoActivity";
    private ImageView personinfo_back;
    private Button personinfo_save;
    private TextView tv_title;
    private ImageView iv_publishview;
    private EditText ed_publishmessage;
    private EditText ed_publishfoodid;
    private TextView tv_limitinput;
    private Button bt_publishVideo;
    private OktHttpUtil okHttpUtil;
    private TidalPatRecordDraftBean mTidalPatRecordDraftBean;
    private PublishVideoInfo publishVideoInfo;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 1:
                    Toast.makeText(PublishVideoActivity.this, "发布成功", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(PublishVideoActivity.this, com.am.shortVideo.activity.MainActivity.class);
                    startActivity(intent);
                    break;
                case 2:
                    Toast.makeText(PublishVideoActivity.this, "发布失败", Toast.LENGTH_SHORT).show();
                    break;
                case 3:
                    Toast.makeText(PublishVideoActivity.this, "商品不存在", Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    LocationListener networkListener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            Log.d(TAG, "onLocationChanged: ");
            float accuracy = location.getAccuracy();//获取精确位置
            double altitude = location.getAltitude();//获取海拔
            final double latitude = location.getLatitude();//获取纬度，平行
            final double longitude = location.getLongitude();//获取经度，垂直
            new Thread(new Runnable() {
                @Override
                public void run() {
                    List<Address> addsList = null;
                    Geocoder geocoder = new Geocoder(PublishVideoActivity.this);
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
                            if (latLongString.contains("市")) {
                                String city = latLongString.substring(0, latLongString.indexOf("市"));
                                publishVideoInfo.setLocation(city);
                                Log.d(TAG, "run: " + city);
                            } else {
                                publishVideoInfo.setLocation(latLongString);
                            }

//                            latLongString = ads.getAddressLine(0);//拿到地址
//                                runOnUiThread(new Runnable() {
//                                @Override
//                                public void run() {
//                                    Log.e("打印拿到的城市的地址", latLongString + ads.getAddressLine(0)  );
//                                    String city=latLongString + ads.getAddressLine(0);
//
//                                }
//                            });
                        }
                    }
                }
            }).start();

        }

        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            Log.d(TAG, "onStatusChanged: ");
        }

        @Override
        public void onProviderEnabled(String provider) {
            Log.d(TAG, "onProviderEnabled: ");
        }

        @Override
        public void onProviderDisabled(String provider) {
            Log.d(TAG, "onProviderDisabled: ");
        }

    };
    private RelativeLayout rl_editext;
    private ProgressDialog progressDialog;
    private boolean isSelectMusic;
    private String curAudio = "";
    private String playName;
    private String mLocalVideoFileName;
    private boolean isFromDraft;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.publishvideo_layout);
        setStatusBar();
        okHttpUtil = OktHttpUtil.getInstance();
        mTidalPatRecordDraftBean = (TidalPatRecordDraftBean) getIntent().getSerializableExtra("mTidalPatRecordDraftBean");
        isSelectMusic = getIntent().getBooleanExtra("isSelectMusic", false);
        isFromDraft = getIntent().getBooleanExtra("isFromDraft", false);
        if (isSelectMusic) {
            curAudio = getIntent().getStringExtra("AudioId");
            playName = getIntent().getStringExtra("Name");
        }
        saveVideo1(mTidalPatRecordDraftBean);
        initView();
    }

    private void setStatusBar() {
        //当FitsSystemWindows设置 true 时，会在屏幕最上方预留出状态栏高度的 padding
        StatusBarUtil.setRootViewFitsSystemWindows(this, false);
        //设置状态栏透明
        StatusBarUtil.setTranslucentStatus(this);
        if (!StatusBarUtil.setStatusBarDarkTheme(this, true)) {
            //如果不支持设置深色风格 为了兼容总不能让状态栏白白的看不清, 于是设置一个状态栏颜色为半透明,
            //这样半透明+白=灰, 状态栏的文字能看得清
            StatusBarUtil.setStatusBarColor(this, 0x55000000);
        }
    }

    private void initView() {
        personinfo_back = (ImageView) findViewById(R.id.personinfo_back);
        tv_title = (TextView) findViewById(R.id.tv_title);
        tv_title.setText(getResources().getString(R.string.bt_publishvideo));
        personinfo_save = (Button) findViewById(R.id.personinfo_save);
        iv_publishview = (ImageView) findViewById(R.id.iv_publishview);
        ed_publishmessage = (EditText) findViewById(R.id.ed_inputthinking);
        ed_publishfoodid = (EditText) findViewById(R.id.ed_inputfoodid);
        tv_limitinput = (TextView) findViewById(R.id.tv_limitinput);
        bt_publishVideo = (Button) findViewById(R.id.bt_publishvideo);
        rl_editext = (RelativeLayout) findViewById(R.id.rl_editext);
        personinfo_save.setText("保存至本地");
//        Glide.with(this).load(mTidalPatRecordDraftBean.getVideoLocalUrl()).diskCacheStrategy(DiskCacheStrategy.RESULT)
//                .crossFade().centerCrop().into(iv_publishview);
        personinfo_save.setOnClickListener(this);
        personinfo_back.setOnClickListener(this);
        ed_publishmessage.addTextChangedListener(this);
        bt_publishVideo.setOnClickListener(this);
        rl_editext.setOnClickListener(this);
        getVideoInfo(mTidalPatRecordDraftBean.getVideoLocalUrl());
        iv_publishview.setImageBitmap(publishVideoInfo.getBitmap());

        Criteria criteria = new Criteria();
        criteria.setAltitudeRequired(true);
        AndPermission.with(this)
                .runtime()
                .permission(Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION)
                .onGranted(new Action<List<String>>() {
                    @SuppressLint("MissingPermission")
                    @Override
                    public void onAction(List<String> data) {
                        LocationManager mLocationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                        String providers = mLocationManager.NETWORK_PROVIDER;
                        Log.d(TAG, "getLocationAddress: " + providers);
                        mLocationManager.getLastKnownLocation(providers);
                        mLocationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 3000, 1, networkListener);
                    }
                })
                .onDenied(new Action<List<String>>() {
                    @Override
                    public void onAction(List<String> data) {

                    }
                })
                .start();

        mLocalVideoFileName = System.currentTimeMillis() + ".mp4";
        if (!isFromDraft) {
            FileUtils.copyFile(mTidalPatRecordDraftBean.getVideoLocalUrl(), Constant.RECORD_VIDEO_PATH, mLocalVideoFileName);
        }
    }

    public boolean isGpsOPen() {
        LocationManager locationManager
                = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        // 通过GPS卫星定位，定位级别可以精确到街（通过24颗卫星定位，在室外和空旷的地方定位准确、速度快）
        boolean gps = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        // 通过WLAN或移动网络(3G/2G)确定的位置（也称作AGPS，辅助GPS定位。主要用于在室内或遮盖物（建筑群或茂密的深林等）密集的地方定位）
        boolean network = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
        if (gps || network) {
            return true;
        }

        return false;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.personinfo_back:
                onBackPressed();
                break;
            case R.id.personinfo_save:
                saveVideo(mTidalPatRecordDraftBean);
                break;
            case R.id.bt_publishvideo:
                if (!isGpsOPen()) {
                    openAlertDialog(this);
                    return;
                }

                String message = ed_publishmessage.getText().toString().trim();
                if (message.isEmpty()) {
                    Toast.makeText(this, "描述不能为空", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (publishVideo()) {
                    HashMap<String, String> maps1 = new HashMap<>();
                    if (isSelectMusic) {
                        maps1.put("audioId", curAudio);
                    } else {
                        maps1.put("audioId", "0");
                    }
                    maps1.put("cover", Constant.DOWNBGM + File.separator + "audio.jpg");
                    if (publishVideoInfo.getLocation() != null) {
                        maps1.put("location", publishVideoInfo.getLocation());
                    } else {
                        maps1.put("location", "");
                    }
                    if (ed_publishfoodid.getText().toString().trim().isEmpty()) {
                        maps1.put("shopId", "null");
                    } else if (!ed_publishfoodid.getText().toString().trim().isEmpty()) {
                        maps1.put("shopId", ed_publishfoodid.getText().toString().trim());
                    }
                    if (isFromDraft) {
                        maps1.put("video", mTidalPatRecordDraftBean.getVideoLocalUrl());
                    } else {
                        maps1.put("video", Constant.RECORD_VIDEO_PATH_TEMP1 + File.separator + "shibo.mp4");
                    }

                    maps1.put("desc", message);

                    maps1.put("duration", publishVideoInfo.getVideoDuraion());
                    maps1.put("height", publishVideoInfo.getVideoheight());
                    maps1.put("width", publishVideoInfo.getVideowidth());
                    openGrogressAlterdialog();
//                    HttpUri.VIDEO.REQUEST_HEADER_PUBLISHVIDEO

                    okHttpUtil.setPostRequest(HttpUri.BASE_URL + "/api/video/compound"
                            , ((MyApplication) getApplication()).getMaps(), maps1, 1, new Callback() {
                                @Override
                                public void onFailure(Call call, IOException e) {
                                    Log.d(TAG, "onFailure: " + e.getMessage());
                                    e.printStackTrace();
                                    handler.sendEmptyMessage(2);
                                    progressDialog.dismiss();
                                }

                                @Override
                                public void onResponse(Call call, Response response) throws IOException {
                                    String publishVideoResult = response.body().string();
                                    Log.d(TAG, "onResponse: \n" + publishVideoResult);
                                    Gson gson = new Gson();
                                    PublishVideo publishVideo = gson.fromJson(publishVideoResult, PublishVideo.class);
                                    if (publishVideo.getCode() == 0 && publishVideo.getMessage().equals("成功上传")) {
                                        File file = new File(Constant.RECORD_VIDEO_PATH, mLocalVideoFileName);
                                        if (file.exists()) {
                                            file.delete();
                                        }

                                        File localFile = new File(mTidalPatRecordDraftBean.getVideoLocalUrl());
                                        if (localFile.exists()) {
                                            localFile.delete();
                                        }
                                        if (isFromDraft) {
                                            EventBus.getDefault().post(new PublishVideoEvent(mTidalPatRecordDraftBean.getVideoLocalUrl()));
                                        }
                                        handler.sendEmptyMessage(1);
                                    } else if (publishVideo.getCode() == 1016) {
                                        handler.sendEmptyMessage(3);

                                    } else {
                                        handler.sendEmptyMessage(2);
                                    }
                                    progressDialog.dismiss();
                                }
                            });
                }
                break;
        }
    }

    public void openAlertDialog(Context context) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("发布时,需要开启定位服务")
                .setPositiveButton("确认", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        openGPS(PublishVideoActivity.this);
                    }
                }).
                setNegativeButton("取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                    }
                }).show();
    }

    public void getVideoInfo(String videoPath) {
        publishVideoInfo = new PublishVideoInfo();
        MediaMetadataRetriever media = new MediaMetadataRetriever();
        media.setDataSource(videoPath);// videoPath 本地视频的路径
        Bitmap bitmap = media.getFrameAtTime(1, MediaMetadataRetriever.OPTION_CLOSEST_SYNC);
        // mIvBigShow.setImageBitmap(bitmap);
        publishVideoInfo.setBitmap(bitmap);
        String timeString = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_DURATION);
        String width = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_WIDTH);
        String height = media.extractMetadata(MediaMetadataRetriever.METADATA_KEY_VIDEO_HEIGHT);
        publishVideoInfo.setVideoDuraion(timeString);
        publishVideoInfo.setVideowidth(width);
        publishVideoInfo.setVideoheight(height);
        saveBitmap(publishVideoInfo);
    }

    /**
     * 保存方法
     */
    public void saveBitmap(PublishVideoInfo publishVideoInfo) {
        Log.e(TAG, "保存图片");
        File f = new File(Constant.DOWNBGM + File.separator + "audio.jpg");
        if (!f.getParentFile().exists()) {
            f.getParentFile().mkdirs();
        }
        if (f.exists()) {
            f.delete();
        }
        try {
            FileOutputStream out = new FileOutputStream(f);
            publishVideoInfo.getBitmap().compress(Bitmap.CompressFormat.PNG, 90, out);
            out.flush();
            out.close();
            Log.i(TAG, "已经保存");
        } catch (FileNotFoundException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }

    //保存视频
    public void saveVideo1(final TidalPatRecordDraftBean tidalPatRecordDraftBean) {
        File file = new File(Constant.RECORD_VIDEO_PATH_TEMP1);
        if (!file.exists()) {
            file.mkdirs();
        }
        if (TextUtils.isEmpty(tidalPatRecordDraftBean.getVideoName())) {
            FileUtils.copyFile(mTidalPatRecordDraftBean.getVideoLocalUrl(), Constant.RECORD_VIDEO_PATH_TEMP1, "shibo" + ".mp4");
        }
    }

    //保存视频
    public void saveVideo(final TidalPatRecordDraftBean tidalPatRecordDraftBean) {
        if (TextUtils.isEmpty(tidalPatRecordDraftBean.getVideoName())) {
            String videoPath = mTidalPatRecordDraftBean.getVideoLocalUrl();
            File file = new File(videoPath);

            if (file.exists()) {
                File newFile = new File(Environment.getExternalStorageDirectory().getAbsolutePath()+"/Download/");
                FileUtils.copyFile(file.getAbsolutePath(),newFile.getAbsolutePath(),"video.mp4");

                ContentResolver localContentResolver = getContentResolver();
                ContentValues localContentValues = getVideoContentValues(this, newFile, System.currentTimeMillis());
                Uri localUri = localContentResolver.insert(MediaStore.Video.Media.EXTERNAL_CONTENT_URI, localContentValues);
                sendBroadcast(new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE, localUri));

                ToastTool.showShort(this, "保存成功："+newFile.getPath());
            }
        }
    }

    public ContentValues getVideoContentValues(Context paramContext, File paramFile, long paramLong) {
        ContentValues localContentValues = new ContentValues();
        localContentValues.put("title", paramFile.getName());
        localContentValues.put("_display_name", paramFile.getName());
        localContentValues.put("mime_type", "video/mp4");
        localContentValues.put("datetaken", Long.valueOf(paramLong));
        localContentValues.put("date_modified", Long.valueOf(paramLong));
        localContentValues.put("date_added", Long.valueOf(paramLong));
        localContentValues.put("_data", paramFile.getAbsolutePath());
        localContentValues.put("_size", Long.valueOf(paramFile.length()));
        return localContentValues;
    }

    public boolean publishVideo() {
        if (ed_publishmessage.getText().toString().trim().length() > 20) {
            Toast.makeText(this, "超过限定数字", Toast.LENGTH_SHORT).show();
            return false;
        }

        return true;
    }

    @Override
    public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

    }

    @Override
    public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
        String content = ed_publishmessage.getText().toString().trim();
        tv_limitinput.setText(content.length() + "/" + 20);
    }

    @Override
    public void afterTextChanged(Editable editable) {

    }

    public void openGPS(Context context) {
        Intent intent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
        startActivityForResult(intent, 887);

    }

    public void openGrogressAlterdialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("文件正在上传");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();
    }

}
