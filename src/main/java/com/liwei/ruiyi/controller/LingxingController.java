package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.service.LingxingService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/lingxing")
public class LingxingController {
    @Autowired
    private HttpServletRequest request;

    @Autowired
    LingxingService lingxingService;

    @RequestMapping("/goSettingPage")
    public String goSettingPage() {
        return "/page/lingxing/setting";
    }

    @RequestMapping("/getIp")
    @ResponseBody
    public String getIp() {
        //获取本地IP
        JSONObject rj = new JSONObject();
        String ownIp = lingxingService.getIp();
        System.out.println("本地IP为:" + ownIp);
        rj.put("status", true);
        rj.put("msg", "方法执行成功");
        return rj.toJSONString();
    }

    @RequestMapping("/checkToken")
    @ResponseBody
    public String checkToken() {
        //获取本地IP
        JSONObject rj = new JSONObject();
        boolean refreshStatus = lingxingService.getOrRefreshToken();
        rj.put("status", refreshStatus);
        rj.put("msg", "token刷新成功");
        return rj.toJSONString();
    }


}
