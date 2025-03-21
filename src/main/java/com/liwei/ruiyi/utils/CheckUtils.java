package com.liwei.ruiyi.utils;

import com.alibaba.fastjson2.JSONObject;

import java.util.List;

public class CheckUtils {
    public static boolean isEmpty(Object obj) {
        if (obj instanceof String) {
            return obj == null || ((String) obj).length() == 0;
        } else if (obj instanceof Object[]) {
            Object[] temp = (Object[]) obj;
            for (int i = 0; i < temp.length; i++) {
                if (!isEmpty(temp[i])) {
                    return false;
                }
            }
            return true;
        } else if (obj instanceof List) {
            return obj == null || ((List) obj).isEmpty();
        } else if (obj instanceof JSONObject) {
            return ((JSONObject) obj).isEmpty();
        }
        return obj == null;
    }
}
