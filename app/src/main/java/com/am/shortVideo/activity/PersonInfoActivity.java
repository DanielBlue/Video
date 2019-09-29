package com.am.shortVideo.activity;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.FileProvider;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.am.shortVideo.R;
import com.bigkoo.pickerview.OptionsPickerView;
import com.bigkoo.pickerview.TimePickerView;
import com.bumptech.glide.Glide;
import com.google.gson.Gson;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONArray;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import application.MyApplication;
import base.BaseActivity;
import bean.ChageUserInfo;
import bean.CityBean;
import bean.MessageWrap;
import bean.UserIMG;
import bean.UserInfo;
import customeview.EditInfoPopupwindow;
import customeview.LoginPopupwindow;
import customeview.MyPopUpWindow;
import customeview.SexPopupWindow;
import de.hdodenhof.circleimageview.CircleImageView;
import http.OktHttpUtil;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import util.FileUtilcll;
import util.GetJsonDataUtil;
import util.HttpUri;

/**
 * Created by JC on 2019/8/1.
 */

public class PersonInfoActivity extends BaseActivity implements MyPopUpWindow.PopupCallback, View.OnClickListener,
        SexPopupWindow.SexCallBack, EditInfoPopupwindow.EditTextInfo {
    private static final String TAG = "PersonInfoActivity";
    private static final String PHOTO_FILE_NAME = "photo.jpg";
    private CircleImageView personinfo_circleImageview;
    private OktHttpUtil okHttpUtil;
    private Uri cropImageUri;
    private File mCropImageFile;
    private File tempFile;
    private Uri destinationUri;
    private TextView personinfo_birthday;
    private TextView personinfo_city;
    private TextView personinfo_account;
    private EditText personinfo_nickname;
    private EditText personinfo_personnalname;
    private TextView personinfo_sex;
    private TimePickerView pvTime1;
    private boolean isChangeInfo = false;
    private ArrayList<CityBean> options1Items = new ArrayList<>();
    private ArrayList<ArrayList<String>> options2Items = new ArrayList<>();
    private ArrayList<ArrayList<ArrayList<String>>> options3Items = new ArrayList<>();
    private ArrayList<String> bankNameList = new ArrayList<>();
    private Thread thread;
    private static final int MSG_LOAD_DATA = 0x0001;
    private static final int MSG_LOAD_SUCCESS = 0x0002;
    private static final int MSG_LOAD_FAILED = 0x0003;
    private boolean isLoaded = false;
    private String province;
    private String city;
    private TextView bank_addr;
    //判断地址数据是否获取成功
    private Callback userinfoCallback = new Callback() {
        @Override
        public void onFailure(Call call, IOException e) {
            Log.e(TAG, "onFailure: userinfoCallback");

        }

        @Override
        public void onResponse(Call call, Response response) throws IOException {
            String userinfoResult = response.body().string();
            Log.d(TAG, "onResponse: userinfoCallback\n" + userinfoResult);
            Gson gson = new Gson();
            UserInfo userinfo = gson.fromJson(userinfoResult, UserInfo.class);
            Message message = new Message();
            message.what = 5;
            message.obj = userinfo;
            mHandler.sendMessage(message);
        }
    };
    private Handler mHandler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MSG_LOAD_DATA:
                    if (thread == null) {//如果已创建就不再重新创建子线程了
                        Log.i("addr", "地址数据开始解析");
                        thread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                // 写子线程中的操作,解析省市区数据
                                initJsonData();
                            }
                        });
                        thread.start();
                    }
                    break;

                case MSG_LOAD_SUCCESS:
                    Log.i("addr", "地址数据获取成功");
                    isLoaded = true;
                    break;

                case MSG_LOAD_FAILED:
                    Log.i("addr", "地址数据获取失败");
                    break;
                case 4:
                    ChageUserInfo chageUserinfo = (ChageUserInfo) msg.obj;
                    if (chageUserinfo.getCode() == 0) {
                        isChangeInfo = true;
                        Toast.makeText(PersonInfoActivity.this, "修改信息成功", Toast.LENGTH_SHORT).show();
                        finish();
                    } else if (chageUserinfo.getCode() == 1005) {
                        new LoginPopupwindow(PersonInfoActivity.this);
                    }
                    break;
                case 5:
                    UserInfo userInfo = (UserInfo) msg.obj;
                    if (userInfo.getCode() == 0) {
                        isChangeInfo = true;
                        if (!userInfo.getData().getUserInfo().getAvatar().isEmpty()) {
                            Glide.with(PersonInfoActivity.this).load(HttpUri.BASE_DOMAIN + userInfo.getData().getUserInfo().getAvatar())
                                    .into(personinfo_circleImageview);
                        }
                        if (!userInfo.getData().getUserInfo().getUid().isEmpty()) {
                            personinfo_account.setText(userInfo.getData().getUserInfo().getUid());
                        }
                        if (!userInfo.getData().getUserInfo().getNickname().isEmpty()) {
                            personinfo_nickname.setText(userInfo.getData().getUserInfo().getNickname());
                        }
                        if (userInfo.getData().getUserInfo().getGender() == 1) {
                            personinfo_sex.setText(getString(R.string.sex_man));
                        } else {
                            personinfo_sex.setText(getString(R.string.sex_woman));
                        }
                        if (!userInfo.getData().getUserInfo().getSignature().isEmpty()) {
                            personinfo_personnalname.setText(userInfo.getData().getUserInfo().getSignature());
                        } else {
                            if (!userInfo.getData().getUserInfo().getUid().isEmpty()) {
                                personinfo_personnalname.setText(userInfo.getData().getUserInfo().getUid());
                            }
                        }
                        if (!userInfo.getData().getUserInfo().getAddress().isEmpty()) {
                            personinfo_city.setText(userInfo.getData().getUserInfo().getAddress());
                        }
                        if (!userInfo.getData().getUserInfo().getBirthday().isEmpty()) {
                            personinfo_birthday.setText(userInfo.getData().getUserInfo().getBirthday());
                        }
                    } else if (userInfo.getCode() == 1005) {
                        new LoginPopupwindow(PersonInfoActivity.this);
                    }
                    break;

            }
        }
    };
    private ImageView personinfo_back;
    private Button personinfo_save;

    @Override
    protected int getLayout() {
        return R.layout.personinfo_layout;
    }

    @Override
    protected void initEventAndData() {
        okHttpUtil = ((MyApplication) getApplication()).getOkHttpUtil();
        initView();
        initData();
        mHandler.sendEmptyMessage(MSG_LOAD_DATA);
        setOnClickListner();
    }

    private void initData() {
        okHttpUtil.sendGetRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_PERSONINFO,
                ((MyApplication) getApplicationContext()).getMaps(), userinfoCallback);
    }

    private void setOnClickListner() {
        //personinfo_account.setOnClickListener(this);
        personinfo_nickname.setOnClickListener(this);
        personinfo_city.setOnClickListener(this);
        personinfo_personnalname.setOnClickListener(this);
        personinfo_birthday.setOnClickListener(this);
        personinfo_sex.setOnClickListener(this);
        personinfo_back.setOnClickListener(this);
        personinfo_save.setOnClickListener(this);
    }

    private void initView() {
        personinfo_circleImageview = (CircleImageView) findViewById(R.id.personinfo_picture);
        personinfo_circleImageview.setOnClickListener(this);
        personinfo_birthday = (TextView) findViewById(R.id.et_personinfo_birthday);
        personinfo_city = (TextView) findViewById(R.id.et_personinfo_city);
        personinfo_account = (TextView) findViewById(R.id.et_personinfo_account);
        personinfo_nickname = (EditText) findViewById(R.id.et_personinfo_nickname);
        personinfo_personnalname = (EditText) findViewById(R.id.et_personinfo_personnalname);
        personinfo_sex = (TextView) findViewById(R.id.et_personinfo_sex);
        personinfo_back = (ImageView) findViewById(R.id.personinfo_back);
        personinfo_save = (Button) findViewById(R.id.personinfo_save);

    }

    public void startcutzoom(Uri uri) {
        String dateFolder = new SimpleDateFormat("yyyyMMddHHmmss", Locale.CHINA)
                .format(new Date());
        mCropImageFile = new File(Environment.getExternalStorageDirectory(),   //创建一个保存裁剪后照片的file
                dateFolder + "_" + "crop_image.jpg");
        // 裁剪图片意图
        Intent intent = new Intent("com.android.camera.action.CROP");

        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // 裁剪框的比例，1：1
        intent.putExtra("aspectX", 1);         //X方向上的比列
        intent.putExtra("aspectY", 1);         // Y方向上的比例
        intent.putExtra("outputX", 100);       //裁剪区的宽度
        intent.putExtra("outputY", 100);       //裁剪区的高度

        intent.putExtra("outputFormat", "JPEG");// 图片格式
        intent.putExtra("noFaceDetection", true);// 取消人脸识别
        intent.putExtra("return-data", false);  //是否在Intent中返回数据
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        }
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(mCropImageFile));
        startActivityForResult(intent, 3);

    }

    @Override
    public void popOnClick(int id) {
        switch (id) {
            case R.id.bt_capture:
                Intent intent_capture = new Intent("android.media.action.IMAGE_CAPTURE");
                tempFile = new File(Environment.getExternalStorageDirectory(),
                        PHOTO_FILE_NAME);
                Uri uri;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                    uri = FileProvider.getUriForFile(
                            this, "com.am.shortVideo",
                            tempFile);
                    intent_capture.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION | Intent.FLAG_GRANT_WRITE_URI_PERMISSION);
                } else {
                    uri = Uri.fromFile(tempFile);
                }
                intent_capture.putExtra(MediaStore.EXTRA_OUTPUT, uri);
                startActivityForResult(intent_capture, 2);
                Log.d(TAG, "popOnClick:bt_capture ");
                break;
            case R.id.bt_album:
                Intent intent = new Intent(Intent.ACTION_PICK);
                intent.setType("image/*");
                startActivityForResult(intent, 1);
                Log.d(TAG, "popOnClick:bt_album ");
                break;
            case R.id.bt_cancel:
                Log.d(TAG, "popOnClick: ");
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case 1:
                Log.d(TAG, "onActivityResult: -->album");
                if (resultCode == RESULT_OK) {
                    Uri uri = data.getData();
                    startcutzoom(uri);
                }
                break;
            case 2:
                Log.d(TAG, "onActivityResult: -->capture");
                if (resultCode == RESULT_OK) {
                    Uri uri;
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        uri = FileProvider.getUriForFile(
                                this, "com.am.shortVideo",
                                tempFile);
                    } else {
                        uri = Uri.fromFile(tempFile);
                    }
                    startcutzoom(uri);
                }
                break;
            case 3:
                if (resultCode == RESULT_OK) {
                    loadImgFile(mCropImageFile.getAbsolutePath());
                    Log.d(TAG, "onstartzoomsucesss: ");
                    //Glide.with(this).load(Uri.fromFile(mCropImageFile)).into(personinfo_circleImageview);
                } else {
                    Log.d(TAG, "onstartzoomfailed: ");
                }


        }

    }

    private void initJsonData() {//解析数据

        /**
         * 注意：assets 目录下的Json文件仅供参考，实际使用可自行替换文件
         * 关键逻辑在于循环体
         *
         * */
        String CityData = new GetJsonDataUtil().getJson(this, "province.json");//获取assets目录下的json文件数据

        ArrayList<CityBean> jsonBean = parseData(CityData);//用Gson 转成实体

        /**
         * 添加省份数据
         *
         * 注意：如果是添加的JavaBean实体，则实体类需要实现 IPickerViewData 接口，
         * PickerView会通过getPickerViewText方法获取字符串显示出来。
         */
        options1Items = jsonBean;

        for (int i = 0; i < jsonBean.size(); i++) {//遍历省份
            ArrayList<String> CityList = new ArrayList<>();//该省的城市列表（第二级）
            ArrayList<ArrayList<String>> Province_AreaList = new ArrayList<>();//该省的所有地区列表（第三极）

            for (int c = 0; c < jsonBean.get(i).getCity_list().size(); c++) {//遍历该省份的所有城市
                String CityName = jsonBean.get(i).getCity_list().get(c);
                CityList.add(CityName);//添加城市

            }

            /**
             * 添加城市数据
             */
            options2Items.add(CityList);
            Log.d(TAG, "initJsonData: ");
        }

        mHandler.sendEmptyMessage(MSG_LOAD_SUCCESS);

    }

    public ArrayList<CityBean> parseData(String result) {//Gson 解析
        ArrayList<CityBean> detail = new ArrayList<>();
        try {
            JSONArray data = new JSONArray(result);
            Gson gson = new Gson();
            for (int i = 0; i < data.length(); i++) {
                CityBean entity = gson.fromJson(data.optJSONObject(i).toString(), CityBean.class);
                detail.add(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
            mHandler.sendEmptyMessage(MSG_LOAD_FAILED);
        }
        return detail;
    }

    public void loadImgFile(String path) {
        okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_USERIMG
                , ((MyApplication) getApplicationContext()).getMaps(), path, new Callback() {
                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e(TAG, "onFailure: ");
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        Log.d(TAG, "onResponse: ");
                        response.headers();
                        String loadupFile = response.body().string();
                        Log.d(TAG, "onResponse: \n" + loadupFile);
                        Gson gson = new Gson();
                        final UserIMG userimg = gson.fromJson(loadupFile, UserIMG.class);
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (userimg.getMessage().contains("成功")) {
                                    Glide.with(PersonInfoActivity.this).load(HttpUri.BASE_DOMAIN + userimg.getData().getAvatarUrl())
                                            .into(personinfo_circleImageview);
                                }
                            }
                        });
                    }
                });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.personinfo_picture:
                MyPopUpWindow popupWindow = new MyPopUpWindow(this);
                popupWindow.setPopOnClickLinstener(this);
                break;
            case R.id.et_personinfo_account:
                new EditInfoPopupwindow(this, 0).setOnTextStringLinstener(this);
                break;
//            case R.id.et_personinfo_nickname:
//                new EditInfoPopupwindow(this, 1).setOnTextStringLinstener(this);
//                break;
            case R.id.et_personinfo_birthday:
                initTimePicker1();
                break;
            case R.id.et_personinfo_city:
                initCityPicker();
                break;
            case R.id.et_personinfo_sex:
                new SexPopupWindow(this).setOnClickLinstenr(this);
                break;
//            case R.id.et_personinfo_personnalname:
//                new EditInfoPopupwindow(this, 2).setOnTextStringLinstener(this);
//                break;
            case R.id.personinfo_back:
                if (isChangeInfo) {
                    EventBus.getDefault().post(MessageWrap.getInstance("true"));
                }
                finish();
                break;
            case R.id.personinfo_save:
                HashMap<String, String> maps = new HashMap<>();
                maps.put("nickname", personinfo_nickname.getText().toString());
                if (personinfo_sex.getText().toString().equals("女")) {
                    maps.put("gender", "2");
                } else if (personinfo_sex.getText().toString().equals("男")) {
                    maps.put("gender", "1");
                }
                maps.put("signature", personinfo_personnalname.getText().toString());
                maps.put("birthday", personinfo_birthday.getText().toString());
                maps.put("address", personinfo_city.getText().toString());
                okHttpUtil.setPostRequest(HttpUri.BASE_URL + HttpUri.PersonInfo.REQUEST_HEADER_CHANGEUSERINFO
                        , ((MyApplication) getApplication()).getMaps(), maps, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                Log.d(TAG, "onFailure: ");
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String changeResult = response.body().string();
                                Log.d(TAG, "onResponse: \n" + changeResult);
                                Gson gson = new Gson();
                                ChageUserInfo changeUserinfo = gson.fromJson(changeResult, ChageUserInfo.class);
                                Message message = new Message();
                                message.what = 4;
                                message.obj = changeUserinfo;
                                mHandler.sendMessage(message);
                            }
                        });
                break;
            default:
        }
    }


    private void initTimePicker1() {//选择出生年月日
        //控制时间范围(如果不设置范围，则使用默认时间1900-2100年，此段代码可注释)
        //因为系统Calendar的月份是从0-11的,所以如果是调用Calendar的set方法来设置时间,月份的范围也要是从0-11
        Date curDate = new Date(System.currentTimeMillis());//获取当前时间
        SimpleDateFormat formatter_year = new SimpleDateFormat("yyyy ");
        String year_str = formatter_year.format(curDate);
        int year_int = (int) Double.parseDouble(year_str);


        SimpleDateFormat formatter_mouth = new SimpleDateFormat("MM ");
        String mouth_str = formatter_mouth.format(curDate);
        int mouth_int = (int) Double.parseDouble(mouth_str);

        SimpleDateFormat formatter_day = new SimpleDateFormat("dd ");
        String day_str = formatter_day.format(curDate);
        int day_int = (int) Double.parseDouble(day_str);


        Calendar selectedDate = Calendar.getInstance();//系统当前时间
        Calendar startDate = Calendar.getInstance();
        startDate.set(1900, 0, 1);
        Calendar endDate = Calendar.getInstance();
        endDate.set(year_int, mouth_int - 1, day_int);

        //时间选择器
        pvTime1 = new TimePickerView.Builder(this, new TimePickerView.OnTimeSelectListener() {
            @Override
            public void onTimeSelect(Date date, View v) {//选中事件回调
                // 这里回调过来的v,就是show()方法里面所添加的 View 参数，如果show的时候没有添加参数，v则为null
                /*btn_Time.setText(getTime(date));*/
                personinfo_birthday.setText(getTime(date));
            }
        })

                .setType(new boolean[]{true, true, true, false, false, false}) //年月日时分秒 的显示与否，不设置则默认全部显示
                .setLabel("年", "月", "日", "", "", "")//默认设置为年月日时分秒
                .setSubmitText("确定")
                .setCancelText("取消")
                .setSubCalSize(18)
                .isCenterLabel(false)
                .setContentSize(21)
                .setDate(selectedDate)
                .setLineSpacingMultiplier(1.2f)
                .setTextXOffset(-10, 0, 10, 0, 0, 0)//设置X轴倾斜角度[ -90 , 90°]
                .setRangDate(startDate, endDate)
//                .setBackgroundId(0x00FFFFFF) //设置外部遮罩颜色
                .setDecorView(null)
                .build();
        pvTime1.show();
    }

    private String getTime(Date date) {//可根据需要自行截取数据显示
//        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        return format.format(date);

    }

    public void initCityPicker() {
        OptionsPickerView pvOptions = new OptionsPickerView.Builder(this, new OptionsPickerView.OnOptionsSelectListener() {
            @Override
            public void onOptionsSelect(int options1, int options2, int options3, View v) {
                //返回的分别是三个级别的选中位置
                province = options1Items.get(options1).getPickerViewText();
                city = options2Items.get(options1).get(options2);
//                        + options3Items.get(options1).get(options2).get(options3);
                personinfo_city.setText(province + city);
            }
        })
                .setSubmitText("确定")
                .setCancelText("取消")
                .setDividerColor(Color.BLACK)
                .setTextColorCenter(Color.BLACK) //设置选中项文字颜色
                .setContentTextSize(20)
                .setOutSideCancelable(false)// default is true
                .build();


        pvOptions.setPicker(options1Items, options2Items);//二级选择器（市区）
//pvOptions.setPicker(options1Items, options2Items, options3Items);//三级选择器
        pvOptions.show();


    }

    @Override
    public void onviewclick(int id) {
        switch (id) {
            case R.id.select_man:
                personinfo_sex.setText(getString(R.string.sex_man));
                break;
            case R.id.select_woman:
                personinfo_sex.setText(getString(R.string.sex_woman));
                break;
        }
    }

    @Override
    public void getTextString(View v, int editType, String info) {
        switch (editType) {
            case 0:
                if (!info.isEmpty()) {
                    personinfo_account.setText(info);
                }
                break;
            case 1:
                if (!info.isEmpty()) {
                    personinfo_nickname.setText(info);
                }
                break;
            case 2:
                if (!info.isEmpty()) {
                    personinfo_personnalname.setText(info);
                }
                break;
        }
    }
}
