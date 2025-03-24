package com.liwei.ruiyi.test;

import com.liwei.ruiyi.utils.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class test {
    public static void main(String[] args) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time = sdf.format(new Date(1742783982890L));
        System.out.println(time);
        //商品信息
    }
}
