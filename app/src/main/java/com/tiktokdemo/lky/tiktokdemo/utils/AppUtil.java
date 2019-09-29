package com.tiktokdemo.lky.tiktokdemo.utils;

import android.content.Context;


import application.MyApplication;

/**
 * Created by lky on 2018/12/11
 */
public class AppUtil {

    public static Context getApplicationContext(){
        return MyApplication.mContext;
    }

    public static String getString(int strRes){
        return getApplicationContext().getString(strRes);
    }

    public static int getColor(int colorRes){
        return getApplicationContext().getResources().getColor(colorRes);
    }

}
