package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.config.LingxingConfig;
import com.liwei.ruiyi.service.LingxingService;
import com.liwei.ruiyi.service.ProfitDayService;
import com.liwei.ruiyi.dao.TProfitDayDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Repository("profitDayService")
public class ProfitDayServiceImpl implements ProfitDayService {

    private static final Logger logger = LoggerFactory.getLogger(ProfitDayServiceImpl.class);

    @Autowired
    LingxingService lingxingService;

    @Autowired
    private TProfitDayDao profitDayDao;

    @Override
    public boolean getProfitReport(String startDate, String endDate) {
        boolean result = false;
        List<String> dates = getDates(startDate, endDate);
        for(String date : dates) {
            JSONObject args = new JSONObject();
            args.put("offset", 0);
            //最大10000，现在先不搜索那么多，搜索太多了内存可能承受不住。
            args.put("length", 10000);
            args.put("monthlyQuery", false);
            args.put("startDate", date);
            args.put("endDate", date);
            args.put("currencyCode", "USD");
            args.put("summaryEnabled", true);
            args.put("orderStatus", "All");
            JSONObject data = lingxingService.post(LingxingConfig.profit_report, args);
            if (data != null && data.getInteger("code") == 0) {
                JSONObject datas = data.getJSONObject("data");
                JSONArray array = datas.getJSONArray("records");
                if (array.size() > 0) {
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject profit = array.getJSONObject(i);
                        profit.put("profit_day", date);
                        profitDayDao.saveOrUpdateProfitReport(profit);
                    }
                }
            } else if (data != null && data.getInteger("code") != 0) {
                logger.info("领星回执的错误信息:{}", data.toString());
                result = false;
            } else {
                result = false;
            }

            break;
        }
        return result;
    }

    /**
     * 解析日期字符串，支持空值和错误格式
     */
    private LocalDate parseDate(String dateStr) {
        if (dateStr == null || dateStr.trim().isEmpty()) {
            return null;
        }
        try {
            return LocalDate.parse(dateStr);
        } catch (DateTimeParseException e) {
            return null;
        }
    }
    private List<String> getDates(String startDate, String endDate) {
        List<String> days = new ArrayList<>();

        // 解析日期并处理空值
        LocalDate start = parseDate(startDate);
        LocalDate end = parseDate(endDate);

        // 处理两个日期都无效的情况
        if (start == null && end == null) {
            return days;
        }

        // 处理单日期场景
        if (start == null) {
            days.add(end.toString());
            return days;
        }
        if (end == null) {
            days.add(start.toString());
            return days;
        }

        // 确保时间顺序正确
        if (start.isAfter(end)) {
            LocalDate temp = start;
            start = end;
            end = temp;
        }

        // 添加日期范围（包含同一天场景）
        LocalDate current = start;
        while (!current.isAfter(end)) {
            days.add(current.format(java.time.format.DateTimeFormatter.ISO_DATE));
            current = current.plusDays(1);
        }
        return days;
    }
}
