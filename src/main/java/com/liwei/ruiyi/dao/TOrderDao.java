package com.liwei.ruiyi.dao;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TSeller;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TOrderDao {

    void saveOrUpdateOrders(JSONObject order);

    void saveOrUpdateOrderItems(String orderId, JSONArray items);
}
