package com.liwei.ruiyi.dao;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface TProPerformanceDao {
    void saveOrUpdateProPerformance(JSONObject obj);
}
