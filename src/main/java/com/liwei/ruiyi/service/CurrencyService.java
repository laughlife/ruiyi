package com.liwei.ruiyi.service;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Service;

@Service
public interface CurrencyService {
    JSONObject analyseExcel(String filePath);
}
