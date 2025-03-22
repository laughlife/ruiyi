package com.liwei.ruiyi.test;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date(1742611088320L));
        System.out.println(time);

        System.out.println(time.substring(1));
    }
}
