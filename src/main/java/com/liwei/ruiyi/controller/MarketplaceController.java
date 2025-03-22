package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.service.LingxingService;
import com.liwei.ruiyi.service.MarketplaceService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/marketplace")
public class MarketplaceController {

    @Autowired
    MarketplaceService marketplaceService;

    @RequestMapping("/refreshMarketplace")
    @ResponseBody
    public String refreshMarketplace() {
        //查询得到亚马逊所有市场列表数据
        JSONObject rj = new JSONObject();
        boolean refreshStatus = marketplaceService.refreshMarketplace();
        rj.put("status", refreshStatus);
        rj.put("msg", refreshStatus?"市场表刷新成功":"市场表刷新失败");
        return rj.toJSONString();
    }

    @RequestMapping("/marketplace_list")
    @ResponseBody
    public String marketplaceList() {
        //查询得到亚马逊对应国家的地区列表数据
        JSONObject rj = new JSONObject();
        boolean checkStatus = marketplaceService.checkAllMarketplaceWorldState();
        rj.put("status", checkStatus);
        rj.put("msg", checkStatus?"市场表刷新成功":"市场表刷新失败");
        return rj.toJSONString();
    }


}
