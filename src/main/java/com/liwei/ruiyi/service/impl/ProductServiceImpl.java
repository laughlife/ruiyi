package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TSeller;
import com.liwei.ruiyi.config.LingxingConfig;
import com.liwei.ruiyi.dao.TSellerDao;
import com.liwei.ruiyi.service.ProductService;
import com.liwei.ruiyi.service.LingxingService;
import com.liwei.ruiyi.utils.DateUtils;
import com.liwei.ruiyi.utils.ListUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

@Repository("feeService")
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    LingxingService lingxingService;

    @Autowired
    TSellerDao sellerDao;


    @Override
    public boolean getOrRefreshProductPerformance(String start_date, String end_date) {

        List<String> dates = getDates(start_date, end_date);

        List<TSeller> sellers = sellerDao.queryAllSellers();
        List<Integer> sidList = new ArrayList<>();
        for (TSeller s : sellers) {
            sidList.add(s.getSid());
        }
        for (String day : dates) {
            List<List<Integer>> ss = ListUtils.splitIntoChunks(sidList, 200);
            for (List<Integer> sid : ss) {
                analyzeProject(day, sid);
            }
        }

        return false;
    }

    public void analyzeProject(String day, List<Integer> sid) {
        for(Integer s : sid){
            JSONArray array = new JSONArray();
            array.add(s);

            JSONObject args = new JSONObject();
            args.put("offset", 0);
            //分页长度，最大10000,默认20
            args.put("length", 10000);
            args.put("sid", array);
            //盲猜是按照交易量排序
            args.put("sort_field", "volume");
            args.put("sort_type", "desc");
            args.put("start_date", day);
            args.put("end_date", day);
            args.put("summary_field", "asin");
            args.put("is_recently_enum", true);

            args.put("field", "volume");
            args.put("exp", "lt");
            args.put("from_value", 0);

            System.err.println(DateUtils.getSystemTime());
            JSONObject data = lingxingService.post(LingxingConfig.get_product_performance, args);
            System.out.println(data);
            try {
                Thread.sleep(1000L*10);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
            break;
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
}
