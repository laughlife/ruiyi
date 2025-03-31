package com.liwei.ruiyi.service;

import org.springframework.stereotype.Service;

@Service
public interface ProductService {
    boolean getOrRefreshProductPerformance(String start_date, String end_date);
}
