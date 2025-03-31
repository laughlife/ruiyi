package com.liwei.ruiyi.test;

import com.liwei.ruiyi.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) {
        Date date = new Date(1743223973502L);
        System.out.println(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(date));

        //commission_amount_estimated
    }
}
