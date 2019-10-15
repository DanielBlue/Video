package util;

import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import application.MyApplication;


/**
 * sp工具类
 */
public final class SpUtils {

    private SpUtils() {
    }

    /**
     * 获得SharedPreferences对象
     *
     * @return
     */
    private static SharedPreferences getSharedPreferences() {
        return PreferenceManager.getDefaultSharedPreferences(
                MyApplication.mContext);
    }

    /**
     * 获得Editor对象
     *
     * @return
     */
    private static SharedPreferences.Editor getEditor() {
        return getSharedPreferences().edit();
    }

    // 存储
    public static void putBoolean(String key, boolean value) {
        getEditor().putBoolean(key, value).apply();
    }

    public static void putInt(String key, int value) {
        getEditor().putInt(key, value).apply();
    }

    public static void putLong(String key, long value) {
        getEditor().putLong(key, value).apply();
    }

    public static void putString(String key, String value) {
        getEditor().putString(key, value).apply();
    }

    public static void remove(String key) {
        getEditor().remove(key).apply();
    }

    // 获取
    public static boolean getBoolean(String key, boolean defValue) {
        return getSharedPreferences().getBoolean(key, defValue);
    }

    public static int getInt(String key, int defValue) {
        return getSharedPreferences().getInt(key, defValue);
    }

    public static long getLong(String key, long defValue) {
        return getSharedPreferences().getLong(key, defValue);
    }

    public static String getString(String key, String defValue) {
        return getSharedPreferences().getString(key, defValue);
    }

    public static void clearAll() {
        getEditor().clear().apply();
    }
}
