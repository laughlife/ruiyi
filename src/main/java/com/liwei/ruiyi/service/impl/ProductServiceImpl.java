package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TSeller;
import com.liwei.ruiyi.config.LingxingConfig;
import com.liwei.ruiyi.dao.TProHistoryDao;
import com.liwei.ruiyi.dao.TProPerformanceDao;
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

@Repository("productService")
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    @Autowired
    LingxingService lingxingService;

    @Autowired
    TSellerDao sellerDao;

    @Autowired
    TProPerformanceDao proPerformanceDao;

    @Autowired
    TProHistoryDao proHistoryDao;

    @Override
    public boolean getOrRefreshProductPerformance(String start_date, String end_date) {

        List<String> dates = getDates(start_date, end_date);
        logger.info("开始获取产品表现数据：{}",DateUtils.getSystemTime());
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
        logger.info("产品表现数据获取结束：{}",DateUtils.getSystemTime());
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
            System.out.println("");
            System.out.println("-------------------------------------------------------------------");
            System.out.println("参数："+args.toString());
            System.out.println("请求时间为："+DateUtils.getSystemTime());
            JSONObject pro_json = lingxingService.post(LingxingConfig.get_product_performance, args);
            System.out.println("返回结果时间为："+DateUtils.getSystemTime());
            if(pro_json.getInteger("code")==0){
                System.out.println("【成功】");
                //数据获取成功
                JSONObject data = pro_json.getJSONObject("data");
                String chain_start_date = data.getString("chain_start_date");
                String chain_end_date = data.getString("chain_end_date");
                String available_inventory_formula_zh = data.getString("available_inventory_formula_zh");
                JSONArray list = data.getJSONArray("list");
                for (int i = 0; i < list.size(); i++) {
                    JSONObject obj = list.getJSONObject(i);
                    JSONArray _tempArray = obj.getJSONArray("sids");
                    JSONObject _tempJson;
                    Integer get_sid = _tempArray.getInteger(0);
                    obj.remove("sids");
                    //拆分父ASIN
                    _tempArray = obj.getJSONArray("parent_asins");
                    _tempJson = _tempArray.getJSONObject(0);
                    String parent_asin = _tempJson.getString("parent_asin");
                    obj.remove("parent_asins");
                    //拆分ASIN
                    _tempArray = obj.getJSONArray("asins");
                    _tempJson = _tempArray.getJSONObject(0);
                    String asin = _tempJson.getString("asin");
                    obj.remove("asins");
                    //产品历史价格快照
                    _tempArray = obj.getJSONArray("price_list");
                    for(int j = 0; j < _tempArray.size(); j++){
                        _tempJson = _tempArray.getJSONObject(j);
                        _tempJson.put("query_date", day);
                        _tempJson.put("asin", asin);
                        proHistoryDao.saveOrUpdateProHistory(_tempJson);
                    }
                    obj.remove("price_list");
                    //产品表现asin维度
                    _tempArray = obj.getJSONArray("seller_store_countries");
                    _tempJson = _tempArray.getJSONObject(0);
                    String country = _tempJson.getString("country");
                    String seller_name = _tempJson.getString("seller_name");
                    obj.remove("seller_store_countries");
                    //这个字段使用不上，直接移除掉
                    obj.remove("has_oprator_log");

                    obj.put("sid", get_sid);
                    obj.put("parent_asin", parent_asin);
                    obj.put("asin", asin);
                    obj.put("query_date", day);
                    obj.put("chain_start_date", chain_start_date);
                    obj.put("chain_end_date", chain_end_date);
                    obj.put("available_inventory_formula_zh", available_inventory_formula_zh);
                    obj.put("country", country);
                    obj.put("seller_name", seller_name);

                    proPerformanceDao.saveOrUpdateProPerformance(obj);
                }

            }else{
                //数据获取失败
                logger.info("请求失败:{}", pro_json.toString());
            }
            try {
                Thread.sleep(1000L*3);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
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
