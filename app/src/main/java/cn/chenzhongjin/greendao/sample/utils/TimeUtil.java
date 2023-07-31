package cn.chenzhongjin.greendao.sample.utils;

import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import cn.chenzhongjin.greendao.sample.database.Order;

public class TimeUtil {


    public static String formateDate(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年MM月dd日");
        return simpleDateFormat.format(new Date(time));
    }


    public static String formateDate(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy年MM月dd日");
        return format.format(date);

    }

    public static String formateDateHH(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("HH时");
        return simpleDateFormat.format(new Date(time));
    }

    public static String formateDateMMDDHH(long time) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM月dd日HH时mm分");
        return simpleDateFormat.format(new Date(time));
    }

    public static String formateDateMMDDHH(Date date) {
        SimpleDateFormat format = new SimpleDateFormat("MM月dd日HH时mm分");
        return format.format(date);

    }

    public static String getTimeScope(Order order) {
        SimpleDateFormat format = new SimpleDateFormat("HH时mm分");
        return format.format(order.getStartTime()) + " - " + format.format(order.getEndTime()) + isTheSameDay(order);
    }

    private static String isTheSameDay(Order order) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(order.getStartTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MINUTE, 0);
        Date date = calendar.getTime();
        long startDayTime = date.getTime() / 1000 * 1000;

        calendar = Calendar.getInstance();
        calendar.setTimeInMillis(order.getEndTime());
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.MINUTE, 0);
        date = calendar.getTime();
        long endDayTime = date.getTime() / 1000 * 1000;
        if (endDayTime == startDayTime) {
            return "";
        } else {
            return "(次日)";
        }
    }

    //    毫秒转小时
    public static int ms2H(long endTime, long startTime) {
        int hour = (int) ((endTime - startTime) / (1000 * 60 * 60));
        System.out.println("hour " + hour);
        return hour;
    }
}
