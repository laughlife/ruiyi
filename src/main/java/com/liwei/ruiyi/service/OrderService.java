package com.liwei.ruiyi.service;

import org.springframework.stereotype.Service;

@Service
public interface OrderService {
    boolean queryOrdersByDate(String startDate, String endDate);

    boolean queryOrderDetailsByDate(String startDate, String endDate);
}
