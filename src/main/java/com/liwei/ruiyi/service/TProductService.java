package com.liwei.ruiyi.service;

import org.springframework.stereotype.Service;

@Service
public interface TProductService {
    boolean getOrRefreshProductPerformance(String start_date, String end_date);
}
