package com.liwei.ruiyi.utils;


import org.springframework.util.DigestUtils;

import java.nio.charset.StandardCharsets;

/**
 * 创建的MD5加密类，测试加密内容
 */
public class MD5 {
    public static String encryptToMd5(String code){
        return DigestUtils.md5DigestAsHex(code.getBytes(StandardCharsets.UTF_8));
    }

    public static void main(String[] args) throws Exception {
        String code = MD5.encryptToMd5("admin");
        System.out.println(code);
    }
}
