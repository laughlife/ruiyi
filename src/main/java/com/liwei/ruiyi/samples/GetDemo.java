package com.liwei.ruiyi.samples;

import com.liwei.ruiyi.core.HttpMethod;
import com.liwei.ruiyi.core.HttpRequest;
import com.liwei.ruiyi.core.HttpResponse;
import com.liwei.ruiyi.okhttp.HttpExecutor;
import com.liwei.ruiyi.sign.ApiSign;
import lombok.extern.slf4j.Slf4j;

import java.util.HashMap;
import java.util.Map;

@Slf4j
public class GetDemo {

    public static void main(String[] args) throws Exception {
        String appId = "xxx";

        Map<String, Object> queryParam = new HashMap<>();
        queryParam.put("timestamp", 1639734344);
        queryParam.put("access_token", "59cf5437-669b-49f5-83c4-3cc1d1404680");
        queryParam.put("app_key", appId);
//        queryParam.put("offset", 0);
//        queryParam.put("length", 20);

        String sign = ApiSign.sign(queryParam, appId);
        queryParam.put("sign", sign);
        log.info("sign:{}", sign);

        HttpRequest<Object> build = HttpRequest.builder(Object.class)
                .method(HttpMethod.GET)
                .endpoint("xxxx")
                .path("erp/sc/data/local_inventory/brand")
                .queryParams(queryParam)
                .build();
        HttpResponse execute = HttpExecutor.create().execute(build);
        log.info("execute:{}", execute.readEntity(Object.class));
    }
}
