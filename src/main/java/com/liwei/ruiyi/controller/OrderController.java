package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.*;

@Controller
@RequestMapping("/order")
public class OrderController {

    @Autowired
    public OrderService orderService;

    @Autowired
    private HttpSession session;


    @Autowired
    HttpServletRequest request;

    @RequestMapping("/queryOrders")
    @ResponseBody
    public String queryOrders(String start_date, String end_date) {
        JSONObject rj = new JSONObject();
        start_date = start_date + " 00:00:00";
        end_date = end_date + " 23:59:59";
        boolean queryStatus = orderService.queryOrdersByDate(start_date, end_date);
        rj.put("status", queryStatus);
        String msg = "订单数据同步成功，时间范围：" + start_date + "至" + end_date;
        rj.put("msg", queryStatus?msg:"订单数据同步失败");
        return rj.toJSONString();
    }

    @RequestMapping("/orderDetails")
    @ResponseBody
    public String orderDetails(String start_date, String end_date) {
        JSONObject rj = new JSONObject();
        start_date = start_date + " 00:00:00";
        end_date = end_date + " 23:59:59";
        boolean queryStatus = orderService.queryOrderDetailsByDate(start_date, end_date);
        rj.put("status", queryStatus);
        String msg = "订单数据同步成功，时间范围：" + start_date + "至" + end_date;
        rj.put("msg", queryStatus?msg:"订单数据同步失败");
        return rj.toJSONString();
    }

}
