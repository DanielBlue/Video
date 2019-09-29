package util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JC on 2019/9/2.
 */

public class FormatTime {
    public static String formatTime(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd hh:mm");
        Date date = new Date(time);
        String formatvalue = simpleDateFormat.format(date);
        return formatvalue;
    }

    public static String formatYHDhm(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
        Date date = new Date(time);
        String formatvalue = simpleDateFormat.format(date);
        return formatvalue;
    }

    public static String formatDuration(int time) {
        String duration;
        int minute = (time / 60);
        int second = time % 60;
        if (minute < 10) {
            if (second >= 10) {
                duration = "0" + minute + ":" + second;

            } else {
                duration = "0" + minute + ":" + "0" + second;
            }
        } else {
            if (second >= 10) {
                duration = minute + ":" + second;

            } else {
                duration = minute + ":" + "0" + second;
            }
        }
        return duration;
    }
}
