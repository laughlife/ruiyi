package com.liwei.ruiyi.service.impl;


import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TLxToken;
import com.liwei.ruiyi.config.LingxingConfig;
import com.liwei.ruiyi.dao.TLxTokenDao;
import com.liwei.ruiyi.service.LingxingService;
import com.liwei.ruiyi.utils.ReadProUtils;
import okhttp3.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import com.liwei.ruiyi.utils.CheckUtils;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.concurrent.TimeUnit;

@Repository("lingxingService")
public class LingxingServiceImpl implements LingxingService {

    private String appid = ReadProUtils.ReadProperties("lingxing.api.appid");
    private String appSecret = ReadProUtils.ReadProperties("lingxing.api.appsecret");

    private String getIpUrl = ReadProUtils.ReadProperties("getIpUrl");
    private String apiUrl = ReadProUtils.ReadProperties("apiUrl");

    OkHttpClient client = new OkHttpClient.Builder()
            .retryOnConnectionFailure(true)
            .connectTimeout(10, TimeUnit.SECONDS)
            .readTimeout(10, TimeUnit.SECONDS)
            .writeTimeout(10, TimeUnit.SECONDS)
            .build();

    @Autowired
    TLxTokenDao lxTokenDao;

    @Override
    public String getIp() {
        Request request = new Request.Builder()
                .url(getIpUrl)
                .get()
                .build();
        JSONObject json = new JSONObject();
        try (Response response = client.newCall(request).execute()) {
            String answer = response.body().string();
            json = JSONObject.parseObject(answer);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        String result = "";
        if (!CheckUtils.isEmpty(json)) {
            //{"code":1,"msg":"操作成功","data":"真实的Ip是: 42.229.234.250","traceId":"ce5ece27-7329-4e79-99bf-28fb72976e5d","success":true}
            String ip = json.getString("data");
            if (StringUtils.isNotBlank(ip)) {
                result = ip.substring(ip.lastIndexOf(":") + 1);
            }
        }
        return result;
    }

    @Override
    public boolean getOrRefreshToken() {

        int count = lxTokenDao.getTokenCount();
        if (count > 0) {
            TLxToken token = lxTokenDao.getToken();
        } else {
            //获取token
            TLxToken token = getTokenByNet();
            if (token == null) {
                return false;
            } else {
                return lxTokenDao.insertToken(token) > 0;
            }

        }
        return false;
    }


    private TLxToken getTokenByNet() {
        TLxToken token = new TLxToken();
        String fullUrl = apiUrl + LingxingConfig.getTokenPath;
        RequestBody formBody = new MultipartBody.Builder()
                .setType(MultipartBody.FORM)
                .addFormDataPart("appId", appid)
                .addFormDataPart("appSecret", appSecret)
                .build();
        // 构造请求
        Request request = new Request.Builder()
                .url(fullUrl)
                .post(formBody)
                .header("Content-Type", "multipart/form-data")
                .build();

        // 发送请求并处理响应
        JSONObject resultJson = new JSONObject();
        try (Response response = client.newCall(request).execute()) {
            if (!response.isSuccessful()) {
                throw new IOException("请求失败: " + response);
            }
            ResponseBody responseBody = response.body();
            if (responseBody != null) {
                String result = responseBody.string();
                resultJson = JSONObject.parseObject(result);
            } else {
                System.out.println("响应为空");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        //获取token
        String code = resultJson.getString("code");
        if ("200".equals(code)) {
            JSONObject data = resultJson.getJSONObject("data");
            String accessToken = data.getString("access_token");
            String refreshToken = data.getString("refresh_token");
            int expireTime = data.getIntValue("expires_in");

            token.setAccessToken(accessToken);
            token.setRefreshToken(refreshToken);
            long currentTime = System.currentTimeMillis();
            long expiresTime = currentTime + expireTime * 1000;
            token.setSaveTime(currentTime);
            token.setExpiresTime(expiresTime);
            //存储token
        } else {
            return null;
        }
        return token;
    }
}
