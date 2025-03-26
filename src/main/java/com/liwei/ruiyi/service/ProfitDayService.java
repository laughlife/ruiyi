package com.liwei.ruiyi.service;

import org.springframework.stereotype.Service;

@Service
public interface ProfitDayService {
    boolean getProfitReport(String startDate, String endDate);
}
