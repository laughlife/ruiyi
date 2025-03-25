package com.liwei.ruiyi.sign;

import org.apache.commons.codec.digest.DigestUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Arrays;

public class ApiSign {

    private static final Logger logger = LoggerFactory.getLogger(ApiSign.class);

    public static String sign(Map<String,Object> params, String appSecret) {
        // 参数排序
        String[] keys = params.keySet().toArray(new String[0]);
        Arrays.sort(keys);
        StringBuilder paramNameValue = new StringBuilder();
        for (String key : keys) {
            String value = String.valueOf(params.get(key));
            paramNameValue.append(key).append("=").append(value.trim()).append("&");
        }
        String paramValue = paramNameValue.toString();
        if (paramValue.endsWith("&")) {
            paramValue = paramValue.substring(0, paramValue.length() - 1);
        }
        // md5加密
        String md5Hex = DigestUtils.md5Hex(paramValue.getBytes(StandardCharsets.UTF_8)).toUpperCase();
        return AesUtil.encryptEcb(md5Hex, appSecret);
    }
}
