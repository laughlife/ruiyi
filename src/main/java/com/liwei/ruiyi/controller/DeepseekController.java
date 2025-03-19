package com.liwei.ruiyi.controller;

import com.alibaba.fastjson2.JSONObject;
import com.liwei.ruiyi.bo.TAdmin;
import com.liwei.ruiyi.service.DeepseekService;
import com.liwei.ruiyi.service.TFontService;
import jakarta.servlet.http.HttpServletRequest;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/deepseek")
public class DeepseekController {

    @Autowired
    private HttpServletRequest request;

    @Autowired
    DeepseekService deepseekService;

    @RequestMapping("/sendMessage")
    @ResponseBody
    public String sendMessage(String uuid, String message) {
        JSONObject rj = new JSONObject();
        rj.put("status", false);
        rj.put("message", "数据获取失败，请稍后再进行尝试。");
        if (StringUtils.isNotEmpty(message)) {
            Thread thread = new Thread(() -> {
                deepseekService.sendMessage(uuid, message);
            });
            thread.start();
            rj.put("status", true);
            rj.put("message", "消息已经发送到deepseek，等待deepseek回执消息。");
        }
        return rj.toJSONString();
    }
}
