package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/system")
public class SystemController {

    @RequestMapping("/refresh")
    @ResponseBody
    public String refresh() {
        // 刷新，保持链接即可，无实际意义，保证session不过期
        JSONObject rj = new JSONObject();
        rj.put("status", true);
        return rj.toJSONString();
    }
}
