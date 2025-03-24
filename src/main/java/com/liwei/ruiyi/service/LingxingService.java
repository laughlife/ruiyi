package com.liwei.ruiyi.service;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TAdmin;
import org.springframework.stereotype.Service;

@Service
public interface LingxingService {
    //获取本地IP
    String getIp();

    //获取或刷新token
    boolean getOrRefreshToken();

    JSONObject get(String url, JSONObject args);

    JSONObject post(String url, JSONObject args);
}