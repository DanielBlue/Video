package util;

import android.util.Log;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;

/**
 * 获取拼音的首字母
 * Created by Administrator on 2019/1/25 0025.
 */

public class PinYinUtil {
    private String TAG = "PinYinUtil";
    StringBuilder buffer;

    public String getPinYin(String chinese) {
        char[] chars = chinese.toCharArray();
        HanyuPinyinOutputFormat hanyuPinyinOutputFormat = new HanyuPinyinOutputFormat();
        hanyuPinyinOutputFormat.setCaseType(HanyuPinyinCaseType.UPPERCASE);
        hanyuPinyinOutputFormat.setToneType(HanyuPinyinToneType.WITHOUT_TONE);
        buffer = new StringBuilder();
        for (int i = 0; i < chars.length; i++) {
            if (chars[i] > 128) {
                try {
                    buffer.append(PinyinHelper.toHanyuPinyinStringArray(chars[i], hanyuPinyinOutputFormat)[0].charAt(0));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                buffer.append(chars[i]);
            }
        }
        Log.d(TAG, "getPinYin: " + buffer.toString());
        return buffer.toString();
    }
}
