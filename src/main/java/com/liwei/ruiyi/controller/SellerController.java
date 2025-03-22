package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.service.MarketplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import com.liwei.ruiyi.service.SellerService;

@Controller
@RequestMapping("/seller")
public class SellerController {

    @Autowired
    SellerService sellerService;

    @RequestMapping("/seller_list")
    @ResponseBody
    public String seller_list() {
        //查询得到企业已授权到领星ERP的全部亚马逊店铺信息
        JSONObject rj = new JSONObject();
        boolean refreshStatus = sellerService.saveOrUpdate();
        rj.put("status", refreshStatus);
        rj.put("msg", refreshStatus?"店铺信息刷新成功":"店铺信息刷新失败");
        return rj.toJSONString();
    }
}
