package com.tiktokdemo.lky.tiktokdemo;

import android.os.Environment;

import java.io.File;

import application.MyApplication;

/**
 * Created by lky on 2018/12/11
 */
public class Constant {

    public static final String APP_LOCAL_DIR = File.separator + "ShiBo";
    public static final String PIC_FILE = Environment.getExternalStorageDirectory().getAbsolutePath() + APP_LOCAL_DIR;
    public static final String DIC_FILE = MyApplication.mContext.getCacheDir().getAbsolutePath() + APP_LOCAL_DIR;

    public static final String RECORD_AUDIO_CACHE_PATH_TEMP = DIC_FILE + File.separator + "record_audio_cache";
    public static final String IMAGE_SAVE_PATH = PIC_FILE + File.separator + "image";
    public static final String RECORD_VIDEO_PATH_TEMP = DIC_FILE + File.separator + "record_video";
//    public static final String RECORD_VIDEO_PATH_TEMP = PIC_FILE + File.separator + "record_video1";
    public static final String RECORD_VIDEO_PATH_TEMP1 = PIC_FILE + File.separator + "record_video1";
    public static final String RECORD_VIDEO_PATH = PIC_FILE + File.separator + "record_video";
    public static final String CUT_AUDIO_CACHE_PATH = PIC_FILE + File.separator + "cut";
    public static final String RECORD_CROP_PHOTO_CACHE_PATH = DIC_FILE + File.separator + "record_crop";
    public static final String RECORD_VIDEO_TEMP_PATH = PIC_FILE + File.separator + "record_video_temp";
    public static final String SPEED_AUDIO_CACHE_PATH = PIC_FILE + File.separator + "speed_audio";
    public static final String DOWNBGM = PIC_FILE + File.separator + "audio";
}
