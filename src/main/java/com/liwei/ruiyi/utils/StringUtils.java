package com.liwei.ruiyi.utils;

import java.util.UUID;

public class StringUtils {
    public static String getRandomString() {
        String uuid = UUID.randomUUID().toString();
        uuid = uuid.replaceAll("-", "");
        return uuid;
    }
}
