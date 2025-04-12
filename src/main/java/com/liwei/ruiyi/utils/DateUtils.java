package com.liwei.ruiyi.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;


public class DateUtils {

    public static void main(String[] args) {
        String startDate = "";
        String endDate = "";
        endDate = DateUtils.getSystemDate();
        startDate = DateUtils.addDay(endDate, -7);
        System.out.println(startDate + "  " + endDate);
    }

    /**
     * 获取系统当前时间
     *
     * @return yyyy-MM-dd HH:mm:ss
     */
    public static String getSystemTime() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        return sdf.format(new Date());
    }

    public static String getSystemTime(String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        return sdf.format(new Date());
    }

    /**
     * 获取系统当前时间
     *
     * @return yyyy-MM-dd
     */
    public static String getSystemDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        return sdf.format(new Date());
    }

    public static String getSystemMonth() {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM");
        return sdf.format(new Date());
    }


    /**
     * 将字符串日期转换为Date类型
     *
     * @param datetime
     * @return
     */
    public static Date StrToDate(String datetime) {
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = null;
        try {
            date = format.parse(datetime);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }



    /**
     * 将日期相加制定的日期
     *
     * @param time   要转换的日期
     * @param addNum 要增加的天数
     * @return 转换好的字符串日期
     */
    public static String addDay(String time, int addNum) {
        // 把time转换成date,再把date转换成
        String[] input = time.split("-");
        int year = Integer.parseInt(input[0]);
        int month = Integer.parseInt(input[1]) - 1;
        int day = Integer.parseInt(input[2]);
        Calendar c = Calendar.getInstance();
        c.set(year, month, day);
        c.add(Calendar.DATE, addNum);
        SimpleDateFormat f = new SimpleDateFormat("yyyy-MM-dd");
        Date date = c.getTime();
        String _time = f.format(date);
        return _time;
    }



}
