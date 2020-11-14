package com.cn.graduationclient.xingcmyAdapter;

import android.net.ParseException;
import android.util.Log;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class TimeStampUtils {

    /**
     * 获取当前时间戳
     * @return
     */
    public static String getTimeStamp() {
        long timeStamp = System.currentTimeMillis();
        //String time = stampToMinute(timeStamp);
        return String.valueOf(timeStamp);
    }

    /**
     * 获取当前时间
     * @return
     */
    public static int[] getTime() {
        int[] time = new int[5];
        //获取当前时间
        Calendar c = Calendar.getInstance();//可以对每个时间域单独修改
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int date = c.get(Calendar.DATE);
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);
        int second = c.get(Calendar.SECOND);
        time[0] = year;
        time[1] = month;
        time[2] = date;
        time[3] = hour;
        time[4] = minute;
        Log.d("xxxxx", year + "/" + (month + 1) + "/" + date + " " + hour + ":" + minute + ":" + second);
        return time;
    }


    /*
     * 将时间戳转换为时间
     */
    public static String stampToDate(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }

    /*
     * 将时间戳转换为时间
     */
    public static String stampToMinute(long timeMillis) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("mm");
        Date date = new Date(timeMillis);
        return simpleDateFormat.format(date);
    }


    /*
     * 将时间转换为时间戳
     */
    public String dateToStamp(String time) throws ParseException, java.text.ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = simpleDateFormat.parse(time);
        long ts = date.getTime();
        return String.valueOf(ts);
    }

    public static String dateTosStamp(String time) throws ParseException, java.text.ParseException {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("ss");
        Date date = simpleDateFormat.parse(time);
        long ts = date.getTime();
        return String.valueOf(ts);
    }

    public static long Date2ms(String _data){
        SimpleDateFormat format =   new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        try {
            Date date = format.parse(_data);
            return date.getTime();
        }catch(Exception e){
            return 0;
        }
    }

}
