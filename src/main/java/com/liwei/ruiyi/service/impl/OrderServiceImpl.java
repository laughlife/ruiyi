package com.liwei.ruiyi.service.impl;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TSeller;
import com.liwei.ruiyi.config.LingxingConfig;
import com.liwei.ruiyi.dao.TSellerDao;
import com.liwei.ruiyi.service.LingxingService;
import com.liwei.ruiyi.service.OrderService;
import com.liwei.ruiyi.dao.TOrderDao;
import com.liwei.ruiyi.service.SellerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;
import java.util.List;

@Repository("orderService")
public class OrderServiceImpl implements OrderService {

    @Autowired
    private TOrderDao orderDao;

    @Autowired
    LingxingService lingxingService;

    @Autowired
    TSellerDao sellerDao;

    @Override
    public boolean queryOrdersByDate(String startDate, String endDate) {
        List<TSeller> sellers = sellerDao.queryAllSellers();
        boolean result = true;
        for (TSeller seller : sellers) {
            JSONObject args = new JSONObject();
            /**
             * 店铺id ，对应查询亚马逊店铺列表接口对应字段【sid】
             */
            args.put("sid", seller.getSid());
            args.put("start_date", startDate);
            args.put("end_date", endDate);
            /**
             * 查询日期类型：【默认1】
             *         1 订购时间【站点时间】
             *         2 订单修改时间【北京时间】
             *         3 平台更新时间【UTC时间】
             */
            args.put("date_type",1);
            args.put("offset",0);
            args.put("length",5000);
            args.put("sort_desc_by_date_type",0);
            args.put("fulfillment_channel",1);

            JSONObject data = lingxingService.post(LingxingConfig.query_orders, args);
            if (data != null && data.getInteger("code") == 0) {
                JSONArray array = data.getJSONArray("data");
                if (array.size() > 0) {
                    for (int i = 0; i < array.size(); i++) {
                        JSONObject order = array.getJSONObject(i);
                        String orderId = order.getString("amazon_order_id");
                        JSONArray items = order.getJSONArray("item_list");
                        orderDao.saveOrUpdateOrders(order);
                        orderDao.saveOrUpdateOrderItems(orderId, items);
                    }
                }
            }else if(data != null && data.getInteger("code") != 0){
                System.err.println("OrderServiceImpl: Line 65    message:"+data.toString());
                result = false;
                break;
            }else{
                result = false;
                break;
            }
        }
        return result;
    }
}
